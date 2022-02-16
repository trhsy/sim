package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.item.ItemBlockWindmill;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * @ClassName BlockLoader
 * @Description todo 方块注册加载
 * @Author Tian
 * @Date 2022/1/2920:40
 **/
public class BlockLoader {
    /*
    建筑箱
     */
    public static Block constructorBox = new BlockConstructorBox();
    /*奶酪块*/
    public static Block blockCheeseBlock = new BlockCheeseBlock();
    /*复合砖块*/
    public static Block blockCompositeBrick = new BlockCompositeBrick();
    /*控制箱*/
    public static Block blockControlBox = new BlockControlBox();
    /*农田箱*/
    public static Block blockFarmingBox = new BlockFarmingBox();
    /*块流体牛奶*/
    public static Block blockFluidMilk = new BlockFluidMilk();
    /*灯箱*/
    public static Block blockLightBox = new BlockLightBox();
    /*标记棒*/
    public static Block blockMarker = new BlockMarker();
    /*挖矿箱*/
    public static Block blockMiningBox = new BlockMiningBox();
    /*路径构建器*/
    public static Block blockPathConstructor = new BlockPathConstructor();
    /*风车*/
    public static Block blockWindmill = new BlockWindmill();

    public BlockLoader(FMLPreInitializationEvent event) {
        register(constructorBox, "sim_constructor_box");
        register(blockCheeseBlock, "sim_cheese");
        register(blockCompositeBrick, "sim_composite_brick");
        register(blockControlBox, "sim_control_box");
        register(blockFarmingBox, "sim_farming_box");
        register(blockFluidMilk, "sim_fluid_Milk");
        register(blockLightBox, "sim_light_box");
        register(blockMiningBox, "sim_mining_box");
        register(blockPathConstructor, "sim_path_constructor");
        register(blockMarker, "sim_marker");

    }

    private static void register(Block block, String name) {
        //注册方块
        GameRegistry.registerBlock(block, name);
    }
}
