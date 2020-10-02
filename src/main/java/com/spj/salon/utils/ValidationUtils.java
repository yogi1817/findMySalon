package com.spj.salon.utils;

import com.spj.salon.barber.entities.Authorities;
import com.spj.salon.customer.model.User;
import com.spj.salon.exception.NotFoundCustomException;
import org.springframework.util.StringUtils;

/**
 * @author Yogesh Sharma
 */
public class ValidationUtils {

    private ValidationUtils() {

    }

    /**
     * @param user
     * @return
     */
    public static User validateUser(User user, Authorities authotities) {

        if (StringUtils.isEmpty(user.getEmail()))
            throw new NotFoundCustomException("Email Cannot be blank", "Please add email id to your request");
        if (user.getModifyDate() == null)
            user.setModifyDate(DateUtils.getTodaysDate());

        if (user.getCreateDate() == null)
            user.setCreateDate(DateUtils.getTodaysDate());

        if (StringUtils.isEmpty(user.getEmail())) {
            user.setEmail(user.getEmail());
        }

        user.setAuthorityId(authotities.getAuthorityId());
        return user;
    }
}
