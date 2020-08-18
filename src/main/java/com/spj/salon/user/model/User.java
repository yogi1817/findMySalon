package com.spj.salon.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.BarberServicesMapping;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.checkin.model.CheckIn;

import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "user", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "addressSet"})
@Data
public class User implements Serializable{

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
	private String loginId;
	private String password;
	private Date createDate;
	private Date modifyDate;
	private String storeName;
	private String loginSource;
	private Long favouriteSalonId;	
	private boolean verified = false;
	
	@Transient
	private String jwtToken;
	
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
	
	public User(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}
	
	public User() {
	}
	
	/**
	 * @return the dailyBarberSet
	 */
	public List<DailyBarbers> getDailyBarberSet() {
		return dailyBarberSet==null? new ArrayList<>(): dailyBarberSet;
	}
	
	/**
	 * @return the barberServicesMappingSet
	 */
	public Set<BarberServicesMapping> getBarberServicesMappingSet() {
		return barberServicesMappingSet==null?new HashSet<>():barberServicesMappingSet;
	}
	
	/**
	 * @return the barberCalendarSet
	 */
	public Set<BarberCalendar> getBarberCalendarSet() {
		return barberCalendarSet==null? new HashSet<>(): barberCalendarSet;
	}	
	
	/**
	 * @return the checkInSet
	 */
	public Set<CheckIn> getCheckInSet() {
		return checkInSet==null? new HashSet<>():checkInSet;
	}
	
	/**
	 * @return the addressSet
	 */
	public Set<Address> getAddressSet() {
		return addressSet==null?new HashSet<>(): addressSet;
	}
}
