package com.spj.salon.user.adapters;

import com.spj.salon.barber.entities.Address;
import com.spj.salon.openapi.resources.AddressInfo;
import com.spj.salon.openapi.resources.UserAddressRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserAdapterMapper {
    Address toDomain(UserAddressRequest userAddressRequest);

    AddressInfo toResponse(Address address);
}
