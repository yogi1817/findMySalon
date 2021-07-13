package com.spj.salon.checkin.adapters;

import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.openapi.resources.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

public interface ICheckinFacade {

    BarberWaitTimeResponse waitTimeEstimate(Optional<Long> barberIdOptional, TimeZone timeZone);

    CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberId, TimeZone timeZone);

    CustomerCheckInResponse checkInCustomerByBarber(long barberId, TimeZone timeZone);

    CustomerCheckoutResponse checkOut(Optional<Long> customerId);

    BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest, TimeZone timeZone);

    boolean isUserAlreadyCheckedIn(long userId);

    CheckIn findCheckedInBarberId(long customerId);

    BarberDetailsCalendar getUserCalendar(Set<BarberCalendar> barberCalendarSet);

    BarberWaitTimeResponse currentWaitTimeEstimateForCustomer(long barberId);

    BarberWaitTimeResponse currentWaitTimeEstimateForCustomerAtBarber(long customerId, long barberId);

    List<UserProfile> getCheckedInCustomer();
}
