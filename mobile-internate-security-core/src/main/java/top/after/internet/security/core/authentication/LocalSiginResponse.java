package top.after.internet.security.core.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * spring-social-wechat
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalSiginResponse implements OAuth2AuthenticationDetails{
	private static final long serialVersionUID = 359837003093211800L;
	
	private String userId;
	
	private String userName;

	private String accessToken;
	
	private String refreshToken;

	private String expiresIn;

}
