package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenPathBoxGui;
import com.trhsy.sim.network.client.PacketOpenWindmillGui;
import com.trhsy.sim.npc.V3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockWindmill extends BlockBase {
    public BlockWindmill(Material material) {
        super(material, "windmill");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
        //this.setTextureName(ModSim.MODID + ":" + "block_windmill");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键激活
     * @Date 11:16 2023/8/20
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     **/
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        try {
            //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
//            SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":computer"));
//            worldIn.playSound(playerIn,pos,soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isRemote) {
                NetWorkLoader.net.sendTo(new PacketOpenWindmillGui(playerIn,new V3(pos, playerIn.dimension)), (EntityPlayerMP) playerIn);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("路径箱onBlockActivated出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
        return true;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 放置
     * @Date 11:16 2023/8/20
     * @Param [world, pos, state, placer, stack]
     **/
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
//        ModSimLoader.loadAllBuildings();

    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
}
