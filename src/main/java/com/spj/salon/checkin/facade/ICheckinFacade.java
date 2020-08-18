package com.spj.salon.checkin.facade;

import javax.naming.ServiceUnavailableException;

import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;

public interface ICheckinFacade {

	public String waitTimeEstimate(long barberId);
	public String checkInUser(long barberId, String time) ;
	public String checkInBarber(long barberId, String time) ;
	public boolean checkOut(long userId);
	public BarberCheckInResponse findBarbersAtZip(BarberCheckInRequest barberCheckInRequest) throws ServiceUnavailableException ;
}
