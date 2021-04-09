package com.greece.titan.common.handler;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.response.Codes;
import com.greece.titan.common.response.ErrorCode;
import com.greece.titan.common.response.ErrorResponse;
import com.greece.titan.common.response.ResponseBase;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(),timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(),timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED,timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED,timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    /*
     * 404 오류
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> NoHandlerFoundException(Exception e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.PAGE_NOT_FOUND,timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 파라메터 오류 처리
     * MissingServletRequestParameterException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> MissingServletRequestParameterException(MissingServletRequestParameterException e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * BusinessException 처리에 대한 정의
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(final BusinessException e,HttpServletRequest request, WebRequest webRequest) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        //e.getErrCd() 오류코드에 해당하는 redis에 등록된 메세지를 조회한다.
        final String message = "redis에 등록된 메세지(" + e.getErrCd() + ")";
        return new ResponseEntity<>(ResponseBase.business(Codes.Response.ERROR.getCode(), message,timestamp.toString()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e,HttpServletRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 오류메세지 로그 출력
        errorWriter(request,e);

        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,timestamp.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 에러메세지 출력
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param e
     */
    public static void errorWriter(HttpServletRequest request,Exception e){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Map<String, Object> errorMap = getErrorMap(request);
        String className = e.getClass().toString();
        String errMassage = "";
        if(!StringUtils.isEmpty(e.getMessage())) {
            errMassage = e.getMessage().toString();
        }
        //String errMassage = "";
        JSONObject jsonError = getJsonStringFromMap(errorMap);

        log.error("============= START =================================================================");
        log.error("[{}] {} ERROR 메세지 :: {}\n\n요청 정보 ::\n{}\n\nException ::\n{}",timestamp,className,errMassage,jsonError.toString(),e);
        log.error("============= END =================================================================");
    }

    /**
     * Error Map 생성
     * 에러 메세지 맵을 생성하여 메신저에 전송한다
     * @param errorMap
     * @param request
     * @return
     */
    public static Map<String, Object> getErrorMap(HttpServletRequest request) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("Request Session",getSessionValue(request));
        errorMap.put("Request header",getHeaderValue(request));
        errorMap.put("Parameters",getParams(request));
        errorMap.put("Request URI",request.getRequestURI());
        errorMap.put("HttpMethod",request.getMethod());
        errorMap.put("Servlet Path",request.getServletPath());
        return errorMap;
    }

    /**
     * request param 정보를 JSONObject 형태로 반환한다.
     * @param request
     * @return
     */
    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }

    /**
     * request session 정보를 JSONObject 형태로 반환한다.
     * @param request
     * @return
     */
    private static JSONObject getSessionValue(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> attributes = request.getSession().getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            jsonObject.put(attribute, request.getSession().getAttribute(attribute));
        }
        return jsonObject;
    }

    /**
     * request Header
     * request Header 정보를 JSONObject 형태로 반환한다.
     * @param request
     * @return
     */
    private static JSONObject getHeaderValue(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        Enumeration<String> headerEnum = request.getHeaderNames();
        while(headerEnum.hasMoreElements()){
            String headerName = (String)headerEnum.nextElement();
            String headerValue = request.getHeader(headerName);
            jsonObject.put(headerName,headerValue);
        }

        return jsonObject;
    }

    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public static JSONObject getJsonStringFromMap(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }

        return jsonObject;
    }

}