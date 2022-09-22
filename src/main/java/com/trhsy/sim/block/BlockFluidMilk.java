package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFluidMilk
 * @Description: 静态牛奶块
 * @date 2022/9/21 0021 下午 3:36
 */
public class BlockFluidMilk extends BlockStaticLiquid {
    public BlockFluidMilk(Material materialIn) {
        super(materialIn);
        this.enableStats = false;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
