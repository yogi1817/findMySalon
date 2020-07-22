package com.spj.salon.security.pojo;

import org.springframework.security.core.userdetails.User;

import com.spj.salon.barber.model.Barber;
import com.spj.salon.utils.AuthorityUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class CustomBarber extends User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String first_name;
	private String last_name;
	private String mobile;
	private String storeName;
	private String loginId;
	private String email;
	
	public CustomBarber(Barber barber) {
		super(barber.getLoginId(), barber.getPassword(), AuthorityUtils.getBarberAuthorities());
		this.id = barber.getBarberId();
		this.first_name = barber.getFirstName();
		this.last_name = barber.getLastName();
		this.mobile = barber.getPhone();
		this.storeName = barber.getStoreName();
		this.loginId = barber.getLoginId();
		this.email = barber.getEmail();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}