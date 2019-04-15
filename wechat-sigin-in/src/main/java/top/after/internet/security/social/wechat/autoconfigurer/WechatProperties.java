package top.after.internet.security.social.wechat.autoconfigurer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.social.autoconfigure.SocialProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "spring.social.wechat")
public class WechatProperties extends SocialProperties {

	@Getter
	@Setter
	private String scope = "snsapi_login";

}
