package com.qxcmp.web.api;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qxcmp.exception.FinanceException;
import com.qxcmp.exception.OrderStatusException;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.finance.OrderStatusEnum;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.components.weixin.WeixinPayScript;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_NOTIFY_URL;


/**
 * 微信支付API
 * <p>
 * 负责平台充值微信相关支付功能
 *
 * @author aaric
 */
@Controller
@RequestMapping("/api/wxmp-cgi/pay")
@RequiredArgsConstructor
public class WeixinPaymentAPI extends QxcmpController {

    private final WxPayService wxPayService;
    private final DepositOrderService depositOrderService;


    /**
     * 平台充值微信扫码支付通用API
     * <p>
     * 该接口返回带微信支付二维码的充值中心页面
     *
     * @param fee     充值金额
     * @param feeType 货币类型
     *
     * @return 带微信支付二维码的充值中心页面
     */
    @PostMapping("/native")
    public ModelAndView weixinNativePayment(@RequestParam int fee,
                                            @RequestParam(defaultValue = "CNY") String feeType,
                                            @RequestParam(defaultValue = "/finance/deposit") String successCallback,
                                            @RequestParam(defaultValue = "/finance/deposit") String failedCallback) {
        return null;
    }

    /**
     * 平台充值微信公众号支付通用API
     *
     * @param fee     充值金额
     * @param feeType 货币类型
     *
     * @return 微信公众号支付页面
     */
    @PostMapping("/mp")
    public ModelAndView weixinMpPayment(@RequestParam int fee,
                                        @RequestParam(defaultValue = "CNY") String feeType,
                                        @RequestParam(required = false) String callback) throws OrderStatusException, WxPayException {

        User user = currentUser().orElseThrow(RuntimeException::new);

        DepositOrder depositOrder = createDepositOrder(fee * 100, feeType, user.getId(), user);

        Map<String, String> wxPayInfo = doWeixinPayment("JSAPI", user, depositOrder);

        return page(viewHelper.nextInfoOverview("正在支付", "支付完成后请耐心等待页面自动跳转，否则充值可能会失败"))
                .addComponent(new WeixinPayScript(wxPayInfo, depositOrder))
                .setTitle("充值中心")
                .addObject("callback", callback)
                .build();
    }

    /**
     * 接收微信服务器发来的异步支付结果通知
     * <p>
     * 此接口用户自动处理订单
     *
     * @param xmlData 支付结果数据
     *
     * @return 回复"SUCCESS"表示平台处理订单成功，否则微信服务器将继续发送该通知
     */
    @PostMapping("/notify")
    public ResponseEntity<String> weixinPaymentNotification(@RequestBody String xmlData) {
        try {
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
            depositOrderService.process(result.getOutTradeNo());
            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    /**
     * 微信支付订单查询接口
     * <p>
     * 如果查询成功则自动处理订单，并返回SUCCESS
     * <p>
     * 如果查询失败则重新发起请求
     *
     * @param transactionId 订单号
     * @param outTradeNo    订单号
     *
     * @return "SUCCESS", "ERROR"
     *
     * @throws WxPayException WxPayException
     */
    @GetMapping("/query")
    public ResponseEntity<String> wechatOrderQuery(@RequestParam(required = false) String transactionId, @RequestParam(required = false) String outTradeNo) throws WxPayException {

        WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(transactionId, outTradeNo);

        if (wxPayOrderQueryResult.getReturnCode().equalsIgnoreCase("SUCCESS")
                && wxPayOrderQueryResult.getResultCode().equalsIgnoreCase("SUCCESS")
                && wxPayOrderQueryResult.getTradeState().equalsIgnoreCase("SUCCESS")) {
            try {
                Optional<DepositOrder> depositOrder = depositOrderService.findOne(wxPayOrderQueryResult.getOutTradeNo());

                if (depositOrder.isPresent() && depositOrder.get().getStatus().equals(OrderStatusEnum.FINISHED)) {
                    return ResponseEntity.ok("SUCCESS");
                }

                depositOrderService.process(wxPayOrderQueryResult.getOutTradeNo());
                return ResponseEntity.ok("SUCCESS");
            } catch (FinanceException e) {
                return ResponseEntity.ok(e.getMessage());
            }
        }

        return ResponseEntity.ok("ERROR");
    }

    private DepositOrder createDepositOrder(int fee, String feeType, String userId, User user) throws OrderStatusException {
        return depositOrderService.create(() -> {
            DepositOrder order = depositOrderService.next();
            order.setUserId(userId);
            order.setUserId(user.getId());
            order.setFee(fee);
            order.setFeeType(feeType);
            return order;
        });
    }

    /**
     * 生成微信预支付交易单
     *
     * @param tradeType    支付类型 NATIVE|JSAPI
     * @param user         用户标识
     * @param depositOrder 平台充值订单
     *
     * @return 预支付结果
     */
    private Map<String, String> doWeixinPayment(String tradeType, User user, DepositOrder depositOrder) throws WxPayException {
        WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder requestBuilder = WxPayUnifiedOrderRequest.newBuilder();
        requestBuilder.notifyURL(systemConfigService.getString(SYSTEM_CONFIG_WECHAT_NOTIFY_URL).orElse(""));
        requestBuilder.deviceInfo("WEB");
        requestBuilder.body(String.format("%s-钱包充值", siteService.getTitle()));
        requestBuilder.outTradeNo(depositOrder.getId());
        requestBuilder.feeType(depositOrder.getFeeType());
        requestBuilder.totalFee(depositOrder.getFee());
        requestBuilder.spbillCreateIp(getRequestAddress());
        requestBuilder.timeStart(new SimpleDateFormat("yyyyMMddHHmmss").format(depositOrder.getTimeStart()));
        requestBuilder.timeExpire(new SimpleDateFormat("yyyyMMddHHmmss").format(depositOrder.getTimeEnd()));
        requestBuilder.openid(user.getOpenID());
        requestBuilder.tradeType(tradeType);
        return wxPayService.getPayInfo(requestBuilder.build());
    }
}
