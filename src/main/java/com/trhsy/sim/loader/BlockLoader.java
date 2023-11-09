package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.*;
import com.trhsy.sim.block.enums.EnumBlockLiving;
import com.trhsy.sim.block.enums.EnumControlBox;
import com.trhsy.sim.block.enums.EnumLightColour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: BlockLoader
 * @Description: 方块注册加载类
 * @date 2023/10/31 上午 10:13
 */
@Mod.EventBusSubscriber
public class BlockLoader {
    /**
     * 建筑箱
     */
    public static Block blockConstructorBox=new BlockConstructorBox();
    /**农田箱**/
    public static BlockFarmingBox blockFarmingBox = new BlockFarmingBox();
    /**挖矿箱**/
    public static BlockMiningBox blockMiningBox = new BlockMiningBox();
    /**路径箱**/
    public static BlockPathBox blockPathBox=new BlockPathBox();
    /**风车**/
    public static BlockWindmill blockWindmill=new BlockWindmill();

    /**
     * 复合砖
     */
    public static BlockCompositeBrick blockCompositeBrick=new BlockCompositeBrick();
    /**
     * 奶酪块
     */
    public static BlockCheese blockCheese=new BlockCheese();

    /**控制箱***/
    public static BlockControlBox blockControlBox=new BlockControlBox();
    /**铜块**/
    public static BlockCopper blockCopper=new BlockCopper();
    /**铜矿**/
    public static BlockCopperOre blockCopperOre=new BlockCopperOre();
    /**锡块**/
    public static BlockTin blockTin=new BlockTin();
    /**锡矿**/
    public static BlockTinOre blockTinOre=new BlockTinOre();

    /**灯箱**/
    public static BlockLightBox blockLightBox=new BlockLightBox();
    /**地毯**/
    public static BlockLiving blockLiving=new BlockLiving();
    /**标记棒**/
    public static BlockMarker blockMarker=new BlockMarker();

