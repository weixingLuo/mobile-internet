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
import top.after.internet.security.social.wechat.api.Wechat;
import top.after.internet.security.social.wechat.connect.WechatConnectionFactory;

/**
 * spring-social-wechat
 * 
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, WechatConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.wechat", name = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WechatAutoConfiguration {

	@AllArgsConstructor
	@Configuration
	@EnableSocial
	@EnableConfigurationProperties(WechatProperties.class)
	@ConditionalOnWebApplication
	protected static class WechatConfigurerAdapter extends SocialAutoConfigurerAdapter {

		private final WechatProperties properties;

		@Override
		protected ConnectionFactory<Wechat> createConnectionFactory() {
			WechatConnectionFactory factory = new WechatConnectionFactory(this.properties.getAppId(),
					this.properties.getAppSecret());
			factory.setScope(this.properties.getScope());
			return factory;
		}

	}

}
