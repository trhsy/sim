package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SimConfigSync
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1123:52
 **/
public class SimConfigSync {
    @SideOnly(Side.CLIENT)
    private static boolean needsRestart;

    public SimConfigSync() {
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.player != null && event.player instanceof EntityPlayerMP && !FMLCommonHandler.instance().getSide().isClient()) {
                ConfigSyncPacket packet = new ConfigSyncPacket();
                packet.categories.add(ConfigLoader.Gameplay);
                packet.categories.add(ConfigLoader.Nameplay);
                TinkerNetwork.sendTo(packet, (EntityPlayerMP)event.player);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("sim配置异步出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void playerJoinedWorld(EntityJoinWorldEvent event) {
        try {
            if (event.entity == Minecraft.getMinecraft().thePlayer) {
                if (needsRestart) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[Sim] " + StatCollector.translateToLocal("config.synced.restart")));
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[Sim] " + StatCollector.translateToLocal("config.synced.ok")));
                }
            }
            MinecraftForge.EVENT_BUS.unregister(this);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("playerJoinedWorld出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void syncConfig(List<ConfigCategory> categories) {
        try {
            needsRestart = false;
            boolean changed = false;
            ConfigLoader.logger.info("正在将配置与服务器同步");
            for (ConfigCategory serverCategory:categories){
                ConfigCategory category = ConfigLoader.configFile.getCategory(serverCategory.getName());
                for (Map.Entry<String, Property> entry:serverCategory.entrySet())  {
                    String name = (String)entry.getKey();
                    Property serverProp = (Property)entry.getValue();
                    Property prop = category.get(name);
                    if (prop == null) {
                        category.put(name, serverProp);
                    } else if (!prop.getString().equals(serverProp.getString())) {
                        prop.setValue(serverProp.getString());
                        needsRestart |= prop.requiresMcRestart();
                        changed = true;
                        ConfigLoader.logger.debug("同步 %s - %s: %s", new Object[]{category.getName(), prop.getName(), prop.getString()});
                    }
                }
            }
            if (ConfigLoader.configFile.hasChanged()) {
                ConfigLoader.configFile.save();
            }
            //ConfigLoader.pulseConfig.flush();
            if (changed) {
                MinecraftForge.EVENT_BUS.register(new SimConfigSync());
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("syncConfig出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
}
