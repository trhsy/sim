package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName BlockCheeseBlock
 * @Description todo 奶酪块
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:06
 * ========================================
 **/
public class BlockCheeseBlock extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockCheeseBlock() {
        super(Material.field_151570_A);
        this.func_149663_c("SUKcheeseBlock");
        this.func_149658_d(ModSimukraft.modid + ":" + "cheeseBlock");
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:cheeseblock");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return this.icons[0];
    }
}
