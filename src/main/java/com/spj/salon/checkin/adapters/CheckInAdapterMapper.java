package com.spj.salon.checkin.adapters;

import com.spj.salon.checkin.pojo.BarberDayOfWeekWithTime;
import com.spj.salon.openapi.resources.DayAndTimeInfo;
import com.spj.salon.openapi.resources.DayOfWeek;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", imports = DayOfWeek.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CheckInAdapterMapper {
    @Mapping(target = "dayOfWeek", expression = "java(DayOfWeek.fromValue(barberDayOfWeekWithTime.getDayOfWeek().name()))")
    DayAndTimeInfo toResponse(BarberDayOfWeekWithTime barberDayOfWeekWithTime);

    List<DayAndTimeInfo> toResponseList(List<BarberDayOfWeekWithTime> barberDayOfWeekWithTime);
}
