package top.after.internet.security.sms.verify;

public interface VerificationCodeService {
	String getVerificationCode(String id,String type,String target);
	VerificationCode create(String type,String target);
	void disable(String id);
}
