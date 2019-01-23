package com.biyong.open.server.controller;


import com.biyong.open.server.controller.param.AppInfo;
import com.biyong.open.server.service.BiYongMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class AppController {
  private final BiYongMerchantService biYongMerchantService;

  @Autowired
  public AppController(BiYongMerchantService biYongMerchantService) {
    this.biYongMerchantService = biYongMerchantService;
  }

  /*
    调用接口初始化私钥等敏感信息
    如果使用接口设置敏感信息，请勿对外网暴露，以免被恶意调用

    eg: 使用curl初始化此应用

curl http://127.0.0.1:25000/app/setup -X POST -H 'Content-Type: application/json' -d '
{
  "appId":"填写你的appId",
  "privateKey":"填写你的私钥",
  "biyongPublicKey":"填写BiYong为你的App提供的公钥",
  "apiUrl":"填写BiYong后台访问url",
  "serverUrl":"填写本服务的访问url"
}'

   */
  @PostMapping(value = "setup")
  public void setupAppInfo(@RequestBody AppInfo appInfo) {
    biYongMerchantService.setupAppInfo(appInfo);
  }
}
