package com.trhsy.sim.common.loader;

import net.minecraftforge.common.MinecraftForge;

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
