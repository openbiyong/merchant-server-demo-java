package com.biyong.open.server.merchant.req;

public class UserUpdateReq {
  private String openId;
  private String token;

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
