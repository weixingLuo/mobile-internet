package top.after.internet.security.sms.verify;

import java.time.LocalDateTime;

public class VerificationCode {

	private String id;
	private String target;
	private String code;
	private String type;
	private LocalDateTime expireTime;
	private boolean disabled;
	
	public VerificationCode(String id, String target, String code, String type, LocalDateTime expireTime) {
		super();
		this.id = id;
		this.target = target;
		this.code = code;
		this.type = type;
		this.expireTime = expireTime;
		this.disabled=false;
	}

	public VerificationCode(String id, String target, String code, String type, LocalDateTime expireTime, boolean disabled) {
		super();
		this.id = id;
		this.target = target;
		this.code = code;
		this.type = type;
		this.expireTime = expireTime;
		this.disabled = disabled;
	}

	public VerificationCode() {
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
