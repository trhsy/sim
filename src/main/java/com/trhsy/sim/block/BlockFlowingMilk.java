package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFlowingMilk
 * @Description:
 * @date 2023/11/07 下午 5:11
 */
public class BlockFlowingMilk extends BlockDynamicLiquid {
    public BlockFlowingMilk() {
        super(Material.WATER);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}