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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemGranulesIron
 * @Description todo 铁粒儿
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:03
 * ========================================
 **/
public class ItemGranulesIron extends Item {
    private IIcon[] icons;

    public ItemGranulesIron() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("granulesIron");
        this.setTextureName(ModSim.MODID + ":granules_iron");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":granules_iron");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icons[0];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String granules_iron = I18n.format("container.sim.granules_iron");
        par3List.add(granules_iron);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[0];
    }
}