package top.after.internet.demo.web;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	public DemoController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/api/test")
	public Object api() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getDetails();
	}
	
	@GetMapping("/user")
	public Object userName(Principal a) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getPrincipal();
	}
	
	@GetMapping("/user/{phone}")
	@PreAuthorize("#phone == authentication.name")
	public Object user(@PathVariable String phone, Principal a) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}
	
	@GetMapping("/menu.html")
	public String index() {
		return wechatMenu();
	}
	private String wechatMenu() {
		return "<!DOCTYPE html>\r\n" + 
				"<html lang=\"zh-cn\">\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"UTF-8\">\r\n" + 
				"    <title>Wechat Login Demo</title>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"    <form action=\"/signin/wechat\" target=\"_blank\" method=\"POST\">\r\n" + 
				"        <button type=\"submit\">微信开放平台登录(扫码登录)</button>\r\n" + 
				"    </form>\r\n" + 
				"    <br />\r\n" + 
				"    <form action=\"/signin/wechatmp\" method=\"POST\">\r\n" + 
				"        <button type=\"submit\">微信公众平台登录(需要在微信浏览器打开)</button>\r\n" + 
				"    </form>\r\n" + 
				"</body>\r\n" + 
				"</html>";
	}
}
