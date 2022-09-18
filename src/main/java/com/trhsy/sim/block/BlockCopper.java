package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockCopper
 * @Description todo 铜块
 * @Author Tian
 * @Date 2022/9/1811:50
 **/
public class BlockCopper extends BlockBase{
    public BlockCopper(Material material) {
        super(material, "copperBlock");
        //用于设定走在方块上的响声。
        this.setStepSound(SoundType.METAL);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(10);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
