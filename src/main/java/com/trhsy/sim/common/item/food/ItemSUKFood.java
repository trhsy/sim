package com.trhsy.sim.common.item.food;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemSUKFood
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:04
 * ========================================
 **/
public class ItemSUKFood extends ItemFood {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    public static final String[] names = new String[]{"foodCheese", "foodBurger", "foodFries", "foodCheeseburger"};

    public ItemSUKFood() {
        super(6, 0.6F, false);
        this.func_77637_a(CreativeTabs.field_78039_h);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[names.length];

        for(int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = par1IconRegister.registerIcon(ModSimukraft.MODID + ":" + names[i]);
        }

    }

    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < names.length ? this.icons[meta] : null;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int x = 0; x < names.length; ++x) {
            par3List.add(new ItemStack(this, 1, x));
        }

    }

    public String getUnlocalizedName(ItemStack is) {
        if (is.getMetadata() == 0) {
            return "item.foodCheese";
        } else if (is.getMetadata() == 1) {
            return "item.foodBurger";
        } else if (is.getMetadata() == 2) {
            return "item.foodFries";
        } else {
            return is.getMetadata() == 3 ? "item.foodCheeseburger" : null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("A tasty snack for folks");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    public int getMetadata(int par1) {
        return par1;
    }
}

