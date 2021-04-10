package com.spj.salon.user.ports.in;

import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;

public interface IRegisterCustomer {
    RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerBarberRequest);
}
