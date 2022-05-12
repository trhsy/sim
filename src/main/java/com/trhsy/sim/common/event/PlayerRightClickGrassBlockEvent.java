package com.trhsy.sim.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * @ClassName PlayerRightClickGrassBlockEvent
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1221:26
 **/
public class PlayerRightClickGrassBlockEvent extends PlayerEvent {
    public final BlockPos pos;
    public final World world;

    public PlayerRightClickGrassBlockEvent(EntityPlayer player, BlockPos pos, World world) {
        super(player);
        this.pos = pos;
        this.world = world;
    }
}
