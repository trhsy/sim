package com.trhsy.sim.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMilk
 * @Description:静态牛奶块
 * @date 2025/9/16 15:21
 */
public class BlockMilk extends Block {

    public BlockMilk() {
        super(Block.Properties.create(Material.WATER).hardnessAndResistance(100F,3F));
        /*
        // 设置未本地化名称
            this.setUnlocalizedName("fluidMilk");
            // 设置硬度
            this.setHardness(100.0F);
            // 设置光照透明度
            this.setLightOpacity(3);
            // 禁用统计信息
            this.disableStats();
            // 设置创造模式标签
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        * */
    }
}
