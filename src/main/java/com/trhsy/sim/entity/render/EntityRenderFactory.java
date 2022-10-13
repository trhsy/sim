package com.trhsy.sim.entity.render;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.render
 * @ClassName: EntityRenderFactory
 * @Description:
 * @date 2022/10/12 11:39
 */
public class EntityRenderFactory<E extends Entity> implements IRenderFactory<E> {
    private final Class<? extends Render<E>> renderClass;

    public EntityRenderFactory(Class<? extends Render<E>> renderClass) {
        this.renderClass = renderClass;
    }

    @Override
    public Render<E> createRenderFor(RenderManager manager) {
        Render<E> eRender=null;
        try {
            eRender=renderClass.getConstructor(RenderManager.class).newInstance(manager);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("渲染实体人出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return eRender;
    }
}
