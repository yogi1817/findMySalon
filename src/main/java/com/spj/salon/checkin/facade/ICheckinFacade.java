package com.spj.salon.checkin.facade;

public interface ICheckinFacade {

	public String waitTimeEstimate(long barberId);
	public boolean checkIn(long barberId, Long userId, String time);
	public boolean checkOut(long userId);
}
