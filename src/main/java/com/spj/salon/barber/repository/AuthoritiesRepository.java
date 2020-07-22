package com.spj.salon.barber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.model.Authorities;

/**
 * 
 * @author Yogesh Sharma
 *
 */

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long>{

}
