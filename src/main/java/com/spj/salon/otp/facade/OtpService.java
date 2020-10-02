package com.spj.salon.otp.facade;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class OtpService {

	private static final Integer EXPIRE_MINS = 30;

	private final LoadingCache<String, Integer> otpCache;
	
	/**
	 * This method will cache the OTP for 30 mins
	 */
	public OtpService() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(@NotNull String key) {
						return 0;
					}
				});
	}

	/**
	 * This method is used to push the opt number against Key. Rewrite the OTP if it exists.
	 * Using user id as key
	 * @param key
	 * @return
	 */
	public int generateOTP(String key) {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		return otp;
	}

	/**
	 * This method is used to return the OPT number against Key->Key values is username
	 * @param key
	 * @return
	 */
	public int getOtp(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * This method is used to clear the OTP catched already
	 * @param key
	 */
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
}