package com.spj.salon.security.pojo;

import org.springframework.security.core.userdetails.User;

import com.spj.salon.utils.AuthorityUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String first_name;
	private String last_name;
	private String mobile;
	private String storeName;
	private String loginId;
	private String email;
	
	public CustomUser(com.spj.salon.user.model.User user) {
		super(user.getLoginId(), user.getPassword(), AuthorityUtils.getUserAuthorities(user));
		this.id = user.getUserId();
		this.first_name = user.getFirstName();
		this.last_name = user.getLastName();
		this.mobile = user.getPhone();
		this.storeName = user.getStoreName();
		this.loginId = user.getLoginId();
		this.email = user.getEmail();
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