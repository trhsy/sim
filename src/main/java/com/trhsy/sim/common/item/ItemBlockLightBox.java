package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemBlockLightBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:01
 * ========================================
 **/
public class ItemBlockLightBox extends ItemBlock {
    private IIcon[] icons;
    public ItemBlockLightBox(Block par1, Block block) {
        super(par1);
        this.setHasSubtypes(true);
        //this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        if (is.getMetadata() == 0) {
            return "tile.lightBox.white";
        } else if (is.getMetadata() == 1) {
            return "tile.lightBox.red";
        } else if (is.getMetadata() == 2) {
            return "tile.lightBox.orange";
        } else if (is.getMetadata() == 3) {
            return "tile.lightBox.yellow";
        } else if (is.getMetadata() == 4) {
            return "tile.lightBox.green";
        } else if (is.getMetadata() == 5) {
            return "tile.lightBox.blue";
        } else if (is.getMetadata() == 6) {
            return "tile.lightBox.purple";
        } else {
            return is.getMetadata() == 7 ? "tile.lightBox.rainbow" : null;
        }
        //return this.getUnlocalizedName() + is.getMetadata();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[8];
        for (int i = 0; i < 8; ++i) {
            this.icons[i] = iconRegister.registerIcon(ModSim.MODID + ":light_block" + i);
        }

    }
    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < 8 ? this.icons[meta] : this.icons[0];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String sim_block_light = I18n.format("container.sim.sim_block_light");
        par3List.add(sim_block_light);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
