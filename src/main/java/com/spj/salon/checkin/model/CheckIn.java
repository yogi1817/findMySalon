package com.spj.salon.checkin.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "check_in", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "check_in_id"))
public class CheckIn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2071110993378978999L;

	public CheckIn() {
		
	}
	
	public CheckIn(Long barberId, Long userId, int time) {
		this.barberMappingId = barberId;
		this.eta = time;
		this.createTimestamp = new Date();
		this.userMappingId = userId;
		this.description = "";
		this.checkedOut = false;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "check_in_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long checkInId;
	
	@Column(name = "user_mapping_id")
	private Long userMappingId;

	@Column(name = "barber_mapping_id")
	private Long barberMappingId;
	
	@Column(name = "service_mapping_id")
	private Long serviceMappingId;
	
	private Date createTimestamp;
	private int eta;
	private String description;
	
	private boolean checkedOut;
	/**
	 * @return the checkInId
	 */
	public Long getCheckInId() {
		return checkInId;
	}
	/**
	 * @param checkInId the checkInId to set
	 */
	public void setCheckInId(Long checkInId) {
		this.checkInId = checkInId;
	}
	/**
	 * @return the userMappingId
	 */
	public Long getUserMappingId() {
		return userMappingId;
	}
	/**
	 * @param userMappingId the userMappingId to set
	 */
	public void setUserMappingId(Long userMappingId) {
		this.userMappingId = userMappingId;
	}
	/**
	 * @return the barberMappingId
	 */
	public Long getBarberMappingId() {
		return barberMappingId;
	}
	/**
	 * @param barberMappingId the barberMappingId to set
	 */
	public void setBarberMappingId(Long barberMappingId) {
		this.barberMappingId = barberMappingId;
	}
	/**
	 * @return the serviceMappingId
	 */
	public Long getServiceMappingId() {
		return serviceMappingId;
	}
	/**
	 * @param serviceMappingId the serviceMappingId to set
	 */
	public void setServiceMappingId(Long serviceMappingId) {
		this.serviceMappingId = serviceMappingId;
	}
	/**
	 * @return the createTimestamp
	 */
	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	/**
	 * @return the eta
	 */
	public int getEta() {
		return eta;
	}
	/**
	 * @param eta the eta to set
	 */
	public void setEta(int eta) {
		this.eta = eta;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the checkedOut
	 */
	public boolean isCheckedOut() {
		return checkedOut;
	}

	/**
	 * @param checkedOut the checkedOut to set
	 */
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	@Override
	public String toString() {
		return "CheckIn [checkInId=" + checkInId + ", userMappingId=" + userMappingId + ", barberMappingId="
				+ barberMappingId + ", serviceMappingId=" + serviceMappingId + ", createTimestamp=" + createTimestamp
				+ ", eta=" + eta + ", description=" + description + ", checkedOut=" + checkedOut + "]";
	}
}
