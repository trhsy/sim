package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * ========================================
 *
 * @ClassName BlockFluidMilk
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:52
 * ========================================
 **/
public class BlockFluidMilk extends BlockFluidClassic {
    private IIcon[] icons;

    public BlockFluidMilk() {
        super(ModSimukraft.SUKfluidMilk, Material.field_151586_h);
        this.setBlockName("fluidMilk");
        this.setCreativeTab(CreativeTabs.field_78026_f);

    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[2];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:milk_still");
        this.icons[1] = iconRegister.func_94245_a("satscapesimukraft:milk_flow");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return meta >= 1 ? this.icons[1] : this.icons[0];
    }
}
