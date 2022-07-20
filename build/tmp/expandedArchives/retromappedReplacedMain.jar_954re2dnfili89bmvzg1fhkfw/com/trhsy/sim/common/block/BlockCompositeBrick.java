package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockCompositeBrick
 * @Description todo 复合砖
 * @Author Tian
 * @Date 2022/5/48:03
 **/
public class BlockCompositeBrick extends Block {
    public BlockCompositeBrick() {
        super(Material.field_151576_e);
        this.func_149672_a(Block.field_149769_e);
        this.func_149711_c(8.0F);
        this.func_149752_b(7.0F);
        this.func_149663_c("compositeBrick");
        //this.setTextureName(ModSim.MODID + ":" + "composite_brick");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }
}
