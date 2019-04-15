package top.after.internet.security.sms.verify.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.after.internet.security.sms.verify.VerificationCode;
import top.after.internet.security.sms.verify.VerificationCodeRepository;

public class InMenmeryVerificationCodeRepository implements VerificationCodeRepository {

	private Map<String, VerificationCode> userCache;

	public InMenmeryVerificationCodeRepository() {
		userCache = new HashMap<>();
	}

	@Override
	public VerificationCode getCodeForSeries(String seriesId) {
		return userCache.get(seriesId);
	}

	@Override
	public void createNewCode(VerificationCode code) {
		userCache.put(code.getId(),code);
	}

	@Override
	public void removeCode(String id) {
		//userCache.remove(id);

	}

	@Override
	public List<VerificationCode> findReuseable(String type, String target) {
		return null;
	}

}
