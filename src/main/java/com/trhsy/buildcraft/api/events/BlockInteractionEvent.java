package com.trhsy.buildcraft.api.events;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

/**
 * ========================================
 *
 * @ClassName BlockInteractionEvent
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:20
 * ========================================
 **/
@Cancelable
public class BlockInteractionEvent extends Event {
    public EntityPlayer player;
    public Block block;
    public int meta;

    public BlockInteractionEvent(EntityPlayer player, Block block) {
        this.player = player;
        this.block = block;
    }

    public BlockInteractionEvent(EntityPlayer player, Block block, int meta) {
        this.player = player;
        this.block = block;
        this.meta = meta;
    }
}
