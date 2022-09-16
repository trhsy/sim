package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockCheese;
import com.trhsy.sim.block.BlockCompositeBrick;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.block.BlockControlBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockLoader {
    /**
     * 建筑箱
     **/
    public static Block blockConstructorBox= new BlockConstructorBox(Material.wood);
    /**
     * 奶酪块
     */
    public static Block blockCheese=new BlockCheese(Material.cake);
    /**
     * 复合砖块
     */
    public static Block blockCompositeBrick=new BlockCompositeBrick(Material.rock);
    /**
     * 住宅控制箱
     */
    public static Block blockControlBox=new BlockControlBox(Material.wood);

    public static void register(IForgeRegistry<Block> registry) {
        registry.register(blockConstructorBox);
    }

    /**
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        try {
            /**建筑盒**/
            register(blockConstructorBox, "block_constructor_box");
            /**奶酪块**/
            register(blockCheese, "block_cheese");
            /**复合砖**/
            register(blockCompositeBrick, "block_composite_brick");
            register(blockControlBox, "block_control_box_top");
        }catch (Exception e){
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            GameRegistry.register(block.setRegistryName(name));
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            //GameRegistry.registerBlock(block.setRegistryName(name));
            //GameRegistry.register();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-register出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 添加模型
     */
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            registerRender(blockConstructorBox);
            registerRender(blockCheese);
            registerRender(blockCompositeBrick);
            registerRender(blockControlBox);
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
            Item item=Item.getItemFromBlock(block);
            ModelLoader.setCustomModelResourceLocation(item, meta, model);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("BlockLoader-registerRender指定名称的模型出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }



}
