package top.after.internet.security.social.wechat.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import top.after.internet.security.social.wechat.UrlConstants;
import top.after.internet.security.social.wechat.api.Wechat;
import top.after.internet.security.social.wechat.api.impl.WechatImpl;
import top.after.internet.security.social.wechat.connect.WechatOAuth2Template;

/**
 * spring-social-wechat
 * 
 */
public class WechatServiceProvider<T extends Wechat> extends AbstractOAuth2ServiceProvider<T> {

	public WechatServiceProvider(String appId, String appSecret) {
		super(getOAuth2Template(appId, appSecret, UrlConstants.QRCONNECT_API_URL));
	}

	public WechatServiceProvider(String appId, String appSecret, String authorizeUrl) {
		super(getOAuth2Template(appId, appSecret, authorizeUrl));
	}

	private static OAuth2Template getOAuth2Template(String appId, String appSecret, String authorizeUrl) {
		OAuth2Template oauth2Template = new WechatOAuth2Template(appId, appSecret, authorizeUrl,
				UrlConstants.ACCESS_TOKEN_API_URL);
		oauth2Template.setUseParametersForClientAuthentication(true);
		return oauth2Template;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getApi(String accessToken) {
		return (T) new WechatImpl(accessToken);
	}

}