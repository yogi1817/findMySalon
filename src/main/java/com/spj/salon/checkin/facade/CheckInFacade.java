package com.spj.salon.checkin.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.barber.repository.BarberRepository;
import com.spj.salon.checkin.model.CheckIn;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.utils.DateUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class CheckInFacade implements ICheckinFacade {

	@Autowired
	BarberRepository barberRepository;
	
	@Autowired
	CheckInRepository checkInRepository;
	
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
}
