package com.spj.salon.barber.entities;

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
@Table(name = "zip_code_lookup", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Data
public class ZipCodeLookup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1359360042449055750L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "serial")
	private Long id;
	
	@Column(unique = true)
	private Long zipCode;
	private double longitude;
	private double latitude;
	private Date createDate;
}
