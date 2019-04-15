package top.after.internet.security.sms.verify.impl;

import java.util.Random;

import top.after.internet.security.sms.verify.VerificationCodeGenerater;

public class NumberVerificationCodeGenerater implements VerificationCodeGenerater {
	private int codeLength;
	
	public NumberVerificationCodeGenerater() {
		this(6);
	}

	public NumberVerificationCodeGenerater(int codeLength) {
		this.codeLength=codeLength;
	}
	
	@Override
	public String generate() {
		String code="";
		for(int i=0; i<codeLength; i++){
			code+=String.valueOf(new Random().nextInt(9));
		}
		return code;
	}

}
