package com.spj.salon.otp.ports.in;

import com.spj.salon.openapi.resources.OtpResponse;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IMyOtpAdapter {
	OtpResponse sendOtpMessage();
	OtpResponse sendOtpMessage(String emailOrPhoneNo);
	OtpResponse validateOtp(int otpNumber);
}
