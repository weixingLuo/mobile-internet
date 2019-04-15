package top.after.internet.security.captcha;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
//@ConditionalOnProperty(prefix = "kaptcha",name="border")
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {
	private final CaptchaProperties captchaProperties;
	
    /**
     * 生产的验证码显示控制
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Producer defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", captchaProperties.getBorder());
        properties.setProperty("kaptcha.border.color", captchaProperties.getBorderColor());
        properties.setProperty("kaptcha.textproducer.font.color", captchaProperties.getTextproducerFontColor());
        properties.setProperty("kaptcha.image.width", captchaProperties.getImageWidth());
        properties.setProperty("kaptcha.image.height", captchaProperties.getImageHeight());
        properties.setProperty("kaptcha.session.key", captchaProperties.getSessionKey());
        properties.setProperty("kaptcha.textproducer.char.length", captchaProperties.getTextproducerCharLength());
        properties.setProperty("kaptcha.textproducer.font.names", captchaProperties.getTextproducerFontNames());
        properties.setProperty("kaptcha.textproducer.char.string", captchaProperties.getTextproducerCharString());
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(DataSource.class)
	public CaptchaCodeRepository jdbcCaptchaCodeRepository(DataSource datasource) {
		JdbcCaptchaCodeRepository jdbcCaptchaCodeRepository = new JdbcCaptchaCodeRepository();
		jdbcCaptchaCodeRepository.setDataSource(datasource);
		return jdbcCaptchaCodeRepository;
	}

	@Bean
    @ConditionalOnMissingBean
	public CaptchaCodeService captchaCodeService(CaptchaCodeRepository jdbcCaptchaCodeRepository) {
		CaptchaCodeService captchaCodeService = new CaptchaCodeService();
		captchaCodeService.setCaptchaCodeRepository(jdbcCaptchaCodeRepository);
		return captchaCodeService;
	}


    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(Producer captchaProducer,CaptchaCodeService captchaCodeService){
        return new CaptchaService(captchaProducer,captchaCodeService);
    }

}
