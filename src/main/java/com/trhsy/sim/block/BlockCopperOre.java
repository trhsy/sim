package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 铜矿
 * @Version: 1.0
 */
public class BlockCopperOre extends Block {
    public BlockCopperOre() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 5.0F));
        /*
        *  super(Material.ROCK, "copperBlockOre");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.STONE);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(5F);
        this.setUnlocalizedName("copperBlockOre");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        * */
    }
}
