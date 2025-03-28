package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenConstructorGui;
import com.trhsy.sim.network.client.PacketOpenSetupGui;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockConstructorBox
 * @Description: 建筑箱
 * @date 2023/10/31 上午 10:16
 */
public class BlockConstructorBox extends BlockBase {
    public NpcData employee;

    public BlockConstructorBox() {
        super(Material.WOOD, "constructorBox");
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //方块硬度
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    /**
     * 放置时
     */

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_building_constructor_activated"));
        worldIn.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键 激活
     * @Date 16:46 2022/10/19
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":sim_u_ddd"));
        worldIn.playSound(playerIn, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);

        int buildDirection = 0;
        if (!worldIn.isRemote) {
            if (ModSimLoader.gamemode == 999) {
                NetWorkLoader.net.sendTo(new PacketOpenSetupGui(), (EntityPlayerMP) playerIn);
                return true;
            }

            int px = (int) Math.floor(playerIn.posX);
            Math.floor(playerIn.posY);
            int pz = (int) Math.floor(playerIn.posZ);
            if (pos.getZ() == pz) {
                if (px < pos.getX()) {
                    buildDirection = 1;
                } else {
                    buildDirection = 3;
                }
            } else if (pos.getX() == px) {
                if (pz < pos.getZ()) {
                    buildDirection = 2;
                } else {
                    buildDirection = 0;
                }
            }

            ModSimLoader.log.info("建筑方向为 " + buildDirection);
            NpcData fd = null;
            for (NpcData f : ModSimLoader.folks) {
                //建筑师
                String v1 = new TextComponentTranslation("container.sim.Vocation1", new Object[0]).getUnformattedText();
                String v2 = new TextComponentTranslation("container.sim.Vocation16", new Object[0]).getUnformattedText();
                if (f.job != null) {
                    V3 v3 = new V3(f.job.workPlace.x, f.job.workPlace.y - 1, f.job.workPlace.z);
                    V3 v31 = V3.fromBlockPos(pos);
                    if ((f.job.jobName.contentEquals(v1) || f.job.jobName.contentEquals(v2)) && v3.equals(v31)) {
                        fd = f;
                        break;
                    }
                }
            }

            if (fd != null) {
               /* if (!fd.job.workPlace.toString().contentEquals(new V3(pos, playerIn.dimension).toString())) {
                    NetWorkLoader.net.sendTo(new PacketOpenConstructorGui(pos, buildDirection, playerIn.dimension), (EntityPlayerMP) playerIn);
                } else {*/
                NetWorkLoader.net.sendTo(new PacketOpenConstructorGui(pos, buildDirection, fd.getClientIdentity(), playerIn.dimension), (EntityPlayerMP) playerIn);
//                }
            } else {
                NetWorkLoader.net.sendTo(new PacketOpenConstructorGui(pos, buildDirection, playerIn.dimension), (EntityPlayerMP) playerIn);
            }
        }

        return true;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 玩家摧毁方块
     * @Date 17:34 2022/11/1
     * @Param [worldIn, pos, state]
     **/
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        //在给定块位置的中心为播放器播放指定的声音 断电 power down
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":power_down"));
        worldIn.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        for (NpcData fd : ModSimLoader.folks) {
            if (fd.job != null && fd.job.workPlace.equals(new V3(pos))) {
                fd.fire();
            }
        }
        //预览位置
        ModSimClientLoader.previewPos1 = null;
        ModSimClientLoader.previewPos2 = null;
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    }
}
