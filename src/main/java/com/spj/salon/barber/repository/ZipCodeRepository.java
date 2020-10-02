package com.spj.salon.barber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.entities.ZipCodeLookup;

/**
 * 
 * @author Yogesh Sharma
 *
 */

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCodeLookup, Long>{

	ZipCodeLookup findByZipCode(long zipCode);
}