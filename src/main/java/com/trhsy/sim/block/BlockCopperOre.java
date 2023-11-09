package com.trhsy.sim.block;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockCopperOre
 * @Description: 铜矿
 * @date 2023/11/07 下午 4:28
 */
public class BlockCopperOre extends BlockOre {
    public BlockCopperOre() {
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.STONE);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(5F);
        this.setUnlocalizedName("copperBlockOre");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * 获得经验
     *
     * @param state
     * @param pos
     * @param fortune
     * @return
     */
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        int i = 0;
        try {
            Random rand = world instanceof World ? ((World) world).rand : new Random();
            i = MathHelper.getInt(rand, 0, 7);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("铜矿getExpDrop出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return i;
    }

    /**
     * 掉落物品
     *
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
