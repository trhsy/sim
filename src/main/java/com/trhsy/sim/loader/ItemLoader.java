package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.item.granules.ItemGranulesCopper;
import com.trhsy.sim.util.Util;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

/**
 * 物品加载类
 */
public class ItemLoader {
    /**铜粒儿**/
    public static Item itemGranulesCopper=new ItemGranulesCopper();
    /**
     * 加载物品
     *
     * @param event
     */
    public ItemLoader(FMLPreInitializationEvent event) {
        try {
            /**铜粒儿**/
            register(itemGranulesCopper, "item_granules_copper");
        }catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 注册任务品
     *
     * @param item
     * @param name
     */
    private static void register(Item item, String name) {
        try {
            //GameRegistry.registerItem(item.setRegistryName(name));
            GameRegistry.register(item.setRegistryName(name));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-register出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
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
        try {
            /**铜粒儿**/
            registerRender(itemGranulesCopper);
        }catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        /**ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
         ModelLoader.setCustomModelResourceLocation(item, 0, model);**/
        try {
            ResourceLocation resourceLocation=item.getRegistryName();
            registerRender(item, 0, resourceLocation.getResourcePath());
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item,int meta,String name) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(ModSim.MODID,name);
            ModelResourceLocation model = new ModelResourceLocation(resourcelocation, "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, model);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("ItemLoader-registerRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
