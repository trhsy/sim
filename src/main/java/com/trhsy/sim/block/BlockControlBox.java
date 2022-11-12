package com.trhsy.sim.block;

import com.trhsy.sim.block.enums.EnumControlBox;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenControlGui;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.util.EnumBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
    public List<NpcData> employees = new CopyOnWriteArrayList<>();
    public static final PropertyEnum<EnumControlBox> TYPE = PropertyEnum.create("type", EnumControlBox.class);

    public BlockControlBox() {
        super(Material.WOOD, TYPE, EnumControlBox.class);
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
     * @return void
     * @Author fan
     * @Description //TODO 返回具有相同ID但不同meta的块列表（例如：wood返回4个块）
     * @Date 10:03 2022/11/7
     * @Param [itemIn, tab, list]
     **/
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            for (EnumControlBox enumControlBox : EnumControlBox.values()) {
                list.add(new ItemStack(this, 1, enumControlBox.getMeta()));
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
        return (state.getValue(TYPE)).getMeta();
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
     * @return net.minecraft.block.state.IBlockState
     * @Author fan
     * @Description //TODO 将给定的元数据转换为此块的BlockState
     * @Date 10:05 2022/11/7
     * @Param [meta]
     **/
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumControlBox.fromMeta(meta));
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //客户端
        if (!worldIn.isRemote) {
            V3 vPos = V3.fromBlockPos(pos);
            Building b = ModSimLoader.getBuildingByV3(vPos);
            //ModSimLoader.log.info("建筑: " + b.buildingName + ", " + b.jobType);
            NpcData fd = null;
            for (NpcData f : ModSimLoader.folks) {
                if (f.job != null) {
                    ModSimLoader.log.info(f.job.workPlace.toString() + " vs " + V3.fromBlockPos(pos).toString());
                    if (f.job.workPlace.toString().equals(V3.fromBlockPos(pos).toString())) {
                        ModSimLoader.log.info("他们是相同的");
                        if ("1528.0,3.0,246.0,0".contentEquals("1524.0,3.0,218.0,0")) {
                            ModSimLoader.log.info("这里也是一样的");
                        }

                        fd = f;
                    }
                }
            }

            if (fd == null) {
                for (NpcData f : ModSimLoader.folks) {
                    //有房子
                    if (f.home == b) {
                        if(b!=null){
                            NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos,b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, f.getClientIdentity(),true), (EntityPlayerMP) playerIn);
                        }
                        return true;
                    }
                }
                NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, false), (EntityPlayerMP) playerIn);
            } else {
                NetWorkLoader.net.sendTo(new PacketOpenControlGui(vPos, b.ID.toString(),b.buildingName,b.buildingType,b.jobType,b.author, fd.getClientIdentity(),false), (EntityPlayerMP) playerIn);
            }

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
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        //控制箱销毁，解除所有NPC
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(V3.fromBlockPos(pos))) {
                fd.fire();
            }
        }
    }
}
