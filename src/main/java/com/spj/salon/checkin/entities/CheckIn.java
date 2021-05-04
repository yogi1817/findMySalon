package com.spj.salon.checkin.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * @author Yogesh Sharma
 */
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "check_in", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "check_in_id"))
public class CheckIn implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2071110993378978999L;

    public CheckIn(Long barberId, Long userId, int time, Long updatedBy) {
        this.barberMappingId = barberId;
        this.eta = time;
        this.userMappingId = userId;
        this.description = "";
        this.checkedOut = false;
        this.updatedBy = updatedBy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_in_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long checkInId;

    @Column(name = "user_mapping_id")
    private Long userMappingId;

    @Column(name = "barber_mapping_id")
    private Long barberMappingId;

    @Column(name = "service_mapping_id")
    private Long serviceMappingId;

    @CreationTimestamp
    private OffsetDateTime checkInTimestamp;
    private OffsetDateTime checkOutTimestamp;

    @CreationTimestamp
    private LocalDate createDate;
    private int eta;
    private String description;

    private boolean checkedOut;
    private Long updatedBy;

    @Transient
    private int rank;
}
