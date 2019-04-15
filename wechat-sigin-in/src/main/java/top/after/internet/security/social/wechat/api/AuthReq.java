package top.after.internet.security.social.wechat.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * spring-social-wechat
 * 
 */
@AllArgsConstructor
@Data
public class AuthReq {

	public String accessToken;

	public String openid;

}
