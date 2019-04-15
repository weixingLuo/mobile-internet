package top.after.internet.security.sms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import top.after.internet.security.core.user.UserAccountService;
import top.after.internet.security.core.user.UserAccountServiceFacade;
import top.after.internet.security.sms.authentication.SmsCodeAuthenticationFilter;
import top.after.internet.security.sms.authentication.SmsCodeUserDetailsAuthenticationProvider;
import top.after.internet.security.sms.verify.VerificationCodeService;


/**
 * @Author: HanLong
 * @Date: Create in 2018/3/24 15:31
 * @Description:    sms短信登录配置
 */
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountServiceFacade userAccountServiceFacade;
    
    @Autowired
    private RememberMeServices rememberMeServices;
    

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        smsCodeAuthenticationFilter.setSmsCodeService(verificationCodeService);
        smsCodeAuthenticationFilter.setRememberMeServices(rememberMeServices);
        // 获取验证码提供者
        SmsCodeUserDetailsAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeUserDetailsAuthenticationProvider();
        
        smsCodeAuthenticationProvider.setUserDetailsService(userAccountService);
        smsCodeAuthenticationProvider.setUserAccountServiceFacade(userAccountServiceFacade);
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
