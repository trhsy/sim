package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

/**
 * @ClassName BlockSpecialBlock
 * @Description todo 特制空方块
 * @Author Tian
 * @Date 2022/5/523:30
 **/
public class BlockSpecial extends BlockAir {
    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID + ":" + "block_special");
    public BlockSpecial() {
//        super(Material.air);
        //this.setRiseRate(5);
        this.func_149675_a(true);
        this.func_149649_H();
        this.func_149711_c(0.0F);
        this.func_149663_c("blockSpecial");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }
    @Override
    public boolean func_149662_c() {
        return true;
    }
}
