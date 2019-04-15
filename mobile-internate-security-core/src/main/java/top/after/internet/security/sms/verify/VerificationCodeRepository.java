package top.after.internet.security.sms.verify;

import java.util.List;

public interface VerificationCodeRepository {
	VerificationCode getCodeForSeries(String seriesId);
	void createNewCode(VerificationCode code);
	void removeCode(String id);
	List<VerificationCode> findReuseable(String type,String target);
}
