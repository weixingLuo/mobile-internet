package top.after.internet.demo.web.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import top.after.internet.security.captcha.CaptchaService;
import top.after.internet.security.sms.verify.SmsVerificationCodeService;

@RestController
@Api
public class LoginController {

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SmsVerificationCodeService smsCodeService;

    @PostMapping("/login/sms/code")
    public Map<String,String> send(@RequestParam String type,
                                   @RequestParam String phone,
                                   @RequestParam(required = false) String series) {
        return smsCodeService.sendSmsVerificationCode(type, phone);
    }

    @PostMapping("/login/refresh")
    public String refresh(@RequestParam("refresh_token") String refreshToken) {
        return null;
    }

    @PostMapping("/login/captcha/code")
    public Map<String, String> send(@RequestParam String type) throws IOException {
        return captchaService.generateCaptchaCode(type);
    }

    @PostMapping("/login/captcha/code_verify")
    public Map<String, String> codeVerify(@RequestParam String code,@RequestParam String series) throws IOException {
        return captchaService.codeVerify(code,series);
    }
}
