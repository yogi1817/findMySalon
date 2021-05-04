package com.spj.salon.checkin.repository;

import com.spj.salon.checkin.entities.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yogesh Sharma
 */
@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    //There should be only 1 record for a user unless the barber dosent check him out
    List<CheckIn> findByUserMappingIdAndCheckedOutAndCreateDate(Long userMappingId, boolean checkedOut, LocalDate todaysDate);

    int countByUserMappingIdAndCheckedOutAndCreateDate(long id, boolean checkoutOut, LocalDate todaysDate);

    List<CheckIn> findByBarberMappingIdAndCreateDateOrderByCheckInTimestampAsc(Long barberMappingId, LocalDate todaysDate);

}
