package com.tjliqy.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.tjliqy.model.Equipment;
import com.tjliqy.server.EquipmentService;
import com.tjliqy.util.SearchUtil;

import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by tjliqy on 2017/7/18.
 */
public class EquipmentController extends Controller {

    public void search(){
        SearchUtil util = new SearchUtil("equipment");
        util.addStrCondition("name",getPara("name"));
        util.addStrCondition("number",getPara("number"));
        Page<Equipment> paginate = Equipment.dao.paginate(getParaToInt("page", 1), getParaToInt("size", 10), util.getSelect(), util.getExceptSelect());
        setAttr("data",paginate);
    }

    /**
     * 增加新设备/修改设备属性
     */
    public void edit() throws ParseException {
        EquipmentService equipmentService = enhance(EquipmentService.class);
        HashMap params = JsonKit.parse(HttpKit.readData(getRequest()), HashMap.class);
        Equipment equipment = equipmentService.edit(params);
        setAttr("equipment",equipment);
    }
}
