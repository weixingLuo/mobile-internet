package top.after.internet.security.web;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import top.after.internet.security.core.config.SecurityProperties;
import top.after.internet.security.core.jwt.JwtAuthenticationProcessingFilter;
import top.after.internet.security.core.user.UserAccountService;
import top.after.internet.security.sms.config.SmsCodeAuthenticationSecurityConfig;

@Configuration
@EnableWebSecurity(debug = true)
@EnableConfigurationProperties(WebSecurityProperties.class)
public class UaaWebSecurityConfig {
	public static String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	@Configuration
	@Order(3)
	public static class UserAccessSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private AuthenticationSuccessHandler authenticationSuccessHandler;
		@Autowired
		private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
		@Autowired
		private WebSecurityProperties webSecurityProperties;


		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()

					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.antMatchers(webSecurityProperties.getUserAccessPath()).hasRole("USER").anyRequest().authenticated()
					.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().formLogin()
					.defaultSuccessUrl(webSecurityProperties.getSuccessUrl())
					.successHandler(authenticationSuccessHandler).and().headers().frameOptions().disable()
//    		.and().httpBasic()
					.and().exceptionHandling()
					.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and().csrf().disable();
			// 禁用缓存
			http.addFilterBefore(jwtAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class);
		}

		public void configure(WebSecurity web) throws Exception {
//			String[] ignoring = concat(webSecurityProperties.getSigninPath(), webSecurityProperties.getIgnoring());
			web.ignoring().antMatchers(webSecurityProperties.getIgnoring());
		}

	}

	@Configuration
	@Order(2)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private WebSecurityProperties webSecurityProperties;
		@Autowired
		private UserAccountService userAccountService;
		@Autowired
		private DataSource datasource;
		@Autowired
		private PasswordEncoder passwordEncoder;

//		public ApiWebSecurityConfigurationAdapter() { super(true); }
		 
//
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userAccountService).and().jdbcAuthentication().dataSource(datasource)
					.passwordEncoder(passwordEncoder).withUser("root").password("root").roles("SYSTEM");
		}

		protected void configure(HttpSecurity http) throws Exception {
			http
            .antMatcher(webSecurityProperties.getSystemAccessPath())
            .authorizeRequests()
                .anyRequest().hasRole("SYSTEM")
                .and()
            .httpBasic();
//                .and().authenticationProvider(authenticationProvider);
		}
		/*
		 * public void configure(WebSecurity web) throws Exception {
		 * web.ignoring().antMatchers(webSecurityProperties.getIgnoring()); }
		 */
	}

	@Configuration
	@Order(1)
	static class UserLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private WebSecurityProperties webSecurityProperties;
		@Autowired
		private SecurityProperties securityProperties;
		@Autowired
		private RememberMeServices rememberMeServices;
		@Autowired
		private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
		@Autowired
		private AuthenticationSuccessHandler authenticationSuccessHandler;
		@Autowired
		private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
		@Autowired
		private AuthenticationFailureHandler authenticationFailureHandler;

		public UserLoginWebSecurityConfigurerAdapter() {
			super(true);
		}

		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers(webSecurityProperties.getSigninPath()).permitAll().and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.antMatcher(webSecurityProperties.getRememberMePath()).rememberMe()
					.tokenValiditySeconds(webSecurityProperties.getRefreshTokenValiditySeconds())
					.rememberMeServices(rememberMeServices).key(securityProperties.getRefreshTokenKey())
					.authenticationSuccessHandler(authenticationSuccessHandler).and()
					.apply(smsCodeAuthenticationSecurityConfig).and().anonymous().and().logout()
					.logoutUrl(webSecurityProperties.getLogoutUrl())
					.logoutSuccessUrl(webSecurityProperties.getLogoutSuccessUrl())
					// .addLogoutHandler((LogoutHandler)rememberMeServices())
					.and().formLogin().defaultSuccessUrl(webSecurityProperties.getSuccessUrl())
					.successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler).and()
					.csrf().disable();
			http.addFilterBefore(jwtAuthenticationProcessingFilter, LogoutFilter.class);
			// http.addFilterAfter(smsRatelimitjFilter(), FilterSecurityInterceptor.class);
		}
		@Autowired
		private UserAccountService userAccountService;
		@Autowired
		private DataSource datasource;
		@Autowired
		private PasswordEncoder passwordEncoder;

		 
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userAccountService).and().jdbcAuthentication().dataSource(datasource)
					.passwordEncoder(passwordEncoder).withUser("root").password("root").roles("SYSTEM");
		}
	}

}
