package com.spj.salon.barber.model;

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
@Table(name = "services", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "service_id"))
public class SalonServices implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1359360042449055750L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long serviceId;
	
	private String serviceName;
	private String serviceDescription;
	private Date createDate;
	private Date modifyDate;
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
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}
	/**
	 * @param serviceDescription the serviceDescription to set
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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
	@Override
	public String toString() {
		return "SalonServices [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceDescription="
				+ serviceDescription + ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}
}
