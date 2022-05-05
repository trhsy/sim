package com.trhsy.sim.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockSpecialBlock
 * @Description todo 特制空方块
 * @Author Tian
 * @Date 2022/5/523:30
 **/
public class BlockSpecial extends Block {
    public BlockSpecial() {
        super(Material.air);
        this.setUnlocalizedName("blockSpecial");
        //设置打破一个区块所需的点击次数。
        this.setHardness(100.0F);
        //设置块的爆炸阻力。返回对象以便于构造。
        this.setResistance(100.0F);
        //this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
