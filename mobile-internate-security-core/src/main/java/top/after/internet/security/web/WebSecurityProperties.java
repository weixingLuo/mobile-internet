package top.after.internet.security.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "internet.security.web")
@Data
public class WebSecurityProperties {

	private String[] ignoring;
	private String successUrl;
	private String userAccessPath;
	private String systemAccessPath;
	private String[] signinPath;
	private String rememberMePath;
	private int refreshTokenValiditySeconds=1209600;
	private String logoutUrl;
	private String logoutSuccessUrl;
}
