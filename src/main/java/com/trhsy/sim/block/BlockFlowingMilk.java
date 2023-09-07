package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFlowingMilk
 * @Description: 流动的牛奶
 * @date 2023/08/03 上午 10:11
 */
public class BlockFlowingMilk extends BlockDynamicLiquid {
    public BlockFlowingMilk(Material materialIn) {
        super(materialIn);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
