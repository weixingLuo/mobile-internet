package top.after.internet.security.social.siginin;

import top.after.internet.security.core.authentication.AuthenticationDetails;

public interface LocalSigninBroker {
	public AuthenticationDetails signin(String userId);
}
