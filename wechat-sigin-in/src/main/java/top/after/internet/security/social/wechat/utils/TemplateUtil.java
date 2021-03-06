package top.after.internet.security.social.wechat.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.social.support.FormMapHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import top.after.internet.security.social.wechat.WechatMappingJackson2HttpMessageConverter;

/**
 * spring-social-wechat
 * 
 */
public class TemplateUtil {

	public static RestTemplate addHttpMessageConverter(RestTemplate restTemplate,
			HttpMessageConverter<?> httpMessageConverter) {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(3);
		converters.add(new FormHttpMessageConverter());
		converters.add(new FormMapHttpMessageConverter());
		converters.add(new WechatMappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(converters);
		return restTemplate;
	}

}
