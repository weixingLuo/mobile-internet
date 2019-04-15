package top.after.internet.security.core.config;

import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.AllArgsConstructor;
import top.after.internet.security.core.authentication.JwtAuthenticationSuccessHandler;
import top.after.internet.security.core.authentication.JsonAuthenctiationFailureHandler;
import top.after.internet.security.core.authentication.RefreshTokenService;
import top.after.internet.security.core.jwt.JwtAccessTokenConverter;
import top.after.internet.security.core.jwt.JwtAuthenticationProcessingFilter;
import top.after.internet.security.core.user.UserAccountService;
import top.after.internet.security.core.user.UserAccountServiceFacade;

@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "internet.security.uaa",name="password-formate")
@EnableConfigurationProperties(SecurityProperties.class)
public class UaaSecurityAutoConfiguration {
	private final SecurityProperties properties;
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtAuthenticationSuccessHandler authenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler(jwtAccessTokenConverter());
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new JsonAuthenctiationFailureHandler();
	}
	@Bean
	@ConditionalOnMissingBean(JwtAccessTokenConverter.class)
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		return new JwtAccessTokenConverter(properties.getJwtSecret(),properties.getJwtExpiration());
	}

	@Bean
	@ConditionalOnMissingBean(PersistentTokenRepository.class)
	public PersistentTokenRepository persistentTokenRepository(DataSource datasource) {
		JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(datasource);
		tokenRepository.setCreateTableOnStartup(properties.isCreateTableOnStartup());
		return tokenRepository;
	}
	

	@Bean
	@ConditionalOnMissingBean(UserAccountService.class)
	public UserAccountService userAccountService(DataSource datasource) {
		return new UserAccountService(datasource);
	}

	@Bean
	public UserAccountServiceFacade userAccountServiceFacade(PasswordEncoder passwordEncoder,UserAccountService userAccountService) {
		UserAccountServiceFacade uas = new UserAccountServiceFacade();
		uas.setPasswordEncoder(passwordEncoder);
		uas.setDefaultPassword(properties.getDefaultPassword());
		uas.setUserAccountService(userAccountService);
		uas.setPasswordPattern(Pattern.compile(properties.getPasswordFormate()));
		return uas;
	}
	
	
	@Bean
	public RememberMeServices rememberMeServices(UserAccountService userAccountService,
			PersistentTokenRepository persistentTokenRepository) {
		return new RefreshTokenService(properties.getRefreshTokenKey(), 
				userAccountService, persistentTokenRepository);
	}
	
	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		return new JwtAuthenticationProcessingFilter(jwtAccessTokenConverter());
	}

}
