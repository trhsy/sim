package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * 建筑箱
 */
public class BlockConstructorBox extends BlockBase{

    public BlockConstructorBox(Material materialIn) {
        super(materialIn,"constructorBox");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //方块硬度
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
