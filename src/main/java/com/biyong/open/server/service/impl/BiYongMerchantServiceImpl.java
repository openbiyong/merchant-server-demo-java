package com.biyong.open.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.biyong.open.server.controller.param.AppInfo;
import com.biyong.open.server.merchant.req.BPayCallbackSetReq;
import com.biyong.open.server.merchant.req.BPayCloseReq;
import com.biyong.open.server.merchant.req.BPayOrderCreateReq;
import com.biyong.open.server.merchant.req.BPayOrderQueryReq;
import com.biyong.open.server.merchant.req.BPayRefundReq;
import com.biyong.open.server.merchant.req.TestReq;
import com.biyong.open.server.merchant.req.UserAuthReq;
import com.biyong.open.server.merchant.req.UserInfoReq;
import com.biyong.open.server.merchant.req.UserUpdateReq;
import com.biyong.open.server.merchant.resp.BPayConfigQueryResp;
import com.biyong.open.server.merchant.resp.BPayOrderCreateResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryListResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryResp;
import com.biyong.open.server.merchant.resp.TestResp;
import com.biyong.open.server.merchant.resp.UserAuthResp;
import com.biyong.open.server.merchant.resp.UserInfoResp;
import com.biyong.open.server.service.BiYongMerchantService;
import com.biyong.open.server.utils.HttpClient;
import com.biyong.open.server.utils.Utils;
import com.biyong.open.server.web.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class BiYongMerchantServiceImpl implements BiYongMerchantService {

  private HttpClient client;
  private String privateKey;
  private String biyongPublicKey;
  public static String serverUrl;
  public static String appId;

  @Override
  public void setupAppInfo(AppInfo appInfo) {
    try {
      this.privateKey = appInfo.getPrivateKey();
      this.biyongPublicKey = appInfo.getBiyongPublicKey();
      this.client = new HttpClient(
          appInfo.getPrivateKey(),
          appInfo.getAppId(),
          appInfo.getBiyongPublicKey(),
          appInfo.getApiUrl().endsWith("/") ? appInfo.getApiUrl() : appInfo.getApiUrl() + "/",
          "SHA256",
          // AES加密模式(设置为null不使用AES加密。正式环境采用https通信，非隐私数据接口建议关闭AES加密)
          null
      );
      serverUrl = appInfo.getServerUrl();
      appId = appInfo.getAppId();
      appInfo.setPrivateKey("***");
      appInfo.setBiyongPublicKey("***");
      System.err.println("设置配置文件: " + JSON.toJSONString(appInfo));
      TestReq req = new TestReq();
      req.setMessage("Hello, BiYong");
      TestResp resp = call("common/test", req, TestResp.class);
      System.err.println("测试接口成功，返回data: " + JSON.toJSONString(resp));
    } catch (BusinessException e) {
      System.err.println("配置错误，无法与服务器完成通信: " + e.getCode() + "|" + e.getMsg());
      this.client = null;
      throw new BusinessException(400, "配置错误，无法与服务器完成通信: " + e.getCode() + "|" + e.getMsg());
    } catch (Exception e) {
      System.err.println("配置错误");
      this.client = null;
      throw e;
    }
  }

  @Override
  public UserAuthResp biyongUserAuth(UserAuthReq req) {
    return call("biyong-user/auth", req, UserAuthResp.class);
  }

  @Override
  public UserInfoResp biyongUserInfo(UserInfoReq req) {
    return call("biyong-user/info", req, UserInfoResp.class);
  }

  @Override
  public void biyongUserUpdate(UserUpdateReq req) {
    call("biyong-user/update", req, null);
  }

  /**
   * 支付相关接口
   */
  @Override
  public BPayConfigQueryResp queryBPayConfig() {
    return call("b-pay/config/query", null, BPayConfigQueryResp.class);
  }

  @Override
  public BPayOrderCreateResp createBPayOrder(BPayOrderCreateReq req) {
    return call("b-pay/order/create", req, BPayOrderCreateResp.class);
  }

  @Override
  public BPayOrderQueryResp queryBPayOrder(BPayOrderQueryReq req) {
    return call("b-pay/order/query", req, BPayOrderQueryResp.class);
  }

  @Override
  public BPayOrderQueryListResp queryBPayOrderListByTime(BPayOrderQueryReq req) {
    return call("b-pay/order/list", req, BPayOrderQueryListResp.class);
  }

  @Override
  public BPayOrderQueryResp closeBPayOrder(BPayCloseReq req) {
    return call("b-pay/order/close", req, BPayOrderQueryResp.class);
  }

  @Override
  public BPayOrderQueryResp refundBPayOrder(BPayRefundReq req) {
    return call("b-pay/order/refund", req, BPayOrderQueryResp.class);
  }

  @Override
  public void setupCallback(BPayCallbackSetReq req) {
    call("b-pay/callback/set", req, null);
  }

  @Override
  public Utils.MerchantRequest readCallback(String rsaSignHashMode, String aesMode, byte[] data) {
    if (client == null) {
      throw new BusinessException(500, "app未初始化");
    }
    Utils.MessageCipher cipher = new Utils.MessageCipher(
        privateKey,
        biyongPublicKey,
        rsaSignHashMode,
        aesMode
    );
    try {
      return cipher.serverDecrypt(data);
    } catch (Exception e) {
      System.out.println("验签失败:" + e.getMessage());
      return null;
    }
  }

  private <T> T call(String uri, Object req, Class<T> respClass) {
    if (client == null) {
      throw new BusinessException(500, "app未初始化");
    }
    BiYongResp br = JSON.parseObject(client.call(uri, req), BiYongResp.class);
    if (br.getStatus() == 0) {
      return respClass == null ? null : JSON.parseObject(br.getData(), respClass);
    } else {
      throw new BusinessException(br.getStatus(), br.getMessage());
    }
  }

  public static class BiYongResp {
    private Integer status;
    private String message;
    private Long timestamp;
    private String data;

    public Integer getStatus() {
      return status;
    }

    public void setStatus(Integer status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
  }
}
