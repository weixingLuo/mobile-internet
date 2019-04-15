package top.after.internet.security.social.wechat.autoconfigurer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.social.autoconfigure.SocialProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * spring-social-wechat
 * 
 */
@ConfigurationProperties(prefix = "spring.social.wechatmp")
public class WechatMpProperties extends SocialProperties {

	@Getter
	@Setter
	private String scope = "snsapi_userinfo";

}
