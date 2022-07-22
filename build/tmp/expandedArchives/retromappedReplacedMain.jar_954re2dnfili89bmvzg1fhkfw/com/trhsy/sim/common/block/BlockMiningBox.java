package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.entity.functionality.MiningBox;
import com.trhsy.sim.common.gui.blocks.GuiMining;
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

import java.util.ArrayList;

/**
 * @ClassName BlockMiningBox
 * @Description todo 挖矿箱
 * @Author Tian
 * @Date 2022/5/523:27
 **/
public class BlockMiningBox extends Block {
    public BlockMiningBox() {
        super(Material.field_151575_d);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(2.0F);
        this.func_149752_b(1.0F);
        this.func_149663_c("miningBox");
        //this.setTextureName(ModSim.MODID + ":" + "mining_box");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }

    @Override
    public void func_176213_c(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            if (BlockMarker.markers.isEmpty()) {
                String Mining_box_area = I18n.func_135052_a("container.sim.Mining_box_area");
                ModSimReloaded.sendChat(Mining_box_area);
            } else {
                MiningBox m;
                ModSimReloaded.theMiningBoxes.add(m = new MiningBox(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), world.field_73011_w.func_177502_q())));
                if (BlockMarker.markers.size() == 1) {
                    m.marker1XYZ = ((Marker) BlockMarker.markers.get(0)).toV3();
                    m.marker2XYZ = null;
                    m.marker3XYZ = null;
                } else {
                    try {
                        int first = BlockMarker.markers.size() - 3;
                        m.marker1XYZ = ((Marker) BlockMarker.markers.get(first)).toV3();
                        m.marker2XYZ = ((Marker) BlockMarker.markers.get(first + 1)).toV3();
                        m.marker3XYZ = ((Marker) BlockMarker.markers.get(first + 2)).toV3();
                    } catch (Exception var7) {
                    }
                }
            }
            super.func_176213_c(world, blockPos, iBlockState);
        } catch (Exception e) {
            ModSimReloaded.log.error("挖矿箱onBlockAdded出错了：" + e.getMessage());
        }

    }

    @Override
    public void func_176206_d(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            FolkData theFolk = FolkData.getFolkByEmployedAt(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), world.field_73011_w.func_177502_q()));
            if (theFolk != null) {
                theFolk.selfFire();
            }

            MiningBox m = MiningBox.getMiningBlockByBoxXYZ(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()));
            ModSimReloaded.theMiningBoxes.remove(m);
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":powerdown", 1.0F, 1.0F);
            super.func_176206_d(world, blockPos,iBlockState);
        } catch (Exception e) {
            ModSimReloaded.log.error("挖矿箱onBlockDestroyedByPlayer出错了：" + e.getMessage());
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
        world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":computer", 1.0F, 1.0F);
        MiningBox miningBlock = MiningBox.getMiningBlockByBoxXYZ(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), thePlayer.field_71093_bK));

            miningBlock.location.theDimension = thePlayer.field_71093_bK;
            ArrayList<FolkData> folks = FolkData.getFolksByEmployedAt(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), thePlayer.field_71093_bK));
            GuiMining ui = new GuiMining(miningBlock, folks);
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_147108_a(ui);
        } catch (Exception e) {
            ModSimReloaded.log.error(e.getMessage());
            if (world.field_72995_K) {
                String Mining_box_Sorry = I18n.func_135052_a("container.sim.Mining_box_Sorry");
                ModSimReloaded.sendChat(Mining_box_Sorry);
            }
        }
        return true;
    }
}
