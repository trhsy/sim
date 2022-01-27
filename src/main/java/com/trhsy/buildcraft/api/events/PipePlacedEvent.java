package com.trhsy.buildcraft.api.events;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName PipePlacedEvent
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:22
 * ========================================
 **/
public class PipePlacedEvent extends Event {
    public EntityPlayer player;
    public String pipeType;
    public int x;
    public int y;
    public int z;

    public PipePlacedEvent(EntityPlayer player, String pipeType, int x, int y, int z) {
        this.player = player;
        this.pipeType = pipeType;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}