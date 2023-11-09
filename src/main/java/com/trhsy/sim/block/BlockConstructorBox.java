package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockConstructorBox
 * @Description: 建筑箱
 * @date 2023/10/31 上午 10:16
 */
public class BlockConstructorBox extends BlockBase{
    public BlockConstructorBox() {
        super(Material.WOOD, "constructorBox");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //方块硬度
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
