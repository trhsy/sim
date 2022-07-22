package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.common.util.LocUtils;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName ItemBlockMeta
 * @Description todo
 * @Author Tian
 * @Date 2022/4/3016:10
 **/
public class ItemBlockMeta extends ItemColored {
    protected IProperty mappingProperty;
    public ItemBlockMeta(Block block) {
        super(block, true);
    }
    @Override
    public String func_77667_c(ItemStack stack) {
        String unlocalizedName=null;
        try {
            if (this.mappingProperty == null) {
                unlocalizedName= super.func_77667_c(stack);
            } else {
                IBlockState state = this.field_150939_a.func_176203_a(stack.func_77960_j());
                String name = state.func_177229_b(this.mappingProperty).toString().toLowerCase(Locale.US);
                unlocalizedName= super.func_77667_c(stack) + "." + name;
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("getUnlocalizedName出错了：" + e.getMessage());
        }
        return unlocalizedName;
    }

    public static void setMappingProperty(Block block, IProperty<?> property) {
        try {
            ((ItemBlockMeta) Item.func_150898_a(block)).mappingProperty = property;
        } catch (Exception e) {
            ModSimReloaded.log.error("setMappingProperty出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        try {
            if (StatCollector.func_94522_b(this.func_77667_c(stack) + ".tooltip")) {
                tooltip.add(EnumChatFormatting.GRAY.toString() + LocUtils.translateRecursive(this.func_77667_c(stack) + ".tooltip", new Object[0]));
            } else if (StatCollector.func_94522_b(super.func_77667_c(stack) + ".tooltip")) {
                tooltip.add(EnumChatFormatting.GRAY.toString() + LocUtils.translateRecursive(super.func_77667_c(stack) + ".tooltip", new Object[0]));
            }

            super.func_77624_a(stack, playerIn, tooltip, advanced);
        } catch (Exception e) {
            ModSimReloaded.log.error("addInformation出错了：" + e.getMessage());
        }

    }

    @SideOnly(Side.CLIENT)
    public void registerItemModels() {
        try {
            Item item = this;
            ResourceLocation loc = GameData.getBlockRegistry().func_177774_c(this.field_150939_a);
            Iterator var3 = this.mappingProperty.func_177700_c().iterator();

            while(var3.hasNext()) {
                Comparable o = (Comparable)var3.next();
                int meta = this.field_150939_a.func_176201_c(this.field_150939_a.func_176223_P().func_177226_a(this.mappingProperty, o));
                String name = this.mappingProperty.func_177702_a(o);
                ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, this.mappingProperty.func_177701_a() + "=" + name));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("registerItemModels出错了：" + e.getMessage());
        }


    }
}
