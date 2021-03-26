package com.spj.salon.barber.ports.in;

import com.spj.salon.openapi.resources.*;

/**
 * @author Yogesh Sharma
 */
public interface IBarberAdapter {
    DailyBarbersResponse addBarbersCountToday(DailyBarbersRequest dailyBarbersRequest);

    BarberServicesResponse addServices(long serviceId, int cost, int time);

    BarberCalendarResponse addBarberCalendar(BarberCalendarRequest barberCalendar);
    
    BarberServicesResponse addService(BarberServicesRequest services);
}
