package com.spj.salon.user.adapters;

import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import com.spj.salon.user.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper {
    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase")
    User toEntity(RegisterCustomerRequest registerCustomerRequest);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase")
    RegisterCustomerResponse toResponse(User register);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase")
    User toEntity(RegisterBarberRequest registerBarberRequest);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase")
    RegisterBarberResponse toBarberResponse(User register);

    @Named("toLowerCase")
    static String toLowerCase(String email) {
        return email.toLowerCase();
    }
}