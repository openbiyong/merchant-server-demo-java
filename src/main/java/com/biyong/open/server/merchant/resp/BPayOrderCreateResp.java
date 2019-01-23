package com.biyong.open.server.merchant.resp;

import com.biyong.open.server.merchant.req.BPayOrderCreateReq;
import java.util.Date;
import java.util.List;

public class BPayOrderCreateResp {
  private String orderCode;
  private String payToken;
  private String outOrderCode;
  private Date createTime;
  private Date expireTime;

  // 接口不返回此参数
  private List<BPayOrderCreateReq.Price> multiPrice;

  public String getOrderCode() {
    return this.orderCode;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public Date getExpireTime() {
    return this.expireTime;
  }

  public List<BPayOrderCreateReq.Price> getMultiPrice() {
    return multiPrice;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public void setExpireTime(Date expireTime) {
    this.expireTime = expireTime;
  }

  public void setMultiPrice(List<BPayOrderCreateReq.Price> multiPrice) {
    this.multiPrice = multiPrice;
  }

  public String getPayToken() {
    return payToken;
  }

  public void setPayToken(String payToken) {
    this.payToken = payToken;
  }

  public String getOutOrderCode() {
    return outOrderCode;
  }

  public void setOutOrderCode(String outOrderCode) {
    this.outOrderCode = outOrderCode;
  }
}
