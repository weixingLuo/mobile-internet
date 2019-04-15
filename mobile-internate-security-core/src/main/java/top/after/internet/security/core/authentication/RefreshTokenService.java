package top.after.internet.security.core.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class RefreshTokenService extends PersistentTokenBasedRememberMeServices {

	private static final String REFRESH_TOKEN = "refresh_token";
	public static final String SUCCESS_LOGIN_REFRESH_TOKEN = "success_login_refresh_Token";

	public RefreshTokenService(String key, UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		setAlwaysRemember(true);
	}

	@Override
	protected String extractRememberMeCookie(HttpServletRequest request) {
		return request.getParameter(REFRESH_TOKEN);
	}
	
	@Override
	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request,
			HttpServletResponse response) {
		String refreshToken = encodeCookie(tokens);
		request.setAttribute(SUCCESS_LOGIN_REFRESH_TOKEN, refreshToken);
	}

}
