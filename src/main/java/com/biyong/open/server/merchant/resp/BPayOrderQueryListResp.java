package com.biyong.open.server.merchant.resp;

import java.util.List;

public class BPayOrderQueryListResp {
  private List<BPayOrderQueryResp> list;

  public List<BPayOrderQueryResp> getList() {
    return list;
  }

  public void setList(List<BPayOrderQueryResp> list) {
    this.list = list;
  }
}
