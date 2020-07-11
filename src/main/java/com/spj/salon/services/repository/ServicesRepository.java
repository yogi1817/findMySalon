package com.spj.salon.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.services.model.Services;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface ServicesRepository extends JpaRepository<Services, Long>{

}
