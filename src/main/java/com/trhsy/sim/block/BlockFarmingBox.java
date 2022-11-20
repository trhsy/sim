package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_kraft_ddd_farming_constructor_activated"));
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
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state){
        //在给定块位置的中心为播放器播放指定的声音 断电 power down
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":power_down"));
        worldIn.playSound(worldIn.playerEntities.get(0),pos, soundEvent, SoundCategory.BLOCKS, 1, 1);
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(V3.fromBlockPos(pos))) {
                fd.fire();
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
}
