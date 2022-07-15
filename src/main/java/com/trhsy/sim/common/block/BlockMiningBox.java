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
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("miningBox");
        //this.setTextureName(ModSim.MODID + ":" + "mining_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            if (BlockMarker.markers.isEmpty()) {
                String Mining_box_area = I18n.format("container.sim.Mining_box_area");
                ModSimReloaded.sendChat(Mining_box_area);
            } else {
                MiningBox m;
                ModSimReloaded.theMiningBoxes.add(m = new MiningBox(new V3(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.provider.getDimensionId())));
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
            super.onBlockAdded(world, blockPos, iBlockState);
        } catch (Exception e) {
            ModSimReloaded.log.error("出差了：" + e.getMessage());
        }

    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            FolkData theFolk = FolkData.getFolkByEmployedAt(new V3(blockPos.getX(), blockPos.getY(), blockPos.getZ(), world.provider.getDimensionId()));
            if (theFolk != null) {
                theFolk.selfFire();
            }

            MiningBox m = MiningBox.getMiningBlockByBoxXYZ(new V3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            ModSimReloaded.theMiningBoxes.remove(m);
            world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":powerdown", 1.0F, 1.0F);
            super.onBlockDestroyedByPlayer(world, blockPos,iBlockState);
        } catch (Exception e) {
            ModSimReloaded.log.error("出差了：" + e.getMessage());
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
        world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":computer", 1.0F, 1.0F);
        MiningBox miningBlock = MiningBox.getMiningBlockByBoxXYZ(new V3(blockPos.getX(), blockPos.getY(), blockPos.getZ(), thePlayer.dimension));

            miningBlock.location.theDimension = thePlayer.dimension;
            ArrayList<FolkData> folks = FolkData.getFolksByEmployedAt(new V3(blockPos.getX(), blockPos.getY(), blockPos.getZ(), thePlayer.dimension));
            GuiMining ui = new GuiMining(miningBlock, folks);
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(ui);
        } catch (Exception e) {
            ModSimReloaded.log.error(e.getMessage());
            if (world.isRemote) {
                String Mining_box_Sorry = I18n.format("container.sim.Mining_box_Sorry");
                ModSimReloaded.sendChat(Mining_box_Sorry);
            }
        }
        return true;
    }
}
