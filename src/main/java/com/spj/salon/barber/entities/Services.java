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
@Table(name = "services", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "service_id"))
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Services implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -8744555709150112945L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long serviceId;

    private String serviceName;
    private String serviceDescription;
    @CreationTimestamp
    private OffsetDateTime createDate;
}
