package com.spj.salon.user.ports.in;

import com.spj.salon.user.entities.User;

import java.util.List;

/**
 * @author Yogesh Sharma
 */
public interface IUserDao {
    List<User> searchUserWithEmailAndAuthority(String email, String authority);

    List<User> searchUserWithUserIdAndAuthority(Long userId, String authority);
}
