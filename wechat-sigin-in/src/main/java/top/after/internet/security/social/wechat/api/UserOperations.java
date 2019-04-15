package top.after.internet.security.social.wechat.api;

import top.after.internet.security.social.wechat.WechatLangEnum;

/**
 * spring-social-wechat
 * 
 */
public interface UserOperations {

	WechatUser getUserProfile(String openId);

	WechatUser getUserProfile(String openId, WechatLangEnum lang);

}
