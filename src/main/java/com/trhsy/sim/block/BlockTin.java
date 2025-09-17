package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockTin
 * @Description:  锡块
 * @date 2025/9/17 14:42
 */
public class BlockTin extends Block{
    public BlockTin(){
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5F,10F));
        /*super(Material.IRON);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.METAL);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(10);
        this.setUnlocalizedName("tinBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
