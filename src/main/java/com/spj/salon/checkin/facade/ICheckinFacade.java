package com.spj.salon.checkin.facade;

import javax.naming.ServiceUnavailableException;

import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;

public interface ICheckinFacade {

	String waitTimeEstimate(long barberId);
	String checkInUser(long barberId, String time) ;
	String checkInBarber(long barberId, String time) ;
	boolean checkOut(long userId);
	BarberCheckInResponse findBarbersAtZip(BarberCheckInRequest barberCheckInRequest) throws ServiceUnavailableException ;
}
