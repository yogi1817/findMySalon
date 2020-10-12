package com.spj.salon.barber.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author Yogesh Sharma
 */
@Entity
@Table(name = "address", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "address_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5833513921844126261L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", unique = true, nullable = false, columnDefinition = "serial")
    private Long addressId;

    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String state;
    private String zip;
    private String country;
    @CreationTimestamp
    private OffsetDateTime createDate;
    @UpdateTimestamp
    private OffsetDateTime modifyDate;
    private double longitude;
    private double latitude;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_Id", referencedColumnName = "user_id", insertable = false, updatable = false) 
    private User user;*/

    @Column(name = "user_id")
    private Long userId;

    /**
     * @return
     */
    public String getAddress() {
        StringBuilder address = new StringBuilder();
        address.append(addressLineOne);
        address.append(StringUtils.isEmpty(addressLineTwo) ? "" : " " + addressLineTwo);
        address.append(" ").append(city);
        address.append(" ").append(state);
        address.append(" ").append(zip);
        return address.toString();
    }
}
