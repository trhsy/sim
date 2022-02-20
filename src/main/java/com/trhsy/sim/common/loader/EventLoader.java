package com.trhsy.sim.common.loader;

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.client.event.EventSounds;
import com.trhsy.sim.common.CommonTickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Minecraft事件
 */
public class EventLoader {
    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    ///**
    // * 在玩家拾取物品时
    // *
    // * @param event
    // */
    //@SubscribeEvent
    //public void onPlayerItemPickup(PlayerEvent.ItemPickupEvent event) {
    //    //检测调用该事件的游戏到底是客户端还是服务端
    //    if (event.player.isServerWorld()) {
    //        String info = String.format("%s picks up: %s", event.player.getItemInUse(), event.pickedUp.getEntityItem());
    //        ConfigLoader.logger().info(info);
    //    }
    //}
    //
    ///**
    // * 玩家在和物品或方块互动时
    // * @param event
    // */
    //@SubscribeEvent
    //public void onPlayerInteract(PlayerInteractEvent event) {
    //
    //    if (!event.world.isRemote) {
    //        String info = String.format("%s interacts with: %s", event.entityPlayer.getItemInUse());
    //        ConfigLoader.logger().info(info);
    //    }
    //}
}
