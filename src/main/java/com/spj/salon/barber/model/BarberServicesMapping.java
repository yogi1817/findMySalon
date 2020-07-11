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
	
	@Column(name = "barber_id")
	private Long barberId;
	
	@Column(name = "service_id")
	private Long serviceId;
	private float serviceCharges;
	private int	timeToPerform;
	private Date createDate;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "service_id", referencedColumnName = "service_id", insertable = false, updatable = false)
	private SalonServices salonServices;

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
		return "BarberServicesMapping [barberId=" + barberId + ", serviceId=" + serviceId + ", serviceCharges="
				+ serviceCharges + ", timeToPerform=" + timeToPerform + ", createDate=" + createDate
				+ ", salonServices=" + salonServices + "]";
	}
}
