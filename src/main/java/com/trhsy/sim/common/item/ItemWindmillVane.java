package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
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
 * @ClassName ItemWindmillVane
 * @Description todo 风车叶片
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:05
 * ========================================
 **/
public class ItemWindmillVane extends Item {
    private IIcon[] icons;

    public ItemWindmillVane() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("windmillVane");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[16];

        for(int i = 0; i <= 15; ++i) {
            this.icons[i] = iconRegister.registerIcon(ModSim.MODID + ":windmill_vane" + i);
        }

    }
    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < 16 ? this.icons[meta] : this.icons[0];
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
        return this.getUnlocalizedName()+ is.getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String windmill_sail = I18n.format("container.sim.windmill_sail");
        par3List.add(windmill_sail);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }

}
