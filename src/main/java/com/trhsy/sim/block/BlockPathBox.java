package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenPathBoxGui;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.util.Courier;
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
 * @ClassName BlockPathBox
 * @Description todo 路径箱
 * @Author Tian
 * @Date 2022/9/1921:53
 **/
public class BlockPathBox extends BlockBase{
    public BlockPathBox(Material material) {
        super(material,"pathBox");
        this.setSoundType(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,  EnumFacing side, float hitX, float hitY, float hitZ) {
        try {
            //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
            SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":computer"));
            worldIn.playSound(playerIn,pos,soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isRemote) {
                NetWorkLoader.net.sendTo(new PacketOpenPathBoxGui(new V3(pos, playerIn.dimension)), (EntityPlayerMP) playerIn);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("路径箱BlockPathBox-onBlockActivated出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }
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
        worldIn.playSound(null,pos, soundEvent, SoundCategory.RECORDS, 1.0F, 1.0F);
        for (Courier courier : ModSimLoader.theCourierPoints) {
            if (courier.loc.equals(pos)) {
                courier.removeFarm(courier.ID);
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
    /**
     * 掉落数量为0，方块敲了就消失
     * @param state
     * @return
     */
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
}
