package top.after.internet.security.social.wechat.api;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import lombok.AllArgsConstructor;

import top.after.internet.security.social.wechat.UrlConstants;
import top.after.internet.security.social.wechat.WechatLangEnum;

/**
 * spring-social-wechat
 * 
 */
@AllArgsConstructor
public class UserTemplate implements UserOperations {

	private RestOperations restOperations;

	private String accessToken;

	@Override
	public WechatUser getUserProfile(String openId) {
		return getUserProfile(openId, WechatLangEnum.EN);
	}

	@Override
	public WechatUser getUserProfile(String openId, WechatLangEnum lang) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(3);
		params.add("openid", openId);
		params.add("lang", lang.getValue());
		params.add("access_token", accessToken);
		return restOperations.postForObject(UrlConstants.USERINFO_API_URL, params, WechatUser.class);
	}

}
