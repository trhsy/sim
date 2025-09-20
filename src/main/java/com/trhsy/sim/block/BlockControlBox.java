package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 控制箱
 * @Version: 1.0
 */
public class BlockControlBox extends Block {
//    public static final EnumProperty<EnumControlBox> TYPE = EnumProperty.create("type", EnumControlBox.class);
    private static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
    public BlockControlBox() {
        //controlBox
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(10.0F,1.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, 0));
        //用于设定走在方块上的响声。
//        this.setSoundType(SoundType.WOOD);
        //方块硬度
//        this.setHardness(10.0F);
        //爆炸
//        this.setResistance(1.0F); // 爆炸抗性调整为 1.0F（原 1 可能过低）
//        this.setUnlocalizedName("controlBox");
//        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumControlBox.TOP));
//        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
