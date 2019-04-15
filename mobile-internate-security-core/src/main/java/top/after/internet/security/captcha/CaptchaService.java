package top.after.internet.security.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Producer;

import top.after.internet.security.core.authentication.WebAuthenticationException;

public class CaptchaService {

    private Producer captchaProducer;
    private CaptchaCodeService captchaCodeService;

    public CaptchaService(Producer captchaProducer,CaptchaCodeService captchaCodeService) {
    	this.captchaCodeService=captchaCodeService;
    	this.captchaCodeService = captchaCodeService;
    }
    
    public Map<String, String> generateCaptchaCode(String type) throws IOException {
        CaptchaCode captchaCode = captchaCodeService.create(type,captchaProducer.createText());
        //生产图片
        BufferedImage bi = captchaProducer.createImage(captchaCode.getCode()); // 创建带有文本的图片
        Map<String, String> res = new HashMap<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", out);
        res.put("img", Base64.getEncoder().encodeToString(out.toByteArray()));
        res.put("series", captchaCode.getId());
        return res;
    }

    public Map<String, String> codeVerify(String code,String series){
        String saveCode = captchaCodeService.getCaptchaCode(series);
        if(!saveCode.equals(code)){
            throw new WebAuthenticationException("图片验证码错误");
        }
        return Collections.singletonMap("series", series);
    }
}
