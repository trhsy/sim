package com.trhsy.sim.npcCode.block;

import com.trhsy.sim.npcCode.V3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.block
 * @ClassName: Marker
 * @Description: 标记棒
 * @date 2023/5/4 16:00
 */
public class Marker {
    public V3 loc;
    EntityPlayerSP placedBy;

    public Marker(V3 l, EntityPlayerSP player) {
        this.loc = l;
        this.placedBy = player;
    }

    public Marker(BlockPos p, int dim, EntityPlayerSP player) {
        this.loc = V3.fromBlockPos(p);
        this.loc.dimension = dim;
        this.placedBy = player;
    }

}
