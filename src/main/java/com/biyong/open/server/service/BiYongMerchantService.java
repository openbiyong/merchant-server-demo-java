package com.biyong.open.server.service;

import com.biyong.open.server.controller.param.AppInfo;
import com.biyong.open.server.merchant.req.UserAuthReq;
import com.biyong.open.server.merchant.req.UserInfoReq;
import com.biyong.open.server.merchant.req.UserUpdateReq;
import com.biyong.open.server.merchant.resp.UserAuthResp;
import com.biyong.open.server.merchant.resp.UserInfoResp;

public interface BiYongMerchantService {
  void setupAppInfo(AppInfo appInfo);

  UserAuthResp biyongUserAuth(UserAuthReq req);

  UserInfoResp biyongUserInfo(UserInfoReq req);

  void biyongUserUpdate(UserUpdateReq req);
}
