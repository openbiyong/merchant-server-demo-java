package com.biyong.open.server.merchant.req;

public class BPayRefundReq {
  private String outOrderCode;
  private String orderCode;

  public String getOutOrderCode() {
    return this.outOrderCode;
  }

  public String getOrderCode() {
    return this.orderCode;
  }

  public void setOutOrderCode(String outOrderCode) {
    this.outOrderCode = outOrderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }
}
