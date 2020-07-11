package com.spj.salon.barber.facade;

import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IBarberFacade {

	public boolean registerBarber(Barber barber);

	public boolean addBarbersCountToday(Long id, DailyBarbers dailyBarbers);

	public boolean addServices(long barberId, long serviceId, int cost, int time);

	public boolean addBarberCalendar(long barberId, BarberCalendar barberCalendar);

	public boolean addBarberAddress(long barberId, Address address);
}
