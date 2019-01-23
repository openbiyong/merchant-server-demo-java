package com.biyong.open.server.merchant.req;

public class BPayCallbackSetReq {
  private String callbackUrl;
  private String rsaSignHashMode;
  private String aesMode;

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getRsaSignHashMode() {
    return rsaSignHashMode;
  }

  public void setRsaSignHashMode(String rsaSignHashMode) {
    this.rsaSignHashMode = rsaSignHashMode;
  }

  public String getAesMode() {
    return aesMode;
  }

  public void setAesMode(String aesMode) {
    this.aesMode = aesMode;
  }
}
