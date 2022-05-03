package com.trhsy.sim.common.loader;

import com.google.common.base.Function;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.*;
import com.trhsy.sim.common.item.ItemBlockMeta;
import com.trhsy.sim.common.util.Util;
import net.minecraft.block.Block;
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
    public static Block blockCityBox=new BlockCityBox();

    /**
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        /**建筑盒**/
        register(blockConstructorBox, "block_constructor_box");
        //register(blockControlBox, "block_control_box");
        /**控制盒**/
        blockControlBox= registerEnumBlock(new BlockControlBox(),"block_control_box");
        ItemBlockMeta.setMappingProperty(blockControlBox,BlockControlBox.TYPE);
        register(blockCheese, "block_cheese");
        register(blockCityBox, "block_city_box");

    }

    /**
     * 注册方块
     *
     * @param block
     * @param name
     */
    private static void register(Block block, String name) {
        GameRegistry.registerBlock(block.setRegistryName(name));
    }

    /**
     * 添加模型
     */
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        registerRender(blockConstructorBox);
        registerRender(blockControlBox, 0, ModSim.MODID+":block_control_box_top");
        registerRender(blockControlBox, 1, ModSim.MODID+":block_control_box_atm");
        registerRender(blockControlBox, 2, ModSim.MODID+":block_control_box_other");
        registerRender(blockCheese);
        registerRender(blockCityBox);
    }

    @SideOnly(Side.CLIENT)
    private static void registerStateMapper(Block block, IStateMapper mapper) {
        ModelLoader.setCustomStateMapper(block, mapper);
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
        registerRender(block, 0, block.getRegistryName());
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
        ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, model);
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 多 Metadata 物品注册
     * @Date 22:28 2022/4/19
     * @Param [block, itemBlock, name]
     **/
    private static <T extends Block> T registers(T block, String name) {
        block.setUnlocalizedName(Util.prefix(name));
        block.setRegistryName(Util.getResource(name));
        GameRegistry.registerBlock(block, Util.resource(name));
        return block;
    }
    protected static <T extends Block> T registers(T block, Class<? extends ItemBlock> itemBlockClazz, String name, Object... itemCtorArgs) {
        if (!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("未本地化的名称必须全部小写！块: %s", name));
        } else {
            block.setUnlocalizedName(Util.prefix(name));
            block.setRegistryName(Util.getResource(name));
            GameRegistry.registerBlock(block, itemBlockClazz, name, itemCtorArgs);
            return block;
        }
    }
    protected static <T extends EnumBlock<?>> T registerEnumBlock(T block, String name) {
        registers(block, ItemBlockMeta.class, name);
        ItemBlockMeta.setMappingProperty(block, block.prop);
        return block;
    }
}
