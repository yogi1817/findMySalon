package com.spj.salon.otp.facade;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IMyOtpService {
	public void sendOtpMessage();
	public void sendOtpMessage(String emailOrPhoneNo);
}
