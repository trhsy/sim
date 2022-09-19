package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockPathBox
 * @Description todo
 * @Author Tian
 * @Date 2022/9/1921:53
 **/
public class BlockPathBox extends BlockBase{
    public BlockPathBox(Material material) {
        super(material,"pathBox");
        this.setStepSound(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
