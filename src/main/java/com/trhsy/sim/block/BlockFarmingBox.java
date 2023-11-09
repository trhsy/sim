package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFarmingBox
 * @Description: 农田箱子
 * @date 2023/11/07 下午 4:32
 */
public class BlockFarmingBox extends BlockBase{
    public BlockFarmingBox() {
        super(Material.WOOD,"farmingBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
