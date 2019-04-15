package top.after.internet.security.sms.verify.impl;

import top.after.internet.security.sms.verify.VerificationCodeGenerater;
import top.after.internet.security.sms.verify.VerificationCodeRepository;

public class DefaultVerificationCodeService extends AbstractVerificationCodeService {

	private VerificationCodeGenerater verificationCodeGenerater;
	private VerificationCodeRepository verificationCodeRepository;
	private long validateTerm = 1000;
	private boolean reuseCode = false;

	public DefaultVerificationCodeService(VerificationCodeGenerater verificationCodeGenerater, VerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeGenerater = verificationCodeGenerater;
		this.verificationCodeRepository = verificationCodeRepository;
	}

	public DefaultVerificationCodeService() {
		this.verificationCodeGenerater = new NumberVerificationCodeGenerater();
		this.verificationCodeRepository = new InMenmeryVerificationCodeRepository();
	}

	public void setVerificationCodeGenerater(VerificationCodeGenerater verificationCodeGenerater) {
		this.verificationCodeGenerater = verificationCodeGenerater;
	}

	public void setVerificationCodeRepository(VerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeRepository = verificationCodeRepository;
	}

	@Override
	public long getValidateTerm() {
		return validateTerm;
	}

	@Override
	public boolean isReuseCode() {
		return reuseCode;
	}

	public void setValidateTerm(long validateTerm) {
		this.validateTerm = validateTerm;
	}

	public void setReuseCode(boolean reuseCode) {
		this.reuseCode = reuseCode;
	}

	@Override
	public VerificationCodeGenerater getVerificationCodeGenerater() {
		return verificationCodeGenerater;
	}

	@Override
	public VerificationCodeRepository getVerificationCodeRepository() {
		return verificationCodeRepository;
	}
}
