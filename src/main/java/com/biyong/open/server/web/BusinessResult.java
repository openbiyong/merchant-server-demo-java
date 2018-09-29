package com.biyong.open.server.web;

public class BusinessResult {
  private Integer code;
  private String msg;
  private Object data;

  public static BusinessResult error(Integer code, String msg) {
    BusinessResult r = new BusinessResult();
    r.code = code;
    r.msg = msg;
    return r;
  }

  public static BusinessResult success(Object data) {
    BusinessResult r = new BusinessResult();
    r.code = 200;
    r.data = data;
    return r;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public Object getData() {
    return data;
  }
}
