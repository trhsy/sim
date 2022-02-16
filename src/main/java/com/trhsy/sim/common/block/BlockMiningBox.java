package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiMining;
import com.trhsy.sim.common.entity.Marker;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.MiningBox;
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

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName BlockMiningBox
 * @Description todo 采矿箱
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:42
 * ========================================
 **/
public class BlockMiningBox extends Block {
    private IIcon[] icons;

    public BlockMiningBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("MiningBox");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":blockMining");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        if (BlockMarker.markers.isEmpty()) {
            String Mining_box_area = I18n.format("container.sim.Mining_box_area");
            ModSim.sendChat(Mining_box_area);
        } else {
            MiningBox m;
            ModSim.theMiningBoxes.add(m = new MiningBox(new V3((double) i, (double) j, (double) k, world.provider.dimensionId)));
            if (BlockMarker.markers.size() == 1) {
                m.marker1XYZ = ((Marker)BlockMarker.markers.get(0)).toV3();
                m.marker2XYZ = null;
                m.marker3XYZ = null;
            } else {
                try {
                    int first = BlockMarker.markers.size() - 3;
                    m.marker1XYZ = ((Marker)BlockMarker.markers.get(first)).toV3();
                    m.marker2XYZ = ((Marker)BlockMarker.markers.get(first + 1)).toV3();
                    m.marker3XYZ = ((Marker)BlockMarker.markers.get(first + 2)).toV3();
                } catch (Exception var7) {
                }
            }

            super.onBlockAdded(world, i, j, k);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int meta) {
        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double) i, (double) j, (double) k, world.provider.dimensionId));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        MiningBox m = MiningBox.getMiningBlockByBoxXYZ(new V3(i, j, k));
        ModSim.theMiningBoxes.remove(m);
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":powerdown", 1.0F, 1.0F);
        super.onBlockDestroyedByPlayer(world, i, j, k, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);
        MiningBox miningBlock = MiningBox.getMiningBlockByBoxXYZ(new V3((double)i, (double)j, (double)k, entityplayer.dimension));

        try {
            miningBlock.location.theDimension = entityplayer.dimension;
            ArrayList<FolkData> folks = FolkData.getFolksByEmployedAt(new V3((double)i, (double)j, (double)k, entityplayer.dimension));
            GuiMining ui = new GuiMining(miningBlock, folks);
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(ui);
        } catch (Exception var14) {
            var14.printStackTrace();
            if (world.isRemote) {
                String Mining_box_Sorry = I18n.format("container.sim.Mining_box_Sorry");
                ModSim.sendChat(Mining_box_Sorry);
            }
        }

        return true;
    }
}
