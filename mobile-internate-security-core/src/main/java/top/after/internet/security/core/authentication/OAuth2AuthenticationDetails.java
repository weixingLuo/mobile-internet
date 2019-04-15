package top.after.internet.security.core.authentication;

public interface OAuth2AuthenticationDetails extends AuthenticationDetails {
	
	public String getAccessToken();
	
	public String getRefreshToken();
	
}
