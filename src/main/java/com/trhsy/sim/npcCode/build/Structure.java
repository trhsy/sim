package com.trhsy.sim.npcCode.build;

import net.minecraft.block.state.IBlockState;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: Structure
 * @Description: 蓝图存储方法
 * @date 2023/08/02 上午 10:38
 */
public class Structure {
    private IBlockState iBlockState;
    private int meta;

    public IBlockState getiBlockState() {
        return iBlockState;
    }

    public void setiBlockState(IBlockState iBlockState) {
        this.iBlockState = iBlockState;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }
}
