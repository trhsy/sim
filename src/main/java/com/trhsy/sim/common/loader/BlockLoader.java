package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.TileEntityWindmill;
import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.fluid.FluidMilk;
import com.trhsy.sim.common.item.ItemBlockWindmill;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

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
    /*
        液体牛奶
         */
    public static Fluid fluidMilk;


    public BlockLoader(FMLPreInitializationEvent event) {
        fluidMilk = new FluidMilk();
        blockLightBox = new BlockLightBox();
        register(constructorBox, "constructor_box");
        register(blockCheeseBlock, "cheese_block");
        register(blockCompositeBrick, "composite_brick");
        register(blockControlBox, "control_box");
        register(blockFarmingBox, "farming_box");
        register(blockFluidMilk, "fluid_milk");
        register(blockLightBox, "light_box");
        register(blockMiningBox, "mining_box");
        register(blockPathConstructor, "path_constructor");
        register(blockMarker, "marker_bar_block");
        register(blockWindmill, "block_windmill");
        GameRegistry.registerTileEntity(TileEntityWindmill.class, "tileentitywindmill");

        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 1), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 2), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 14)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 3), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 11)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 4), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 10)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 5), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 6), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 5)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockLightBox, 1, 7), new Object[]{blockLightBox, new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 14), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 10), new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 5)});
    }

    private static void register(Block block, String name) {
        //注册方块
        GameRegistry.registerBlock(block, name);
    }


}
