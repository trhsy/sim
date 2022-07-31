package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.entity.*;
import com.trhsy.sim.common.entity.EntityAlignBeam;
import com.trhsy.sim.common.entity.EntityConBox;
import com.trhsy.sim.common.entity.EntityFolk;
import com.trhsy.sim.common.entity.EntityWindmill;
import com.trhsy.sim.common.event.PlayerRightClickGrassBlockEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @ClassName EntityLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/5/2121:07
 **/
public class EntityLoader {
    private static int nextID = 0;

    public EntityLoader() {
        //System.out.println("开始加载实体");
        try {
            registerEntity(EntityFolk.class, "EntityFolk", 80, 3, true);
            registerEntityEgg(EntityFolk.class, 0xffff66, 0x660000);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("EntityLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            // TODO
            registerEntityRender(EntityFolk.class, RenderEntityFolk.class);
            registerEntityRender(EntityAlignBeam.class, RenderAlignBeam.class);
            registerEntityRender(EntityConBox.class, RenderConBox.class);
            registerEntityRender(EntityWindmill.class, RenderWindmill.class);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @param entityClass
     * @param render
     * @return void
     * @Author fan
     * @Description //TODO 注册实体渲染模型
     * @Date 17:19 2022/5/22
     * @Param [entityClass, render]
     */
    @SideOnly(Side.CLIENT)
    private static <T extends Entity> void registerEntityRender(Class<T> entityClass, Class<? extends Render<T>> render) {
        try {
            RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<T>(render));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerEntityRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerEntity出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 注册实体到生物蛋中
     * @Date 22:38 2022/5/21
     * @Param [entityClass, eggPrimary, eggSecondary]
     **/
    private static void registerEntityEgg(Class<? extends Entity> entityClass, int eggPrimary, int eggSecondary) {
        try {
            EntityRegistry.registerEgg(entityClass, eggPrimary, eggSecondary);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerEntityEgg出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @SubscribeEvent
    public void onPlayerClickGrassBlock(PlayerRightClickGrassBlockEvent event) {
        try {
            /*if (!event.world.isRemote)
        {
            ItemStack heldItem = event.entityPlayer.getHeldItem();
            if (ItemLoader.goldenEgg.equals(heldItem.getItem()))
            {
                EntityLiving entityLiving = new EntityFolk(event.world);
                BlockPos pos = event.pos;
                entityLiving.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                --heldItem.stackSize;
                event.world.spawnEntityInWorld(entityLiving);
                return;
            }
            BlockPos pos = event.pos;
            Entity tnt = new EntityTNTPrimed(event.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, null);
            event.world.spawnEntityInWorld(tnt);
            event.entityPlayer.triggerAchievement(AchievementLoader.explosionFromGrassBlock);
        }*/
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onPlayerClickGrassBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
