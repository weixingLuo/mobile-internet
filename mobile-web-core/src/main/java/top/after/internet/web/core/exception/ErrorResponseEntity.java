package top.after.internet.web.core.exception;

import java.io.Serializable;

public class ErrorResponseEntity implements Serializable{
	private static final long serialVersionUID = 2988581156417364482L;
	private int code;
    private String message;

	public ErrorResponseEntity(String message) {
		super();
		this.code = 0;
		this.message = message;
	}
	public ErrorResponseEntity(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}