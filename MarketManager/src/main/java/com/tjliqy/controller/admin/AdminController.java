package com.tjliqy.controller.admin;


import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tjliqy.exception.BizException;
import com.tjliqy.interceptors.AuthInterceptor;
import com.tjliqy.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by tjliqy on 2017/4/13.
 */
public class AdminController extends Controller {

    @Clear(AuthInterceptor.class)
    public void login() {
        Optional<User> userOptional = Optional.ofNullable(JsonKit.parse(HttpKit.readData(getRequest()), User.class));
        if (userOptional.isPresent()) {
            User user = User.dao.findFirst("select * from user where account = ? and password = md5(?)", userOptional.get().getAccount(), userOptional.get().getPassword());
            if (user != null) {
                setSessionAttr(AuthInterceptor.USER, user);
                user.setPassword("");
                setAttr("user", user);
            } else {
                throw new BizException("用户名或密码错误", 1);
            }
        } else {
            throw new BizException("登录错误", 1);
        }
    }

    @Clear(AuthInterceptor.class)
    public void signup() {
        Optional<User> userOptional = Optional.ofNullable(JsonKit.parse(HttpKit.readData(getRequest()), User.class));
        if (userOptional.isPresent()) {
            User user = User.dao.findFirst("select * from USER where ACCOUNT = ? ", userOptional.get().getAccount());
            if (user == null) {
                user = userOptional.get();
                user.setPassword(HashKit.md5(user.getPassword()));
                user.save();
                user.setPassword("");
                setAttr("user",user);
            }else {
                throw new BizException("用户名已经被注册",1);
            }
        } else {
            throw new BizException();
        }
    }

    public void user() {
        User user = (User) getSessionAttr(AuthInterceptor.USER);
        user.setPassword("");
        setAttr("user", user);
    }

    public void logout() {
        removeSessionAttr(AuthInterceptor.USER);
    }
}
