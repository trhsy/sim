package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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

    public BlockCompositeBrick() {
        super(Material.rock);
        this.setStepSound(Block.soundTypeStone);
        this.setHardness(8.0F);
        this.setResistance(7.0F);
        this.setUnlocalizedName("compositeBrick");
        this.setTextureName(ModSim.MODID + ":" + "composite_brick");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":composite_brick");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }
}