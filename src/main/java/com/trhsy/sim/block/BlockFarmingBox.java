package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockFarmingBox
 * @Description todo 农田箱
 * @Author Tian
 * @Date 2022/9/1812:00
 **/
public class BlockFarmingBox extends BlockBase{
    public BlockFarmingBox(Material material) {
        super(material,"farmingBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
