package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketAddNewMarker;
import com.trhsy.sim.network.client.PacketOpenMarkerGui;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.block.Marker;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMarker
 * @Description: 标记棒
 * @date 2023/11/08 上午 10:34
 */
public class BlockMarker extends Block {
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1, 0.625D);
    public BlockMarker() {
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
//        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setUnlocalizedName("markerBar");
        this.setLightLevel(0.1F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CARPET_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.8125D, 0.6875D);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return (new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)).offset(pos);
    }
    /**
     * 用于在重建块以进行渲染时确定环境光遮挡和剔除
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

    /**
     * 方块被放置
     * @param world
     * @param pos
     * @param state
     * @param placer
     * @param stack
     */
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand , EnumFacing side, float hitX, float hitY, float hitZ) {
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
        for (Marker marker : ModSimClientLoader.markers) {
            V3 vPos = new V3(pos);
            if(vPos.equals(marker.loc)){
                ModSimClientLoader.markers.remove(marker);
            }
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    }
}
