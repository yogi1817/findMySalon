package com.spj.salon.barber.adapters;

import com.spj.salon.barber.entities.Address;
import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.openapi.resources.BarberAddressRequest;
import com.spj.salon.openapi.resources.BarberCalendarRequest;
import com.spj.salon.openapi.resources.DailyBarbersRequest;
import com.spj.salon.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", imports = {DateUtils.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BarberAdapterMapper {

    DailyBarbers fromRequest(DailyBarbersRequest source);

    @Mapping(source = "salonOpensAt", target = "salonOpenTime", dateFormat = "hh:mm aa")
    @Mapping(source = "salonClosesAt", target = "salonCloseTime", dateFormat = "hh:mm aa")
    @Mapping(source = "calendarDate", target = "calendarDate", dateFormat = "yyyy-MM-dd")
    BarberCalendar toDomain(BarberCalendarRequest barberCalendarRequest);

    Address toDomain(BarberAddressRequest barberAddressRequest);
}
