package top.after.internet.security.social.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "internet.security.social")
@Data
public class SocialSigninProperties {

	private String signInUrl="/signin";
	private String signUpUrl="/signup";
}
