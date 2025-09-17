package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockWindmill
 * @Description:
 * @date 2025/9/17 14:45
 */
public class BlockWindmill extends BlockContainer {
    public BlockWindmill() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(1F, 5F));
        /*super(Material.WOOD);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
//        this.setUnlocalizedName("windmill");
        //this.setTextureName(ModSim.MODID + ":" + "windmill");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
//        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.isBurning = isBurning;*/

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader) {
        return null;
    }
}
