package top.after.internet.security.sms.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import top.after.internet.security.core.user.UserAccountServiceFacade;

public class SmsCodeUserDetailsAuthenticationProvider implements AuthenticationProvider {
	private UserDetailsService userDetailsService;
	private UserAccountServiceFacade userAccountServiceFacade;

	public SmsCodeUserDetailsAuthenticationProvider() {
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
		String username = authenticationToken.getPrincipal().toString();
		UserDetails user;
		try {
			user = userDetailsService.loadUserByUsername(username);
		}catch (UsernameNotFoundException e){
			//TODO

			userAccountServiceFacade.register(username);
			user = userDetailsService.loadUserByUsername(username);
		}
		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}



	public boolean supports(Class<?> authentication) {
		return (SmsCodeAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public UserAccountServiceFacade getUserAccountServiceFacade() {
		return userAccountServiceFacade;
	}

	public void setUserAccountServiceFacade(UserAccountServiceFacade userAccountServiceFacade) {
		this.userAccountServiceFacade = userAccountServiceFacade;
	}

}
