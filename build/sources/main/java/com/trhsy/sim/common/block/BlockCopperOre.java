package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ItemLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * 铜矿
 */
public class BlockCopperOre extends BlockOre {
    public BlockCopperOre(){
        //用于设定走在方块上的响声。
        this.setStepSound(Block.soundTypePiston);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(5F);
        this.setUnlocalizedName("copperBlockOre");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * 获得经验
     * @param iBlockAccess
     * @param blockPos
     * @param p_getExpDrop_3_
     * @return
     */
    @Override
    public int getExpDrop(IBlockAccess iBlockAccess, BlockPos blockPos, int p_getExpDrop_3_) {
        int i=0;
        try {
            IBlockState state = iBlockAccess.getBlockState(blockPos);
            Random rand = iBlockAccess instanceof World ? ((World)iBlockAccess).rand : new Random();
            i = MathHelper.getRandomIntegerInRange(rand, 0, 7);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("铜矿getExpDrop出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return i;
    }

    /**
     * 掉落物品
     * @param iBlockState
     * @param random
     * @param p_getItemDropped_3_
     * @return
     */
    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int p_getItemDropped_3_) {
        return Item.getItemFromBlock(BlockLoader.blockCopperOre);
    }
}
