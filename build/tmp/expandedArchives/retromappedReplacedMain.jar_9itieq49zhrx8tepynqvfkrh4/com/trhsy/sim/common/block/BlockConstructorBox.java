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
        super(Material.field_151575_d);
        //名字
        this.func_149663_c("constructorBox");
        //方块硬度
        this.func_149711_c(0.5F);
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }
}
