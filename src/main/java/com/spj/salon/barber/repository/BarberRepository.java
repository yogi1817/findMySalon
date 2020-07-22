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

	public long countByEmail(String email);
	public long countByLoginId(String loginId);
	public long countByPhone(String phone);
	public Barber findByLoginId(String loginId);
}
