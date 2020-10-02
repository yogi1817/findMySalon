package com.spj.salon.barber.entities;

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

import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "barber_services", schema = "usa")
@Data
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
}
