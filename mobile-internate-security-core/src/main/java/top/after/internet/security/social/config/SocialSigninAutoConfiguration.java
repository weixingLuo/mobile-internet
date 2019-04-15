package top.after.internet.security.social.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;

import lombok.AllArgsConstructor;
import top.after.internet.security.core.jwt.JwtAccessTokenConverter;
import top.after.internet.security.core.user.UserAccountService;
import top.after.internet.security.social.siginin.JdbcSigninBroker;
import top.after.internet.security.social.siginin.LocalSigninBroker;
import top.after.internet.security.social.siginin.SimpleSignInAdapter;

@Configuration
@ConditionalOnProperty(prefix = "internet.security.social",name="sign-in-url")
@EnableConfigurationProperties(SocialSigninProperties.class)
@AllArgsConstructor
public class SocialSigninAutoConfiguration{
	private final SocialSigninProperties properties;
	
    @Autowired
    private DataSource dataSource;
    @Bean
    @ConditionalOnMissingBean
    public RequestCache getRequestCache() {
    	return new HttpSessionRequestCache();
    }
    
    @Bean
	@ConditionalOnMissingBean
    public LocalSigninBroker getLocalSigninBroker(RememberMeServices rememberMeServices,UserAccountService userAccountService,JwtAccessTokenConverter jwtAccessTokenConverter) {
    	return new JdbcSigninBroker(rememberMeServices,userAccountService,jwtAccessTokenConverter);
    }
	@Bean
	@ConditionalOnMissingBean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		return registry;
	}
	
    @Bean
	@ConditionalOnMissingBean
    public SignInAdapter getSignInAdapter(RequestCache requestCache,LocalSigninBroker localSigninService) {
    	return new SimpleSignInAdapter(requestCache, localSigninService);
    }
    
	/*
	 * @Bean
	 * 
	 * @ConditionalOnMissingBean(ConnectionSignUp.class) public ConnectionSignUp
	 * getConnectionSignUp() { return (Connection<?> connection) ->
	 * connection.getKey().getProviderUserId(); }
	 */
	@Bean
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
		/*
		 * if (getConnectionSignUp() != null) {
		 * repository.setConnectionSignUp(getConnectionSignUp()); }
		 */
        return repository;
    }
	
    /**
     * 处理注册流程的工具类
     *
     * @param factoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator factoryLocator) {
        return new ProviderSignInUtils(factoryLocator, getUsersConnectionRepository(factoryLocator));
    }
    
	@Bean
	@ConditionalOnMissingBean(ProviderSignInController.class)
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
		ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
		providerSignInController.setSignInUrl(properties.getSignInUrl());
		providerSignInController.setSignUpUrl(properties.getSignUpUrl());
		return providerSignInController;
	}
	
}
