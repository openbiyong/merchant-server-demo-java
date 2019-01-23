package com.biyong.open.server.service;

import com.biyong.open.server.controller.param.AppInfo;
import com.biyong.open.server.merchant.req.BPayCallbackSetReq;
import com.biyong.open.server.merchant.req.BPayCloseReq;
import com.biyong.open.server.merchant.req.BPayOrderCreateReq;
import com.biyong.open.server.merchant.req.BPayOrderQueryReq;
import com.biyong.open.server.merchant.req.BPayRefundReq;
import com.biyong.open.server.merchant.req.UserAuthReq;
import com.biyong.open.server.merchant.req.UserInfoReq;
import com.biyong.open.server.merchant.req.UserUpdateReq;
import com.biyong.open.server.merchant.resp.BPayConfigQueryResp;
import com.biyong.open.server.merchant.resp.BPayOrderCreateResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryListResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryResp;
import com.biyong.open.server.merchant.resp.UserAuthResp;
import com.biyong.open.server.merchant.resp.UserInfoResp;
import com.biyong.open.server.utils.Utils;

public interface BiYongMerchantService {
  void setupAppInfo(AppInfo appInfo);

  UserAuthResp biyongUserAuth(UserAuthReq req);

  UserInfoResp biyongUserInfo(UserInfoReq req);

  void biyongUserUpdate(UserUpdateReq req);

  BPayConfigQueryResp queryBPayConfig();

  BPayOrderCreateResp createBPayOrder(BPayOrderCreateReq req);

  BPayOrderQueryResp queryBPayOrder(BPayOrderQueryReq req);

  BPayOrderQueryListResp queryBPayOrderListByTime(BPayOrderQueryReq req);

  BPayOrderQueryResp closeBPayOrder(BPayCloseReq req);

  BPayOrderQueryResp refundBPayOrder(BPayRefundReq req);

  void setupCallback(BPayCallbackSetReq req);

  Utils.MerchantRequest readCallback(String rsaSignHashMode, String aesMode, byte[] data);
}
