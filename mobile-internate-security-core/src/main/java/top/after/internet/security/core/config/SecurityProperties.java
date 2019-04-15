package top.after.internet.security.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "internet.security.uaa")
@Data
public class SecurityProperties {

	/**
	 * 密码格式（正则表达式）
	 */
	private String passwordFormate=".*?";
	/**
	 * 默认密码（自动登录时的密码）
	 */
	private String defaultPassword;
	/**
	 * 开发测试时才能为true
	 */
	private boolean createTableOnStartup=false;
	private String refreshTokenKey="key";
	/**
	 * jwt密钥
	 */
	private String jwtSecret;
	/**
	 * accessToken有效期，单位秒
	 */
	private Long jwtExpiration;
}
