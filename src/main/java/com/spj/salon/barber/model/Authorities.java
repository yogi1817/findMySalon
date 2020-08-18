package com.spj.salon.barber.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.spj.salon.user.model.User;

import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "authorities", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "authority_id"))
@Data
public class Authorities implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5833513921844126261L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authority_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long authorityId;
	
	private String authority;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="authority_id", referencedColumnName = "authority_id", insertable = false, updatable = false) 
    private User user;
}
