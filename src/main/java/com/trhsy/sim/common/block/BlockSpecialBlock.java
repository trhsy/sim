package com.trhsy.sim.common.block;

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @ClassName BlockSpecialBlock
 * @Description todo 空气
 * @Author Tian
 * @Date 2022/4/413:53
 **/
public class BlockSpecialBlock extends Block {
    public BlockSpecialBlock() {
        super(Material.air);
        this.setUnlocalizedName("blockSpecial");
        //设置打破一个区块所需的点击次数。
        this.setHardness(100.0F);
        //设置块的爆炸阻力。返回对象以便于构造。
        this.setResistance(100.0F);
        //this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    /**
     * @Author fan
     * @Description //TODO 返回具有相同ID但不同元的块列表（例如：wood返回4个块）
     * @Date 13:58 2022/4/4
     * @Param [item, tab, list]
     * @return void
     **/
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < 8; ++i) {
            list.add(new ItemStack(item, 1, i));
        }

    }
}