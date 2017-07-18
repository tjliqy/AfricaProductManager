package com.tjliqy.server;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.tjliqy.exception.BizException;
import com.tjliqy.model.Goods;
import com.tjliqy.model.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by tjliqy on 2017/3/31.
 */
public class GoodService {

    public static int CLOTHES_TYPE = 1;
    public static int FOOD_TYPE = 2;
    public static int SELL_TYPE = 3;
    public static int PURCHASE_TYPE = 4;

    @Before(Tx.class)
    public Goods purchase(HashMap params) {
        Optional<Object> purchaseOptional = Optional.ofNullable(params.get("purchase"));
        if (params.get("id") != null) {
            Optional<Goods> optional = Optional.ofNullable(Goods.dao.findById(params.get("id")));
            if (optional.isPresent()) {
                params.remove("sell");
                params.remove("purchase");
                Goods goods = JsonKit.parse(JsonKit.toJson(params), Goods.class);
                int oldQuantity = optional.get().getQuantity();
                if (purchaseOptional.isPresent()){
                    int purchaseNum = Integer.valueOf((String) purchaseOptional.get());
                    goods.setQuantity(purchaseNum + oldQuantity);
                    addLog(goods, purchaseNum, PURCHASE_TYPE);
                }else {
                    goods.setQuantity(oldQuantity);
                }
                goods.update();
                return goods;
            } else {
                throw new BizException("没有这个商品", 404);
            }
        } else {
            params.put("quantity",params.get("purchase"));
            params.remove("purchase");
            Goods goods = JsonKit.parse(JsonKit.toJson(params), Goods.class);
            goods.save();
            addLog(goods, goods.getQuantity(), PURCHASE_TYPE);
            return goods;
        }
    }

    @Before(Tx.class)
    public Goods sell(Integer goodsId, Integer num) {
        Optional<Goods> goods = Optional.ofNullable(Goods.dao.findById(goodsId));
        if (goods.isPresent()) {
            Integer perNum = goods.get().getQuantity();
            if (perNum < num) {
                throw new BizException("没有库存", 404);
            } else {
                goods.get().setQuantity(perNum - num);
                goods.get().update();
            }
            addLog(goods.get(), num, SELL_TYPE);
            return goods.get();
        } else {
            throw new BizException("没有该商品", 404);
        }
    }


    public Goods toggleSelling(int goodsId) {
        Optional<Goods> goods = Optional.ofNullable(Goods.dao.findById(goodsId));
        if (goods.isPresent()) {
            int selling = goods.get().getSelling();
            goods.get().setSelling(selling*-1);
            goods.get().update();
        }
        return goods.get();
    }

    private void addLog(Goods goods, long num, int sellOrPurchase) {
        Log log = new Log();
        log.setGoodId(goods.getId());
        log.setCost(goods.getPrize());
        log.setNum(num);
        log.setTotalCost(goods.getPrize().multiply(BigDecimal.valueOf(num)));
        log.setType(sellOrPurchase);
        log.save();
    }
}
