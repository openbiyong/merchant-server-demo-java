package com.biyong.open.server.merchant.resp;

import java.math.BigDecimal;
import java.util.List;

public class BPayConfigQueryResp {
  private List<AppBPayCoinConfig> coinConfigs;
  private Integer maxCoinNum;
  private Integer settleDays;
  private Callback callback;

  public List<AppBPayCoinConfig> getCoinConfigs() {
    return coinConfigs;
  }

  public void setCoinConfigs(List<AppBPayCoinConfig> coinConfigs) {
    this.coinConfigs = coinConfigs;
  }

  public Integer getSettleDays() {
    return settleDays;
  }

  public void setSettleDays(Integer settleDays) {
    this.settleDays = settleDays;
  }

  public Callback getCallback() {
    return callback;
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public Integer getMaxCoinNum() {
    return maxCoinNum;
  }

  public void setMaxCoinNum(Integer maxCoinNum) {
    this.maxCoinNum = maxCoinNum;
  }

  public static class AppBPayCoinConfig {
    private String coinName;
    private String coinDisplayName;
    private BigDecimal dayLimit;
    private BigDecimal feeRate;
    private BigDecimal perMin;
    private BigDecimal perMax;

    public String getCoinName() {
      return coinName;
    }

    public void setCoinName(String coinName) {
      this.coinName = coinName;
    }

    public String getCoinDisplayName() {
      return coinDisplayName;
    }

    public void setCoinDisplayName(String coinDisplayName) {
      this.coinDisplayName = coinDisplayName;
    }

    public BigDecimal getDayLimit() {
      return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
      this.dayLimit = dayLimit;
    }

    public BigDecimal getFeeRate() {
      return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
      this.feeRate = feeRate;
    }

    public BigDecimal getPerMin() {
      return perMin;
    }

    public void setPerMin(BigDecimal perMin) {
      this.perMin = perMin;
    }

    public BigDecimal getPerMax() {
      return perMax;
    }

    public void setPerMax(BigDecimal perMax) {
      this.perMax = perMax;
    }
  }

  public static class Callback {
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
}
