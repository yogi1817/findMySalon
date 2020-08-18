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

import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "services", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "service_id"))
@Data
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
}
