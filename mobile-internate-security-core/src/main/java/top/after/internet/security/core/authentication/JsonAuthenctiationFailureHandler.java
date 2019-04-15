package top.after.internet.security.core.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description: 登录失败回调处理
 */
public class JsonAuthenctiationFailureHandler implements AuthenticationFailureHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		logger.error("登录失败", exception);

		// 判断是用哪一种方式进行登录的
		// 如果是JSON，则返回JSON字符串；否则进行页面的跳转
		if(exception instanceof WebAuthenticationException) {
			WebAuthenticationException bizE = (WebAuthenticationException) exception;
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(new ResponseEntity(bizE.getCode(),bizE.getMessage())));
		}else {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(objectMapper.writeValueAsString(new ResponseEntity(exception.getMessage())));
		}
		response.flushBuffer();
	}
	
	public class ResponseEntity{
		private int code;
	    private String message;

		public ResponseEntity(String message) {
			super();
			this.code = 0;
			this.message = message;
		}
		public ResponseEntity(int code, String message) {
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
}
