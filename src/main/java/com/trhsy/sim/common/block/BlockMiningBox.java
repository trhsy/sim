package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockMiningBox
 * @Description todo
 * @Author Tian
 * @Date 2022/5/523:27
 **/
public class BlockMiningBox extends Block {
    public BlockMiningBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("miningBox");
        //this.setTextureName(ModSim.MODID + ":" + "mining_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
