package com.spj.salon.checkin.adapters;

import com.spj.salon.openapi.resources.*;

public interface ICheckinFacade {

	BarberWaitTimeResponse waitTimeEstimate(long barberId);
	CustomerCheckInResponse checkInCustomerByCustomer(long barberId) ;
	CustomerCheckInResponse checkInCustomerByBarber(long barberId) ;
	CustomerCheckoutResponse checkOut(long userId);
	BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest) ;
}
