package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.block.BlockCheeseBlock;
import com.trhsy.sim.common.block.BlockConstructorBox;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * @ClassName BlockLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2920:40
 **/
public class BlockLoader {
    /*
    建筑箱
     */
    public static Block constructorBox = new BlockConstructorBox();

    public BlockLoader(FMLPreInitializationEvent event) {
        register(constructorBox, "sim-u-constructorBox");
    }

    private static void register(Block block, String name) {
        //注册方块
        GameRegistry.registerBlock(block, name);
    }
}
