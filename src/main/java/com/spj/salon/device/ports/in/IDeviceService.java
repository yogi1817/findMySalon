package com.spj.salon.device.ports.in;

import com.spj.salon.device.entities.UserDevice;

import java.util.List;

/**
 * @author Yogesh Sharma
 */
public interface IDeviceService {
    public void deregisterDevice(String nativeDeviceId);
    public List<UserDevice> getAllDevicesByScorecardId(String scorecardId);
    public UserDevice getDeviceByNativeDeviceId(String nativeDeviceId);
    public UserDevice newRegisterDevice(UserDevice device);
}
