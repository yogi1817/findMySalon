package com.spj.salon.device.ports.in;

import com.spj.salon.device.entities.UserDevice;

import java.util.List;

/**
 * @author Yogesh Sharma
 */
public interface IDeviceService {
    void deregisterDevice(String nativeDeviceId);
    List<UserDevice> getAllDevicesByScorecardId(String scorecardId);
    UserDevice getDeviceByNativeDeviceId(String nativeDeviceId);
    UserDevice newRegisterDevice(UserDevice device);
}
