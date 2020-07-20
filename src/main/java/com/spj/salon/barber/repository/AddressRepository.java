package com.spj.salon.barber.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spj.salon.barber.model.Address;

/**
 * 
 * @author Yogesh Sharma
 *
 */

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{    
	
	/**
	 * Haversine formulae - instead of 3958.8 * 2 i changes it to 5000*2 
	 * to get road results approx. and not bird fly
	 * 
	 * @param longitude
	 * @param latitude
	 * @param distance
	 * @param long1
	 * @param long2
	 * @param lat1
	 * @param lat2
	 * @return
	 */
	@Query(value = "select t2.address_id, t2.distance from( "
							+"SELECT a.mapping_id, a.address_id, "
							+"10000 * "
							+ "ASIN"
							+ "(SQRT"
							+ "( "
								+ "POWER(SIN((?2 - a.latitude) * pi()/180 /2), 2)"
								+ "+COS(?2 * pi()/180 )"
								+ "*COS(a.latitude * pi()/180)"
								+ "*POWER(SIN((?1-a.longitude) * pi()/180 /2), 2)"
							+ ")"
							+ ") distance "
							+"FROM usa.address a "
							+ "where a.longitude between ?4 and ?5 "
							+ "and a.latitude between ?6 and ?7) t2 " 
							+ "where t2.distance < ?3 order by t2.distance", nativeQuery = true)
	public List<Map<String, Object>> getBarbersId(double longitude, double latitude, 
			double distance, double long1, double long2, double lat1, double lat2);
}
