package com.qxcmp.mall.web;

import com.google.common.collect.Sets;
import com.qxcmp.exception.ShoppingCartServiceException;
import com.qxcmp.mall.Consignee;
import com.qxcmp.mall.ConsigneeService;
import com.qxcmp.mall.ShoppingCartService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mall/consignee")
@RequiredArgsConstructor
public class ConsigneeController extends QxcmpController {

    private final ConsigneeService consigneeService;

    private final ShoppingCartService shoppingCartService;

    private final MallPageHelper mallPageHelper;

    @GetMapping("")
    public ModelAndView consigneePage() {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Consignee> consignees = consigneeService.findByUser(user.getId());

        if (consignees.isEmpty()) {
            return redirect("/mall/consignee/new");
        }

        return page().addComponent(mallPageHelper.nextMobileConsignee(consignees))
                .build();
    }

    @GetMapping("/select")
    public ModelAndView consigneeSelectPage(@RequestParam String id) throws ShoppingCartServiceException {

        User user = currentUser().orElseThrow(RuntimeException::new);

        shoppingCartService.update(shoppingCartService.findByUserId(user.getId()).getId(), shoppingCart -> shoppingCart.setConsigneeId(id));

        return redirect("/mall/cart/order");

    }

    @GetMapping("/new")
    public ModelAndView consigneeNewPage(final MallConsigneeNewForm form) {
        return page().addComponent(new Grid().setContainer().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(convertToForm(form)))))
                .build();
    }

    @PostMapping("/new")
    public ModelAndView consigneeNewPage(@Valid final MallConsigneeNewForm form, BindingResult bindingResult,
                                         @RequestParam(value = "add_labels", required = false) boolean addLabels,
                                         @RequestParam(value = "remove_labels", required = false) Integer removeLabels) {

        if (addLabels) {
            form.getLabels().add("");
            return page().addComponent(new Grid().setContainer().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(convertToForm(form)))))
                    .build();
        }

        if (Objects.nonNull(removeLabels)) {
            form.getLabels().remove(removeLabels.intValue());
            return page().addComponent(new Grid().setContainer().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(convertToForm(form)))))
                    .build();
        }

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Grid().setContainer().setVerticallyPadded().addItem(new Row().addCol(new Col().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))))
                    .build();
        }

        Consignee consignee = consigneeService.create(() -> {
            final Consignee next = consigneeService.next();
            next.setUserId(user.getId());
            next.setName(form.getLabels().isEmpty() ? form.getConsigneeName() : form.getLabels().stream().findFirst().orElse(""));
            next.setConsigneeName(form.getConsigneeName());
            next.setAddress(form.getAddress());
            next.setTelephone(form.getTelephone());
            next.setEmail(form.getEmail());
            next.setDateCreated(new Date());
            next.setDateModified(new Date());
            next.setLabels(Sets.newLinkedHashSet(form.getLabels()));
            return next;
        });

        shoppingCartService.update(shoppingCartService.findByUserId(user.getId()).getId(), shoppingCart -> shoppingCart.setConsigneeId(consignee.getId()));


        return redirect("/mall/cart/order");
    }
}
