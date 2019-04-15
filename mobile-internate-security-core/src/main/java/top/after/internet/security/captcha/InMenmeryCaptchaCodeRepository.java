package top.after.internet.security.captcha;



import java.util.HashMap;
import java.util.Map;

public class InMenmeryCaptchaCodeRepository implements CaptchaCodeRepository {

	private Map<String, CaptchaCode> userCache;

	public InMenmeryCaptchaCodeRepository() {
		userCache = new HashMap<>();
	}

	@Override
	public CaptchaCode getCodeForSeries(String seriesId) {
		return userCache.get(seriesId);
	}

	@Override
	public void createNewCode(CaptchaCode code) {
		userCache.put(code.getId(),code);
	}

	@Override
	public void setVerified(String id) {
		CaptchaCode code = userCache.get(id);
		code.setVerify(Boolean.TRUE);
	}


}
