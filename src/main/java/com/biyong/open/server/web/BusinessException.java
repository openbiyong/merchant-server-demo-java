package com.biyong.open.server.web;

public class BusinessException extends RuntimeException {
  private Integer code;
  private String msg;

  public BusinessException(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
