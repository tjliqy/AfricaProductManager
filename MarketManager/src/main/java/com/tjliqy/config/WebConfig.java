package com.tjliqy.config;

import com.jfinal.config.*;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
//import com.tjliqy.controller.admin.AdminController;
import com.tjliqy.IndexController;
import com.tjliqy.controller.admin.AdminController;
import com.tjliqy.controller.admin.EquipmentController;
import com.tjliqy.controller.admin.GoodsController;
import com.tjliqy.controller.admin.LogController;
import com.tjliqy.interceptors.AuthInterceptor;
import com.tjliqy.interceptors.JsonResponse;
import com.tjliqy.model._MappingKit;

/**
 * Created by tjliqy on 2017/3/26.
 */
public class WebConfig extends JFinalConfig{

    public void configConstant(Constants constants) {
        PropKit.use("db.properties");
        constants.setJsonFactory(new JacksonFactory());
        constants.setViewType(ViewType.FREE_MARKER);
        constants.setDevMode(PropKit.getBoolean("devMode", false));
        // 设置文件上传和下载基础路径
//        String basePath = PropKit.use("common.properties").get("fileUploadFolder");
//        constants.setBaseUploadPath(basePath);
//        constants.setBaseDownloadPath(basePath);
    }

    public void configRoute(Routes routes) {
        routes.addInterceptor(new JsonResponse());
//        routes.addInterceptor(new AuthInterceptor());
        routes.add("/",IndexController.class);
        routes.add("/api/admin",AdminController.class);
        routes.add("/api/goods", GoodsController.class);
        routes.add("/api/equipment", EquipmentController.class);
        routes.add("/api/log", LogController.class);
    }

    public void configEngine(Engine engine) {

    }

    public void configPlugin(Plugins plugins) {
        //db plugin
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        plugins.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        //add sql template engine
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());

        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        plugins.add(arp);

    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {

    }
}
