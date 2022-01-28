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
 * @ClassName BlockCompositeBrick
 * @Description todo 复合砖块
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:07
 * ========================================
 **/
public class BlockCompositeBrick extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockCompositeBrick(Material par2Material) {
        super(par2Material);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSimukraft.MODID + ":compositebrick");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }
}