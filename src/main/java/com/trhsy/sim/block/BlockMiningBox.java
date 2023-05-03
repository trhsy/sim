package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenControlGui;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @ClassName BlockMiningBox
 * @Description todo 挖矿箱
 * @Author Tian
 * @Date 2022/9/1921:50
 **/
public class BlockMiningBox extends BlockBase{
    public BlockMiningBox(Material material) {
        super(material,"miningBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_kraft_ddd_mining_constructor_activated"));
        worldIn.playSound(playerIn,pos, soundEvent, SoundCategory.BLOCKS, 1, 1);
        return true;
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
        worldIn.playSound(pos.getX(),pos.getY(),pos.getZ(), soundEvent, SoundCategory.RECORDS, 1, 1,false);
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(new V3(pos))) {
                fd.fire();
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 获取此块可以删除的项的元数据。当块被破坏时调用此方法。它基于块的旧元数据返回被删除项的元数据。
     * @Date 10:04 2022/11/7
     * @Param [state]
     **/
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }


}
