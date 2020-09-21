package com.spj.salon.checkin.facade;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.naming.ServiceUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.dto.BarberAddressDTO;
import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.model.ZipCodeLookup;
import com.spj.salon.barber.repository.AddressRepository;
import com.spj.salon.barber.repository.ZipCodeRepository;
import com.spj.salon.checkin.model.CheckIn;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;
import com.spj.salon.utils.DateUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class CheckInFacade implements ICheckinFacade {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CheckInRepository checkInRepository;
	
	@Autowired
	private ZipCodeRepository zipCodeRepo;
	
	/*@Autowired
	private GeoApiContext context;*/
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired 
	private GoogleGeoCodingClient googleGeoCodingClient;
	
	private static final Logger logger = LogManager.getLogger(CheckInFacade.class.getName());
	
	@Override
	public String waitTimeEstimate(long barberId) {
		Optional<User> barberOpt = userRepository.findById(barberId);
		if(barberOpt.isPresent()) {
			User barber = barberOpt.get();
			Set<BarberCalendar> barberCalSet = barber.getBarberCalendarSet();
			List<DailyBarbers> dailyBarberList = barber.getDailyBarberSet();
			Set<CheckIn> checkInSet = barber.getCheckInSet();
			
			return getEstimateWaitTime(barberCalSet, dailyBarberList, checkInSet);
		}
		return null;
	}

	/**
	 * This method will calculate the checkin time available for a given service
	 * @param barberCalSet
	 * @param dailyBarberSet
	 * @param checkin
	 * @return
	 */
	private String getEstimateWaitTime(Set<BarberCalendar> barberCalSet, List<DailyBarbers> dailyBarberList,
			Set<CheckIn> checkInSet) {
		BarberCalendar todaysBarberCal = getTodaysBarberCal(barberCalSet);
		if(CollectionUtils.isEmpty(dailyBarberList)) {
			return "No Barbers Data available";
		}
		
		DailyBarbers todaysBarbers = dailyBarberList.get(0);
		if(todaysBarbers.getBarbersCount()==0) {
			return "No Barbers Available";
		}
		
		if(todaysBarberCal==null || todaysBarberCal.getSalonCloseTime().before(DateUtils.getNowTimePlus60Mins1970())){ 
			return "No open appointments Today"; 
		}
		 
		checkInSet = getCheckIn(checkInSet);
		if(CollectionUtils.isEmpty(checkInSet)) {
			return "0";
		}else {
			int checkinCount = checkInSet.size();
			int barbersCount = todaysBarbers.getBarbersCount();
			
			int waitTime = (checkinCount/barbersCount)*15;
			return waitTime+"";
		}
	}

	//TODO: remove after changing the outer join to criteria query
	private Set<CheckIn> getCheckIn(Set<CheckIn> checkInSet) {
		Set<CheckIn> newCheckInSet = new HashSet<>();
		for (CheckIn checkIn : checkInSet) {
			if(!checkIn.isCheckedOut())
				newCheckInSet.add(checkIn);
		}
		return newCheckInSet;
	}

	/**
	 * This method will iterate through the cal set and find out todays working hours
	 * It will also check if it a special day like 4th July
	 * @param barberCalSet
	 * @return
	 */
	private BarberCalendar getTodaysBarberCal(Set<BarberCalendar> barberCalSet) {
		for (BarberCalendar barberCalendar : barberCalSet) {
			if(DateUtils.getTodaysDay().equals(barberCalendar.getCalendarDay())) {
				return barberCalendar;
			}
		}
		return null;
	}

	@Override
	public String checkInUser(long barberId, String time) {
		boolean checkInAvailable = true;
		String waitTimeEstimate = null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User user = userRepository.findByLoginId(loginId);
		
		Optional<User> barberOpt = userRepository.findById(barberId);
		
		if(barberOpt.isPresent()) {
			User barber = barberOpt.get();
			waitTimeEstimate = waitTimeEstimate(barber.getUserId());
			int waitTime = 0;
			try {
				waitTime = Integer.parseInt(waitTimeEstimate);
			}catch(NumberFormatException ne) {
				checkInAvailable = false;
			}
			if(checkInAvailable) {
				CheckIn checkIn = new CheckIn(barberId, user.getUserId(), Integer.parseInt(time));
				checkInRepository.saveAndFlush(checkIn);
				return "Check in with waitTime "+waitTime;
			}
		}
		return waitTimeEstimate;
	}

	@Override
	public String checkInBarber(long userId, String time) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		User barber = userRepository.findByLoginId(loginId);
		boolean checkInAvailable = true;
		
		String waitTimeEstimate = null;
		if(barber!=null) {
			waitTimeEstimate = waitTimeEstimate(barber.getUserId());
			int waitTime = 0;
			try {
				waitTime = Integer.parseInt(waitTimeEstimate);
			}catch(NumberFormatException ne) {
				checkInAvailable = false;
			}
			if(checkInAvailable) {
				CheckIn checkIn = new CheckIn(barber.getUserId(), userId, Integer.parseInt(time));
				checkInRepository.saveAndFlush(checkIn);
				return "Check in with waitTime "+waitTime;
			}
		}
		return waitTimeEstimate;
	}
	
	@Override
	public boolean checkOut(long userId) {
		List<CheckIn> checkInList = checkInRepository.findByUserMappingIdAndCheckedOut(userId, false);
		if(!CollectionUtils.isEmpty(checkInList)) {
			for (CheckIn checkIn : checkInList) {
				checkIn.setCheckedOut(true);
				checkInRepository.saveAndFlush(checkIn);
			}
			
			return true;
		}
		return false;
	}

	/**
	 * @throws ServiceUnavailableException 
	 * 
	 */
	@Override
	public BarberCheckInResponse findBarbersAtZip(BarberCheckInRequest barberCheckInRequest) throws ServiceUnavailableException {
		double longitude = 0;
		double latitude = 0;
		
		if(barberCheckInRequest.getZipCode()==null &&
				barberCheckInRequest.getLatitude()!=null &&
				barberCheckInRequest.getLongitude()!=null) {
			
			longitude = barberCheckInRequest.getLongitude();
			latitude = barberCheckInRequest.getLatitude();
		}else {
			longitude = 0;
			latitude = 0;
			
			ZipCodeLookup zipCodeLookUp = zipCodeRepo.findByZipCode(Long.parseLong(barberCheckInRequest.getZipCode()));
			if(zipCodeLookUp!=null) {
				longitude = zipCodeLookUp.getLongitude();
				latitude = zipCodeLookUp.getLatitude();
			}
		}
		
		if(longitude==0 || latitude==0){
			/*try {
				//This is how we do it using goodle api
				GeocodingResult[] results = GeocodingApi.geocode(context, zipCode).await();
				*/
				try {
					GeocodingResult[] results = googleGeoCodingClient.findGeocodingResult(barberCheckInRequest.getZipCode());
					longitude = results[0].geometry.location.lng;
					latitude = results[0].geometry.location.lat;
					
					//This will update zip code table async
					CompletableFuture.runAsync(() -> {
						saveZipCode(barberCheckInRequest.getZipCode(), results);
					});
				} catch (IOException e) {
					e.printStackTrace();
					throw new ServiceUnavailableException("Google service unavailable");
				}
						
				logger.debug("longitude "+ longitude);
				logger.debug("latitude "+ latitude);
		}
		
		return findBarbersWithinXMiles(longitude, latitude, barberCheckInRequest.getDistance());
	}
	
	/**
	 * 1° of latitude ~= 69 miles
	 * 1° of longitude ~= cos(latitude)*69
	 * @param longitude
	 * @param latitude
	 * @param distance
	 */
	private BarberCheckInResponse findBarbersWithinXMiles(double longitude, double latitude, Double distance) {
		List<BarberAddressDTO> barbersaddressDTOList = new ArrayList<>();
		BarberAddressDTO barberAddressDTO = new BarberAddressDTO();
		BarberCheckInResponse response = new BarberCheckInResponse();
		
		double long1 = longitude - distance/Math.abs(Math.cos(Math.toRadians(latitude))*69);
		double long2 = longitude + distance/Math.abs(Math.cos(Math.toRadians(latitude))*69);
		double lat1 = latitude - (distance/69); 
		double lat2 = latitude+(distance/69);
		
		logger.debug("long1 --> {}, long2 --> {}, lat1 --> {}. lat2 --> {}", long1, long2, lat1, lat2);

		logger.debug("longitude --> {}, latitude --> {}", longitude, latitude);
		List<Map<String, Object>> addressIds = 
				addressRepo.getBarbersId(longitude, latitude, distance, long1, long2, lat1, lat2);
		
		for (Map<String, Object> map : addressIds) {
			Optional<Address> barbersAddress =  addressRepo.findById(((BigInteger) map.get("address_id")).longValue());
			if(barbersAddress.isPresent()) {
				Optional<User> user = userRepository.findById(barbersAddress.get().getUserId());
				barberAddressDTO = new BarberAddressDTO();
				barberAddressDTO.setAddressLineOne(barbersAddress.get().getAddressLineOne());
				barberAddressDTO.setAddressLineTwo(barbersAddress.get().getAddressLineTwo());
				barberAddressDTO.setCity(barbersAddress.get().getCity());
				barberAddressDTO.setState(barbersAddress.get().getState());
				barberAddressDTO.setZip(barbersAddress.get().getZip());
				barberAddressDTO.setEmail(user.get().getEmail());
				barberAddressDTO.setFirstName(user.get().getFirstName());
				barberAddressDTO.setLastName(user.get().getLastName());
				barberAddressDTO.setMiddleName(user.get().getMiddleName());
				barberAddressDTO.setPhone(user.get().getPhone());
				barberAddressDTO.setStoreName(user.get().getStoreName());
				barberAddressDTO.setDistance((Double) map.get("distance"));
				barberAddressDTO.setWaitTime(waitTimeEstimate(barbersAddress.get().getUserId()));
				barbersaddressDTOList.add(barberAddressDTO);
			}
		}
		
		if(CollectionUtils.isEmpty(barbersaddressDTOList)) {
			response.setMessage("No Barbers Found within the range");
		}else {
			response.setMessage(barbersaddressDTOList.size()+" Barbers Found");
		}
		response.setBarberAddressDTO(barbersaddressDTOList);
		return response;
	}

	/**
	 * Save zip code from google api to lookuptable
	 * @param address
	 * @param results
	 */
	private void saveZipCode(String zipCode, GeocodingResult[] results) {
		ZipCodeLookup zipCodeLookup = new ZipCodeLookup();
		zipCodeLookup.setZipCode(Long.parseLong(zipCode));
		zipCodeLookup.setLatitude(results[0].geometry.location.lat);
		zipCodeLookup.setLongitude(results[0].geometry.location.lng);
		zipCodeLookup.setCreateDate(DateUtils.getTodaysDate());
		
		zipCodeRepo.saveAndFlush(zipCodeLookup);
	}
}
