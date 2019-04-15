package top.after.internet.security.social.wechat.autoconfigurer;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.autoconfigure.SocialAutoConfigurerAdapter;
import org.springframework.social.autoconfigure.SocialWebAutoConfiguration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

import lombok.AllArgsConstructor;
import top.after.internet.security.social.wechat.api.WechatMp;
import top.after.internet.security.social.wechat.connect.WechatMpConnectionFactory;

/**
 * spring-social-wechat
 * 
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, WechatMpConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.wechatmp", name = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WechatMpAutoConfiguration {

	@AllArgsConstructor
	@Configuration
	@EnableSocial
	@EnableConfigurationProperties(WechatMpProperties.class)
	@ConditionalOnWebApplication
	protected static class WechatConfigurerAdapter extends SocialAutoConfigurerAdapter {

		private final WechatMpProperties properties;

		@Override
		protected ConnectionFactory<WechatMp> createConnectionFactory() {
			WechatMpConnectionFactory factory = new WechatMpConnectionFactory(this.properties.getAppId(),
					this.properties.getAppSecret());
			factory.setScope(this.properties.getScope());
			return factory;
		}

	}

}
