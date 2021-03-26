package com.spj.salon.barber.ports.in;

import com.spj.salon.openapi.resources.*;

import java.util.Optional;

/**
 * @author Yogesh Sharma
 */
public interface IBarberAdapter {
    DailyBarbersResponse addBarbersCountToday(DailyBarbersRequest dailyBarbersRequest);

    BarberServicesResponse addServices(long serviceId, int cost, int time);

    BarberCalendarResponse addBarberCalendar(BarberCalendarRequest barberCalendar);

    BarberAddressResponse addBarberAddress(BarberAddressRequest barberAddressRequest);

    BarberServicesResponse addService(BarberServicesRequest services);

    BarberProfile getBarberProfile(Optional<Long> barberId);
}
