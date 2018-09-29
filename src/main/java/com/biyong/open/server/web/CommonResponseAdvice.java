package com.biyong.open.server.web;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(
      MethodParameter methodParameter,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter methodParameter,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    Class clazz = methodParameter.getParameterType();
    if (clazz.equals(BusinessResult.class) ||
        clazz.equals(ResponseEntity.class)) {
      return body;
    } else {
      return BusinessResult.success(body);
    }
  }

  @ResponseBody
  @ExceptionHandler(BusinessException.class)
  public Object exceptionHandler(BusinessException e) {
    System.out.println("BusinessException:" + e.getCode() + "|" + e.getMsg());
    return BusinessResult.error(e.getCode(), e.getMsg());
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  public Object exceptionHandler(Exception e) {
    e.printStackTrace();
    return BusinessResult.error(400, e.getClass().getName() + "|" + e.getMessage());
  }
}