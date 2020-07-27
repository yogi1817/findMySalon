package com.spj.salon.barber.facade;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.naming.ServiceUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.BarberServicesMapping;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
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

	private static final Logger logger = LoggerFactory.getLogger(BarberFacade.class.getName());

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
	
	@Override
	public User register(User user, UserType userType) {
		if (CollectionUtils.isEmpty(userDao.searchUserWithLoginIdAuthority(user.getLoginId(), userType.getResponse()))) {
			
			Authorities  auth = authoritiesRepository.findByAuthority(userType.getResponse());
			user = ValidationUtils.validateUser(user, auth);
			userRepository.saveAndFlush(user);
			
			myEmailService.sendOtpMessage(user.getLoginId());
			
			//JSONIgnore us not working to setting it to null before returning to user
			user.setPassword(null);
			return user;
		}

		throw new DuplicateKeyException("User already Exists");
	}

	@Override
	public boolean addBarbersCountToday(DailyBarbers dailyBarbers) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User user = userRepository.findByLoginId(loginId);
		if (user!=null) {
			List<DailyBarbers> dailyBarberSet = user.getDailyBarberSet();
			dailyBarberSet.add(dailyBarbers);
			user.setDailyBarberSet(dailyBarberSet);
			userRepository.saveAndFlush(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean addServices(long serviceId, int cost, int time) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User user = userRepository.findByLoginId(loginId);
		
		Optional<Services> servicesOpt = serviceRepo.findById(serviceId);
		BarberServicesMapping barberServicesMapping = null;
		if (servicesOpt.isPresent()) {
			Set<BarberServicesMapping> barberServicesMappingsSet = user.getBarberServicesMappingSet();
			barberServicesMapping = new BarberServicesMapping();
			barberServicesMapping.setUserId(user.getUserId());
			barberServicesMapping.setServiceId(serviceId);
			barberServicesMapping.setServiceCharges(cost);
			barberServicesMapping.setTimeToPerform(time);
			barberServicesMapping.setCreateDate(DateUtils.getTodaysDate());

			barberServicesMappingsSet.add(barberServicesMapping);
			user.setBarberServicesMappingSet(barberServicesMappingsSet);

			userRepository.saveAndFlush(user);

			return true;
		}

		return false;
	}

	@Override
	public boolean addBarberCalendar(BarberCalendar barberCalendar) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User user = userRepository.findByLoginId(loginId);
		
		if(user!=null) {
			Set<BarberCalendar> barberCalendarSet = user.getBarberCalendarSet();
			barberCalendar = ValidationUtils.validateBarberCalendar(barberCalendar, user.getUserId());
			barberCalendarSet.add(barberCalendar);

			userRepository.saveAndFlush(user);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean addBarberAddress(Address address) throws ServiceUnavailableException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User barber = userRepository.findByLoginId(loginId);
		
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
			e1.printStackTrace();
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
