package com.spj.salon.barber.ports.in;

import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.security.pojo.UserType;

public interface IRegisterBarber {
    public RegisterBarberResponse registerBarber(RegisterBarberRequest registerBarberRequest);
}
