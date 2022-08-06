package com.trhsy.sim.common;

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.common.config.SimConfigSync;
import com.trhsy.sim.common.core.entity.CommonTickHandler;
import com.trhsy.sim.common.core.entity.folk.genetics.Race;
import com.trhsy.sim.common.core.entity.folk.traits.Traits;
import com.trhsy.sim.common.loader.*;
import com.trhsy.sim.common.util.UpdateChecker;
import com.trhsy.sim.packets.NetWorkLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * 服务端代理
 */
public class CommonProxy {
    public boolean ranStartup = false;

    public CommonProxy() {
    }

    /**
     * 所有Mod初始化之前调用,这时候应该加载配置文件，实例化物品和方块，并注册它们。
     *
     * @param event
     */
    public void preInit(FMLPreInitializationEvent event) {
        try {
            ModSimReloaded.log = event.getModLog();

            new UpdateChecker(event);
            /**配置**/
            ConfigLoader.load(event);
            /**创造模式物品栏**/
            new CreativeTabsLoader(event);
            /**流体注册加载**/
            new FluidLoader(event);
            /**物品加载注册**/
            new ItemLoader(event);
            /**方块加载注册**/
            new BlockLoader(event);
            /**事件加载**/
            new EventLoader();
            /**合成表**/
            new CraftingLoader();

            /**矿物生成**/
            new WorldGeneratorLoader();
            /**矿物辞典**/
            new OreDictionaryLoader(event);
            /**实体加载**/
            new EntityLoader();
            /**加载GUI**/
            new GuiElementLoader();
            //Traits
            Traits.loadTraits();
            Race.loadRaces();
            new NetWorkLoader(event);
            registerMisc();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("preInit出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 用于该Mod的初始化,这时候应该为Mod进行设置，如注册合成表和烧炼系统，并且向其他Mod发送交互信息。
     *
     * @param event
     */
    public void init(FMLInitializationEvent event) {

    }

    /**
     * 在所有Mod都初始化之后调用,这时候应该接收其他Mod发送的交互信息，并完成对Mod的设置
     *
     * @param event
     */
    public void postInit(FMLPostInitializationEvent event) {
        try {
            MinecraftForge.EVENT_BUS.register(new SimConfigSync());
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("postInit出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 系统命令
     *
     * @param event
     */
    public void serverStarting(FMLServerStartingEvent event) {
        try {
            new CommandLoader(event);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("serverStarting出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    public void registerMisc() {
        MinecraftForge.EVENT_BUS.register(new CommonTickHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
    }
    @SideOnly(Side.CLIENT)
    public World getClientWorld() {
        World world = null;
        try {
            world = FMLClientHandler.instance().getServer().getEntityWorld();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getClientWorld出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return world;
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        EntityPlayer entityPlayer = null;
        try {
            entityPlayer = ctx.getServerHandler().playerEntity;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getPlayerEntity出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return entityPlayer;
    }


}
