package com.spj.salon.device.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "user_device", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDevice implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 5833513921844126261L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    private String nativeDeviceId;
    private Integer deviceTypeId;
    private String deviceModel;
    private Integer gmtOffsetMinutes;
    private String osVersion;
    private Boolean pushNotificationEnabled;
    private String pushToken;
    private String applicationVersion;
    private String email;
    private LocalDateTime lastLoggedInDate;
    private LocalDateTime dateAdded;
    private LocalDateTime dateUpdated;
}
