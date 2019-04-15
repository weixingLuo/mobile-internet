package top.after.internet.security.social.wechat.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

import top.after.internet.security.social.wechat.UrlConstants;
import top.after.internet.security.social.wechat.api.WechatMp;

/**
 * spring-social-wechat
 * 
 */
public class WechatMpConnectionFactory extends OAuth2ConnectionFactory<WechatMp> {

	public WechatMpConnectionFactory(String appId, String appSecret) {
		this(appId, appSecret, new WechatAdapter<WechatMp>());
	}

	public WechatMpConnectionFactory(String appId, String appSecret, ApiAdapter<WechatMp> apiAdapter) {
		super("wechatmp", new WechatServiceProvider<WechatMp>(appId, appSecret, UrlConstants.AUTHORIZE_API_URL),
				apiAdapter);
	}

	@Override
	protected String extractProviderUserId(AccessGrant accessGrant) {
		return ((WechatAccessGrant) accessGrant).getOpenid();
	}

}
