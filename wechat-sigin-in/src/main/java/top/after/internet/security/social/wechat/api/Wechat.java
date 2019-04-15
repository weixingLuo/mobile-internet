package top.after.internet.security.social.wechat.api;

import org.springframework.social.ApiBinding;
import org.springframework.web.client.RestOperations;

/**
 * spring-social-wechat
 * 
 */
public interface Wechat extends ApiBinding {

	UserOperations userOperations();

	RestOperations restOperations();

}
