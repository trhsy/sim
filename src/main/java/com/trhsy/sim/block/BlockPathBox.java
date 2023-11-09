package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockPathBox
 * @Description:
 * @date 2023/11/08 上午 11:09
 */
public class BlockPathBox extends BlockBase{
    public BlockPathBox() {
        super(Material.WOOD,"pathBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    /**
     * 掉落数量为0，方块敲了就消失
     * @param state
     * @return
     */
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
}
