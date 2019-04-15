package top.after.internet.security.captcha;

import java.time.LocalDateTime;

public class CaptchaCode {

	private String id;
	private String code;
	private String type;
	private LocalDateTime expireTime;
	/**
	 * 是否已经检验通过
	 */
	private boolean verify;
	
	public CaptchaCode(String id, String code, String type, LocalDateTime expireTime) {
		super();
		this.id = id;
		this.code = code;
		this.type = type;
		this.expireTime = expireTime;
		this.verify = false;
	}

	public CaptchaCode(String id, String code, String type, LocalDateTime expireTime, boolean verify) {
		this.id = id;
		this.code = code;
		this.type = type;
		this.expireTime = expireTime;
		this.verify = verify;
	}

	public CaptchaCode() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}
}
