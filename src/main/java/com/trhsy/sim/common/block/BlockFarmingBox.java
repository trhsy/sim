package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.entity.functionality.Marker;
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
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("farmingBox");
        //this.setTextureName(ModSim.MODID + ":" + "farming_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * 在被玩家摧毁的时候
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double) par2, (double) par3, (double) par4, world.provider.dimensionId));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        FarmingBox m = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double) par2, (double) par3, (double) par4, world.provider.dimensionId));
        ModSimReloaded.theFarmingBoxes.remove(m);
        world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":powerdown", 1.0F, 1.0F);
        super.onBlockDestroyedByPlayer(world, blockPos,iBlockState);
    }

    /**
     * 方块被激活
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (BlockMarker.markers.isEmpty()) {
            String farming_box_isEmpty = I18n.format("container.sim.farming_box_isEmpty");
            ModSimReloaded.sendChat(farming_box_isEmpty);
        } else if (BlockMarker.markers.size() != 3) {
            String farming_box_size = I18n.format("container.sim.farming_box_size");
            ModSimReloaded.sendChat(farming_box_size + BlockMarker.markers.size());
        } else {
            FarmingBox m;
            ModSimReloaded.theFarmingBoxes.add(m = new FarmingBox(new V3((double) par2, (double) par3, (double) par4, world.provider.dimensionId)));

            try {
                int first = BlockMarker.markers.size() - 3;
                m.marker1XYZ = ((Marker) BlockMarker.markers.get(first)).toV3();
                m.marker2XYZ = ((Marker)BlockMarker.markers.get(first + 1)).toV3();
                m.marker3XYZ = ((Marker)BlockMarker.markers.get(first + 2)).toV3();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            super.onBlockAdded(world, blockPos, iBlockState);
    }

    /**
     * 当右键方块时
     * @param world
     * @param blockPos
     * @param iBlockState
     * @param thePlayer
     * @param enumFacing
     * @param par7
     * @param par8
     * @param par9
     * @return
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":computer", 1.0F, 1.0F);

        try {
            FarmingBox farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double)i, (double)j, (double)k, entityplayer.dimension));
            farmingBlock.location.theDimension = entityplayer.dimension;
            FolkData folk = FolkData.getFolkByEmployedAt(new V3((double)i, (double)j, (double)k, entityplayer.dimension));
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new GuiFarming(farmingBlock, folk));
        } catch (Exception var13) {
            String farming_box_Sorry = I18n.format("container.sim.farming_box_Sorry");
            ModSimReloaded.sendChat(farming_box_Sorry);
        }

        return true;
    }
}
