package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockFarmingBox
 * @Description todo
 * @Author Tian
 * @Date 2022/5/49:51
 **/
public class BlockFarmingBox extends Block {
    public BlockFarmingBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("farmingBox");
        //this.setTextureName(ModSim.MODID + ":" + "farming_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
