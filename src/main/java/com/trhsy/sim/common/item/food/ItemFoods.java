package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * 新的食物，奶酪，汉堡，薯条，奶酪汉堡
 */
public class ItemFoods extends ItemFood {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public static final String[] names = new String[]{"foodCheese", "foodBurger", "foodFries", "foodCheeseburger"};

    public ItemFoods() {
        super(6, 0.6F, false);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[names.length];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = par1IconRegister.registerIcon(ModSim.MODID + ":" + names[i]);
        }

    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < names.length ? this.icons[meta] : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int x = 0; x < names.length; ++x) {
            subItems.add(new ItemStack(this, 1, x));
        }

    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        switch (is.getMetadata()) {
            case 0:
                return "item.foodCheese";
            case 1:
                return "item.foodBurger";
            case 2:
                return "item.foodFries";
            case 3:
                return "item.foodCheeseburger";
            default:
                return "";

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String sim_folks = I18n.format("container.sim.sim_folks");
        par3List.add(sim_folks);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
