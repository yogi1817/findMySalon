package com.spj.salon.security.pojo;

import com.spj.salon.utils.AuthorityUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.User;

/**
 * @author Yogesh Sharma
 */
@Data
@EqualsAndHashCode()
public class CustomUser extends User {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String first_name;
    private String last_name;
    private String mobile;
    private String storeName;
    private String email;

    public CustomUser(com.spj.salon.customer.entities.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.getUserAuthorities(user));
        this.id = user.getUserId();
        this.first_name = user.getFirstName();
        this.last_name = user.getLastName();
        this.mobile = user.getPhone();
        this.storeName = user.getStoreName();
        this.email = user.getEmail();
    }
}