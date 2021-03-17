package com.spj.salon.checkin.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.DayOfWeek;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BarberDayOfWeekWithTime implements Serializable, Comparable<BarberDayOfWeekWithTime> {

    private DayOfWeek dayOfWeek ;
    private String salonOpenTime;
    private String salonCloseTime;

    @Override
    public int compareTo(@NotNull BarberDayOfWeekWithTime barberDayOfWeekWithTime) {
        return this.dayOfWeek.compareTo(barberDayOfWeekWithTime.dayOfWeek);
    }
}
