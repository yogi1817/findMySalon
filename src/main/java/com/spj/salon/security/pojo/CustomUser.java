package com.spj.salon.security.pojo;

import org.springframework.security.core.userdetails.User;

import com.spj.salon.utils.AuthorityUtils;

public class CustomUser extends User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String first_name;
	private String last_name;
	private String mobile;
	private String loginType;
	private Long favouriteSalonId;
	
	public CustomUser(com.spj.salon.user.model.User user) {
		super(user.getEmail(), user.getPassword(), AuthorityUtils.getUserAuthorities());
		this.id = user.getUserId();
		this.first_name = user.getFirstName();
		this.last_name = user.getLastName();
		this.mobile = user.getPhone();
		this.loginType = user.getLoginType();
		this.favouriteSalonId = user.getFavouriteSalonId();
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
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the favouriteSalonId
	 */
	public Long getFavouriteSalonId() {
		return favouriteSalonId;
	}

	/**
	 * @param favouriteSalonId the favouriteSalonId to set
	 */
	public void setFavouriteSalonId(Long favouriteSalonId) {
		this.favouriteSalonId = favouriteSalonId;
	}
}