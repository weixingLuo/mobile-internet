package top.after.internet.security.sms.verify;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import top.after.internet.security.sms.vender.ShortMessageProvider;

@AllArgsConstructor
public class SmsVerificationCodeService {
	@Autowired
	private ShortMessageProvider shortMessageProvider;
	@Autowired
	private VerificationCodeService verificationCodeService;
	private String template;
	

	public Map<String,String> sendSmsVerificationCode(String type, String phone) {
		VerificationCode verificationCode = verificationCodeService.create(type, phone);
		shortMessageProvider.send(phone, template,verificationCode.getCode());
		Map<String,String> map = new HashMap<>();
		map.put("series",verificationCode.getId());
		return map;
	}
}
