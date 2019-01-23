package com.biyong.open.server.controller.param;

public class AppInfo {
  private String appId;
  private String privateKey;
  private String biyongPublicKey;
  private String apiUrl;
  private String serverUrl;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getBiyongPublicKey() {
    return biyongPublicKey;
  }

  public void setBiyongPublicKey(String biyongPublicKey) {
    this.biyongPublicKey = biyongPublicKey;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
}
