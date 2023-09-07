package com.trhsy.sim.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * 创建块的基础
 * @author Trhsy
 */
public class BlockBase extends Block {
    /**
     * 模组名称
     */
    private String name;
    public BlockBase(Material material, String name) {
        super(material);
        this.name = name;
        this.setUnlocalizedName(name);
        //this.setRegistryName(name);
    }


}
