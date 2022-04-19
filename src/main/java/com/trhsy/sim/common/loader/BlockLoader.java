package com.trhsy.sim.common.loader;

import com.google.common.base.Function;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockConstructorBox;
import com.trhsy.sim.common.block.BlockControlBox;
import com.trhsy.sim.common.block.EnumControlBoxMaterial;
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
    public static Block blockControlBox = new BlockControlBox();

    /**
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        /**建筑盒**/
        register(blockConstructorBox, "block_constructor_box");
        /**控制盒**/
        register(blockControlBox, new ItemMultiTexture(blockControlBox, blockControlBox, new Function<ItemStack, String>() {
            @Override
            public String apply(ItemStack input) {
                return EnumControlBoxMaterial.values()[input.getMetadata() >> 3].getName();
            }
        }), "block_control_box");
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
        /*registerStateMapper(blockControlBox, new StateMap.Builder().withName(BlockControlBox.MATERIAL)
                .withSuffix("_furnace").ignore(BlockControlBox.FACING).build());*/
        registerRender(blockControlBox, 0, ModSim.MODID + ":" + "block_control_box");
        registerRender(blockControlBox, 1, ModSim.MODID + ":" + "block_control_box.side");
        registerRender(blockControlBox, 2, ModSim.MODID + ":" + "block_control_box.ATM");
        registerRender(blockControlBox, 3, ModSim.MODID + ":" + "block_control_box.other");
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
        ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);

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
    private static void register(Block block, ItemBlock itemBlock, String name) {
        GameRegistry.registerBlock(block.setRegistryName(name), (Class<? extends ItemBlock>) null);
        GameRegistry.registerItem(itemBlock.setRegistryName(name));
        GameData.getBlockItemMap().put(block, itemBlock);
    }
}
