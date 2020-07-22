package com.spj.salon.barber.model;

import java.io.Serializable;

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

import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "authorities", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "authority_id"))
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
	
	@Column(name = "mapping_id")
	private Long mappingId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mapping_Id", referencedColumnName = "barber_id", insertable = false, updatable = false) 
    private Barber barber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mapping_Id", referencedColumnName = "user_id", insertable = false, updatable = false) 
    private User user;

	/**
	 * @return the authorityId
	 */
	public Long getAuthorityId() {
		return authorityId;
	}

	/**
	 * @param authorityId the authorityId to set
	 */
	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Authorities [authorityId=" + authorityId + ", authority=" + authority + ", mappingId=" + mappingId
				+ ", barber=" + barber + ", user=" + user + "]";
	}
}
