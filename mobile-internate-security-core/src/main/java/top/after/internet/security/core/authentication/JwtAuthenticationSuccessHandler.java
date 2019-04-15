package top.after.internet.security.core.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import top.after.internet.security.core.jwt.JwtAccessTokenConverter;
import top.after.internet.security.core.user.UserAccount;


public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String ACCESS_TOKEN_MAX_AGE = "access_token_max_age";
	public static String ACCESS_TOKEN = "access_token";
	ObjectMapper om = new ObjectMapper();
	JwtAccessTokenConverter jwtAccessTokenConverter;
	
	public JwtAuthenticationSuccessHandler(JwtAccessTokenConverter jwtAccessTokenConverter) {
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserAccount user = (UserAccount)authentication.getPrincipal();
		String accessToken = jwtAccessTokenConverter.generateToken(user);
		Object refreshToken = request.getAttribute(RefreshTokenService.SUCCESS_LOGIN_REFRESH_TOKEN);
		LocalSiginResponse result = new LocalSiginResponse(user.getId().toString(),user.getUsername(),accessToken,(String)refreshToken,jwtAccessTokenConverter.getExpiration().toString());
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(200);
        om.writeValue(response.getOutputStream(), result);
	}

}
