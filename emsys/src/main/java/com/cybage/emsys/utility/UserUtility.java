package com.cybage.emsys.utility;

import java.util.Random;

public class UserUtility {

	public static final String USER_EXISTS = "User already exists";
	public static final String USER_NOT_FOUND = "User not found";
	public static final String WRONG_PASSWORD = "Wrong Password";
	public static final String ACCOUNT_BLOCKED = "Your account has been blocked due to 3 failed attempts";
	public static final String OTP_SENT = "Otp has been sent to you successfully";

	public static Integer generateOTP() {
		Random random = new Random();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        return Integer.parseInt(formatted);
	}
	
}