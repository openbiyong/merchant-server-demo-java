package com.biyong.open.server.merchant.resp;

public class UserInfoResp {
  private Boolean auth;
  private Boolean pubInfoAuth;
  private String firstName;
  private String lastName;
  private String selfieUrl;
  private Boolean isKycPass;
  private Boolean phoneAuth;
  private String phone;

  public Boolean getAuth() {
    return auth;
  }

  public void setAuth(Boolean auth) {
    this.auth = auth;
  }

  public Boolean getPubInfoAuth() {
    return pubInfoAuth;
  }

  public void setPubInfoAuth(Boolean pubInfoAuth) {
    this.pubInfoAuth = pubInfoAuth;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSelfieUrl() {
    return selfieUrl;
  }

  public void setSelfieUrl(String selfieUrl) {
    this.selfieUrl = selfieUrl;
  }

  public Boolean getKycPass() {
    return isKycPass;
  }

  public void setKycPass(Boolean kycPass) {
    isKycPass = kycPass;
  }

  public Boolean getPhoneAuth() {
    return phoneAuth;
  }

  public void setPhoneAuth(Boolean phoneAuth) {
    this.phoneAuth = phoneAuth;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
