package com.trhsy.sim.common.item;

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
    public String getUnlocalizedName(ItemStack stack) {
        if (this.mappingProperty == null) {
            return super.getUnlocalizedName(stack);
        } else {
            IBlockState state = this.block.getStateFromMeta(stack.getMetadata());
            String name = state.getValue(this.mappingProperty).toString().toLowerCase(Locale.US);
            return super.getUnlocalizedName(stack) + "." + name;
        }
    }

    public static void setMappingProperty(Block block, IProperty<?> property) {
        ((ItemBlockMeta) Item.getItemFromBlock(block)).mappingProperty = property;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (StatCollector.canTranslate(this.getUnlocalizedName(stack) + ".tooltip")) {
            tooltip.add(EnumChatFormatting.GRAY.toString() + LocUtils.translateRecursive(this.getUnlocalizedName(stack) + ".tooltip", new Object[0]));
        } else if (StatCollector.canTranslate(super.getUnlocalizedName(stack) + ".tooltip")) {
            tooltip.add(EnumChatFormatting.GRAY.toString() + LocUtils.translateRecursive(super.getUnlocalizedName(stack) + ".tooltip", new Object[0]));
        }

        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @SideOnly(Side.CLIENT)
    public void registerItemModels() {
        Item item = this;
        ResourceLocation loc = GameData.getBlockRegistry().getNameForObject(this.block);
        Iterator var3 = this.mappingProperty.getAllowedValues().iterator();

        while(var3.hasNext()) {
            Comparable o = (Comparable)var3.next();
            int meta = this.block.getMetaFromState(this.block.getDefaultState().withProperty(this.mappingProperty, o));
            String name = this.mappingProperty.getName(o);
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc, this.mappingProperty.getName() + "=" + name));
        }

    }
}
