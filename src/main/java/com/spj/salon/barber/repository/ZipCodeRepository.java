package com.spj.salon.barber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.model.ZipCodeLookup;

/**
 * 
 * @author Yogesh Sharma
 *
 */

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCodeLookup, Long>{

	public ZipCodeLookup findByZipCode(long zipCode);
}