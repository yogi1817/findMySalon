package com.spj.salon.barber.facade;

import javax.naming.ServiceUnavailableException;

import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IBarberFacade {

	public User register(User barber, String userType);

	public boolean addBarbersCountToday(DailyBarbers dailyBarbers);

	public boolean addServices(long serviceId, int cost, int time);

	public boolean addBarberCalendar(BarberCalendar barberCalendar);

	public boolean addBarberAddress(Address address) throws ServiceUnavailableException ;
}
