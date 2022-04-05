package com.trhsy.sim.common.item.food;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemDrink extends ItemBucketMilk {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    public static final String[] names = new String[]{"drinkBeerEmpty", "drinkBeer"};

    public ItemDrink() {
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[names.length];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = par1IconRegister.registerIcon(ModSim.MODID+":" + names[i]);
        }

    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < names.length ? this.icons[meta] : null;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int x = 0; x < names.length; ++x) {
            par3List.add(new ItemStack(this, 1, x));
        }

    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        if (is.getMetadata() == 0) {
            return "item.drinkBeerEmpty";
        } else {
            return is.getMetadata() == 1 ? "item.drinkBeer" : null;
        }
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack is, World world, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 2));
        return new ItemStack(is.getItem(), 1, 0);
    }
}
