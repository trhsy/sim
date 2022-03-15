package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiBuildingConstructor;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName BlockConstructorBox
 * @Description todo 建筑箱
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:09
 * ========================================
 **/
public class BlockConstructorBox extends Block {
    public String buildDirection = "";
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockConstructorBox() {
        super(Material.wood);
        this.setUnlocalizedName("constructorBox");
        this.setTextureName(ModSim.MODID + ":" + "constructor_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = par1IconRegister.registerIcon(ModSim.MODID + ":constructor_box");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        if (!par1World.isRemote) {
            par1World.playSoundEffect((double) par2, (double) par3, (double) par4, ModSim.MODID + ":powerdown", 1.0F, 1.0F);
        }

        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double)par2, (double)par3, (double)par4, par1World.provider.dimensionId));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        if (!par1World.isRemote) {
            par1World.playSoundEffect((double) par2, (double) par3, (double) par4, ModSim.MODID + ":constructoractivated", 1.0F, 1.0F);
        }

        super.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer thePlayer, int par6, float par7, float par8, float par9) {
        par1World.playSoundEffect((double) par2, (double) par3, (double) par4, ModSim.MODID + ":computer", 1.0F, 1.0F);
        int px = (int)Math.floor(thePlayer.posX);
        int py = (int)Math.floor(thePlayer.posY);
        int pz = (int)Math.floor(thePlayer.posZ);
        if (par4 == pz) {
            if (px < par2) {
                this.buildDirection = "-x";
            } else {
                this.buildDirection = "+x";
            }
        } else if (par2 == px) {
            if (pz < par4) {
                this.buildDirection = "-z";
            } else {
                this.buildDirection = "+z";
            }
        }

        V3 loc = new V3((double)par2, (double)par3, (double)par4, thePlayer.dimension);
        Minecraft mc = Minecraft.getMinecraft();
        GuiBuildingConstructor ui = new GuiBuildingConstructor(loc, this.buildDirection, (ArrayList)null);
        mc.displayGuiScreen(ui);
        return true;
    }
}
