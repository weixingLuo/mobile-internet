package top.after.internet.security.sms.verify.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import top.after.internet.security.core.authentication.WebAuthenticationException;
import top.after.internet.security.sms.verify.VerificationCode;
import top.after.internet.security.sms.verify.VerificationCodeGenerater;
import top.after.internet.security.sms.verify.VerificationCodeRepository;
import top.after.internet.security.sms.verify.VerificationCodeService;

public abstract class AbstractVerificationCodeService implements VerificationCodeService {
	
	public AbstractVerificationCodeService() {
	}

	@Override
	public String getVerificationCode(String id,String type,String target) {
		VerificationCode saved=getVerificationCodeRepository().getCodeForSeries(id);
		boolean accepteable = validateSaved(saved,type,target);
		if(!accepteable) {
			throw new WebAuthenticationException(10,"验证码失效");
		}
		return saved.getCode();
	}

	@Override
	public VerificationCode create(String type,String target) {
		if(isReuseCode()) {
			Optional<VerificationCode> saved = find(type, target);
			if(saved.isPresent()) {
				return saved.get();
			}
		}
		String code = getVerificationCodeGenerater().generate();
		String id = UUID.randomUUID().toString().replaceAll("-","");
		LocalDateTime expireTime=LocalDateTime.now().plus(getValidateTerm(), ChronoUnit.MINUTES);
		VerificationCode verificationCode = new VerificationCode(id,target,code,type,expireTime);
		getVerificationCodeRepository().createNewCode(verificationCode);
		return verificationCode;
	}

	private Optional<VerificationCode> find(String type,String target) {
		List<VerificationCode> existes = getVerificationCodeRepository().findReuseable(type, target);
		if(existes == null || existes.isEmpty()) {
			return Optional.empty();
		}
		return existes.stream().filter(o->o.getExpireTime().isAfter(LocalDateTime.now())).findFirst();
	}
	public  abstract long getValidateTerm();
	public abstract boolean isReuseCode();
	public abstract VerificationCodeGenerater getVerificationCodeGenerater();
	public abstract VerificationCodeRepository getVerificationCodeRepository();
	
	@Override
	public void disable(String id) {
		getVerificationCodeRepository().removeCode(id);
	}
	
	private boolean validateSaved(VerificationCode saved, String smsCodeType,String target) {
		if(saved == null) {
			return false;
		}
		if(saved.isDisabled()) {
			return false;
		}
		if(saved.getExpireTime().isBefore(LocalDateTime.now())) {
			return false;
		}
		if(!saved.getType().equals(smsCodeType)) {
			return false;
		}
		if(!saved.getTarget().equals(target)) {
			return false;
		}
		return true;
	}
	
}
