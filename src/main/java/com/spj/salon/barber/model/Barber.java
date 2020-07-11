package com.spj.salon.barber.model;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.spj.salon.checkin.model.CheckIn;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "barber", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "barber_id"))
public class Barber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152775027804549377L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "barber_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long barberId;
	
	private String firstName;
	private String lastName;
	private String middleName;
	private String email;
	private String phone;
	private String loginId;
	private String password;
	private Date createDate;
	private Date modifyDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "barber_mapping_id", referencedColumnName = "barber_id")
	@OrderBy("createTimestamp DESC")
	private List<DailyBarbers> dailyBarberSet;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "barber_id", referencedColumnName = "barber_id")
	private Set<BarberServicesMapping> barberServicesMappingSet;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "barber_mapping_id", referencedColumnName = "barber_id")
	private Set<BarberCalendar> barberCalendarSet;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "barber_mapping_id", referencedColumnName = "barber_id")
	private Set<CheckIn> checkInSet;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "mapping_id", referencedColumnName = "barber_id")
	private Set<Address> addressSet;
	
	@Transient
	private Long authCode;
	
	/**
	 * @return the barberId
	 */
	public Long getBarberId() {
		return barberId;
	}
	/**
	 * @param barberId the barberId to set
	 */
	public void setBarberId(Long barberId) {
		this.barberId = barberId;
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
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return the authCode
	 */
	public Long getAuthCode() {
		return authCode;
	}
	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(Long authCode) {
		this.authCode = authCode;
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
	@Override
	public String toString() {
		return "Barber [barberId=" + barberId + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
				+ middleName + ", email=" + email + ", phone=" + phone + ", loginId=" + loginId + ", password="
				+ password + ", createDate=" + createDate + ", modifyDate=" + modifyDate + ", dailyBarberSet="
				+ dailyBarberSet + ", barberServicesMappingSet=" + barberServicesMappingSet + ", barberCalendarSet="
				+ barberCalendarSet + ", checkInSet=" + checkInSet + ", addressSet=" + addressSet + ", authCode="
				+ authCode + "]";
	}
}
