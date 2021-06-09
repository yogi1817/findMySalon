package com.spj.salon.checkin.adapters;

import com.spj.salon.barber.entities.Address;
import com.spj.salon.checkin.pojo.BarberDayOfWeekWithTime;
import com.spj.salon.openapi.resources.AddressInfo;
import com.spj.salon.openapi.resources.DayAndTimeInfo;
import com.spj.salon.openapi.resources.DayOfWeek;
import com.spj.salon.openapi.resources.UserProfile;
import com.spj.salon.user.entities.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", imports = DayOfWeek.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CheckInAdapterMapper {
    AddressInfo toResponse(Address address);

    @Mapping(target = "dayOfWeek", expression = "java(DayOfWeek.fromValue(barberDayOfWeekWithTime.getDayOfWeek().name()))")
    DayAndTimeInfo toResponse(BarberDayOfWeekWithTime barberDayOfWeekWithTime);

    List<DayAndTimeInfo> toResponseList(List<BarberDayOfWeekWithTime> barberDayOfWeekWithTime);

    UserProfile toUserProfile(User user);
}
