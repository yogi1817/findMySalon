package com.spj.salon.user.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "authorities", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "authority_id"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authorities implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 5833513921844126261L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long authorityId;

    private String authority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", referencedColumnName = "authority_id", insertable = false, updatable = false)
    private User user;
}
