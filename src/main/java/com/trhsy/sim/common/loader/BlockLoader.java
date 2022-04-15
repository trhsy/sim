package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.block.BlockConstructorBox;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
     * 加载方块
     *
     * @param event
     */
    public BlockLoader(FMLPreInitializationEvent event) {
        register(blockConstructorBox, "block_constructor_box");
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
}
