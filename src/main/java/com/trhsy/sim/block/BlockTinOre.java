package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockTinOre
 * @Description: 锡矿
 * @date 2023/11/08 上午 11:17
 */
public class BlockTinOre extends BlockOre {
    public BlockTinOre(){
//        super(Material.ROCK, "tinBlockOre");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.STONE);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(5);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(5F);
        this.setUnlocalizedName("tinBlockOre");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
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
        return Item.getItemFromBlock(this);
    }

    /**
     * 将此块的水滴作为EntityItems生成到世界中。
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    }
}