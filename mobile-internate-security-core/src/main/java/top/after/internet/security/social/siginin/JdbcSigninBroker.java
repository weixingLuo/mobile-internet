package top.after.internet.security.social.siginin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AllArgsConstructor;
import top.after.internet.security.core.authentication.AuthenticationDetails;
import top.after.internet.security.core.authentication.LocalSiginResponse;
import top.after.internet.security.core.jwt.JwtAccessTokenConverter;
import top.after.internet.security.core.user.UserAccount;
import top.after.internet.security.core.user.UserAccountService;

@AllArgsConstructor
public class JdbcSigninBroker implements LocalSigninBroker {

	private RememberMeServices rememberMeServices;
	private UserAccountService userAccountService;
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Override
	public AuthenticationDetails signin(String userId) {
		UserAccount account = userAccountService.loadUserById(userId);
		String accessToken = jwtAccessTokenConverter.generateToken(account);
		UsernamePasswordAuthenticationToken successfulAuthentication = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
		String refreshToken = generateRefreshToken(successfulAuthentication);
		LocalSiginResponse result = new LocalSiginResponse(account.getId().toString(),account.getUsername(),accessToken,(String)refreshToken,jwtAccessTokenConverter.getExpiration().toString());
		
		return result;
	}

	private String generateRefreshToken(Authentication successfulAuthentication) {
		ServletRequestAttributes attrs = getServletRequestAttributes();
		HttpServletRequest request = attrs.getRequest();
		rememberMeServices.loginSuccess(request, attrs.getResponse(), successfulAuthentication);
		return (String) request.getAttribute("success_login_refresh_Token");
	}

	public static ServletRequestAttributes getServletRequestAttributes() throws IllegalStateException {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException("当前线程中不存在 Request 上下文");
		}
		return attrs;
	}
}
