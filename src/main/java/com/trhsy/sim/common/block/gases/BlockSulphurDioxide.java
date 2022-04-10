package com.trhsy.sim.common.block.gases;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * @ClassName BlockSulphurDioxide
 * @Description todo 二氧化硫
 * @Author Tian
 * @Date 2022/4/1013:02
 **/
public class BlockSulphurDioxide extends BlockGas {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    public BlockSulphurDioxide() {
        this.setRiseRate(5);
        this.setTickRandomly(true);
        this.disableStats();
        this.setHardness(0.0F);
        this.setUnlocalizedName("blockSulphurDioxide");
        this.setTextureName(ModSim.MODID +":gasDispenser");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID +":blockSulphurDioxide");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }
}
