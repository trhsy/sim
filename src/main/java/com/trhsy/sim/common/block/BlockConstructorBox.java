package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName BlockConstructorBox
 * @Description todo 牛奶块
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:09
 * ========================================
 **/
public class BlockConstructorBox extends Block {
    public String buildDirection = "";
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockConstructorBox() {
        super(Material.field_151575_d);
        this.func_149663_c("constructorBox");
        this.func_149647_a(CreativeTabs.field_78026_f);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister par1IconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = par1IconRegister.func_94245_a("satscapesimukraft:blockConstruction");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return this.icons[0];
    }

    public void func_149664_b(World par1World, int par2, int par3, int par4, int par5) {
        if (!par1World.field_72995_K) {
            par1World.func_72908_a((double)par2, (double)par3, (double)par4, "satscapesimukraft:powerdown", 1.0F, 1.0F);
        }

        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double)par2, (double)par3, (double)par4, par1World.field_73011_w.field_76574_g));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        super.func_149664_b(par1World, par2, par3, par4, par5);
    }

    public void func_149726_b(World par1World, int par2, int par3, int par4) {
        if (!par1World.field_72995_K) {
            par1World.func_72908_a((double)par2, (double)par3, (double)par4, "satscapesimukraft:constructoractivated", 1.0F, 1.0F);
        }

        super.func_149726_b(par1World, par2, par3, par4);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149727_a(World par1World, int par2, int par3, int par4, EntityPlayer thePlayer, int par6, float par7, float par8, float par9) {
        par1World.func_72908_a((double)par2, (double)par3, (double)par4, "satscapesimukraft:computer", 1.0F, 1.0F);
        int px = (int)Math.floor(thePlayer.field_70165_t);
        int py = (int)Math.floor(thePlayer.field_70163_u);
        int pz = (int)Math.floor(thePlayer.field_70161_v);
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

        V3 loc = new V3((double)par2, (double)par3, (double)par4, thePlayer.field_71093_bK);
        Minecraft mc = Minecraft.func_71410_x();
        GuiBuildingConstructor ui = new GuiBuildingConstructor(loc, this.buildDirection, (ArrayList)null);
        mc.func_147108_a(ui);
        return true;
    }
}
