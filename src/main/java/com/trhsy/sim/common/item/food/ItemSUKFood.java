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
        this.func_77627_a(true);
    }

    @SideOnly(Side.CLIENT)
    public void func_94581_a(IIconRegister par1IconRegister) {
        this.icons = new IIcon[names.length];

        for(int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = par1IconRegister.func_94245_a("satscapesimukraft:" + names[i]);
        }

    }

    public IIcon func_77617_a(int meta) {
        return meta >= 0 && meta < names.length ? this.icons[meta] : null;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int x = 0; x < names.length; ++x) {
            par3List.add(new ItemStack(this, 1, x));
        }

    }

    public String func_77667_c(ItemStack is) {
        if (is.func_77960_j() == 0) {
            return "item.foodCheese";
        } else if (is.func_77960_j() == 1) {
            return "item.foodBurger";
        } else if (is.func_77960_j() == 2) {
            return "item.foodFries";
        } else {
            return is.func_77960_j() == 3 ? "item.foodCheeseburger" : null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("A tasty snack for folks");
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    public int func_77647_b(int par1) {
        return par1;
    }
}

