package top.after.internet.security.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "sms")
@Data
public class SmsCodeProperties {

	private String template;
}
