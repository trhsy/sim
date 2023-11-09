package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.FluidLoader;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMilk
 * @Description: 静态牛奶块
 * @date 2023/10/31 下午 1:58
 */
public class BlockMilk extends BlockFluidClassic {

    public BlockMilk() {
        super(FluidLoader.fluidMilk,Material.WATER);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
