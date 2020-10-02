package com.spj.salon.common.register;

import com.spj.salon.customer.model.User;
import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper {
    User toEntity(RegisterCustomerRequest registerCustomerRequest);

    RegisterCustomerResponse toResponse(User register);

    User toEntity(RegisterBarberRequest registerBarberRequest);

    RegisterBarberResponse toBarberResponse(User register);
}
