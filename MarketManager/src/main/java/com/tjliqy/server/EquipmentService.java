package com.tjliqy.server;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.tjliqy.exception.BizException;
import com.tjliqy.model.Equipment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by tjliqy on 2017/7/18.
 */
public class EquipmentService {

    @Before(Tx.class)
    public Equipment edit(HashMap params) throws ParseException {
        if(params.get("id") != null){
            Optional<Equipment> optional = Optional.ofNullable(Equipment.dao.findById(params.get("id")));
            if (optional.isPresent()){
                params.remove("lastValue");
                params.remove("purchaseDate");
                params.remove("depreciationRate");
                params.remove("");
                Equipment equipment = JsonKit.parse(JsonKit.toJson(params),Equipment.class);
                equipment.update();
                return equipment;
            }else {
                throw new BizException("没有这个设备",404);
            }
        }else {

            Equipment equipment = JsonKit.parse(JsonKit.toJson(params),Equipment.class);
            equipment.save();
            return equipment;
        }
    }
}
