package com.tjliqy;

import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

/**
 * Created by tjliqy on 2017/3/26.
 */
public class RenderJsonFactory extends RenderFactory {
    @Override
    public Render getJsonRender() {
        return super.getJsonRender();
    }

    @Override
    public Render getJsonRender(String key, Object value) {
        return super.getJsonRender(key, value);
    }

    @Override
    public Render getJsonRender(String[] attrs) {
        return super.getJsonRender(attrs);
    }

    @Override
    public Render getJsonRender(String jsonText) {
        return super.getJsonRender(jsonText);
    }

    @Override
    public Render getJsonRender(Object object) {
        return super.getJsonRender(object);
    }
}
