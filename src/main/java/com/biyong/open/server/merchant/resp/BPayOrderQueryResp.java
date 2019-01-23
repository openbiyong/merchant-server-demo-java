package com.biyong.open.server.merchant.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BPayOrderQueryResp {
  private String outOrderCode;
  private String orderName;
  private String orderCode;
  private String status;
  private List<Price> multiPrice;
  private String coinName;
  private BigDecimal balance;
  private BigDecimal fee;
  private BigDecimal feeRate;
  private Date payTime;
  private String remark;
  private Date settleTime;
  private Date expireTime;
  private Date createTime;
  private Date updateTime;
  private Integer expireSec;

  public String getOrderName() {
    return orderName;
  }

  public void setOrderName(String orderName) {
    this.orderName = orderName;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<Price> getMultiPrice() {
    return multiPrice;
  }

  public void setMultiPrice(List<Price> multiPrice) {
    this.multiPrice = multiPrice;
  }

  public String getCoinName() {
    return coinName;
  }

  public void setCoinName(String coinName) {
    this.coinName = coinName;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public BigDecimal getFeeRate() {
    return feeRate;
  }

  public void setFeeRate(BigDecimal feeRate) {
    this.feeRate = feeRate;
  }

  public Date getPayTime() {
    return payTime;
  }

  public void setPayTime(Date payTime) {
    this.payTime = payTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getSettleTime() {
    return settleTime;
  }

  public void setSettleTime(Date settleTime) {
    this.settleTime = settleTime;
  }

  public Date getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Date expireTime) {
    this.expireTime = expireTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getExpireSec() {
    return expireSec;
  }

  public void setExpireSec(Integer expireSec) {
    this.expireSec = expireSec;
  }

  public String getOutOrderCode() {
    return outOrderCode;
  }

  public void setOutOrderCode(String outOrderCode) {
    this.outOrderCode = outOrderCode;
  }

  public static class Price {
    private String coinName;
    private BigDecimal balance;

    public String getCoinName() {
      return coinName;
    }

    public void setCoinName(String coinName) {
      this.coinName = coinName;
    }

    public BigDecimal getBalance() {
      return balance;
    }

    public void setBalance(BigDecimal balance) {
      this.balance = balance;
    }
  }
}
