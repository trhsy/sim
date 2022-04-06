package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.block.fluid.FluidMilk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;

/**
 * ========================================
 *
 * @ClassName BlockFluidMilk
 * @Description todo 流体牛奶
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:52
 * ========================================
 **/
public class BlockFluidMilk extends BlockFluidClassic {
    private IIcon[] icons;

    public BlockFluidMilk() {
        super(new FluidMilk(), Material.water);
        this.setUnlocalizedName("fluidMilk");
        //this.setTextureName(ModSim.MODID + ":" + "milk_still");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[2];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":milk_still");
        this.icons[1] = iconRegister.registerIcon(ModSim.MODID + ":milk_flow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta >= 1 ? this.icons[1] : this.icons[0];
    }
}
