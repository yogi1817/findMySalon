package com.spj.salon.barber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.entities.Services;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface ServicesRepository extends JpaRepository<Services, Long>{

}
