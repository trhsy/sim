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
 * @ClassName BlockPlacedDownEvent
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:21
 * ========================================
 **/
@Cancelable
public class BlockPlacedDownEvent extends Event {
    public EntityPlayer player;
    public Block block;
    public int meta;
    public int x;
    public int y;
    public int z;

    public BlockPlacedDownEvent(EntityPlayer player, Block block, int meta, int x, int y, int z) {
        this.player = player;
        this.block = block;
        this.meta = meta;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}