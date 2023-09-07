package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketAddNewMarker;
import com.trhsy.sim.network.client.PacketOpenMarkerGui;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.Marker;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @ClassName BlockMarker
 * @Description todo
 * @Author Tian 标记棒
 * @Date 2022/9/1921:17
 **/
public class BlockMarker extends BlockBase{
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1, 0.625D);
    public BlockMarker(Material material) {
        super(material,"markerBar");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
//        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setLightLevel(0.1F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CARPET_AABB;
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.8125D, 0.6875D);
    }

    public AxisAlignedBB getSelectedBoundingBoxAlignedBB(IBlockState state, World worldIn, BlockPos pos) {
        return (new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)).offset(pos);
    }
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!world.isRemote) {
            NetWorkLoader.net.sendTo(new PacketAddNewMarker(pos), (EntityPlayerMP)placer);
        }
    }
    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键
     * @Date 11:39 2022/11/7
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":computer"));
        worldIn.playSound(playerIn,pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!worldIn.isRemote) {
            V3 vPos = new V3(pos, playerIn.dimension);
            NetWorkLoader.net.sendTo(new PacketOpenMarkerGui(vPos,playerIn.dimension), (EntityPlayerMP) playerIn);
        }
        return true;
    }

    /**
     * 销毁时
     * @param worldIn
     * @param pos
     * @param state
     */
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state){
        for (Marker marker :ModSimClientLoader.markers) {
            V3 vPos = new V3(pos);
            if(vPos.equals(marker.loc)){
                ModSimClientLoader.markers.remove(marker);
            }
        }
    }
}
