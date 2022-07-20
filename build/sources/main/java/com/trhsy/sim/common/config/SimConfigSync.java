package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ConfigLoader;
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
        if (event.player != null && event.player instanceof EntityPlayerMP && !FMLCommonHandler.instance().getSide().isClient()) {
            ConfigSyncPacket packet = new ConfigSyncPacket();
            packet.categories.add(ConfigLoader.Modules);
            packet.categories.add(ConfigLoader.Gameplay);
            packet.categories.add(ConfigLoader.Nameplay);
            TinkerNetwork.sendTo(packet, (EntityPlayerMP)event.player);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void playerJoinedWorld(EntityJoinWorldEvent event) {
        if (event.entity == Minecraft.getMinecraft().thePlayer) {
            if (needsRestart) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[Sim] " + StatCollector.translateToLocal("config.synced.restart")));
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[Sim] " + StatCollector.translateToLocal("config.synced.ok")));
            }
        }

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public static void syncConfig(List<ConfigCategory> categories) {
        needsRestart = false;
        boolean changed = false;
        ConfigLoader.logger.info("正在将配置与服务器同步");
        Iterator var2 = categories.iterator();

        while(var2.hasNext()) {
            ConfigCategory serverCategory = (ConfigCategory)var2.next();
            ConfigCategory category = ConfigLoader.pulseConfig.getCategory();
            if (!serverCategory.getName().equals(category.getName())) {
                category = ConfigLoader.configFile.getCategory(serverCategory.getName());
            }

            Iterator var5 = serverCategory.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Property> entry = (Map.Entry)var5.next();
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

        ConfigLoader.pulseConfig.flush();
        if (changed) {
            MinecraftForge.EVENT_BUS.register(new SimConfigSync());
        }

    }
}
