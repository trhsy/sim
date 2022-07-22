package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName BlockLivingBlock
 * @Description todo 地毯
 * @Author Tian
 * @Date 2022/5/414:09
 **/
public class BlockLiving extends EnumBlock<EnumBlockLiving> {
    //public static final PropertyEnum<EnumBlockLiving> COLOR = PropertyEnum.<EnumBlockLiving>create("color", EnumBlockLiving.class);
    public static final PropertyEnum<EnumBlockLiving> TYPE = PropertyEnum.func_177709_a("type", EnumBlockLiving.class);

    public BlockLiving() {
        super(Material.field_151593_r, TYPE, EnumBlockLiving.class);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLiving.WHITE));
        this.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.func_149711_c(10.0F);
        this.func_149752_b(1.0F);
        this.func_149663_c("livingBlock");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor func_180659_g(IBlockState state) {
        return ((EnumBlockLiving) state.func_177229_b(TYPE)).getMapColor();
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean func_149662_c() {
        return false;
    }

    @Override
    public boolean func_149686_d() {
        return false;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void func_149683_g() {
        this.setBlockBoundsFromMeta(0);
    }

    @Override
    public void func_180654_a(IBlockAccess worldIn, BlockPos pos) {
        this.setBlockBoundsFromMeta(0);
    }

    protected void setBlockBoundsFromMeta(int meta) {
        int i = 0;
        float f = (float) (1 * (1 + i)) / 16.0F;
        this.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return super.func_176196_c(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
    public void func_176204_a(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos)) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return !worldIn.func_175623_d(pos.func_177977_b());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean func_176225_a(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? true : super.func_176225_a(worldIn, pos, side);
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int func_180651_a(IBlockState state) {
        return ((EnumBlockLiving) state.func_177229_b(TYPE)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void func_149666_a(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 16; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(TYPE, EnumBlockLiving.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int func_176201_c(IBlockState state) {
        return ((EnumBlockLiving) state.func_177229_b(TYPE)).getMetadata();
    }

    @Override
    protected BlockState func_180661_e() {
        return new BlockState(this, new IProperty[]{TYPE});
    }
}
