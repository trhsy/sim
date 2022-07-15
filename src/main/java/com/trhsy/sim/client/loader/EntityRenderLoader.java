package com.trhsy.sim.client.loader;

import com.trhsy.sim.common.loader.EntityLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;

/**
 * @ClassName EntityRenderLoader
 * @Description todo 实体渲染加载
 * @Author Tian
 * @Date 2022/5/2216:49
 **/
public class EntityRenderLoader {
    public EntityRenderLoader() {
        try {
            EntityLoader.registerRenders();
        } catch (Exception e) {
            ModSimReloaded.log.error("实体渲染加载出差了：" + e.getMessage());
        }
    }
}
