package com.spj.salon.customer.adapters;

import com.spj.salon.customer.entities.User;
import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import org.mapstruct.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper {
    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase" )
    User toEntity(RegisterCustomerRequest registerCustomerRequest);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase" )
    RegisterCustomerResponse toResponse(User register);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase" )
    User toEntity(RegisterBarberRequest registerBarberRequest);

    @Mapping(source = "email", target = "email", qualifiedByName = "toLowerCase" )
    RegisterBarberResponse toBarberResponse(User register);

    @Named("toLowerCase")
    public static String toLowerCase(String email) {
        return email.toLowerCase();
    }
}