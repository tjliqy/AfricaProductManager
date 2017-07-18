package com.tjliqy.controller.admin;

import com.jfinal.core.Controller;
//import com.tjliqy.interceptor.JsonInterceptor;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tjliqy.model.Goods;
import com.tjliqy.server.GoodService;
import com.tjliqy.util.SearchUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tjliqy on 2017/3/30.
 */
//@Before(JsonInterceptor.class)
public class GoodsController extends Controller {

    /**
     * 采购商品
     * POST
     *
     */
    public void purchase() {
        GoodService goodService = enhance(GoodService.class);
        HashMap params = JsonKit.parse(HttpKit.readData(getRequest()), HashMap.class);
        Goods goods = goodService.purchase(params);
        setAttr("goods",goods);
    }

    /**
     * 销售商品
     * POST
     */
    public void sell() {
        GoodService goodService = enhance(GoodService.class);
        HashMap params = JsonKit.parse(HttpKit.readData(getRequest()), HashMap.class);
        Goods goods = goodService.sell((Integer) params.get("id"), Integer.valueOf((String) params.get("quantity")));
        setAttr("goods",goods);
    }


    public void search() {
        SearchUtil util = new SearchUtil("goods");
        util.addStrCondition("name", getPara("name"));
        util.addStrCondition("brand",getPara("brand"));
        util.addIntCondition("prize",getPara("prize"));
        util.addIntCondition("type",getPara("type"));
        util.addIntCondition("quantity",getPara("cost"));
        util.addIntCondition("selling",getPara("selling",getPara("selling")));
        util.addIntRangeCondition("cost",getPara("minCost"),getPara("larCost"));
        util.addIntRangeCondition("prize",getPara("minPrize"),getPara("larPrize"));
        util.addOrderCondition(getPara("orderCon"), getParaToBoolean("desc", true));
        Page<Goods> paginate = Goods.dao.paginate(getParaToInt("page", 1), getParaToInt("size", 10), util.getSelect(), util.getExceptSelect());
        setAttr("data", paginate);
    }

    /**
     * 改变商品上下架状态
     * POST
     */
    public void toggleSelling(){
        GoodService goodService = enhance(GoodService.class);
        HashMap params = JsonKit.parse(HttpKit.readData(getRequest()), HashMap.class);
        Goods goods = goodService.toggleSelling((Integer) params.get("id"));
        setAttr("goods",goods);
    }


    public void detail() {
        int id = getParaToInt("id");
        Record record;
//        record = Db.findFirst("select * from goods G,food F where G.id = ? AND F.id = ?", id, id);
        record = Db.findFirst("select * from goods where id = ?", id);
//
//        if (record == null){
//            record = Db.findFirst("select * from goods G,clothes C where G.id = ? AND C.id = ?",id,id);
//        }
        setAttr("data",record);
    }
    /**
     * todo 这个方法需要打磨。
     */
    public void list4Purchase(){
        List<Goods> list = Goods.dao.find("select * from goods orderby quantity");

        setAttr("list",list);
    }

}
