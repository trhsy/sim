package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.functionality.FarmingBox;
import com.trhsy.sim.common.core.entity.functionality.Marker;
import com.trhsy.sim.client.gui.blocks.GuiFarming;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @ClassName BlockFarmingBox
 * @Description todo 养殖箱
 * @Author Tian
 * @Date 2022/5/49:51
 **/
public class BlockFarmingBox extends Block {
    public BlockFarmingBox() {
        super(Material.field_151575_d);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(2.0F);
        this.func_149752_b(1);
        this.func_149663_c("farmingBox");
        //this.setTextureName(ModSim.MODID + ":" + "farming_box");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }

    /**
     * 在被玩家摧毁的时候
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void func_176206_d(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            FolkData theFolk = FolkData.getFolkByEmployedAt(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), world.field_73011_w.func_177502_q()));
            if (theFolk != null) {
                theFolk.selfFire();
            }

            FarmingBox m = FarmingBox.getFarmingBlockByBoxXYZ(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), world.field_73011_w.func_177502_q()));
            ModSimReloaded.theFarmingBoxes.remove(m);
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":powerdown", 1, 1);
            super.func_176206_d(world, blockPos,iBlockState);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onBlockDestroyedByPlayer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 方块被激活
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void func_176213_c(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            if (BlockMarker.markers.isEmpty()) {
                String farming_box_isEmpty = I18n.func_135052_a("container.sim.farming_box_isEmpty");
                ModSimReloaded.sendChat(farming_box_isEmpty);
            } else if (BlockMarker.markers.size() != 3) {
                String farming_box_size = I18n.func_135052_a("container.sim.farming_box_size");
                ModSimReloaded.sendChat(farming_box_size + BlockMarker.markers.size());
            } else {
                FarmingBox m;
                ModSimReloaded.theFarmingBoxes.add(m = new FarmingBox(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), world.field_73011_w.func_177502_q())));

                try {
                    int first = BlockMarker.markers.size() - 3;
                    m.marker1XYZ = ((Marker) BlockMarker.markers.get(first)).toV3();
                    m.marker2XYZ = ((Marker) BlockMarker.markers.get(first + 1)).toV3();
                    m.marker3XYZ = ((Marker) BlockMarker.markers.get(first + 2)).toV3();
                } catch (Exception e) {
                    //var7.printStackTrace();
                }

            }
            super.func_176213_c(world, blockPos, iBlockState);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 当右键方块时
     * @param world
     * @param blockPos
     * @param iBlockState
     * @param entityplayer
     * @param enumFacing
     * @param par7
     * @param par8
     * @param par9
     * @return
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityplayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":computer", 1, 1);

                FarmingBox farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), entityplayer.field_71093_bK));
                if(farmingBlock!=null){
                    farmingBlock.location.theDimension = entityplayer.field_71093_bK;
                    FolkData folk = FolkData.getFolkByEmployedAt(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), entityplayer.field_71093_bK));
                    Minecraft mc = Minecraft.func_71410_x();
                    mc.func_147108_a(new GuiFarming(farmingBlock, folk));
                }else{
                    String farming_box_Sorry = I18n.func_135052_a("container.sim.farming_box_Sorry");
                    ModSimReloaded.sendChat(farming_box_Sorry);
                }


        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }
        return true;
    }
}
