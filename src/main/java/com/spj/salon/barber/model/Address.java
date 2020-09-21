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

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "address", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "address_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_Id", referencedColumnName = "user_id", insertable = false, updatable = false) 
    private User user;*/
	
	@Column(name = "user_id")
	private Long userId;
	
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
