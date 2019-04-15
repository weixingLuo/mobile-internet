package top.after.internet.security.sms.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import lombok.AllArgsConstructor;
import top.after.internet.security.captcha.CaptchaAutoConfiguration;
import top.after.internet.security.core.config.UaaSecurityAutoConfiguration;
import top.after.internet.security.sms.vender.LocalShortMessageProvider;
import top.after.internet.security.sms.vender.ShortMessageProvider;
import top.after.internet.security.sms.verify.SmsVerificationCodeService;
import top.after.internet.security.sms.verify.VerificationCodeRepository;
import top.after.internet.security.sms.verify.VerificationCodeService;
import top.after.internet.security.sms.verify.impl.DefaultVerificationCodeService;
import top.after.internet.security.sms.verify.impl.InMenmeryVerificationCodeRepository;
import top.after.internet.security.sms.verify.impl.JdbcVerificationCodeRepository;

@AllArgsConstructor
@Configuration
@ConditionalOnBean(UaaSecurityAutoConfiguration.class)
@EnableConfigurationProperties(SmsCodeProperties.class)
@Import(CaptchaAutoConfiguration.class)
public class SmsCodeAuthenticationAutoConfig {
	private final SmsCodeProperties properties;
	
    @Bean
	@ConditionalOnMissingBean
    public SmsVerificationCodeService smsVerificationCodeService(ShortMessageProvider shortMessageProvider,
    		VerificationCodeService verificationCodeService){
        return new SmsVerificationCodeService(shortMessageProvider, verificationCodeService, properties.getTemplate());
    }

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(DataSource.class)
	public VerificationCodeRepository jdbcVerificationCodeRepository(DataSource datasource) {
		JdbcVerificationCodeRepository jdbcVerificationCodeRepository = new JdbcVerificationCodeRepository();
		jdbcVerificationCodeRepository.setDataSource(datasource);
		return jdbcVerificationCodeRepository;
	}
	
	@Bean
	@ConditionalOnMissingBean({DataSource.class,VerificationCodeRepository.class})
	public VerificationCodeRepository inMenmeryVerificationCodeRepository() {
		return new InMenmeryVerificationCodeRepository();
	}

	@Bean
	@ConditionalOnMissingBean
	public VerificationCodeService verificationCodeService(VerificationCodeRepository verificationCodeRepository) {
		DefaultVerificationCodeService defaultVerificationCodeService = new DefaultVerificationCodeService();
		defaultVerificationCodeService.setVerificationCodeRepository(verificationCodeRepository);
        defaultVerificationCodeService.setReuseCode(Boolean.TRUE);
		return defaultVerificationCodeService;
	}

	@Bean
	@ConditionalOnMissingBean
	public ShortMessageProvider shortMessageProvider() {
		return new LocalShortMessageProvider();
	}

	@Bean
	public SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig() {
		return new SmsCodeAuthenticationSecurityConfig();
	}

}
