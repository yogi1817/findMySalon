package com.spj.salon.barber.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "barber_services", schema = "usa")
public class BarberServicesMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6214756683528070357L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "barber_services_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long barberServicesId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "service_id")
	private Long serviceId;
	private float serviceCharges;
	private int	timeToPerform;
	private Date createDate;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", referencedColumnName = "service_id", insertable = false, updatable = false)
	private SalonServices salonServices;

	/**
	 * @return the barberServicesId
	 */
	public Long getBarberServicesId() {
		return barberServicesId;
	}

	/**
	 * @param barberServicesId the barberServicesId to set
	 */
	public void setBarberServicesId(Long barberServicesId) {
		this.barberServicesId = barberServicesId;
	}

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
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceCharges
	 */
	public float getServiceCharges() {
		return serviceCharges;
	}

	/**
	 * @param serviceCharges the serviceCharges to set
	 */
	public void setServiceCharges(float serviceCharges) {
		this.serviceCharges = serviceCharges;
	}

	/**
	 * @return the timeToPerform
	 */
	public int getTimeToPerform() {
		return timeToPerform;
	}

	/**
	 * @param timeToPerform the timeToPerform to set
	 */
	public void setTimeToPerform(int timeToPerform) {
		this.timeToPerform = timeToPerform;
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
	 * @return the salonServices
	 */
	public SalonServices getSalonServices() {
		return salonServices;
	}

	/**
	 * @param salonServices the salonServices to set
	 */
	public void setSalonServices(SalonServices salonServices) {
		this.salonServices = salonServices;
	}

	@Override
	public String toString() {
		return "BarberServicesMapping [userId=" + userId + ", serviceId=" + serviceId + ", serviceCharges="
				+ serviceCharges + ", timeToPerform=" + timeToPerform + ", createDate=" + createDate
				+ ", salonServices=" + salonServices + "]";
	}
}
