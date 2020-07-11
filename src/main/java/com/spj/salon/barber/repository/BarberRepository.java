package com.spj.salon.barber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.model.Barber;

/**
 * 
 * @author Yogesh Sharma
 *
 */

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long>{

	long countByEmail(String email);
	long countByPhone(String phone);
}
