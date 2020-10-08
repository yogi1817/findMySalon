package com.spj.salon.barber.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "services", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "service_id"))
@Data
public class SalonServices implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1359360042449055750L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long serviceId;

    private String serviceName;
    private String serviceDescription;
    @CreationTimestamp
    private OffsetDateTime createDate;
}
