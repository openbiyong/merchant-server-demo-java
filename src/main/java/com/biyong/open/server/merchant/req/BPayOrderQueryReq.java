package com.biyong.open.server.merchant.req;

import java.util.Date;

public class BPayOrderQueryReq {
  private String payToken;
  private String outOrderCode;
  private String orderCode;
  private Date startTime;
  private Date endTime;
  private Integer pageNo;
  private Integer pageSize;


  public String getPayToken() {
    return this.payToken;
  }

  public String getOutOrderCode() {
    return this.outOrderCode;
  }

  public String getOrderCode() {
    return this.orderCode;
  }

  public Date getStartTime() {
    return this.startTime;
  }

  public Date getEndTime() {
    return this.endTime;
  }

  public Integer getPageNo() {
    return this.pageNo;
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public void setPayToken(String payToken) {
    this.payToken = payToken;
  }

  public void setOutOrderCode(String outOrderCode) {
    this.outOrderCode = outOrderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
}
