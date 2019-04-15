package top.after.internet.security.social.siginin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import top.after.internet.security.core.authentication.OAuth2AuthenticationDetails;

@Component
public class SimpleSignInAdapter implements SignInAdapter {

	private final RequestCache requestCache;
	private final LocalSigninBroker localSigninService;

	public SimpleSignInAdapter(RequestCache requestCache,LocalSigninBroker localSigninService) {
		this.requestCache = requestCache;
		this.localSigninService = localSigninService;
	}

	
	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		connection.getApi();
		OAuth2AuthenticationDetails token = (OAuth2AuthenticationDetails)localSigninService.signin(localUserId);
		String originalUrl = extractOriginalUrl(request);
		return appendToken(token,originalUrl);
	}

	private String appendToken(OAuth2AuthenticationDetails token,String originalUrl) {
		StringBuffer sb = new StringBuffer(originalUrl);
		if(sb.indexOf("?")>0) {
			sb.append('&');
		}else {
			sb.append('?');
		}
		sb.append("access_token=").append(token.getAccessToken());
		sb.append('&').append("userId=").append(token.getUserId());
		return  sb.toString();
	}
	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}
		 
	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}


}
