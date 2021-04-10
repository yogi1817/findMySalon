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

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "daily_barbers", schema = "usa",
        uniqueConstraints = @UniqueConstraint(columnNames = "daily_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DailyBarbers implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 7027628947547933472L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long dailyId;

    private String barbersDescription;
    private int barbersCount;
    @CreationTimestamp
    private OffsetDateTime createTimestamp;

    @Column(name = "user_id")
    private Long userId;
}
