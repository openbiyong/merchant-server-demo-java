package com.biyong.open.server.controller;


import com.biyong.open.server.service.impl.BiYongMerchantServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "page")
public class WebController {

  private void loadServerInfo(Model model) {
    model.addAttribute("serverUrl", BiYongMerchantServiceImpl.serverUrl);
    model.addAttribute("appId", BiYongMerchantServiceImpl.appId);
  }

  @GetMapping("{page}")
  public String setupAppInfo(@PathVariable("page") String page, Model model) {
    loadServerInfo(model);
    switch (page) {
      case "auth":
      case "pay":
      case "payReturn":
        break;
      default:
        page = "auth";
    }
    return page;
  }
}
