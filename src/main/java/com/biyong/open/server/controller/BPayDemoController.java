package com.biyong.open.server.controller;


import com.alibaba.fastjson.JSON;
import com.biyong.open.server.merchant.req.BPayCallbackSetReq;
import com.biyong.open.server.merchant.req.BPayCloseReq;
import com.biyong.open.server.merchant.req.BPayOrderCreateReq;
import com.biyong.open.server.merchant.req.BPayOrderQueryReq;
import com.biyong.open.server.merchant.req.BPayRefundReq;
import com.biyong.open.server.merchant.resp.BPayConfigQueryResp;
import com.biyong.open.server.merchant.resp.BPayOrderCreateResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryListResp;
import com.biyong.open.server.merchant.resp.BPayOrderQueryResp;
import com.biyong.open.server.service.BiYongMerchantService;
import com.biyong.open.server.utils.Utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("b-pay")
public class BPayDemoController {
  private final BiYongMerchantService biYongMerchantService;

  @Autowired
  public BPayDemoController(BiYongMerchantService biYongMerchantService) {
    this.biYongMerchantService = biYongMerchantService;
  }

  private static ConcurrentHashMap<String, BPayOrderQueryResp> successOrders = new ConcurrentHashMap<>();

  /**
   * 支付配置查询
   */
  @GetMapping(value = "config/query")
  public BPayConfigQueryResp queryConfig() {
    return biYongMerchantService.queryBPayConfig();
  }

  /**
   * 创建支付订单
   */
  @GetMapping(value = "create")
  public BPayOrderCreateResp create() {
    Random random = new Random();
    BPayOrderCreateReq req = new BPayOrderCreateReq();
    req.setOutOrderCode("OUT" + System.currentTimeMillis());
    req.setOrderName("一个GRAM");
    req.setExpireSec(60 + random.nextInt(180));
    req.setRemark("备注是一个汉语词汇，读音为bèi zhù。词语的一般解释表册上供填写附注的栏目；第二种意思指在备注栏内所加的注解说明。");

    // 在允许范围内随机生成支付价格
    List<BPayOrderCreateReq.Price> list = new ArrayList<>();
    BPayConfigQueryResp config = queryConfig();
    for (BPayConfigQueryResp.AppBPayCoinConfig c : config.getCoinConfigs()) {
      if (c.getCoinName().equals("GRAM") || random.nextBoolean()) {
        BPayOrderCreateReq.Price price = new BPayOrderCreateReq.Price();
        price.setBalance(randomBetween(c.getPerMin(), c.getPerMax().divide(new BigDecimal(100), 5, RoundingMode.DOWN)));
        price.setCoinName(c.getCoinName());
        list.add(price);
        if (list.size() >= config.getMaxCoinNum()) {
          break;
        }
      }
    }
    req.setMultiPrice(list);

    // 创建支付订单
    BPayOrderCreateResp resp = biYongMerchantService.createBPayOrder(req);
    resp.setMultiPrice(list);
    return resp;
  }

  private static BigDecimal randomBetween(BigDecimal min, BigDecimal max) {
    return min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)))
              .setScale(min.scale(), RoundingMode.DOWN).stripTrailingZeros();
  }

  /**
   * 查询单笔订单
   */
  @GetMapping(value = "query")
  public BPayOrderQueryResp query(
      @RequestParam(value = "payToken", required = false) String payToken,
      @RequestParam(value = "orderCode", required = false) String orderCode,
      @RequestParam(value = "outOrderCode", required = false) String outOrderCode) {
    BPayOrderQueryReq req = new BPayOrderQueryReq();
    req.setPayToken(payToken);
    req.setOrderCode(orderCode);
    req.setOutOrderCode(outOrderCode);
    return biYongMerchantService.queryBPayOrder(req);
  }

  /**
   * 查询订单列表
   * 参数:
   *
   * startTime
   * endTime
   * pageNo
   * pageSize
   */
  @PostMapping(value = "query/list")
  public BPayOrderQueryListResp queryList(@RequestBody BPayOrderQueryReq req) {
    return biYongMerchantService.queryBPayOrderListByTime(req);
  }

  /**
   * 单笔订单关闭
   */
  @GetMapping(value = "close")
  public BPayOrderQueryResp close(
      @RequestParam(value = "orderCode", required = false) String orderCode,
      @RequestParam(value = "outOrderCode", required = false) String outOrderCode) {
    BPayCloseReq req = new BPayCloseReq();
    req.setOrderCode(orderCode);
    req.setOutOrderCode(outOrderCode);
    return biYongMerchantService.closeBPayOrder(req);
  }

  /**
   * 单笔订单退款
   */
  @GetMapping(value = "refund")
  public BPayOrderQueryResp refund(
      @RequestParam(value = "orderCode", required = false) String orderCode,
      @RequestParam(value = "outOrderCode", required = false) String outOrderCode) {
    BPayRefundReq req = new BPayRefundReq();
    req.setOrderCode(orderCode);
    req.setOutOrderCode(outOrderCode);
    return biYongMerchantService.refundBPayOrder(req);
  }

  @PostMapping(value = "callback/setup")
  public void setupCallback(@RequestBody BPayCallbackSetReq req) {
    biYongMerchantService.setupCallback(req);
  }

  /**
   * 支付成功后回调
   */
  @PostMapping(value = "callback")
  public void callback(
      @RequestParam(value = "test", required = false, defaultValue = "false") Boolean testCall,
      HttpServletRequest request) throws IOException {
    String rsaSignHashMode = request.getHeader(Utils.Headers.RsaSignHashMode.name());
    String aesMode = request.getHeader(Utils.Headers.AesEncryptMode.name());
    System.out.println("receive callback " + rsaSignHashMode +
                       (StringUtils.isEmpty(aesMode) ? "" : " AES/" + aesMode));
    byte[] data = Utils.readInputStream(request.getInputStream());
    Utils.MerchantRequest result = biYongMerchantService.readCallback(rsaSignHashMode, aesMode, data);
    if (result == null || result.getData() == null) {
      System.out.println("sign error, fake callback ignored");
      return;
    }
    String s = new String(result.getData());
    if (testCall) {
      // 服务器测试调用
      System.out.println(s);
      return;
    }
    System.out.println("order success callback: " + s);
    BPayOrderQueryResp order = JSON.parseObject(s, BPayOrderQueryResp.class);
    successOrders.put(order.getOutOrderCode(), order);
  }

  /**
   * 用户回到页面后查询订单结果
   */
  @GetMapping(value = "returnQuery/{outOrderCode}")
  public BPayOrderQueryResp returnQuery(@PathVariable("outOrderCode") String outOrderCode) {
    BPayOrderQueryResp r = successOrders.get(outOrderCode);
    if (r == null) {
      // 查询一下到底成功没有
      r = query(null, null, outOrderCode);
      if (r.getStatus().equals("USER_PAY_SUCCESS")) {
        successOrders.put(outOrderCode, r);
      } else {
        return null;
      }
    }
    return r;
  }
}
