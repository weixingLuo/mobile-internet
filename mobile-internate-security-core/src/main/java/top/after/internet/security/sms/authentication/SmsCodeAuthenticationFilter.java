package top.after.internet.security.sms.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import top.after.internet.security.core.authentication.WebAuthenticationException;
import top.after.internet.security.sms.verify.VerificationCodeService;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private VerificationCodeService smsCodeService;
	private String smsCodeType = "login";
	
    // 请求参数key
    private String mobileParameter = "phone";
    private String codeParameter = "smsCode";
    private String seriesParameter = "series";
    // 请求接口的url
    private static String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/login/sms";
    // 是否只支持POST
    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(DEFAULT_LOGIN_PROCESSING_URL_MOBILE, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 根据请求参数名，获取请求value
        String mobile = obtainMobile(request);
        String smsCode = obtainSmsCode(request);
        String series = obtainSeries(request);
        
        additionalAuthenticationChecks(mobile,smsCode,series);
        
        // 生成对应的AuthenticationToken
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile,smsCode);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    protected void additionalAuthenticationChecks(String mobile,
    		String smsCode,
    		String series) throws AuthenticationException {
		if (smsCode.equals("")) {
			logger.debug("Authentication failed: no credentials provided");
			throw new WebAuthenticationException("短信验证码错误");
		}
		String saved = smsCodeService.getVerificationCode(series,smsCodeType,mobile);
		if(!smsCode.equals(saved)) {
			throw new WebAuthenticationException("短信验证码错误");
		}
		smsCodeService.disable(series);
	}
    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        return trim(request.getParameter(mobileParameter));
    }
    /**
     * 获取短信验证码
     */
    protected String obtainSmsCode(HttpServletRequest request) {
        return trim(request.getParameter(codeParameter));
    }
    /**
     * 获取短信令牌
     */
    protected String obtainSeries(HttpServletRequest request) {
        return trim(request.getParameter(seriesParameter));
    }
    private String trim(String s) {
    	if (s == null) {
            s = "";
        }
        return s.trim();
    }
	protected void setDetails(HttpServletRequest request,
			SmsCodeAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

    public VerificationCodeService getSmsCodeService() {
        return smsCodeService;
    }

    public void setSmsCodeService(VerificationCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }
}
