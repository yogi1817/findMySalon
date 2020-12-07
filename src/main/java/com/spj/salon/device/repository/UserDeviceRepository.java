package com.spj.salon.device.repository;

import com.spj.salon.device.entities.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Yogesh Sharma
 */
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    UserDevice findFirstByNativeDeviceId(String nativeDeviceId);

    void deleteByNativeDeviceId(String nativeDeviceId);

    List<UserDevice> findByEmail(String email);
}
