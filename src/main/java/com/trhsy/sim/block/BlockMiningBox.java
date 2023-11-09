package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMiningBox
 * @Description: 采矿箱
 * @date 2023/11/08 上午 10:50
 */
public class BlockMiningBox extends BlockBase {
    public BlockMiningBox() {
        super(Material.WOOD,"miningBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    /**
     * @return int
     * @Author fan
     * @Description //TODO 获取此块可以删除的项的元数据。当块被破坏时调用此方法。它基于块的旧元数据返回被删除项的元数据。
     * @Date 10:04 2022/11/7
     * @Param [state]
     **/
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
}
