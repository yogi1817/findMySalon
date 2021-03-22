package com.spj.salon.checkin.adapters;

import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.openapi.resources.*;

import java.util.Optional;

public interface ICheckinFacade {

	BarberWaitTimeResponse waitTimeEstimate(long barberId);
	CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberId) ;
	CustomerCheckInResponse checkInCustomerByBarber(long barberId) ;
	CustomerCheckoutResponse checkOut(Optional<Long> customerId);
	BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest) ;
	BarberWaitTimeResponse waitTimeEstimateAtBarberForCustomerInOauthHeader();
	boolean isUserAlreadyCheckedIn(long userId);
	CheckIn findCheckedInBarberId(long customerId);
}
