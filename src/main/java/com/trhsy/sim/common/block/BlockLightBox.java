package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName BlockLightBox
 * @Description todo 灯箱
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:02
 * ========================================
 **/
public class BlockLightBox extends Block {
    private IIcon[] icons;
    protected BlockLightBox() {
        super(Material.field_151575_d);
        this.func_149715_a(1.0F);
        this.func_149647_a(CreativeTabs.field_78026_f);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(2.0F);
        this.func_149752_b(1.0F);
        this.func_149675_a(true);
        this.func_149663_c("SUKlight");
    }
    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[8];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:blockLightWhite");
        this.icons[1] = iconRegister.func_94245_a("satscapesimukraft:blockLightRed");
        this.icons[2] = iconRegister.func_94245_a("satscapesimukraft:blockLightOrange");
        this.icons[3] = iconRegister.func_94245_a("satscapesimukraft:blockLightYellow");
        this.icons[4] = iconRegister.func_94245_a("satscapesimukraft:blockLightGreen");
        this.icons[5] = iconRegister.func_94245_a("satscapesimukraft:blockLightBlue");
        this.icons[6] = iconRegister.func_94245_a("satscapesimukraft:blockLightPurple");
        this.icons[7] = iconRegister.func_94245_a("satscapesimukraft:blockLightRainbow");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return meta < 8 && meta >= 0 ? this.icons[meta] : this.icons[0];
    }

    public BlockLightBox idDropped(int par1, Random par2Random, int par3) {
        return this;
    }

    public int func_149692_a(int j) {
        return j;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Block blockId, CreativeTabs par2CreativeTabs, List par3List) {
        for(int meta = 0; meta < 8; ++meta) {
            par3List.add(new ItemStack(blockId, 1, meta));
        }

    }

    public int func_149635_D() {
        return 16777215;
    }
}
