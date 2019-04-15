package top.after.internet.security.core.authentication;

import org.springframework.security.core.AuthenticationException;

public class WebAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = -6581868273699128189L;
	private int code;
	public int getCode() {
		return code;
	}
	public WebAuthenticationException(String msg) {
		super(msg);
		code =0;
	}
	public WebAuthenticationException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	public WebAuthenticationException(String msg, Throwable t) {
		super(msg, t);
		code = 0;
	}

}
