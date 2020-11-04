package com.spj.salon.checkin.adapters;

import com.spj.salon.openapi.resources.*;

import java.util.Optional;

public interface ICheckinFacade {

	BarberWaitTimeResponse waitTimeEstimate(long barberId);
	CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberId) ;
	CustomerCheckInResponse checkInCustomerByBarber(long barberId) ;
	CustomerCheckoutResponse checkOut(long userId);
	BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest) ;

	BarberWaitTimeResponse waitTimeEstimateAtBarberForCustomerInOauthHeader();
}
