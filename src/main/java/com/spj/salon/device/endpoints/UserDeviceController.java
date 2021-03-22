package com.spj.salon.device.endpoints;

import com.spj.salon.device.entities.UserDevice;
import com.spj.salon.device.ports.in.IDeviceService;
import com.spj.salon.openapi.endpoint.DevicesApiDelegate;
import com.spj.salon.openapi.resources.DeviceInfoResponse;
import com.spj.salon.openapi.resources.DeviceTypeResponse;
import com.spj.salon.openapi.resources.RegisterDeviceRequest;
import com.spj.salon.openapi.resources.RegisterDeviceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Yogesh Sharma
 */
@Controller
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class UserDeviceController implements DevicesApiDelegate {

    private final IDeviceService deviceService;
    private final DeviceEndpointMapper endpointMapper;

    @Override
    public ResponseEntity<Void> deregisterDevice(String nativeDeviceId) {
        try {
            deviceService.deregisterDevice(nativeDeviceId);
        } catch (NoSuchElementException e) {
            log.error("Device De-Registration failed, device not found: {}", nativeDeviceId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Device De-Registration failed, exception caught: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<DeviceInfoResponse> getAllDevices(String email) {
        try {
            List<UserDevice> devices = deviceService.getAllDevicesByScorecardId(email);
            return ResponseEntity.ok(new DeviceInfoResponse().devices(endpointMapper.toResponse(devices)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<DeviceTypeResponse> getNativeDeviceInformation(String nativeDeviceId) {
        try {
            UserDevice device = deviceService.getDeviceByNativeDeviceId(nativeDeviceId);
            if (device == null) throw new NoSuchElementException();
            Integer deviceTypeId = device.getDeviceTypeId();
            String appVersion = device.getApplicationVersion();
            return ResponseEntity.ok(new DeviceTypeResponse()
                    .deviceType(deviceTypeId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<RegisterDeviceResponse> newRegisterDevice(String nativeDeviceId, RegisterDeviceRequest registerDeviceRequest) {
        try {
            deviceService.newRegisterDevice(endpointMapper.toDevice(registerDeviceRequest, nativeDeviceId));
            return ResponseEntity.ok(new RegisterDeviceResponse().message("OK"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegisterDeviceResponse().message(ex.getMessage()));
        }
    }
}
