package com.trhsy.sim.common.block.fluid;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

/**
 * @ClassName BlockFluidMilk
 * @Description todo 注册流体到方块
 * @Author Tian
 * @Date 2022/5/823:11
 **/
public class BlockFluidMilk extends BlockFluidClassic {
    public BlockFluidMilk() {
        super(new FluidMilk(), Material.water);
        this.setUnlocalizedName("fluidMilk");
        //this.setTextureName(ModSim.MODID + ":" + "milk_still");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }
}
