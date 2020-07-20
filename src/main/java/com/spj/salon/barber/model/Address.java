package com.spj.salon.barber.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.StringUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "address", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "address_id"))
public class Address implements Serializable{

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
	private Date createDate;
	private Date modifyDate;
	private double longitude;
	private double latitude;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mapping_Id", referencedColumnName = "barber_id", insertable = false, updatable = false) 
    private Barber barber;
	
	@Column(name = "mapping_id")
	private Long mappingId;
	/**
	 * @return the addressId
	 */
	public Long getAddressId() {
		return addressId;
	}
	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	/**
	 * @return the addressLineOne
	 */
	public String getAddressLineOne() {
		return addressLineOne;
	}
	/**
	 * @param addressLineOne the addressLineOne to set
	 */
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	/**
	 * @return the addressLineTwo
	 */
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	/**
	 * @param addressLineTwo the addressLineTwo to set
	 */
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * @return the mappingId
	 */
	public Long getMappingId() {
		return mappingId;
	}
	/**
	 * @param mappingId the mappingId to set
	 */
	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}
	
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", addressLineOne=" + addressLineOne + ", addressLineTwo="
				+ addressLineTwo + ", city=" + city + ", state=" + state + ", zip=" + zip + ", country=" + country
				+ ", createDate=" + createDate + ", modifyDate=" + modifyDate + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", mappingId=" + mappingId + "]";
	}
	
	/**
	 * @return the barber
	 */
	public Barber getBarber() {
		return barber;
	}
	
	/**
	 * @param barber the barber to set
	 */
	public void setBarber(Barber barber) {
		this.barber = barber;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getAddress() {
		StringBuilder address = new StringBuilder();
		address.append(addressLineOne);
		address.append(StringUtils.isEmpty(addressLineTwo)?"":" "+addressLineTwo);
		address.append(" "+ city);
		address.append(" "+ state);
		address.append(" "+ zip);
		return address.toString();
	}
}
