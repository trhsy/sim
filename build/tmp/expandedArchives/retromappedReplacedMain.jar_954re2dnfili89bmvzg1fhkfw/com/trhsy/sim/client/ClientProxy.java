package com.trhsy.sim.client;

import com.trhsy.sim.client.loader.EntityRenderLoader;
import com.trhsy.sim.client.loader.ItemRenderLoader;
import com.trhsy.sim.common.CommonProxy;
import com.trhsy.sim.common.loader.KeyLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.opengl.Display;


/**
 * 客户端代理
 */
public class ClientProxy extends CommonProxy {
    /**所有Mod初始化之前调用,这时候应该加载配置文件，实例化物品和方块，并注册它们。
     * @param event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        try {
            super.preInit(event);
            new ItemRenderLoader();
            new EntityRenderLoader();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("客户端代理初始化出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**用于该Mod的初始化,这时候应该为Mod进行设置，如注册合成表和烧炼系统，并且向其他Mod发送交互信息。
     * @param event
     */
    @Override
    public void init(FMLInitializationEvent event) {
        try {
            //Display.setTitle(Display.getTitle() + " 丨 模拟城市 丨 官方Q群: 749090174  丨 由TRHSY重制 丨 微信公众号：dasha500");
            String title=I18n.func_135052_a("container.sim.title");
            Display.setTitle(Display.getTitle() +title);
            super.init(event);
            /**热键**/
            new KeyLoader();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("客户端代理初始化出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**在所有Mod都初始化之后调用,这时候应该接收其他Mod发送的交互信息，并完成对Mod的设置
     * @param event
     */
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        try {
            super.postInit(event);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("客户端代理初始化出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        EntityPlayer entityPlayer=null;
        try {
            entityPlayer=(EntityPlayer)(ctx.side.isClient() ? Minecraft.func_71410_x().field_71439_g : super.getPlayerEntity(ctx));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("客户端代理初始化出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return entityPlayer;
    }
    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().field_71441_e;
    }

}
