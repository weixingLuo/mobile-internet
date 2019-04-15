package top.after.internet.security.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "kaptcha")
@Data
public class CaptchaProperties {
	private String border = "yes";
	private String borderColor = "105,179,90";
    private String textproducerFontColor="blue";
    private String imageWidth="125";
    private String imageHeight="45";
    private String sessionKey="code";
    private String textproducerCharLength="4";
    private String textproducerFontNames="宋体,楷体,微软雅黑";
    private String textproducerCharString="2345678abcdefhkmnortuwx";
}
