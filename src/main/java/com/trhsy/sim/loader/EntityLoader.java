package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.entity.render.EntityRenderFactory;
import com.trhsy.sim.entity.render.RenderEntityFolk;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: EntityLoader
 * @Description:
 * @date 2022/10/12 9:42
 */
public class EntityLoader {
    private static int nextID = 0;
    public EntityLoader() {


    }
    public static void init() {
        registerEntity(EntityFolk.class, "EntityFolk", 64, 3, true);
    }
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            // TODO
            registerEntityRender(EntityFolk.class, RenderEntityFolk.FACTORY);
            //registerEntityRender(EntityAlignBeam.class, RenderAlignBeam.class);
            //registerEntityRender(EntityConBox.class, RenderConBox.class);
            //registerEntityRender(EntityWindmill.class, RenderWindmill.class);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @param entityClass
     * @param renderFactory
     * @return void
     * @Author fan
     * @Description //TODO 注册实体渲染模型
     * @Date 17:19 2022/5/22
     * @Param [entityClass, render]
     */
    @SideOnly(Side.CLIENT)
    private static <T extends Entity> void registerEntityRender(Class<T> entityClass, IRenderFactory<? super T> renderFactory) {
        try {
            RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerEntityRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @return void
     * @Author fan
     * @Description //TODO 注册实体
     * @Date 22:38 2022/5/21
     * @Param [entityClass, name, trackingRange, updateFrequency, sendsVelocityUpdates]
     **/
    private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange,
                                       int updateFrequency, boolean sendsVelocityUpdates) {
        try {
            EntityRegistry.registerModEntity(entityClass, name, nextID++, ModSim.instance, trackingRange, updateFrequency,
                    sendsVelocityUpdates);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerEntity出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
