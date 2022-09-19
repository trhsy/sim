package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockMiningBox
 * @Description todo
 * @Author Tian
 * @Date 2022/9/1921:50
 **/
public class BlockMiningBox extends BlockBase{
    public BlockMiningBox(Material material) {
        super(material,"miningBox");
        this.setStepSound(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
