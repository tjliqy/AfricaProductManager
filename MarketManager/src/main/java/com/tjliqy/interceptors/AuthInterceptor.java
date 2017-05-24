package com.tjliqy.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.tjliqy.exception.BizException;

import javax.mail.Session;
import java.util.Optional;

/**
 * Created by tjliqy on 2017/4/13.
 */
public class AuthInterceptor implements Interceptor {

    public static final String USER = "user";

    @Override
    public void intercept(Invocation invocation) {
        Controller controller = invocation.getController();
        Optional<Session> sessionOptional = Optional.ofNullable(controller.getSessionAttr(USER));
        if(sessionOptional.isPresent()){
            invocation.invoke();
        }else {
            throw new BizException("未登录",401);
        }
    }
}
