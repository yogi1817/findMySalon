package com.spj.salon.device.adapters;

import com.spj.salon.device.entities.UserDevice;
import com.spj.salon.device.ports.in.IDeviceService;
import com.spj.salon.device.repository.UserDeviceRepository;
import com.spj.salon.exception.NotFoundCustomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Yogesh Sharma
 */
@Service
@AllArgsConstructor
@Slf4j
public class DeviceServiceImpl implements IDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Override
    public void deregisterDevice(String nativeDeviceId) {
        if(getDeviceByNativeDeviceId(nativeDeviceId)==null){
            throw new NotFoundCustomException("No Device found", nativeDeviceId);
        }

        userDeviceRepository.deleteByNativeDeviceId(nativeDeviceId);
    }

    @Override
    public List<UserDevice> getAllDevicesByScorecardId(String email) {
        return userDeviceRepository.findByEmail(email);
    }

    @Override
    public UserDevice getDeviceByNativeDeviceId(String nativeDeviceId) {
        UserDevice foundDevice = userDeviceRepository.findFirstByNativeDeviceId(nativeDeviceId);
        if (foundDevice == null) {
            return null;
        }

        return foundDevice;
    }

    @Override
    public UserDevice newRegisterDevice(UserDevice device) {
        return userDeviceRepository.save(device);
    }
}
