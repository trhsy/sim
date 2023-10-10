package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.enums.EnumControlBox;
import com.trhsy.sim.block.enums.EnumLightColour;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenControlGui;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.util.EnumBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * 控制箱
 */
public class BlockControlBox extends EnumBlock<EnumControlBox> {
    //
    public List<NpcData> employees = new CopyOnWriteArrayList<NpcData>();
    public static final PropertyEnum<EnumControlBox> TYPE = PropertyEnum.create("type", EnumControlBox.class);

    public BlockControlBox() {
        super(Material.WOOD,TYPE,EnumControlBox.class);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //方块硬度
        this.setHardness(10.0F);
        //爆炸
        this.setResistance(1);
        this.setUnlocalizedName("controlBox");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumControlBox.TOP));
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
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
    /**
     * @return void
     * @Author fan
     * @Description //TODO 返回具有相同ID但不同meta的块列表（例如：wood返回4个块）
     * @Date 10:03 2022/11/7
     * @Param [itemIn, tab, list]
     **/
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        try {
            for (EnumControlBox enumControlBox : EnumControlBox.values()) {
                list.add(new ItemStack(itemIn, 1, enumControlBox.getMetadata()));
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("控制箱getSubBlocks出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 将BlockState转换为正确的元数据值
     * @Date 10:04 2022/11/7
     * @Param [state]
     **/
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(TYPE)).getMetadata();
    }
    /**
     * @return net.minecraft.block.state.IBlockState
     * @Author fan
     * @Description //TODO 将给定的元数据转换为此块的BlockState
     * @Date 10:05 2022/11/7
     * @Param [meta]
     **/
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumControlBox.byMetadata(meta));
    }


    /**
     * @return net.minecraft.block.state.BlockStateContainer
     * @Author fan
     * @Description //TODO
     * @Date 10:05 2022/11/7
     * @Param []
     **/
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{TYPE});
    }


    /**
     * 销毁时要丢弃的项目数量
     *
     * @param random
     * @return
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键
     * @Date 11:39 2022/11/7
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        //在给定块位置的中心为播放器播放指定的声音 computer 控制箱激活
        SoundEvent soundEvent=new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_ddd"));
        worldIn.playSound(playerIn,pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        //客户端
        if (!worldIn.isRemote) {
            //获取控制箱位置
            V3 vPos = new V3(pos,playerIn.dimension);
            //获取该位置的建筑
            Building b = ModSimLoader.getBuildingByV3(vPos);
            List<NpcData> occupants=new CopyOnWriteArrayList<NpcData>();
            //ModSimLoader.log.info("建筑: " + b.buildingName + ", " + b.jobType);
            /*
            NpcData fd = null;
            for (NpcData f : ModSimLoader.folks) {
                if (f.job != null) {
                    ModSimLoader.log.info(f.job.workPlace.toString() + " vs " + vPos.toString());
                    if (f.job.workPlace.toString().equals(vPos.toString())) {
                        fd = f;
                    }
                }
            }*/
            if(b!=null){
               /* if(b.livingXYZ!=null){
                    vPos= b.livingXYZ;
                }else*/ if(b.controlXYZ!=null){
                    vPos=b.controlXYZ;
                }
                //存在则查询其下拥有者/员工
                occupants=b.occupants;
                if(occupants!=null&&occupants.size()>0){
                    //建筑是住宅
                    if(b.buildingType.equals(I18n.format("container.sim.sim_gui_BC_Residential"))){
                        NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos,b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, occupants,true), (EntityPlayerMP) playerIn);
                    }else{
                        NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, occupants,false), (EntityPlayerMP) playerIn);
                    }
                }else{
                    //没有员工
                    NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, occupants,false), (EntityPlayerMP) playerIn);
                }
            }else{
                //建筑等于空不存在提示错误信息
                NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, "","","","","", false), (EntityPlayerMP) playerIn);
            }
            /*if (fd == null) {
                for (NpcData f : ModSimLoader.folks) {
                    //有房子
                    if (f.home == b) {
                        if(b!=null){

                        }
                        return true;
                    }
                }
                if(b!=null){
                    NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, false), (EntityPlayerMP) playerIn);
                }
            } else {

            }*/

        }

        return true;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 玩家销毁时
     * @Date 10:50 2022/11/7
     * @Param [worldIn, pos, state]
     **/
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        //控制箱销毁，解除所有NPC
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(new V3(pos))) {
                fd.fire();
            }
        }
    }
}
