package com.spj.salon.barber.adapters;

import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.entities.Services;
import com.spj.salon.openapi.resources.BarberCalendarRequest;
import com.spj.salon.openapi.resources.BarberServicesRequest;
import com.spj.salon.openapi.resources.DailyBarbersRequest;
import com.spj.salon.utils.DateUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring", imports = {DateUtils.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BarberAdapterMapper {

    @Mapping(target = "userId")
    @Mapping(target = "dailyId")
    @Mapping(target = "createTimestamp")
    DailyBarbers fromRequest(DailyBarbersRequest source);

    @Mapping(target = "userId")
    @Mapping(target = "createDate")
    @Mapping(target = "barberCalendarId")
    @Mapping(source = "salonOpensAt", target = "salonOpenTime", dateFormat = "hh:mm aa")
    @Mapping(source = "salonClosesAt", target = "salonCloseTime", dateFormat = "hh:mm aa")
    @Mapping(source = "calendarDate", target = "calendarDate", dateFormat = "yyyy-MM-dd")
    BarberCalendar toDomain(BarberCalendarRequest barberCalendarRequest);

    @Mapping(target = "serviceId")
    @Mapping(target = "createDate")
    Services toDomain(BarberServicesRequest barberServicesRequest);

    default String unwrapJsonNullable(JsonNullable<BarberCalendarRequest.CalendarDayEnum> calendarDayEnumJsonNullable) {
        if (calendarDayEnumJsonNullable.isPresent() && calendarDayEnumJsonNullable.get() != null)
            return calendarDayEnumJsonNullable.get().getValue();
        return null;
    }
}
