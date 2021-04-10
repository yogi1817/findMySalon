package com.spj.salon.barber.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "barber_calendar", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "barber_calendar_id"))
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BarberCalendar implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3785966067355560454L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barber_calendar_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long barberCalendarId;

    @Column(name = "user_id")
    private Long userId;

    //Date format "20:30 PM"
    private Date salonOpenTime;
    //Date format "20:30 PM"
    private Date salonCloseTime;
    private String calendarDay;
    //only populate if you wanted to overwrite the calendar day like 4th July.
    private Date calendarDate;

    @CreationTimestamp
    private OffsetDateTime createDate;
}
