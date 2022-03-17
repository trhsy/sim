package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemWindmillSails
 * @Description todo 风车帆
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:05
 * ========================================
 **/
public class ItemWindmillSails extends Item {
    private IIcon[] icons;

    public ItemWindmillSails() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("windmillSails");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[16];
        for (int i = 0; i < 16; ++i) {
            this.icons[i] = iconRegister.registerIcon(ModSim.MODID + ":windmill_sails" + i);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int x = 0; x < 16; ++x) {
            subItems.add(new ItemStack(this, 1, x));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        return this.getUnlocalizedName() + is.getMetadata();
        //return "item.windmillSails" + is.getMetadata();
    }


    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < 16 ? this.icons[meta] : this.icons[0];
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String windmill = I18n.format("container.sim.windmill");
        par3List.add(windmill);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
}
