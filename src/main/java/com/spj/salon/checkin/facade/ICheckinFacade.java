package com.spj.salon.checkin.facade;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import com.spj.salon.barber.dto.BarberAddressDTO;

public interface ICheckinFacade {

	public String waitTimeEstimate(long barberId);
	public String checkInUser(long barberId, String time) ;
	public String checkInBarber(long barberId, String time) ;
	public boolean checkOut(long userId);
	public List<BarberAddressDTO> findBarbersAtZip(String zipCode, String distance) throws ServiceUnavailableException ;
}
