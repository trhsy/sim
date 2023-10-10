package com.trhsy.sim.loader;

import com.trhsy.sim.block.BlockLightBox;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.*;
import com.trhsy.sim.block.enums.EnumBlockLiving;
import com.trhsy.sim.block.enums.EnumControlBox;
import com.trhsy.sim.block.enums.EnumLightColour;
import com.trhsy.sim.util.EnumBlock;
import com.trhsy.sim.util.ItemBlockMeta;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class BlockLoader {
    /**
     * 建筑箱
     **/
    public static BlockConstructorBox blockConstructorBox= new BlockConstructorBox(Material.WOOD);
    /**农田箱**/
    public static BlockFarmingBox blockFarmingBox = new BlockFarmingBox(Material.WOOD);
    /**挖矿箱**/
    public static BlockMiningBox blockMiningBox = new BlockMiningBox(Material.WOOD);
    /**
     * 奶酪块
     */
    public static BlockCheese blockCheese=new BlockCheese(Material.CAKE);
    /**
     * 复合砖块
     */
    public static BlockCompositeBrick blockCompositeBrick=new BlockCompositeBrick(Material.ROCK);
    /**控制箱***/
    public static BlockControlBox blockControlBox=new BlockControlBox();

    /**铜块**/
    public static BlockCopper blockCopper=new BlockCopper(Material.IRON);
    /**铜矿**/
    public static BlockCopperOre blockCopperOre=new BlockCopperOre();
    /**锡块**/
    public static BlockTin blockTin=new BlockTin(Material.IRON);
    /**锡矿**/
    public static BlockTinOre blockTinOre=new BlockTinOre();
    /**灯箱**/
    public static BlockLightBox blockLightBox=new BlockLightBox();

    /**地毯**/
    public static BlockLiving blockLiving=new BlockLiving();
    /**标记棒**/
    public static BlockMarker blockMarker=new BlockMarker(Material.WOOD);
    /**路径箱**/
    public static BlockPathBox blockPathBox=new BlockPathBox(Material.WOOD);
    /**路径箱**/
    public static BlockSpecial blockSpecial=new BlockSpecial();
    /**风车**/
    public static BlockWindmill blockWindmill=new BlockWindmill(Material.WOOD);
    /**流动牛奶块***/
    public static BlockDynamicLiquid flowing_milk= new BlockFlowingMilk(Material.WATER);
    /**静态牛奶块***/
    public static BlockFluidClassic milk= new BlockMilk(Material.WATER);

    /**
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        try {
            /**建筑盒**/
            register(256,blockConstructorBox, "block_constructor_box");
            /**农田盒**/
            register(257,blockFarmingBox, "block_farming_box");
            /**挖矿盒**/
            register(258,blockMiningBox, "block_mining_box");

            /**奶酪块**/
            register(259,blockCheese, "block_cheese");
            /**复合砖**/
            register(260,blockCompositeBrick, "block_composite_brick");
            /**控制箱**/
            //register(261,blockControlBox, "block_control_box");
            blockControlBox=registerEnumBlock(new BlockControlBox(), "block_control_box");
            /**铜块**/
            register(262,blockCopper, "block_copper");
            /**铜矿**/
            register(263,blockCopperOre, "block_copper_ore");
            /**锡块**/
            register(264,blockTin, "block_tin");
            /**锡矿**/
            register(265,blockTinOre, "block_tin_ore");
            /**标记棒**/
            register(266,blockMarker, "block_marker");
            /**路径箱**/
            register(267,blockPathBox, "block_path_box");
            /**特制方块空气**/
            register(268,blockSpecial, "block_special");

            /**风车**/
            register(269,blockWindmill, "block_windmill");

            /**灯箱**/
            //register(270,blockLightBox, "block_light_box");
            blockLightBox=registerEnumBlock(new BlockLightBox(), "block_light_box");
            /**毛毯，生活区，夜晚移动**/
            //register(271,blockLiving, "block_living");
            blockLiving=registerEnumBlock(new BlockLiving(), "block_living");
            /**流体牛奶**/
            register(272,milk, "milk");
            //register(flowing_milk, "flowing_milk");
        }catch (Exception e){
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    private static void register(int id,Block block, String name) {
        register(id, block,new ResourceLocation(ModSim.MODID,name));
    }
    /**
     * 注册方块
     *
     * @param block
     * @param name
     */
    private static void register(int id,Block block, ResourceLocation name) {
        try {
            Block.REGISTRY.register(id,name,block);
            Item.REGISTRY.register(Block.getIdFromBlock(block),(ResourceLocation)Block.REGISTRY.getNameForObject(block),new ItemBlock(block));
            //
            //GameRegistry.register(block.setRegistryName(name));
            //GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-register出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    protected static <K extends EnumBlock<?>> K registerEnumBlock(K block, String name) {
        try {
            new ItemBlockMeta(block, block.prop);
            registers(Block.getIdFromBlock(block),block, ItemBlockMeta.class, name);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerEnumBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return block;
    }
    protected static <K extends Block> K registers(int id,K block, Class<? extends ItemBlock> itemBlockClazz, String name, Object... itemCtorArgs) {
        try {
            block.setRegistryName(name);
            Block.REGISTRY.register(id,new ResourceLocation(ModSim.MODID,name),block);
            Item item=new ItemMultiTexture(block,block, new ItemMultiTexture.Mapper()
            {
                @Override
                public String apply(ItemStack p_apply_1_)
                {
                    String unlocalizedName="";
                    if(block==BlockLoader.blockControlBox){
                        unlocalizedName=EnumControlBox.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
                    }else if(block==BlockLoader.blockLiving){
                        unlocalizedName= EnumBlockLiving.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
                    }else if(block==BlockLoader.blockLightBox){
                        unlocalizedName=EnumLightColour.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
                    }
                    return unlocalizedName;
                }
            });
            Item.REGISTRY.register(Block.getIdFromBlock(block),(ResourceLocation)Block.REGISTRY.getNameForObject(block),item);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registers出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return block;
    }
    /**
     * 添加模型
     */
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            /**建筑盒**/
            registerRender(blockConstructorBox);
            /**农田盒**/
            registerRender(blockFarmingBox);
            /**挖矿盒**/
            registerRender(blockMiningBox);
            /**奶酪块**/
            registerRender(blockCheese);
            /**复合砖**/
            registerRender(blockCompositeBrick);
            for (int i = 0; i < 3; i++) {
                /**控制箱**/
                registerRender(blockControlBox,i,"block_control_box"+i);
            }
            /**铜块**/
            registerRender(blockCopper);
            /**铜矿**/
            registerRender(blockCopperOre);
            /**灯箱**/
            for(int i = 0; i < 8; ++i) {
            registerRender(blockLightBox,i,"block_light_box"+i);
            }
            /**地毯**/
            for(int i = 0; i < 16; ++i) {
                registerRender(blockLiving,i,"block_living"+i);
            }
            /**标记棒**/
            registerRender(blockMarker);
            /**采矿箱**/
            registerRender(blockMiningBox);
            /**路径箱**/
            registerRender(blockPathBox);
            /**风车**/
            registerRender(blockWindmill);
            /**空气**/
            registerRender(blockSpecial);
            /**锡块**/
            registerRender(blockTin);
            /**锡矿**/
            registerRender(blockTinOre);
            /**牛奶块**/
            registerRender(milk);
        }catch (Exception e){
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @SideOnly(Side.CLIENT)
    private static void registerStateMapper(Block block, IStateMapper mapper) {
        try {
            ModelLoader.setCustomStateMapper(block, mapper);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-registerStateMapper出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            ResourceLocation resourceLocation=block.getRegistryName();
            registerRender(block, 0,resourceLocation.getResourcePath() );
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-registerRender注册模型 出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            ResourceLocation resourcelocation = new ResourceLocation(ModSim.MODID,name);

            ModelResourceLocation model = new ModelResourceLocation(resourcelocation, "inventory");
            //if(name.contains("block_light_box")){
            //    resourcelocation = new ResourceLocation(ModSim.MODID,name);
            //    model = new ModelResourceLocation(resourcelocation, "color=" + meta );
            //}
            Item item=Item.getItemFromBlock(block);
            ModelLoader.setCustomModelResourceLocation(item, meta, model);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-registerRender指定名称的模型出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }



}
