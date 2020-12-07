package com.spj.salon.device.endpoints;

import com.spj.salon.device.entities.UserDevice;
import com.spj.salon.openapi.resources.DeviceInfoResponseDevices;
import com.spj.salon.openapi.resources.RegisterDeviceRequest;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceEndpointMapper {
    List<DeviceInfoResponseDevices> toResponse(List<UserDevice> source);

    default UserDevice toDevice(RegisterDeviceRequest source, String nativeDeviceId) {
        return toDevice(source, new RegisterDeviceRequestContext(nativeDeviceId));
    }

    UserDevice toDevice(RegisterDeviceRequest source, @Context RegisterDeviceRequestContext context);

    @AllArgsConstructor
    @Value
    class RegisterDeviceRequestContext {
        String nativeDeviceId;
    }
}
