package com.spj.salon.user.repository;

import com.spj.salon.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yogesh Sharma
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPhone(String phoneNumber);

    User findByUserId(Long userId);
}