    /**流动牛奶块***/
    public static BlockDynamicLiquid flowing_milk= new BlockFlowingMilk();
    /**
     * 静态牛奶块
     */
    public static BlockFluidClassic milk=new BlockMilk();
    /**特除方块**/
    public static BlockSpecial blockSpecial=new BlockSpecial();
    /**
     * 注册方块
     * @param event
     */
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event){
        //注册方块
        //建筑箱
        event.getRegistry().register(blockConstructorBox.setRegistryName(ModSim.MODID+":block_constructor_box"));
        //农田箱
        event.getRegistry().register(blockFarmingBox.setRegistryName(ModSim.MODID+":block_farming_box"));
        //采矿箱
        event.getRegistry().register(blockMiningBox.setRegistryName(ModSim.MODID+":block_mining_box"));
        //路径箱
        event.getRegistry().register(blockPathBox.setRegistryName(ModSim.MODID+":block_path_box"));
        //标记棒
        event.getRegistry().register(blockMarker.setRegistryName(ModSim.MODID+":block_marker"));
        //风车
        event.getRegistry().register(blockWindmill.setRegistryName(ModSim.MODID+":block_windmill"));
        //牛奶
        event.getRegistry().register(milk.setRegistryName(ModSim.MODID+":milk"));
        //复合砖
        event.getRegistry().register(blockCompositeBrick.setRegistryName(ModSim.MODID+":block_composite_brick"));
        //奶酪块
        event.getRegistry().register(blockCheese.setRegistryName(ModSim.MODID+":block_cheese"));
        //控制箱
        event.getRegistry().register(blockControlBox.setRegistryName(ModSim.MODID+":block_control_box"));
        //铜块
        event.getRegistry().register(blockCopper.setRegistryName(ModSim.MODID+":block_copper"));
        //铜矿
        event.getRegistry().register(blockCopperOre.setRegistryName(ModSim.MODID+":block_copper_ore"));
        //锡块
        event.getRegistry().register(blockTin.setRegistryName(ModSim.MODID+":block_tin"));
        //锡矿
        event.getRegistry().register(blockTinOre.setRegistryName(ModSim.MODID+":block_tin_ore"));
        //灯箱
        event.getRegistry().register(blockLightBox.setRegistryName(ModSim.MODID+":block_light_box"));
        //地毯
        event.getRegistry().register(blockLiving.setRegistryName(ModSim.MODID+":block_living"));
        //特除方块
        event.getRegistry().register(blockSpecial.setRegistryName(ModSim.MODID+":block_special"));

    }

    /**
     * 同时注册为物品
     * @param event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        //注册为物品
        //建筑箱
        event.getRegistry().register(new ItemBlock(blockConstructorBox).setRegistryName(ModSim.MODID+":block_constructor_box"));
        //农田箱
        event.getRegistry().register(new ItemBlock(blockFarmingBox).setRegistryName(ModSim.MODID+":block_farming_box"));

        //采矿箱
        event.getRegistry().register(new ItemBlock(blockMiningBox).setRegistryName(ModSim.MODID+":block_mining_box"));
        //控制箱
        Item blockControlBoxItem=new ItemMultiTexture(blockControlBox,blockControlBox,new ItemMultiTexture.Mapper(){
            @Override
            public String apply(ItemStack var1) {
                return EnumControlBox.byMetadata(var1.getMetadata()).getUnlocalizedName();
            }
        });
        event.getRegistry().register(blockControlBoxItem.setRegistryName(ModSim.MODID+":block_control_box"));
        //路径箱
        event.getRegistry().register(new ItemBlock(blockPathBox).setRegistryName(ModSim.MODID+":block_path_box"));
        //标记棒
        event.getRegistry().register(new ItemBlock(blockMarker).setRegistryName(ModSim.MODID+":block_marker"));
        //标记棒
        event.getRegistry().register(new ItemBlock(blockWindmill).setRegistryName(ModSim.MODID+":block_windmill"));

        //复合砖
        event.getRegistry().register(new ItemBlock(blockCompositeBrick).setRegistryName(ModSim.MODID+":block_composite_brick"));
        //牛奶
        event.getRegistry().register(new ItemBlock(milk).setRegistryName(ModSim.MODID+":milk"));
        //奶酪块
        event.getRegistry().register(new ItemBlock(blockCheese).setRegistryName(ModSim.MODID+":block_cheese"));

        //铜块
        event.getRegistry().register(new ItemBlock(blockCopper).setRegistryName(ModSim.MODID+":block_copper"));
        //铜矿
        event.getRegistry().register(new ItemBlock(blockCopperOre).setRegistryName(ModSim.MODID+":block_copper_ore"));

        //锡块
        event.getRegistry().register(new ItemBlock(blockTin).setRegistryName(ModSim.MODID+":block_tin"));
        //锡矿
        event.getRegistry().register(new ItemBlock(blockTinOre).setRegistryName(ModSim.MODID+":block_tin_ore"));

        //灯箱
        Item blockLightBoxItem=new ItemMultiTexture(blockLightBox,blockLightBox,new ItemMultiTexture.Mapper(){
            @Override
            public String apply(ItemStack var1) {
                return EnumLightColour.byMetadata(var1.getMetadata()).getUnlocalizedName();
            }
        });
        event.getRegistry().register(blockLightBoxItem.setRegistryName(ModSim.MODID+":block_light_box"));
        //地毯
        Item blockLivingItem=new ItemMultiTexture(blockLiving,blockLiving,new ItemMultiTexture.Mapper(){
            @Override
            public String apply(ItemStack var1) {
                return EnumBlockLiving.byMetadata(var1.getMetadata()).getUnlocalizedName();
            }
        });
        event.getRegistry().register(blockLivingItem.setRegistryName(ModSim.MODID+":block_living"));
        //特制方块空气
        event.getRegistry().register(new ItemBlock(blockSpecial).setRegistryName(ModSim.MODID+":block_special"));
    }

    /**
     * 注册材质
     * @param event
     */
    @SubscribeEvent
    public static void registerItemBlockModel(ModelRegistryEvent event){
        //注册材质
        //建筑箱
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockConstructorBox),0,new ModelResourceLocation(blockConstructorBox.getRegistryName(),"inventory"));
        //农田箱
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockFarmingBox),0,new ModelResourceLocation(blockFarmingBox.getRegistryName(),"inventory"));
        //采矿箱
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockMiningBox),0,new ModelResourceLocation(blockMiningBox.getRegistryName(),"inventory"));
        //路径箱
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockPathBox),0,new ModelResourceLocation(blockPathBox.getRegistryName(),"inventory"));
        //标记棒
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockMarker),0,new ModelResourceLocation(blockMarker.getRegistryName(),"inventory"));
        //牛奶
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(milk),0,new ModelResourceLocation(milk.getRegistryName(),"inventory"));
        //复合砖
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockCompositeBrick),0,new ModelResourceLocation(blockCompositeBrick.getRegistryName(),"inventory"));
        //奶酪块
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockCheese),0,new ModelResourceLocation(blockCheese.getRegistryName(),"inventory"));
        //控制箱 三个元素
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockControlBox),0,new ModelResourceLocation(blockControlBox.getRegistryName(),"inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockControlBox),1,new ModelResourceLocation(blockControlBox.getRegistryName()+"_atm","inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockControlBox),2,new ModelResourceLocation(blockControlBox.getRegistryName()+"_other","inventory"));
        //铜块
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockCopper),0,new ModelResourceLocation(blockCopper.getRegistryName(),"inventory"));
        //铜矿
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockCopperOre),0,new ModelResourceLocation(blockCopperOre.getRegistryName(),"inventory"));
        //锡块
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockTin),0,new ModelResourceLocation(blockTin.getRegistryName(),"inventory"));
        //锡矿
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockTinOre),0,new ModelResourceLocation(blockTinOre.getRegistryName(),"inventory"));
        //灯箱
        for (int i = 0; i < 8; i++) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLightBox),i,new ModelResourceLocation(blockLightBox.getRegistryName()+""+i,"inventory"));
        }

        //地毯
        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLiving),i,new ModelResourceLocation(blockLiving.getRegistryName()+""+i,"inventory"));
        }
        //特除方块
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockSpecial),0,new ModelResourceLocation(blockSpecial.getRegistryName(),"inventory"));
        //风车
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockWindmill),0,new ModelResourceLocation(blockWindmill.getRegistryName(),"inventory"));
    }
}
