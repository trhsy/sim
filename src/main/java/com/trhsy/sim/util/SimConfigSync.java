package com.trhsy.sim.util;

import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.ConfigSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: SimConfigSync
 * @Description:
 * @date 2022/10/11 10:53
 */
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
                NetWorkLoader.net.sendTo(packet, (EntityPlayerMP)event.player);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("sim配置异步出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static void syncConfig(List<ConfigCategory> categories) {
        try {
            needsRestart = false;
            boolean changed = false;
            ModSimLoader.log.info("正在将配置与服务器同步");
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
                        ModSimLoader.log.debug("同步 %s - %s: %s", new Object[]{category.getName(), prop.getName(), prop.getString()});
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
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("syncConfig出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
}
