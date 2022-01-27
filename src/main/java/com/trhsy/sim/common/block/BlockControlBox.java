package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName BlockControlBox
 * @Description todo 控制盒
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:10
 * ========================================
 **/
public class BlockControlBox extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    @SideOnly(Side.CLIENT)
    public BlockControlBox() {
        super(Material.field_151575_d);
        this.setBlockName("controlBox");
        this.setCreativeTab(CreativeTabs.field_78026_f);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister par1IconRegister) {
        this.icons = new IIcon[4];
        this.icons[0] = par1IconRegister.func_94245_a("satscapesimukraft:blockControlTop");
        this.icons[1] = par1IconRegister.func_94245_a("satscapesimukraft:blockControlSide");
        this.icons[2] = par1IconRegister.func_94245_a("satscapesimukraft:blockATM");
        this.icons[3] = par1IconRegister.func_94245_a("satscapesimukraft:blockControlTopOther");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int par1, int par2) {
        switch(par2) {
            case 0:
                return this.icons[0];
            case 1:
                switch(par1) {
                    case 0:
                        return this.icons[1];
                    case 1:
                        return this.icons[2];
                    default:
                        return this.icons[3];
                }
            default:
                System.out.println("Invalid metadata for " + this.func_149739_a());
                return this.icons[0];
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.func_72908_a((double)i, (double)j, (double)k, "satscapesimukraft:computer", 1.0F, 1.0F);
        GuiControlBox ui = null;
        GuiBankATM ui2 = null;
        Minecraft mc = Minecraft.getMinecraft();
        mc.func_71364_i();
        if (world.func_72805_g(i, j, k) != 0 && world.func_72805_g(i, j, k) != 2) {
            if (ModSimukraft.gameMode == GameMode.CREATIVE) {
                mc.displayGuiScreen((GuiScreen)null);
                ModSimukraft.sendChat("The Bank is not active when in Creative Mode (as there's no money!)");
            } else {
                ui2 = new GuiBankATM(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK), entityplayer);
                mc.displayGuiScreen(ui2);
            }
        } else {
            ui = new GuiControlBox(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK), entityplayer);
            mc.displayGuiScreen(ui);
        }

        return true;
    }

    public int func_149745_a(Random random) {
        return 0;
    }
}