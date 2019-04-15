package top.after.internet.web.core.exception;

public class BizException extends RuntimeException {
	private static final long serialVersionUID = -1491704921119507143L;
	private int code;

    public BizException() {
        super();
    }

    public BizException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}