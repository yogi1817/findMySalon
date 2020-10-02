package com.spj.salon.customer.ports.in;

import com.spj.salon.customer.model.User;
import com.spj.salon.openapi.resources.*;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface ICustomerAdapter {

	CustomerFavouriteBarberResponse addFavouriteSalon(Long userId);
	boolean updateVerifiedFlag(String loginId);
	UpdatePasswordResponse updatePassword(UpdatePasswordRequest user, String clientHost);
	AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHost);
}