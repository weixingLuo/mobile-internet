package top.after.internet.web.core.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionController extends BasicErrorController{

	public GlobalExceptionController() {
		super(new DefaultErrorAttributes(), new ErrorProperties());
	}

    private static final String PATH = "/error";

	@RequestMapping(/* produces = {MediaType.APPLICATION_JSON_VALUE} */)
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        if (!StringUtils.isEmpty((String)body.get("exception")) && body.get("exception").equals(BizException.class.getName())){
            body.put("status", HttpStatus.FORBIDDEN.value());
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Map<String, Object>>(body, status);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}