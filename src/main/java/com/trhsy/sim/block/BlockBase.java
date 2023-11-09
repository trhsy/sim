package com.trhsy.sim.block;


import com.trhsy.sim.ModSim;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockBase
 * @Description: 方块的基础类
 * @date 2023/10/31 上午 10:13
 */
public class BlockBase extends Block {
    /**
     * 模组名称
     */
    private String name;
    public BlockBase(Material blockMaterialIn,String name) {
        super(blockMaterialIn);
        this.name = name;
        this.setUnlocalizedName(name);
    }
}
