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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName ItemGranulesTin
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:03
 * ========================================
 **/
public class ItemGranulesTin extends Item {
    private IIcon[] icons;

    public ItemGranulesTin(int par1) {
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":granulesTin");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icons[0];
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Tin granules";
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[0];
    }
}
