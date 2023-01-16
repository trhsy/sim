package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenFarmGui;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.FarmBox;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @ClassName BlockFarmingBox
 * @Description todo 农田箱
 * @Author Tian
 * @Date 2022/9/1812:00
 **/
public class BlockFarmingBox extends BlockBase{
    public BlockFarmingBox(Material material) {
        super(material,"farmingBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    /**
     * @Author fan
     * @Description //TODO 激活
     * @Date 12:26 2022/12/10
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     * @return boolean
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_kraft_ddd_farming_constructor_activated"));
        worldIn.playSound(playerIn,pos, soundEvent, SoundCategory.BLOCKS, 1, 1);
        if (!worldIn.isRemote) {
            V3 vPos = V3.fromBlockPos(pos);
            NpcData fd = null;
            for (NpcData f:ModSimLoader.folks){
                if (f.job != null && f.job.workPlace.toString().equals(V3.fromBlockPos(pos).toString())) {
                    fd = f;
                }
            }
            //获取当前农田箱
            FarmBox fBox = ModSimLoader.getFarm(vPos);
            if (fBox == null) {
                //这个养殖箱坏了，请把它打碎重新放好
                ModSimLoader.sendChat(I18n.format("container.sim.gui_Farming_broken"));
                return false;
            }

            if (fd == null) {
                NetWorkLoader.net.sendTo(new PacketOpenFarmGui(fBox.ID, vPos, fBox.facing,fBox.farmType, fBox.x, fBox.z), (EntityPlayerMP)playerIn);
            } else {
                NetWorkLoader.net.sendTo(new PacketOpenFarmGui(fBox.ID, vPos, fBox.facing,fBox.farmType, fBox.x, fBox.z, fd.getClientIdentity()), (EntityPlayerMP)playerIn);
            }
        }
        return true;
    }
    /**
     * @Author fan
     * @Description //TODO 
     * @Date 12:27 2022/12/10
     * @Param [world, pos, state, placer, stack]
     * @return void
     **/
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!world.isRemote) {
            V3 markerPos = null;
            //东
            EnumFacing facing = EnumFacing.EAST;
            int px = (int)Math.floor(placer.posX);
            Math.floor(placer.posY);
            int pz = (int)Math.floor(placer.posZ);
            if (pos.getZ() == pz) {
                if (px < pos.getX()) {
                    facing = EnumFacing.EAST;
                } else {
                    //西
                    facing = EnumFacing.WEST;
                }
            } else if (pos.getX() == px) {
                if (pz < pos.getZ()) {
                    //南
                    facing = EnumFacing.SOUTH;
                } else {
                    //北
                    facing = EnumFacing.NORTH;
                }
            }
            //for (FarmBox farmBox : ModSimLoader.farms) {
            //    if (farmBox.loc.equals(markerPos)) {
            //        farmBox.removeFarm(farmBox.ID);
            //    }
            //}
            FarmBox fb = new FarmBox(V3.fromBlockPos(pos), (V3)markerPos, 6, 6);
            ModSimLoader.farms.add(fb);
            fb.facing = facing;
            fb.saveFarm();
        }

    }
    /**
     * @Author fan
     * @Description //TODO 玩家摧毁方块
     * @Date 17:34 2022/11/1
     * @Param [worldIn, pos, state]
     * @return void
     **/
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state){
        //在给定块位置的中心为播放器播放指定的声音 断电 power down
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":power_down"));
        worldIn.playSound(worldIn.playerEntities.get(0),pos, soundEvent, SoundCategory.BLOCKS, 1, 1);
        for (FarmBox farmBox : ModSimLoader.farms) {
            if (farmBox.loc.equals(pos)) {
                farmBox.removeFarm(farmBox.ID);
            }
        }
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(V3.fromBlockPos(pos))) {
                fd.fire();
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
}
