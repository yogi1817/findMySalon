package com.spj.salon.barber.ports.in;

import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;

public interface IRegisterBarber {
    RegisterBarberResponse registerBarber(RegisterBarberRequest registerBarberRequest);
}
