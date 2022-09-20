package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;

public class BlockTinOre extends BlockOre {
    public BlockTinOre(){
        //用于设定走在方块上的响声。
        this.setStepSound(SoundType.STONE);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(5F);
        this.setUnlocalizedName("tinBlockOre");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
