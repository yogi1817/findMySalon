package com.spj.salon.customer.ports.in;

import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;
import com.spj.salon.openapi.resources.UpdatePasswordRequest;
import com.spj.salon.openapi.resources.UpdatePasswordResponse;

/**
 * @author Yogesh Sharma
 */
public interface ICustomerAdapter {

    CustomerFavouriteBarberResponse addFavouriteSalon(Long userId);

    UpdatePasswordResponse updatePassword(UpdatePasswordRequest user);
}