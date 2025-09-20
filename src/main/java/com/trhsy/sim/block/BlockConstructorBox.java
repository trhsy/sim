package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: block
 * @Description: 建筑箱
 * @date 2025/9/3 22:54
 */
public class BlockConstructorBox extends Block {

    public BlockConstructorBox() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2F,1F));
    }
    /**
     * 放置
     * @param worldIn
     * @param pos
     * @param state
     * @param placer
     * @param stack
     */
/*    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED = SoundLoader.SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED;// 对应 sounds.json 中的键
        if (SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED == null || SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED.getRegistryName() == null) {
            ModSim.log.error("播放失败：sim:SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED 声音事件未注册");
        } else {

            worldIn.playSound(pos.getX(),pos.getY(), pos.getZ(),SIM_U_BUILDING_CONSTRUCTOR_ACTIVATED, SoundCategory.BLOCKS, 1.0F, 1.0F,false);
        }
    }*/

}
