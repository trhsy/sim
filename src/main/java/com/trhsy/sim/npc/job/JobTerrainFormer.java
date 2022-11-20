package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @ClassName JobTerrainFormer
 * @Description todo 地形构造师
 * @Author TRHSY
 * @Date 2022/11/1622:23
 **/
public class JobTerrainFormer extends Job{

    public JobTerrainFormer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
    }
    public JobTerrainFormer(NpcData folk, V3 pos, World world) {
        super(folk, pos, world);
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation16");
    }
}
