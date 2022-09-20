package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWindmill extends BlockBase{
    public BlockWindmill(Material material) {
        super(material,"windmill");
        //用于设定走在方块上的响声。
        this.setStepSound(SoundType.WOOD);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
        //this.setTextureName(ModSim.MODID + ":" + "block_windmill");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
