package com.spj.salon.customer.ports.in;

import com.spj.salon.openapi.resources.AuthenticationRequest;
import com.spj.salon.openapi.resources.AuthenticationResponse;
import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;
import com.spj.salon.openapi.resources.RefreshRequest;

/**
 * @author Yogesh Sharma
 */
public interface ICustomerAdapter {
    CustomerFavouriteBarberResponse addFavouriteSalon(Long userId);

    AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader);

    AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String orElse);
}