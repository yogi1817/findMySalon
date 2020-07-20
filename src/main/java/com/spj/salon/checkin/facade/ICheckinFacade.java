package com.spj.salon.checkin.facade;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import com.spj.salon.barber.dto.BarberAddressDTO;

public interface ICheckinFacade {

	public String waitTimeEstimate(long barberId);
	public boolean checkIn(long barberId, Long userId, String time) ;
	public boolean checkOut(long userId);
	public List<BarberAddressDTO> findBarbersAtZip(String zipCode, String distance) throws ServiceUnavailableException ;
}
