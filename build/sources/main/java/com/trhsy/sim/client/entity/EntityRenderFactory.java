package com.trhsy.sim.client.entity;

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
        try {
            return renderClass.getConstructor(RenderManager.class).newInstance(manager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
