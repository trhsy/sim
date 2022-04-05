package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.blocks.GuiFarming;
import com.trhsy.sim.common.entity.Marker;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName BlockFarmingBox
 * @Description todo 养殖箱
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:10
 * ========================================
 **/
public class BlockFarmingBox extends Block {
    private IIcon[] icons;

    public BlockFarmingBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("farmingBox");
        this.setTextureName(ModSim.MODID + ":" + "farming_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":farming_box");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double) par2, (double) par3, (double) par4, par1World.provider.dimensionId));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        FarmingBox m = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double) par2, (double) par3, (double) par4, par1World.provider.dimensionId));
        ModSim.theFarmingBoxes.remove(m);
        par1World.playSoundEffect((double) par2, (double) par3, (double) par4, ModSim.MODID + ":powerdown", 1.0F, 1.0F);
        super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        if (BlockMarker.markers.isEmpty()) {
            String farming_box_isEmpty = I18n.format("container.sim.farming_box_isEmpty");
            ModSim.sendChat(farming_box_isEmpty);
        } else if (BlockMarker.markers.size() != 3) {
            String farming_box_size = I18n.format("container.sim.farming_box_size");
            ModSim.sendChat(farming_box_size + BlockMarker.markers.size());
        } else {
            FarmingBox m;
            ModSim.theFarmingBoxes.add(m = new FarmingBox(new V3((double) par2, (double) par3, (double) par4, par1World.provider.dimensionId)));

            try {
                int first = BlockMarker.markers.size() - 3;
                m.marker1XYZ = ((Marker) BlockMarker.markers.get(first)).toV3();
                m.marker2XYZ = ((Marker)BlockMarker.markers.get(first + 1)).toV3();
                m.marker3XYZ = ((Marker)BlockMarker.markers.get(first + 2)).toV3();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            super.onBlockAdded(par1World, par2, par3, par4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);

        try {
            FarmingBox farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double)i, (double)j, (double)k, entityplayer.dimension));
            farmingBlock.location.theDimension = entityplayer.dimension;
            FolkData folk = FolkData.getFolkByEmployedAt(new V3((double)i, (double)j, (double)k, entityplayer.dimension));
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new GuiFarming(farmingBlock, folk));
        } catch (Exception var13) {
            String farming_box_Sorry = I18n.format("container.sim.farming_box_Sorry");
            ModSim.sendChat(farming_box_Sorry);
        }

        return true;
    }
}
