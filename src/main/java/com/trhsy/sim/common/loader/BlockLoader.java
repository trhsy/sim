package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.block.functionality.TileEntityWindmill;
import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.block.BlockSpecialBlock;
import com.trhsy.sim.common.block.fluid.FluidMilk;
import com.trhsy.sim.common.block.gases.BlockCarbonDioxide;
import com.trhsy.sim.common.block.gases.BlockGasDispenser;
import com.trhsy.sim.common.block.gases.BlockRadiationGas;
import com.trhsy.sim.common.block.gases.BlockSulphurDioxide;
import com.trhsy.sim.common.item.ItemBlockLightBox;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

/**
 * @ClassName BlockLoader
 * @Description todo 方块注册加载
 * @Author Tian
 * @Date 2022/1/2920:40
 **/
public class BlockLoader {
    public static Block livingBlock=new BlockLivingBlock();
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

    public static Block blockATMControlBox = new BlockATMControlBox();
    public static Block blockOtherControlBox = new BlockOtherControlBox();
    /*农田箱*/
    public static Block blockFarmingBox = new BlockFarmingBox();
    /*块流体牛奶*/
    public static Block blockFluidMilk = new BlockFluidMilk();
    public static Block cityBox=new BlockCityBox();
    /*灯箱*/
    public static Block LightBoxWhite = new BlockLightBox("white");
    public static Block lightBoxRed = new BlockLightBox("red");
    public static Block lightBoxOrange = new BlockLightBox("orange");
    public static Block lightBoxYellow = new BlockLightBox("yellow");
    public static Block lightBoxGreen = new BlockLightBox("green");
    public static Block lightBoxBlue = new BlockLightBox("blue");
    public static Block lightBoxPurple = new BlockLightBox("purple");
    public static Block lightBoxRainbow = new BlockLightBox("rainbow");
    /*标记棒*/
    public static Block blockMarker = new BlockMarker();
    /*挖矿箱*/
    public static Block blockMiningBox = new BlockMiningBox();
    /*路径构建器*/
    //public static Block blockPathConstructor = new BlockPathConstructor();
    /*风车*/
    public static Block blockWindmill = new BlockWindmill();
    /*特除方块*/
    public static Block specialBlock=new BlockSpecialBlock();
    //二氧化碳
    public static Block blockCarbonDioxide=new BlockCarbonDioxide();
    //二氧化硫
    public static Block blockSulphurDioxide=new BlockSulphurDioxide();
    //辐射
    public static Block blockRadiationGas=new BlockRadiationGas();
    //加气机
    public static Block blockCarbonDioxideGasDispenser=new BlockGasDispenser("carbonDioxide");
    public static Block blockSulphurDioxideGasDispenser=new BlockGasDispenser("sulphurDioxide");
    public static Block blockRadiationGasGasDispenser=new BlockGasDispenser("radiationGas");

    /*
        液体牛奶
         */
    public static Fluid fluidMilk = new FluidMilk();
    public BlockLoader(){}

    public BlockLoader(FMLPreInitializationEvent event) {
        GameRegistry.registerBlock(livingBlock, "living_block");
        register(constructorBox, "constructor_box");
        register(blockCheeseBlock, "cheese_block");
        register(blockCompositeBrick, "composite_brick");
        register(blockControlBox, "control_box");

        register(blockATMControlBox, "control_box_ATM");
        register(blockOtherControlBox, "control_box_other");

        register(blockFarmingBox, "farming_box");
        register(blockFluidMilk, "fluid_milk");


        register(LightBoxWhite, "light_box_white");
        register(lightBoxRed, "light_box_red");
        register(lightBoxOrange, "light_box_orange");
        register(lightBoxYellow, "light_box_yellow");
        register(lightBoxGreen, "light_box_green");
        register(lightBoxBlue, "light_box_blue");
        register(lightBoxPurple, "light_box_purple");
        register(lightBoxRainbow, "light_box_rainbow");

        register(blockMiningBox, "mining_box");
        register(specialBlock, "special_block");
        register(blockCarbonDioxide, "carbon_dioxide_block");
        register(blockSulphurDioxide, "sulphur_dioxide_block");
        register(blockRadiationGas, "radiation_gas_block");
        register(blockCarbonDioxideGasDispenser, "gas_dispenser_carbon_block");
        register(blockSulphurDioxideGasDispenser, "gas_dispenser_sulphur_block");
        register(blockRadiationGasGasDispenser, "gas_dispenser_radiation_block");

        register(blockMarker, "marker_bar_block");
        register(blockWindmill, "block_windmill");
        register(cityBox, "city_box");
        nameBlocks();
        GameRegistry.registerTileEntity(TileEntityWindmill.class, "tileentitywindmill");


    }

    private static void register(Block block, String name) {
        //注册方块
        GameRegistry.registerBlock(block, name);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Block block) {

        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
    }
    public static void nameBlocks() {
        /*LanguageRegistry.instance().addStringLocalization("tile.lightBox.White.name", "灯箱(白色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Red.name", "灯箱(红色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Orange.name", "灯箱(橙色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Yellow.name", "灯箱(黄色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Green.name", "灯箱(绿色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Blue.name", "灯箱(蓝色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Purple.name", "灯箱(紫色)");
        LanguageRegistry.instance().addStringLocalization("tile.lightBox.Rainbow.name", "灯箱(彩色)");*/

    }

}
