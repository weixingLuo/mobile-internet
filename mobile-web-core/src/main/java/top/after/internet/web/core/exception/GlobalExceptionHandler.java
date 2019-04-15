package top.after.internet.web.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * 定义要捕获的异常 可以多个 @ExceptionHandler({})
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
	/*
	 * @ExceptionHandler(BizException.class) public ErrorResponseEntity
	 * customExceptionHandler(HttpServletRequest request, final Exception e,
	 * HttpServletResponse response) {
	 * response.setStatus(HttpStatus.BAD_REQUEST.value()); BizException exception =
	 * (BizException) e; return new ErrorResponseEntity(exception.getCode(),
	 * exception.getMessage()); }
	 */
	/*
	 * @ExceptionHandler(RuntimeException.class) public ErrorResponseEntity
	 * runtimeExceptionHandler(HttpServletRequest request, final Exception e,
	 * HttpServletResponse response) {
	 * response.setStatus(HttpStatus.BAD_REQUEST.value()); RuntimeException
	 * exception = (RuntimeException) e; return new ErrorResponseEntity(500,
	 * exception.getMessage()); }
	 */

    /**
     * 通用的接口映射异常处理
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
    	logger.error(request.toString(), ex);
    	if(logger.isDebugEnabled()) {
    		logger.debug(body);
    	}
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + "，参数：" + exception.getName()
                    + ",信息：" + exception.getLocalizedMessage());
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), "参数转换失败"), status);
        }
        if(ex instanceof BizException) {
        	BizException exception = (BizException) ex;
        	return new ResponseEntity<>(new ErrorResponseEntity(exception.getCode(), exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponseEntity(status.value(), "参数转换失败"), status);
    }
}