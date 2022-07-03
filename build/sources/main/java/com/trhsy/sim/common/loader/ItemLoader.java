package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.item.ItemGranulesCopper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 物品加载类
 */
public class ItemLoader {
    public static Item itemGranulesCopper=new ItemGranulesCopper();
    /**
     * 加载物品
     *
     * @param event
     */
    public ItemLoader(FMLPreInitializationEvent event) {
        register(itemGranulesCopper, "item_granules_copper");
    }

    /**
     * 注册任务品
     *
     * @param item
     * @param name
     */
    private static void register(Item item, String name) {
        GameRegistry.registerItem(item.setRegistryName(name));
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 注册材质
     * @Date 20:45 2022/4/15
     * @Param []
     **/
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        registerRender(itemGranulesCopper);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, model);
    }
}
