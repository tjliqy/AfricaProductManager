package com.tjliqy.server;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.tjliqy.exception.BizException;
import com.tjliqy.model.Clothes;
import com.tjliqy.model.Food;
import com.tjliqy.model.Goods;
import com.tjliqy.model.Log;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (params.get("id") != null) {
            int purchaseNum = Integer.valueOf((String) params.get("num"));
            Optional<Goods> optional = Optional.ofNullable(Goods.dao.findById(params.get("id")));
            if (optional.isPresent()) {
                Goods goods = optional.get();
                goods.setNum(purchaseNum + optional.get().getNum());
                goods.update();
                addLog(goods, purchaseNum, PURCHASE_TYPE);
                return goods;
            } else {
                throw new BizException("没有这个商品", 404);
            }
        } else {
            Goods goods = new Goods();
            if (params.get("type").equals(CLOTHES_TYPE)) {
                Clothes clothes = new Clothes();
                clothes.setColor((String) params.get("color"));
                clothes.setSize((String) params.get("size"));
                clothes.setCrowd((String) params.get("crowd"));
                params.remove("color");
                params.remove("size");
                params.remove("crowd");
                params.remove("shelf_life");
                params.remove("origin");
                goods = JsonKit.parse(JsonKit.toJson(params), Goods.class);
                goods.save();
                clothes.setId(goods.getId());
                clothes.save();
            } else if (params.get("type").equals(FOOD_TYPE)) {
                Food food = new Food();
                DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    food.setShelfLife(df.parse((String) params.get("shelf_life")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                food.setOrigin((String) params.get("origin"));
                params.remove("color");
                params.remove("size");
                params.remove("crowd");
                params.remove("shelf_life");
                params.remove("origin");
                goods = JsonKit.parse(JsonKit.toJson(params), Goods.class);
                goods.save();
                food.setId(goods.getId());
                food.save();
            } else {
                throw new BizException();
            }
            addLog(goods, goods.getNum(), PURCHASE_TYPE);
            return goods;
        }
    }

    @Before(Tx.class)
    public Goods sell(Integer goodsId, Integer num) {
        Optional<Goods> goods = Optional.ofNullable(Goods.dao.findById(goodsId));
        if (goods.isPresent()) {
            Integer perNum = goods.get().getNum();
            if (perNum < num) {
                throw new BizException("没有库存", 404);
            } else {
                goods.get().setNum(perNum - num);
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

    private void addLog(Goods goods, int num, int sellOrPurchase) {
        Log log = new Log();
        log.setGoodId(goods.getId());
        log.setCost(goods.getCost());
        log.setNum(num);
        log.setTotalCost(goods.getCost().multiply(BigDecimal.valueOf(num)));
        log.setType(sellOrPurchase);
        log.save();
    }
}
