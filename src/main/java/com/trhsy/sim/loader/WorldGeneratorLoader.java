package com.trhsy.sim.loader;

import com.trhsy.sim.worldgen.WorldGeneratorCopperOre;
import com.trhsy.sim.worldgen.WorldGeneratorTinOre;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: WorldGeneratorLoader
 * @Description: 矿物生成
 * @date 2022/10/11 14:23
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
        try {
            MinecraftForge.ORE_GEN_BUS.register(this);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("WorldGeneratorLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
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
        try {
            if (!event.getPos().equals(this.pos)) {
                this.pos = event.getPos();
                worldGeneratorCopperOre.generate(event.getWorld(), event.getRand(), event.getPos());
                worldGeneratorTinOre.generate(event.getWorld(), event.getRand(), event.getPos());
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("onOreGenPost出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
