package com.trhsy.sim.client;

import com.trhsy.sim.client.loader.EntityRenderLoader;
import com.trhsy.sim.client.loader.ItemRenderLoader;
import com.trhsy.sim.common.CommonProxy;
import com.trhsy.sim.common.loader.KeyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.xml.ws.handler.MessageContext;

/**
 * 客户端代理
 */
public class ClientProxy extends CommonProxy {
    /**所有Mod初始化之前调用,这时候应该加载配置文件，实例化物品和方块，并注册它们。
     * @param event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new ItemRenderLoader();
        new EntityRenderLoader();
    }

    /**用于该Mod的初始化,这时候应该为Mod进行设置，如注册合成表和烧炼系统，并且向其他Mod发送交互信息。
     * @param event
     */
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        /**热键**/
        new KeyLoader();
    }

    /**在所有Mod都初始化之后调用,这时候应该接收其他Mod发送的交互信息，并完成对Mod的设置
     * @param event
     */
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (EntityPlayer)(ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }
}
