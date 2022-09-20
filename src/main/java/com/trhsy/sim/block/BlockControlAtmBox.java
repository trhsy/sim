package com.trhsy.sim.block;


import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.Random;

/**
 * 银行控制箱
 * @author Trhsy
 */
public class BlockControlAtmBox extends BlockBase {

    public BlockControlAtmBox(Material material) {
        super(material,"controlBoxAtm");
        this.setStepSound(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * 销毁时要丢弃的项目数量
     * @param random
     * @return
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
