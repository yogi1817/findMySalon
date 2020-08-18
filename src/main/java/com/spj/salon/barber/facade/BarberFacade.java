package com.spj.salon.barber.facade;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.naming.ServiceUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.BarberServicesMapping;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.client.OAuthClient;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.otp.facade.MyEmailService;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.user.dao.IUserDao;
import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;
import com.spj.salon.utils.DateUtils;
import com.spj.salon.utils.ValidationUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class BarberFacade implements IBarberFacade {

	private static final Logger logger = LogManager.getLogger(BarberFacade.class.getName());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private ServicesRepository serviceRepo;

	@Autowired
	private GoogleGeoCodingClient googleGeoCodingClient;
	
	@Autowired
	private MyEmailService myEmailService;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OAuthClient oAuthClient;
	
	/**
	 * This method registers all types of users available in userType
	 */
	@Override
	public User register(User user, UserType userType, String clientHost) {
		if (CollectionUtils.isEmpty(userDao.searchUserWithLoginIdAuthority(user.getLoginId(), userType.getResponse()))) {
			
			Authorities  auth = authoritiesRepository.findByAuthority(userType.getResponse());
			user = ValidationUtils.validateUser(user, auth);
			
			//userCopy is needed to get the jwt before we encrypt the password
			User userCopy = new User(user.getLoginId()==null?user.getEmail():user.getLoginId(), user.getPassword());
			
			user.setPassword(passwordEncoder.encode(StringUtils.isEmpty(user.getPassword())?"defaultpassword":user.getPassword()));
			userRepository.saveAndFlush(user);
			
			logger.debug("User registered sucessfully {}", user.getLoginId());
			myEmailService.sendOtpMessage(user.getLoginId());
			
			user.setJwtToken(oAuthClient.getJwtToken(userCopy, clientHost).getJwtToken());
			
			//JSONIgnore us not working to setting it to null before returning to user
			user.setPassword(null);
			
			logger.info("Call completed successfully", user.getLoginId());
			return user;
		}

		logger.error("User ALready Exists for loginId {}", user.getLoginId());
		throw new DuplicateKeyException("User already Exists");
	}

	/**
	 * This method will be called every morning by Barber to provide how many barbers are available today.
	 * This can be called again if at the later time some barbers are unavailable,
	 * the code will read the latest record of barber count available on the current day
	 */
	@Override
	public boolean addBarbersCountToday(DailyBarbers dailyBarbers) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		logger.info("Barber found in jwt with loginId {}", loginId);
		User user = userRepository.findByLoginId(loginId);
		if (user!=null) {
			logger.info("Barber found for loginId {}", loginId);
			List<DailyBarbers> dailyBarberSet = user.getDailyBarberSet();
			dailyBarberSet.add(dailyBarbers);
			user.setDailyBarberSet(dailyBarberSet);
			userRepository.saveAndFlush(user);
			logger.info("Barber count saved into DB {}", loginId);
			return true;
		}
		logger.error("No Barber found for loginId {}", loginId);
		return false;
	}

	/**
	 * This method add services provide by barbers like haircut etc.
	 */
	@Override
	public boolean addServices(long serviceId, int cost, int time) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		logger.info("Barber found in jwt with loginId {}", loginId);
		
		User user = userRepository.findByLoginId(loginId);
		
		Optional<Services> servicesOpt = serviceRepo.findById(serviceId);
		BarberServicesMapping barberServicesMapping = null;
		if (servicesOpt.isPresent()) {
			logger.info("Services found in DB with loginId {}", loginId);
			Set<BarberServicesMapping> barberServicesMappingsSet = user.getBarberServicesMappingSet();
			barberServicesMapping = new BarberServicesMapping();
			barberServicesMapping.setUserId(user.getUserId());
			barberServicesMapping.setServiceId(serviceId);
			barberServicesMapping.setServiceCharges(cost);
			barberServicesMapping.setTimeToPerform(time);
			barberServicesMapping.setCreateDate(DateUtils.getTodaysDate());

			logger.info("Adding new service DB for barber with loginId {} and services {}", loginId, barberServicesMapping);
			barberServicesMappingsSet.add(barberServicesMapping);
			user.setBarberServicesMappingSet(barberServicesMappingsSet);

			userRepository.saveAndFlush(user);

			return true;
		}

		return false;
	}

	/**
	 * This method will add barber calendar. The barber can send his daily calander like wednesday from 10 to 4.
	 * He can also send calendar with date and mentions his hours.
	 */
	@Override
	public boolean addBarberCalendar(BarberCalendar barberCalendar) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		logger.info("Barber found in jwt with loginId {}", loginId);
		
		User user = userRepository.findByLoginId(loginId);
		
		if(user!=null) {
			Set<BarberCalendar> barberCalendarSet = user.getBarberCalendarSet();
			barberCalendar = ValidationUtils.validateBarberCalendar(barberCalendar, user.getUserId());
			barberCalendarSet.add(barberCalendar);

			userRepository.saveAndFlush(user);
			logger.info("Barber cal is updated with loginId {}", loginId);
			return true;
		}
		
		logger.error("No Barber found to update cal with loginId {}", loginId);
		return false;
	}

	/**
	 * This method add the barber address in address table. It also add longi and lati into the table using geolocation api
	 */
	@Override
	public boolean addBarberAddress(Address address) throws ServiceUnavailableException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		logger.info("Barber found in jwt with loginId {}", loginId);
		
		User barber = userRepository.findByLoginId(loginId);
		
		if(barber==null) {
			logger.error("Barber not found in DB with loginId {}", loginId);
			throw new NotFoundCustomException("Barber not found",loginId);
		}
		try {
			/*
			 * GeocodingResult[] results = GeocodingApi.geocode(context,
			 * address.getAddress()).await();
			 */

			GeocodingResult[] results = googleGeoCodingClient
					.findGeocodingResult(URLEncoder.encode(address.getAddress(), "UTF-8"));

			validateAddress(address, barber.getUserId(), results);
			Set<Address> barberAddresses = barber.getAddressSet();
			barberAddresses.add(address);
			barber.setAddressSet(barberAddresses);

			userRepository.saveAndFlush(barber);
			
			return true;
		} catch (IOException e1) {
			logger.error("Geo location service failed with message {}", e1.getMessage());
			throw new ServiceUnavailableException("Google api not available");
		}
	}

	/**
	 * 
	 * @param address
	 * @param barberId
	 * @param results
	 */
	private void validateAddress(Address address, Long barberId, GeocodingResult[] results) {
		address.setUserId(barberId);
		address.setCreateDate(DateUtils.getTodaysDate());
		address.setModifyDate(DateUtils.getTodaysDate());
		logger.debug("longitude " + results[0].geometry.location.lng);
		logger.debug("latitude " + results[0].geometry.location.lat);
		address.setLongitude(results[0].geometry.location.lng);
		address.setLatitude(results[0].geometry.location.lat);
	}
}
