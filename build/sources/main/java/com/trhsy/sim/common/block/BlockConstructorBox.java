package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * 建筑箱
 */
public class BlockConstructorBox extends Block {

    public BlockConstructorBox() {
        super(Material.wood);
        //名字
        this.setUnlocalizedName("constructorBox");
        //方块硬度
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
