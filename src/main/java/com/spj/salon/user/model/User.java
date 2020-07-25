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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonSetter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.BarberServicesMapping;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.checkin.model.CheckIn;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "user", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "addressSet"})
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
	private boolean verified = false;;
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private List<Authorities> authorities;
	
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
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the favouriteSalonId
	 */
	public Long getFavouriteSalonId() {
		return favouriteSalonId;
	}
	/**
	 * @param favouriteSalonId the favouriteSalonId to set
	 */
	public void setFavouriteSalonId(Long favouriteSalonId) {
		this.favouriteSalonId = favouriteSalonId;
	}
	/**
	 * @return the createDate
	 */
	@JsonGetter
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	@JsonSetter
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the modifyDate
	 */
	@JsonGetter
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	@JsonSetter
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * @return the loginSource
	 */
	public String getLoginSource() {
		return loginSource;
	}
	/**
	 * @param loginSource the loginSource to set
	 */
	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}
	/**
	 * @return the dailyBarberSet
	 */
	public List<DailyBarbers> getDailyBarberSet() {
		return dailyBarberSet==null? new ArrayList<>(): dailyBarberSet;
	}
	/**
	 * @param dailyBarberSet the dailyBarberSet to set
	 */
	public void setDailyBarberSet(List<DailyBarbers> dailyBarberSet) {
		this.dailyBarberSet = dailyBarberSet;
	}
	/**
	 * @return the barberServicesMappingSet
	 */
	public Set<BarberServicesMapping> getBarberServicesMappingSet() {
		return barberServicesMappingSet==null?new HashSet<>():barberServicesMappingSet;
	}
	/**
	 * @param barberServicesMappingSet the barberServicesMappingSet to set
	 */
	public void setBarberServicesMappingSet(Set<BarberServicesMapping> barberServicesMappingSet) {
		this.barberServicesMappingSet = barberServicesMappingSet;
	}
	
	/**
	 * @return the barberCalendarSet
	 */
	public Set<BarberCalendar> getBarberCalendarSet() {
		return barberCalendarSet==null? new HashSet<>(): barberCalendarSet;
	}
	/**
	 * @param barberCalendarSet the barberCalendarSet to set
	 */
	public void setBarberCalendarSet(Set<BarberCalendar> barberCalendarSet) {
		this.barberCalendarSet = barberCalendarSet;
	}
	
	
	/**
	 * @return the checkInSet
	 */
	public Set<CheckIn> getCheckInSet() {
		return checkInSet==null? new HashSet<>():checkInSet;
	}
	/**
	 * @param checkInSet the checkInSet to set
	 */
	public void setCheckInSet(Set<CheckIn> checkInSet) {
		this.checkInSet = checkInSet;
	}
	/**
	 * @return the addressSet
	 */
	public Set<Address> getAddressSet() {
		return addressSet==null?new HashSet<>(): addressSet;
	}
	/**
	 * @param addressSet the addressSet to set
	 */
	public void setAddressSet(Set<Address> addressSet) {
		this.addressSet = addressSet;
	}

	/**
	 * @return the authorities
	 */
	public List<Authorities> getAuthorities() {
		return authorities==null? new ArrayList<>() : authorities;
	}
	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}
	
	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}
	/**
	 * @param verified the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
				+ middleName + ", email=" + email + ", phone=" + phone + ", loginId=" + loginId + ", password="
				+ password + ", createDate=" + createDate + ", modifyDate=" + modifyDate + ", storeName=" + storeName
				+ ", loginSource=" + loginSource + ", favouriteSalonId=" + favouriteSalonId + ", verified=" + verified
				+ ", authorities=" + authorities + ", dailyBarberSet=" + dailyBarberSet + ", barberServicesMappingSet="
				+ barberServicesMappingSet + ", barberCalendarSet=" + barberCalendarSet + ", checkInSet=" + checkInSet
				+ ", addressSet=" + addressSet + "]";
	}
}
