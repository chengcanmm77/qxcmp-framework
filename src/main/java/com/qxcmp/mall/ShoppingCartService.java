package com.qxcmp.mall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * 购物车服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartService extends AbstractEntityService<ShoppingCart, String, ShoppingCartRepository> {

    /**
     * 获取用户购物车
     * <p>
     * 如果用户存在但是购物车不存在，则返回新创建的购物车
     *
     * @param userId 用户ID
     *
     * @return 用户购物车
     */
    public ShoppingCart findByUserId(String userId) {
        return repository.findByUserId(userId).orElseGet(() -> create(() -> {
            ShoppingCart shoppingCart = next();
            shoppingCart.setUserId(userId);
            return shoppingCart;
        }));
    }


    @Override
    public ShoppingCart create(Supplier<ShoppingCart> supplier) {
        ShoppingCart shoppingCart = supplier.get();

        if (StringUtils.isNotEmpty(shoppingCart.getId())) {
            return null;
        }

        shoppingCart.setId(IDGenerator.next());

        return super.create(() -> shoppingCart);
    }
}
