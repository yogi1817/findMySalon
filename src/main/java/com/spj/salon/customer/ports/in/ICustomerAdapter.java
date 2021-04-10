package com.spj.salon.customer.ports.in;

import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;

/**
 * @author Yogesh Sharma
 */
public interface ICustomerAdapter {
    CustomerFavouriteBarberResponse addFavouriteSalon(Long userId);
}