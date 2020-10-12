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
@Table(name = "zip_code_lookup", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class ZipCodeLookup implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1359360042449055750L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    @Column(unique = true)
    private Long zipCode;
    private double longitude;
    private double latitude;
    @CreationTimestamp
    private OffsetDateTime createDate;
}
