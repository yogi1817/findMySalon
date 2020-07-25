package com.spj.salon.utils;

import org.springframework.util.StringUtils;

import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class ValidationUtils {

	private ValidationUtils() {
		
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public static User validateUser(User user) {
		
		if(StringUtils.isEmpty(user.getEmail()))
			throw new NotFoundCustomException("Email Cannot be blank", "Please add email id to your request");
		if(user.getModifyDate()==null)
			user.setModifyDate(DateUtils.getTodaysDate());
		
		if(user.getCreateDate()==null)
			user.setCreateDate(DateUtils.getTodaysDate());
		
		if (StringUtils.isEmpty(user.getLoginId())) {
			user.setLoginId(user.getEmail());
		}
		
		return user;
	}

	/**
	 * 
	 * @param barberCalendar
	 * @param userId
	 * @return
	 */
	public static BarberCalendar validateBarberCalendar(BarberCalendar barberCalendar, Long userId) {
		barberCalendar.setUserId(userId);
		if(barberCalendar.getSalonOpenTime()==null)
			barberCalendar.setSalonOpenTime(DateUtils.getHoursAndMinutes(barberCalendar.getSalonOpensAt()));
		if(barberCalendar.getSalonCloseTime()==null)
			barberCalendar.setSalonCloseTime(DateUtils.getHoursAndMinutes(barberCalendar.getSalonClosesAt()));
		if(!StringUtils.isEmpty(barberCalendar.getCalendayDateString()) && barberCalendar.getCalendarDate()==null)
			barberCalendar.setCalendarDate(DateUtils.getFormattedDate(barberCalendar.getCalendayDateString(), "MM/dd/yyyy"));
		if(barberCalendar.getModifyDate()==null) {
			barberCalendar.setModifyDate(DateUtils.getTodaysDate());
		}
		return barberCalendar;
	}
}
