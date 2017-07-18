package com.tjliqy;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.tjliqy.interceptors.JsonResponse;

/**
 * Created by tjliqy on 2017/7/16.
 */
public class IndexController extends Controller {
    @Clear(JsonResponse.class)
    public void index(){
        render("index.html");
    }
}
