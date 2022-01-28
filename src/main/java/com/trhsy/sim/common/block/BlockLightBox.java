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
        super(Material.wood);
        this.setLightLevel(1.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setTickRandomly(true);
        this.setBlockName("SUKlight");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[8];
        this.icons[0] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightWhite");
        this.icons[1] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightRed");
        this.icons[2] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightOrange");
        this.icons[3] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightYellow");
        this.icons[4] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightGreen");
        this.icons[5] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightBlue");
        this.icons[6] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightPurple");
        this.icons[7] = iconRegister.registerIcon(ModSimukraft.MODID + ":blockLightRainbow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < 8 && meta >= 0 ? this.icons[meta] : this.icons[0];
    }

    public BlockLightBox idDropped(int par1, Random par2Random, int par3) {
        return this;
    }

    @Override
    public int damageDropped(int j) {
        return j;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Block blockId, CreativeTabs par2CreativeTabs, List par3List) {
        for(int meta = 0; meta < 8; ++meta) {
            par3List.add(new ItemStack(blockId, 1, meta));
        }

    }

    @Override
    public int getBlockColor() {
        return 16777215;
    }
}
