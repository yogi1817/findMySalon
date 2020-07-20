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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.dto.BarberAddressDTO;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.model.ZipCodeLookup;
import com.spj.salon.barber.repository.AddressRepository;
import com.spj.salon.barber.repository.BarberRepository;
import com.spj.salon.barber.repository.ZipCodeRepository;
import com.spj.salon.checkin.model.CheckIn;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.utils.DateUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class CheckInFacade implements ICheckinFacade {

	@Autowired
	private BarberRepository barberRepository;
	
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
	
	
	private static final Logger logger = LoggerFactory.getLogger(CheckInFacade.class.getName());
	
	@Override
	public String waitTimeEstimate(long barberId) {
		Optional<Barber> barberOpt = barberRepository.findById(barberId);
		if(barberOpt.isPresent()) {
			Barber barber = barberOpt.get();
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
		
		if(todaysBarberCal.getSalonCloseTime().before(DateUtils.getNowTimePlus60Mins1970())){ 
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
	public boolean checkIn(long barberId, Long userId, String time) {
		Optional<Barber> barberOpt = barberRepository.findById(barberId);
		if(barberOpt.isPresent()) {
			Barber barber = barberOpt.get();
			Set<CheckIn> checkInSet = barber.getCheckInSet();
			CheckIn checkIn = new CheckIn(barberId, userId, Integer.parseInt(time));
			checkInSet.add(checkIn);
			
			barberRepository.saveAndFlush(barber);
			return true;
		}
		return false;
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
	 * 
	 */
	@Override
	public List<BarberAddressDTO> findBarbersAtZip(String zipCode, String distance) {
		double longitude = 0;
		double latitude = 0;
		
		ZipCodeLookup zipCodeLookUp = zipCodeRepo.findByZipCode(Long.parseLong(zipCode));
		if(zipCodeLookUp!=null) {
			longitude = zipCodeLookUp.getLongitude();
			latitude = zipCodeLookUp.getLatitude();
		}else if(longitude==0 || latitude==0){
			/*try {
				//This is how we do it using goodle api
				GeocodingResult[] results = GeocodingApi.geocode(context, zipCode).await();
				*/
				
				try {
					GeocodingResult[] results = googleGeoCodingClient.findGeocodingResult(zipCode);
					longitude = results[0].geometry.location.lng;
					latitude = results[0].geometry.location.lat;
					
					//This will update zip code table async
					CompletableFuture.runAsync(() -> {
						saveZipCode(zipCode, results);
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				logger.debug("longitude "+ longitude);
				logger.debug("latitude "+ latitude);
		}
		
		return findBarbersWithinXMiles(longitude, latitude, Double.parseDouble(distance));
	}
	
	/**
	 * 1° of latitude ~= 69 miles
	 * 1° of longitude ~= cos(latitude)*69
	 * @param longitude
	 * @param latitude
	 * @param distance
	 */
	private List<BarberAddressDTO> findBarbersWithinXMiles(double longitude, double latitude, Double distance) {
		List<BarberAddressDTO> barbersddressDTOList = new ArrayList<>();
		BarberAddressDTO barberAddressDTO = new BarberAddressDTO();
		
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
				barberAddressDTO = new BarberAddressDTO();
				barberAddressDTO.setAddressLineOne(barbersAddress.get().getAddressLineOne());
				barberAddressDTO.setAddressLineTwo(barbersAddress.get().getAddressLineTwo());
				barberAddressDTO.setCity(barbersAddress.get().getCity());
				barberAddressDTO.setState(barbersAddress.get().getState());
				barberAddressDTO.setZip(barbersAddress.get().getZip());
				barberAddressDTO.setEmail(barbersAddress.get().getBarber().getEmail());
				barberAddressDTO.setFirstName(barbersAddress.get().getBarber().getFirstName());
				barberAddressDTO.setLastName(barbersAddress.get().getBarber().getLastName());
				barberAddressDTO.setMiddleName(barbersAddress.get().getBarber().getMiddleName());
				barberAddressDTO.setPhone(barbersAddress.get().getBarber().getPhone());
				barberAddressDTO.setStoreName(barbersAddress.get().getBarber().getStoreName());
				barberAddressDTO.setDistance((Double) map.get("distance"));
				barbersddressDTOList.add(barberAddressDTO);
			}
		}
		
		return barbersddressDTOList;
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
