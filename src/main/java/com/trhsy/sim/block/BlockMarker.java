package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMarker
 * @Description:标记棒
 * @date 2025/9/16 15:19
 */
public class BlockMarker extends Block {
    public BlockMarker() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2F,1F));
        /*
        * super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
//        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setUnlocalizedName("markerBar");
//        this.setLightLevel(0.1F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
