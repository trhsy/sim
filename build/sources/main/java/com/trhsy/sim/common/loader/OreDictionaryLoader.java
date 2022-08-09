package com.trhsy.sim.common.loader;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * 矿物辞典
 */
public class OreDictionaryLoader {

    public OreDictionaryLoader(FMLPreInitializationEvent event) {
        try {
            /**
             * 锡矿
             */
            List<ItemStack> oreTin = OreDictionary.getOres("oreTin");
            for (ItemStack itemStack : oreTin) {
                OreDictionary.registerOre("oreTin", itemStack);
            }
            /**
             * 铜矿
             */
            List<ItemStack> oreCopper = OreDictionary.getOres("oreCopper");
            for (ItemStack itemStack : oreCopper) {
                OreDictionary.registerOre("oreCopper", itemStack);
            }
            /**
             * 锡锭
             */
            List<ItemStack> ingotTin = OreDictionary.getOres("ingotTin");
            for (ItemStack itemStack : ingotTin) {
                OreDictionary.registerOre("ingotTin", itemStack);
            }
            /**
             * 铜锭
             */
            List<ItemStack> ingotCopper = OreDictionary.getOres("ingotCopper");
            for (ItemStack itemStack : ingotCopper) {
                OreDictionary.registerOre("ingotCopper", itemStack);
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("OreDictionaryLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
