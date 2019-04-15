package top.after.internet.security.captcha;



public interface CaptchaCodeRepository {
	CaptchaCode getCodeForSeries(String seriesId);
	void createNewCode(CaptchaCode code);
	void setVerified(String id);
}
