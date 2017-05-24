package com.tjliqy.interceptors;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.tjliqy.exception.BizException;

/**
 * Created by yanz on 2017/3/23.
 */
public class JsonResponse implements Interceptor {
    private static final String RES_KEY = "errno";
    private static final String MSG_KEY = "msg";

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        controller.setAttr(MSG_KEY, "success");
        controller.setAttr(RES_KEY, -1);

        try {
            inv.invoke();
        } catch (Exception ex) {
            ex.printStackTrace();
            controller.setAttr(MSG_KEY, ex instanceof BizException ? ((BizException) ex).getErrorCode() + ": " + ((BizException) ex).getMsg() : "unknown error.");
            controller.setAttr(RES_KEY, ex instanceof BizException ? ((BizException) ex).getErrorCode() : 1);
        }
        controller.renderJson();
    }
}
