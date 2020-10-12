package com.spj.salon.barber.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "barber_services", schema = "usa")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BarberServicesMapping implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6214756683528070357L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barber_services_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long barberServicesId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "service_id")
    private Long serviceId;
    private float serviceCharges;
    private int timeToPerform;
    @CreationTimestamp
    private OffsetDateTime createDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "service_id", insertable = false, updatable = false)
    private SalonServices salonServices;
}
