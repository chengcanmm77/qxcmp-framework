package com.qxcmp.mall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.exception.ShoppingCartServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 购物车项目服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartItemService extends AbstractEntityService<ShoppingCartItem, Long, ShoppingCartItemRepository> {

    private final CommodityService commodityService;


    /**
     * 获取用户的购物车项目
     *
     * @param userId 用户ID
     * @return 用户的购物车项目
     */
    public List<ShoppingCartItem> findByUser(String userId) {
        return repository.findByUserIdOrderByDateCreatedDesc(userId);
    }

    /**
     * 获取用户已经选择的购物车项目
     *
     * @param userId 用户ID
     * @return 用户已经选择的购物车项目
     */
    public List<ShoppingCartItem> findSelectedByUser(String userId) {
        return findByUser(userId).stream().filter(ShoppingCartItem::isSelected).collect(Collectors.toList());
    }

    /**
     * 添加商品到用户购物车
     * <p>
     * 如果商品未曾添加，则添加数量为1的商品到用户购物车
     * <p>
     * 如果商品已经添加，则设置商品在用户购物车的数量+1
     *
     * @param userId      用户ID
     * @param commodityId 要添加的商品
     * @throws ShoppingCartServiceException 如果添加失败抛出该异常
     */
    public void addCommodity(String userId, long commodityId) throws ShoppingCartServiceException {
        try {
            Commodity commodity = commodityService.findOne(commodityId).orElseThrow(() -> new ShoppingCartServiceException("Commodity not exist"));

            Optional<ShoppingCartItem> targetItem = findByUser(userId).stream().filter(shoppingCartItem -> shoppingCartItem.getCommodity().getId() == commodityId).findFirst();

            if (targetItem.isPresent()) {
                update(targetItem.get().getId(), shoppingCartItem -> {
                    shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + 1);
                    shoppingCartItem.setDateModified(new Date());
                    shoppingCartItem.setSelected(true);
                    shoppingCartItem.setCommodity(commodity);
                });
            } else {
                create(() -> {
                    ShoppingCartItem shoppingCartItem = next();
                    shoppingCartItem.setUserId(userId);
                    shoppingCartItem.setSelected(true);
                    shoppingCartItem.setQuantity(1);
                    shoppingCartItem.setDateCreated(new Date());
                    shoppingCartItem.setDateModified(new Date());
                    shoppingCartItem.setOriginPrice(commodity.getSellPrice());
                    shoppingCartItem.setCommodity(commodity);
                    return shoppingCartItem;
                });
            }
        } catch (Exception e) {
            throw new ShoppingCartServiceException("Can' create shopping cart item", e);
        }
    }

    /**
     * 修改购物车项目里面的商品数量
     *
     * @param id     购物车项目ID
     * @param amount 要设置的商品数量，必须大于零
     * @throws ShoppingCartServiceException 如果设置失败抛出该异常
     */
    public void modifyAmount(long id, int amount) throws ShoppingCartServiceException {
        update(id, shoppingCartItem -> shoppingCartItem.setQuantity(amount));
    }

}
