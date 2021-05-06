package com.spj.salon.checkin.adapters;

import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.openapi.resources.*;

import java.util.Optional;
import java.util.Set;

public interface ICheckinFacade {

    BarberWaitTimeResponse waitTimeEstimate(Optional<Long> barberIdOptional);

    CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberId);

    CustomerCheckInResponse checkInCustomerByBarber(long barberId);

    CustomerCheckoutResponse checkOut(Optional<Long> customerId);

    BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest);

    boolean isUserAlreadyCheckedIn(long userId);

    CheckIn findCheckedInBarberId(long customerId);

    BarberDetailsCalendar getUserCalendar(Set<BarberCalendar> barberCalendarSet);

    BarberWaitTimeResponse currentWaitTimeEstimateForCustomer(long barberId);

    BarberWaitTimeResponse currentWaitTimeEstimateForCustomerAtBarber(long customerId, long barberId);
}
