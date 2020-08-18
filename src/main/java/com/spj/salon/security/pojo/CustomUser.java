package com.spj.salon.security.pojo;

import org.springframework.security.core.userdetails.User;

import com.spj.salon.utils.AuthorityUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
}