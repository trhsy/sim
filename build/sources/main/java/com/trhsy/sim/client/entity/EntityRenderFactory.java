package com.trhsy.sim.client.entity;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * @ClassName EntityRenderFactory
 * @Description todo EntityRenderFactory类的作用是传入一个Class类的实例，去调用它代表的类的一个传入RenderManager的构造方法生成。
 * @Author Tian
 * @Date 2022/5/2216:53
 **/
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
            ModSimReloaded.log.error("渲染实体人出错了：" + e.getMessage());
        }
        return eRender;
    }
}
