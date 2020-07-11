package com.spj.salon.checkin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.checkin.model.CheckIn;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long>{
	
	//There should be only 1 record for a user unless the barber dosent check him out
	List<CheckIn> findByUserMappingIdAndCheckedOut(Long userMappingId, boolean checkodOut);
}
