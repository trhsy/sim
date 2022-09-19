package com.trhsy.sim.util;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

import java.util.Locale;

/**
 * @author Administrator
 */
public class ItemBlockMeta extends ItemColored {
    protected IProperty mappingProperty;
    public ItemBlockMeta(Block block) {
        super(block, true);
    }
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String unlocalizedName=null;
        try {
            if (this.mappingProperty == null) {
                unlocalizedName= super.getUnlocalizedName(stack);
            } else {
                IBlockState state = this.block.getStateFromMeta(stack.getMetadata());
                String name = state.getValue(this.mappingProperty).toString().toLowerCase(Locale.US);
                unlocalizedName= super.getUnlocalizedName(stack) + "." + name;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("getUnlocalizedName出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return unlocalizedName;
    }

    public static void setMappingProperty(Block block, IProperty<?> property) {
        try {
            ((ItemBlockMeta) Item.getItemFromBlock(block)).mappingProperty = property;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("setMappingProperty出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}
