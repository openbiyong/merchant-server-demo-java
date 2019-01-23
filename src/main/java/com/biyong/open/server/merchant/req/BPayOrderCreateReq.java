package com.biyong.open.server.merchant.req;

import java.math.BigDecimal;
import java.util.List;

public class BPayOrderCreateReq {
  private String outOrderCode;
  private String orderName;
  private List<Price> multiPrice;
  private String remark;
  private Integer expireSec;

  public String getOrderName() {
    return orderName;
  }

  public void setOrderName(String orderName) {
    this.orderName = orderName;
  }

  public List<Price> getMultiPrice() {
    return multiPrice;
  }

  public void setMultiPrice(List<Price> multiPrice) {
    this.multiPrice = multiPrice;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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
      return this.coinName;
    }

    public BigDecimal getBalance() {
      return this.balance;
    }

    public void setCoinName(String coinName) {
      this.coinName = coinName;
    }

    public void setBalance(BigDecimal balance) {
      this.balance = balance;
    }
  }
}
