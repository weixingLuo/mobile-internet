package top.after.internet.security.core.user;

import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import top.after.internet.security.core.authentication.WebAuthenticationException;

public class UserAccountServiceFacade {
	private PasswordEncoder passwordEncoder;
	private UserDetailsManager userAccountService;
	private String defaultPassword="0Lli|oIO1";
	private Pattern passwordPattern=Pattern.compile(".*");
	
	public UserAccountServiceFacade() {
		super();
	}
	public UserAccountServiceFacade(PasswordEncoder passwordEncoder,
			UserDetailsManager userAccountService) {
		this.passwordEncoder=passwordEncoder;
		this.userAccountService=userAccountService;
	}
	
	public UserDetails register(String username) {
		String password=getDefaultPassword();
		return register(username,password);
	}
	
	/**
	 * 校验密码是否符合规则
	 * @param password
	 * @return
	 */
	private boolean validateFormate(String password) {
		return passwordPattern.matcher(password).find();
	}

	public UserDetails register(String username,String password) {
		boolean isPassWordMatching = validateFormate(password);
		if(!isPassWordMatching) {
			throw new WebAuthenticationException("密码格式不合法");
		}
		boolean exists = userAccountService.userExists(username);
		if(exists) {
			throw new WebAuthenticationException("账号已经存在");
		}
		UserDetails user = buildUser(username, password);
		userAccountService.createUser(user);
		return user;
	}
	
	public void resetPassword(String username, String newPassword) {
		boolean isPassWordMatching = validateFormate(newPassword);
		if(!isPassWordMatching) {
			throw new WebAuthenticationException("密码格式不合法");
		}
		newPassword = passwordEncoder.encode(newPassword);
		UserDetails userDetails = userAccountService.loadUserByUsername(username);
		if(userDetails == null) {
			throw new WebAuthenticationException("账号不存在");
		}
		userAccountService.changePassword(userDetails.getPassword(), newPassword);
	}
	
	/**
	 * 系统默认创建互联网账号
	 * @param username
	 * @param password
	 * @return
	 */
	public UserDetails buildUser(String username,String password) {
		return User.withUsername(username)
				.passwordEncoder(passwordEncoder::encode)
				.password(password)
				.roles("USER")
				.build();
	}
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	public String getDefaultPassword() {
		return defaultPassword;
	}
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public UserDetailsManager getUserAccountService() {
		return userAccountService;
	}
	public void setUserAccountService(UserDetailsManager userAccountService) {
		this.userAccountService = userAccountService;
	}
	public Pattern getPasswordPattern() {
		return passwordPattern;
	}
	public void setPasswordPattern(Pattern passwordPattern) {
		this.passwordPattern = passwordPattern;
	}
	
	public static void main(String[] args) {
		Pattern passwordPattern=Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\-\\_\\*]{6,12}$");
		boolean b = passwordPattern.matcher("0LlioIO1-_*").find();
		System.out.println(b);
	}
}
