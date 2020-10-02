package com.spj.salon.otp.facade;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IMyOtpService {
	void sendOtpMessage();
	void sendOtpMessage(String emailOrPhoneNo);
}
