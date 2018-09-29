package com.biyong.open.server.merchant.resp;

import java.util.List;

public class UserAuthResp {
  private List<String> authList;
  private String openId;
  private UserInfoResp userInfo;

  public List<String> getAuthList() {
    return authList;
  }

  public void setAuthList(List<String> authList) {
    this.authList = authList;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public UserInfoResp getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfoResp userInfo) {
    this.userInfo = userInfo;
  }
}
