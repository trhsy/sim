package com.trhsy.sim.common.loader;

import com.google.common.base.Function;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.block.fluid.BlockFluidMilk;
import com.trhsy.sim.common.item.ItemBlockMeta;
import com.trhsy.sim.common.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

/**
 * 块 加载类
 */
public class BlockLoader {
    /**
     * 建筑箱
     **/
    public static Block blockConstructorBox = new BlockConstructorBox();
    /**
     * 控制盒
     **/
    public static Block blockControlBox;
    /**
     * @Author fan
     * @Description //TODO 奶酪块
     * @Date 17:20 2022/5/3
     * @Param 
     * @return 
     **/
    public static Block blockCheese=new BlockCheese();
    /**
     * @Author fan
     * @Description //TODO 城市箱
     * @Date 8:04 2022/5/4
     * @Param
     * @return
     **/
    public static Block blockCityBox=new BlockCityBox();
    /**
     * @Author fan
     * @Description //TODO 复合砖
     * @Date 8:05 2022/5/4
     * @Param
     * @return
     **/
    public static Block blockCompositeBrick=new BlockCompositeBrick();
    /**
     * @Author fan
     * @Description //TODO 养殖箱
     * @Date 10:02 2022/5/4
     * @Param
     * @return
     **/
    public static Block blockFarmingBox=new BlockFarmingBox();
    /**灯箱**/
    public static Block blockLightBox;
    /**地毯**/
    public static Block blockLiving;
    /**标记棒**/
    public static Block blockMarker=new BlockMarker();
    /**采矿箱**/
    public static Block blockMiningBox=new BlockMiningBox();
    /**特制方块空气**/
    public static Block blockSpecial=new BlockSpecial();
    /**风车**/
    public static Block blockWindmill=new BlockWindmill();
    /**牛奶方块**/
    public static Block blockFluidMilk=new BlockFluidMilk();
    /**铜块**/
    public static Block blockCopper=new BlockCopper();
    /**锡块**/
    public static Block blockTin=new BlockTin();
    /**铜矿**/
    public static BlockOre blockCopperOre=new BlockCopperOre();
    /**铜矿**/
    public static BlockOre blockTinOre=new BlockTinOre();
    /**
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        try {
            /**建筑盒**/
            register(blockConstructorBox, "block_constructor_box");
            //register(blockControlBox, "block_control_box");
            /**控制盒**/
            blockControlBox= registerEnumBlock(new BlockControlBox(),"block_control_box");
            ItemBlockMeta.setMappingProperty(blockControlBox,BlockControlBox.TYPE);
            /**奶酪块**/
            register(blockCheese, "block_cheese");
            /**城市路径**/
            register(blockCityBox, "block_city_box");
            /**复合砖**/
            register(blockCompositeBrick, "block_composite_brick");
            /**养殖箱**/
            register(blockFarmingBox, "block_farming_box");
            /**标记棒**/
            register(blockMarker,"block_marker");
            /**采矿箱**/
            register(blockMiningBox,"block_mining_box");
            /**特制方块空气**/
            register(blockSpecial,"block_special");
            /**灯箱**/
            blockLightBox= registerEnumBlock(new BlockLightBox(),"block_light_box");
            ItemBlockMeta.setMappingProperty(blockLightBox,BlockLightBox.TYPE);
            /**毛毯，生活区，夜晚移动**/
            blockLiving= registerEnumBlock(new BlockLiving(),"block_living");
            ItemBlockMeta.setMappingProperty(blockLiving,BlockLiving.TYPE);
            /**风车**/
            register(blockWindmill,"block_windmill");
            /**流体牛奶方块**/
            register(blockFluidMilk,"fluid_milk");
            /**铜块**/
            register(blockCopper,"block_copper");
            /**锡块**/
            register(blockTin,"block_tin");
            /**铜矿**/
            register(blockCopperOre,"block_copper_ore");
            /**锡矿**/
            register(blockTinOre,"block_tin_ore");
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("BlockLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 注册方块
     *
     * @param block
     * @param name
     */
    private static void register(Block block, String name) {
        try {
            GameRegistry.registerBlock(block.setRegistryName(name));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("register出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 添加模型
     */
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            registerRender(blockConstructorBox);
            registerRender(blockControlBox, 0, ModSim.MODID+":block_control_box_top");
            registerRender(blockControlBox, 1, ModSim.MODID+":block_control_box_atm");
            registerRender(blockControlBox, 2, ModSim.MODID+":block_control_box_other");
            registerRender(blockCheese);
            registerRender(blockCityBox);
            registerRender(blockCompositeBrick);
            registerRender(blockFarmingBox);
            registerRender(blockLightBox, 0, ModSim.MODID+":block_light_box_white");
            registerRender(blockLightBox, 1, ModSim.MODID+":block_light_box_red");
            registerRender(blockLightBox, 2, ModSim.MODID+":block_light_box_orange");
            registerRender(blockLightBox, 3, ModSim.MODID+":block_light_box_yellow");
            registerRender(blockLightBox, 4, ModSim.MODID+":block_light_box_green");
            registerRender(blockLightBox, 5, ModSim.MODID+":block_light_box_blue");
            registerRender(blockLightBox, 6, ModSim.MODID+":block_light_box_purple");
            registerRender(blockLightBox, 7, ModSim.MODID+":block_light_box_rainbow");
            registerRender(blockLiving,0,ModSim.MODID+":block_living_white");
            registerRender(blockLiving,1,ModSim.MODID+":block_living_orange");
            registerRender(blockLiving,2,ModSim.MODID+":block_living_magenta");
            registerRender(blockLiving,3,ModSim.MODID+":block_living_light_blue");
            registerRender(blockLiving,4,ModSim.MODID+":block_living_yellow");
            registerRender(blockLiving,5,ModSim.MODID+":block_living_lime");
            registerRender(blockLiving,6,ModSim.MODID+":block_living_pink");
            registerRender(blockLiving,7,ModSim.MODID+":block_living_gray");
            registerRender(blockLiving,8,ModSim.MODID+":block_living_silver");
            registerRender(blockLiving,9,ModSim.MODID+":block_living_cyan");
            registerRender(blockLiving,10,ModSim.MODID+":block_living_purple");
            registerRender(blockLiving,11,ModSim.MODID+":block_living_blue");
            registerRender(blockLiving,12,ModSim.MODID+":block_living_brown");
            registerRender(blockLiving,13,ModSim.MODID+":block_living_green");
            registerRender(blockLiving,14,ModSim.MODID+":block_living_red");
            registerRender(blockLiving,15,ModSim.MODID+":block_living_black");
            registerRender(blockMarker);
            registerRender(blockMiningBox);
            registerRender(blockSpecial);
            registerRender(blockWindmill);
            /**铜块**/
            registerRender(blockCopper);
            /**锡块**/
            registerRender(blockTin);
            /**铜矿**/
            registerRender(blockCopperOre);
            /**锡矿**/
            registerRender(blockTinOre);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerStateMapper(Block block, IStateMapper mapper) {
        try {
            ModelLoader.setCustomStateMapper(block, mapper);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerStateMapper出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 注册模型
     *
     * @param block
     */
    @SideOnly(Side.CLIENT)
    private static void registerRender(Block block) {
        /*ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);*/
        try {
            registerRender(block, 0, block.getRegistryName());
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 指定名称的模型
     * @Date 22:48 2022/4/19
     * @Param [block, meta, name]
     **/
    @SideOnly(Side.CLIENT)
    private static void registerRender(Block block, int meta, String name) {
        try {
            ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, model);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 多 Metadata 物品注册
     * @Date 22:28 2022/4/19
     * @Param [block, itemBlock, name]
     **/
    private static <T extends Block> T registers(T block, String name) {
        try {
            block.setUnlocalizedName(Util.prefix(name));
            block.setRegistryName(Util.getResource(name));
            GameRegistry.registerBlock(block, Util.resource(name));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registers出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return block;
    }
    protected static <T extends Block> T registers(T block, Class<? extends ItemBlock> itemBlockClazz, String name, Object... itemCtorArgs) {
        try {
            if (!name.equals(name.toLowerCase(Locale.US))) {
                throw new IllegalArgumentException(String.format("未本地化的名称必须全部小写！块: %s", name));
            } else {
                block.setUnlocalizedName(Util.prefix(name));
                block.setRegistryName(Util.getResource(name));
                GameRegistry.registerBlock(block, itemBlockClazz, name, itemCtorArgs);

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registers出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return block;
    }
    protected static <T extends EnumBlock<?>> T registerEnumBlock(T block, String name) {
        try {
            registers(block, ItemBlockMeta.class, name);
            ItemBlockMeta.setMappingProperty(block, block.prop);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerEnumBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return block;
    }
}
