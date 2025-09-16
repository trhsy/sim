package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 铜块
 * @Version: 1.0
 */
public class BlockCopper extends Block {
    public BlockCopper() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0F,10.0F));
        /*super(Material.IRON);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.METAL);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(10);

        this.setUnlocalizedName("copperBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
