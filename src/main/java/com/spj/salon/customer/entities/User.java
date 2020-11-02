package com.spj.salon.customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spj.salon.barber.entities.*;
import com.spj.salon.checkin.entities.CheckIn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "user", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "addressSet"})
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2761741425148039955L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long userId;

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    @CreationTimestamp
    private OffsetDateTime createDate;
    @UpdateTimestamp
    private OffsetDateTime modifyDate;
    private String storeName;
    private String loginSource;
    private Long favouriteSalonId;
    @Builder.Default
    private boolean verified = false;

    @Column(name = "authority_id")
    private long authorityId;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", referencedColumnName = "authority_id", insertable = false, updatable = false)
    private Authorities authority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OrderBy("createTimestamp DESC")
    private List<DailyBarbers> dailyBarberSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Set<BarberServicesMapping> barberServicesMappingSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Set<BarberCalendar> barberCalendarSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_mapping_id", referencedColumnName = "user_id")
    private Set<CheckIn> checkInSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Set<Address> addressSet;

    /**
     * @return the dailyBarberSet
     */
    public List<DailyBarbers> getDailyBarberSet() {
        return dailyBarberSet == null ? new ArrayList<>() : dailyBarberSet;
    }

    /**
     * @return the barberServicesMappingSet
     */
    public Set<BarberServicesMapping> getBarberServicesMappingSet() {
        return barberServicesMappingSet == null ? new HashSet<>() : barberServicesMappingSet;
    }

    /**
     * @return the barberCalendarSet
     */
    public Set<BarberCalendar> getBarberCalendarSet() {
        return barberCalendarSet == null ? new HashSet<>() : barberCalendarSet;
    }

    /**
     * @return the checkInSet
     */
    public Set<CheckIn> getCheckInSet() {
        return checkInSet == null ? new HashSet<>() : checkInSet;
    }

    /**
     * @return the addressSet
     */
    public Set<Address> getAddressSet() {
        return addressSet == null ? new HashSet<>() : addressSet;
    }
}
