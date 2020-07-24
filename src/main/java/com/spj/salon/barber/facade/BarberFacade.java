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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.BarberServicesMapping;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.barber.repository.BarberRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.utils.DateUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class BarberFacade implements IBarberFacade {

	private static final Logger logger = LoggerFactory.getLogger(BarberFacade.class.getName());

	@Autowired
	private BarberRepository barberRepository;

	@Autowired
	private ServicesRepository serviceRepo;

	@Autowired
	private GoogleGeoCodingClient googleGeoCodingClient;

	@Autowired
	private AuthoritiesRepository authRepo;
	
	@Override
	public Barber registerBarber(Barber barber) {
		if (validateBarber(barber) && barberRepository.countByLoginId(barber.getLoginId()) == 0) {
			barberRepository.save(barber);
			addAuthority(barber);
			
			//JSONIgnore us not working to setting it to null before returning to user
			barber.setPassword(null);
			return barber;
		}

		throw new DuplicateKeyException("User already Exists");
	}

	/**
	 * 
	 * @param barber
	 * @return
	 */
	private Barber addAuthority(Barber barber) {
		Authorities authority = new Authorities();
		authority.setAuthority("USER");
		authority.setMappingId(barber.getBarberId());
		
		authRepo.saveAndFlush(authority);
		
		return barber;
	}

	/**
	 * Validate the Barber method
	 * 
	 * @param barber
	 */
	private boolean validateBarber(Barber barber) {
		if (StringUtils.isEmpty(barber.getLoginId())) {
			barber.setLoginId(barber.getEmail());
		}

		barber.setCreateDate(DateUtils.getTodaysDate());
		barber.setModifyDate(DateUtils.getTodaysDate());
		// TODO: encrypt password
		// TODO: validate phone
		// TODO: validate email
		if (barber.getAuthCode() != null && barber.getAuthCode().equals(12345L)) {
			return true;
		} else
			return false;
	}

	@Override
	public boolean addBarbersCountToday(Long id, DailyBarbers dailyBarbers) {
		Optional<Barber> barberOpt = barberRepository.findById(id);
		validateDailyBarber(dailyBarbers);
		if (barberOpt.isPresent()) {
			Barber barber = barberOpt.get();
			List<DailyBarbers> dailyBarberSet = barber.getDailyBarberSet();
			dailyBarberSet.add(dailyBarbers);
			barber.setDailyBarberSet(dailyBarberSet);
			barberRepository.saveAndFlush(barber);
			return true;
		}
		return false;
	}

	/**
	 * Validate DailyBarber Object
	 * 
	 * @param dailyBarbers
	 */
	private void validateDailyBarber(DailyBarbers dailyBarbers) {
		dailyBarbers.setCreateTimestamp(DateUtils.getCurrentTimestamp());
		// TODO: verify that barbers count is more than 0
	}

	@Override
	public boolean addServices(long barberId, long serviceId, int cost, int time) {
		Optional<Barber> barberOpt = barberRepository.findById(barberId);
		Optional<Services> servicesOpt = serviceRepo.findById(serviceId);
		BarberServicesMapping barberServicesMapping = null;
		if (barberOpt.isPresent() && servicesOpt.isPresent()) {
			Barber barber = barberOpt.get();
			Set<BarberServicesMapping> barberServicesMappingsSet = barber.getBarberServicesMappingSet();
			barberServicesMapping = new BarberServicesMapping();
			barberServicesMapping.setBarberId(barberId);
			barberServicesMapping.setServiceId(serviceId);
			barberServicesMapping.setServiceCharges(cost);
			barberServicesMapping.setTimeToPerform(time);
			barberServicesMapping.setCreateDate(DateUtils.getTodaysDate());

			barberServicesMappingsSet.add(barberServicesMapping);
			barber.setBarberServicesMappingSet(barberServicesMappingsSet);

			barberRepository.saveAndFlush(barber);

			return true;
		}

		return false;
	}

	@Override
	public boolean addBarberCalendar(long barberId, BarberCalendar barberCalendar) {
		validateBarberCalendar(barberCalendar, barberId);
		Optional<Barber> barberOpt = barberRepository.findById(barberId);
		if (barberOpt.isPresent()) {
			Barber barber = barberOpt.get();
			Set<BarberCalendar> barberCalendarSet = barber.getBarberCalendarSet();
			barberCalendarSet.add(barberCalendar);

			barberRepository.saveAndFlush(barber);
			return true;
		}
		return false;
	}

	private void validateBarberCalendar(BarberCalendar barberCalendar, long barberId) {
		barberCalendar.setModifyDate(DateUtils.getTodaysDate());
		barberCalendar.setSalonOpenTime(DateUtils.getHoursAndMinutes(barberCalendar.getSalonOpensAt()));
		barberCalendar.setSalonCloseTime(DateUtils.getHoursAndMinutes(barberCalendar.getSalonClosesAt()));
		barberCalendar.setBarberMappingId(barberId);
		if (barberCalendar.getCalendayDateString() != null)
			barberCalendar
					.setCalendarDate(DateUtils.getFormattedDate(barberCalendar.getCalendayDateString(), "MM/dd/yyyy"));
	}

	@Override
	public boolean addBarberAddress(long barberId, Address address) throws ServiceUnavailableException {
		Optional<Barber> barberOpt = barberRepository.findById(barberId);
		try {
			/*
			 * GeocodingResult[] results = GeocodingApi.geocode(context,
			 * address.getAddress()).await();
			 */

			GeocodingResult[] results = googleGeoCodingClient
					.findGeocodingResult(URLEncoder.encode(address.getAddress(), "UTF-8"));

			validateAddress(address, barberId, results);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ServiceUnavailableException("Google api not available");
		}

		if (barberOpt.isPresent()) {
			Barber barber = barberOpt.get();
			Set<Address> barberAddresses = barber.getAddressSet();
			barberAddresses.add(address);
			barber.setAddressSet(barberAddresses);

			barberRepository.saveAndFlush(barber);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param address
	 * @param barberId
	 * @param results
	 */
	private void validateAddress(Address address, Long barberId, GeocodingResult[] results) {
		address.setMappingId(barberId);
		address.setCreateDate(DateUtils.getTodaysDate());
		address.setModifyDate(DateUtils.getTodaysDate());
		logger.debug("longitude " + results[0].geometry.location.lng);
		logger.debug("latitude " + results[0].geometry.location.lat);
		address.setLongitude(results[0].geometry.location.lng);
		address.setLatitude(results[0].geometry.location.lat);
	}
}
