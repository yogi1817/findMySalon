package com.spj.salon.customer.ports.in;

import com.spj.salon.openapi.resources.*;

import java.util.Optional;

/**
 *
 * @author Yogesh Sharma
 *
 */
public interface ICustomerAdapter {

	CustomerFavouriteBarberResponse addFavouriteSalon(Long userId);
	UpdatePasswordResponse updatePassword(UpdatePasswordRequest user);
	AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHost);
	AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String clientHost);
    CustomerProfile getCustomerProfile(Optional<Long> customerId);
}