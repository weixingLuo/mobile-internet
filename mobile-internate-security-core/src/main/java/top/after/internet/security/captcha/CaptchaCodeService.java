package top.after.internet.security.captcha;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import top.after.internet.security.core.authentication.WebAuthenticationException;

public class CaptchaCodeService{

	private CaptchaCodeRepository captchaCodeRepository = new InMenmeryCaptchaCodeRepository();
	private long validateTerm = 1000;

	public CaptchaCodeService() {
	}

	public String getCaptchaCode(String id) {
        CaptchaCode saved = captchaCodeRepository.getCodeForSeries(id);
        boolean accepteable = validateSaved(saved);
		if(!accepteable) {
			throw new WebAuthenticationException("图片验证码失效");
		}
		return saved.getCode();
	}

	public CaptchaCode create(String type,String code) {
		String id = UUID.randomUUID().toString().replaceAll("-","");
		LocalDateTime expireTime=LocalDateTime.now().plus(validateTerm, ChronoUnit.MINUTES);
		CaptchaCode captchaCode = new CaptchaCode(id,code,type,expireTime);
		captchaCodeRepository.createNewCode(captchaCode);
		return captchaCode;
	}

	public void setVerified(String id) {
		captchaCodeRepository.setVerified(id);
	}

	public Boolean verify(String id,String type) {
		CaptchaCode saved = captchaCodeRepository.getCodeForSeries(id);
		if(saved == null) {
			return false;
		}
		if(saved.getExpireTime().isBefore(LocalDateTime.now())) {
			return false;
		}
		if(saved.isVerify() || !saved.getType().equals(type)){
			return false;
		}
		return true;
	}

	private boolean validateSaved(CaptchaCode saved) {
		if(saved == null) {
			return false;
		}
		if(saved.getExpireTime().isBefore(LocalDateTime.now())) {
			return false;
		}
		return true;
	}

	public void setCaptchaCodeRepository(CaptchaCodeRepository captchaCodeRepository) {
		this.captchaCodeRepository = captchaCodeRepository;
	}

	public void setValidateTerm(long validateTerm) {
		this.validateTerm = validateTerm;
	}
}
