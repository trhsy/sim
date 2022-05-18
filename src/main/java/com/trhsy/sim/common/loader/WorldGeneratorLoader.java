package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.worldgen.WorldGeneratorCopperOre;
import com.trhsy.sim.common.worldgen.WorldGeneratorTinOre;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 矿物生成
 */
public class WorldGeneratorLoader {
    /**
     * 铜矿物生成
     */
    private static WorldGenerator worldGeneratorCopperOre = new WorldGeneratorCopperOre();
    /**锡矿生成**/
    private static WorldGenerator worldGeneratorTinOre = new WorldGeneratorTinOre();
    private BlockPos pos;
    /**
     * 监听矿物生成线程
     */
    public WorldGeneratorLoader() {
        MinecraftForge.ORE_GEN_BUS.register(this);
    }

    /**
     * @param event
     */
    @SubscribeEvent
    public void onOreGenGenerateMinable(OreGenEvent.GenerateMinable event) {
        //不生成安山岩
        //if (event.type == OreGenEvent.GenerateMinable.EventType.ANDESITE) {
        //    event.setResult(Event.Result.DENY);
        //}
    }

    @SubscribeEvent
    public void onOreGenPost(OreGenEvent.Post event) {
        if (!event.pos.equals(this.pos)) {
            this.pos = event.pos;
            worldGeneratorCopperOre.generate(event.world, event.rand, event.pos);
            worldGeneratorTinOre.generate(event.world, event.rand, event.pos);
        }
    }
}
