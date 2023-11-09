package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockCompositeBrick
 * @Description: 复合砖
 * @date 2023/11/02 下午 5:11
 */
public class BlockCompositeBrick extends BlockBase {
    public BlockCompositeBrick() {
        super(Material.ROCK, "compositeBrick");
        this.setSoundType(SoundType.STONE);
        this.setHardness(8.0F);
        this.setResistance(7.0F);
        //设置挖掘工具、挖掘等级
        this.setHarvestLevel("pickaxe",0);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}