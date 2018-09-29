package com.biyong.open.server.controller;


import com.biyong.open.server.merchant.req.UserAuthReq;
import com.biyong.open.server.merchant.req.UserInfoReq;
import com.biyong.open.server.merchant.req.UserUpdateReq;
import com.biyong.open.server.merchant.resp.UserAuthResp;
import com.biyong.open.server.merchant.resp.UserInfoResp;
import com.biyong.open.server.service.BiYongMerchantService;
import com.biyong.open.server.utils.AsyncUtil;
import com.biyong.open.server.web.BusinessException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class DemoController {
  private final BiYongMerchantService biYongMerchantService;

  @Autowired
  public DemoController(BiYongMerchantService biYongMerchantService) {
    this.biYongMerchantService = biYongMerchantService;
  }

  /**
   * 使用token获取用户，并从biyong后台拉取用户信息
   */
  @GetMapping(value = "login/{token}")
  public UserInfoResp login(@PathVariable("token") String token) {
    for (User user : userStorage.values()) {
      if (user.token.equals(token)) {
        UserInfoReq req = new UserInfoReq();
        req.setOpenId(user.openId);
        return biYongMerchantService.biyongUserInfo(req);
      }
    }
    throw new BusinessException(900, "token error");
  }

  /**
   * 使用biyToken从币用后台拉取信息
   */
  @GetMapping(value = "auth/{biyToken}")
  public UserInfoResp auth(@PathVariable("biyToken") String authToken) {
    UserAuthReq req = new UserAuthReq();
    req.setAuthToken(authToken);
    UserAuthResp resp = biYongMerchantService.biyongUserAuth(req);
    afterUserAuth(resp);
    return resp.getUserInfo();
  }

  private void afterUserAuth(UserAuthResp resp) {
    AsyncUtil.run(() -> {
      try {
        User user = new User();
        user.openId = resp.getOpenId();
        user.token = UUID.randomUUID().toString();
        saveUser(user);
        UserUpdateReq req1 = new UserUpdateReq();
        req1.setOpenId(user.openId);
        req1.setToken(user.token);
        biYongMerchantService.biyongUserUpdate(req1);
      } catch (Exception e) {
        // TODO 上报失败异常处理
        e.printStackTrace();
      }
    });
  }

  // demo 用户只存储于内存中
  private final Map<String, User> userStorage = new HashMap<>();

  private static class User {
    private String openId;
    private String token;
  }

  private synchronized void saveUser(User user) {
    userStorage.put(user.openId, user);
  }
}
