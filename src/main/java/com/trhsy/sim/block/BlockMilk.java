package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.FluidLoader;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFluidMilk
 * @Description: 静态牛奶块
 * @date 2022/9/21 0021 下午 3:36
 */
public class BlockMilk extends BlockFluidClassic {
    public BlockMilk(Material materialIn) {
        super(FluidLoader.fluidMilk,materialIn);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

}
