package com.spj.salon.user.repository;

import com.spj.salon.user.entities.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yogesh Sharma
 */

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    Authorities findByAuthority(String authority);
}
