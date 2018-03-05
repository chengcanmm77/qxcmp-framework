package com.qxcmp.web.controller;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.util.FinancePageHelper;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aaric
 */
@Controller
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinancePageController extends QxcmpController {


    private final SystemConfigService systemConfigService;
    private final FinancePageHelper financePageHelper;

    /**
     * 获取钱包充值页面
     * <p>
     * 该页面作为整个平台资金来源的唯一入口，即平台的所有消费必须先充值到钱包以后才能进行消费
     *
     * @return 钱包充值页面
     */
    @GetMapping("/deposit")
    public ModelAndView depositGet() {

        return page().addComponent(new Grid().setContainer().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(financePageHelper.nextDepositComponent(systemConfigService)))))
                .setTitle("充值中心")
                .build();
    }

//    @GetMapping("/finance/deposit/order")
//    public ModelAndView depositList(Pageable pageable) {
//        Page<DepositOrder> depositOrders = depositOrderService.findAll(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "timeStart"));
//        Pagination pagination = paginationHelper.next(depositOrders, "/finance/deposit/order");
//        List<ListViewItem> items = depositOrders.getContent().stream().map(depositOrder -> ListViewItem.builder()
//                .title(String.format("【%s】交易金额：%s", depositOrder.getStatus().getValue(), new DecimalFormat("￥0.00").format((double) depositOrder.getFee() / 100)))
//                .description(String.format("交易时间：%s", new SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(depositOrder.getTimeStart())))
//                .link("/finance/deposit/order/" + depositOrder.getEntityId())
//                .build()).collect(Collectors.toList());
//
//        if (items.isEmpty()) {
//            return builder().setTitle("充值记录")
//                    .setResult("充值记录", "您还没有任何充值记录")
//                    .setResultNavigation("前往充值", "/finance/deposit").build();
//        }
//
//        return builder().setTitle("充值记录")
//                .setResult("充值记录", "")
//                .addListView(ListView.builder().items(items).pagination(pagination).build())
//                .setResultNavigation("继续充值", "/finance/deposit")
//                .build();
//    }
//
//    /**
//     * 充值订单详情页面
//     *
//     * @param id 订单号
//     *
//     * @return 充值订单详情页面
//     */
//    @GetMapping("/finance/deposit/order/{id}")
//    public ModelAndView depositDetails(@PathVariable String id) {
//        Optional<DepositOrder> depositOrderOptional = depositOrderService.findOne(id);
//
//        if (!depositOrderOptional.isPresent() || !StringUtils.equals(depositOrderOptional.get().getUserId(), currentUser().getEntityId())) {
//            return error(HttpStatus.NOT_FOUND, "充值记录不存在").build();
//        }
//
//        DepositOrder depositOrder = depositOrderOptional.get();
//
//        return builder().setTitle("充值记录详情")
//                .setResult("充值记录", depositOrder.getStatus().getValue())
//                .addDictionaryView(extractDepositOrderToDictionaryView(depositOrder))
//                .setResultNavigation("返回充值记录", "/finance/deposit/order")
//                .build();
//    }
//
//    private DictionaryView extractDepositOrderToDictionaryView(DepositOrder depositOrder) {
//        return DictionaryView.builder()
//                .dictionary("订单号", depositOrder.getEntityId())
//                .dictionary("充值金额", new DecimalFormat("￥0.00").format((double) depositOrder.getFee() / 100))
//                .dictionary("充值时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(depositOrder.getTimeStart()))
//                .dictionary("备注", depositOrder.getDescription())
//                .build();
//    }
}
