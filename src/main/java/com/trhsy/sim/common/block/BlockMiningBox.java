package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.MiningBox;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.apache.logging.log4j.Marker;

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
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78026_f);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:blockMining");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return this.icons[0];
    }

    public void func_149726_b(World world, int i, int j, int k) {
        if (BlockMarker.markers.isEmpty()) {
            ModSimukraft.sendChat("You need to place down 3 markers first to mark out the mining area");
        } else {
            MiningBox m;
            ModSimukraft.theMiningBoxes.add(m = new MiningBox(new V3((double)i, (double)j, (double)k, world.field_73011_w.field_76574_g)));
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

            super.func_149726_b(world, i, j, k);
        }
    }

    public void func_149664_b(World world, int i, int j, int k, int meta) {
        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double)i, (double)j, (double)k, world.field_73011_w.field_76574_g));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        MiningBox m = MiningBox.getMiningBlockByBoxXYZ(new V3(i, j, k));
        ModSimukraft.theMiningBoxes.remove(m);
        world.func_72908_a((double)i, (double)j, (double)k, "satscapesimukraft:powerdown", 1.0F, 1.0F);
        super.func_149664_b(world, i, j, k, meta);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.func_72908_a((double)i, (double)j, (double)k, "satscapesimukraft:computer", 1.0F, 1.0F);
        MiningBox miningBlock = MiningBox.getMiningBlockByBoxXYZ(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK));

        try {
            miningBlock.location.theDimension = entityplayer.field_71093_bK;
            ArrayList<FolkData> folks = FolkData.getFolksByEmployedAt(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK));
            GuiMining ui = new GuiMining(miningBlock, folks);
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_147108_a(ui);
        } catch (Exception var14) {
            var14.printStackTrace();
            if (world.field_72995_K) {
                ModSimukraft.sendChat("Sorry, there was a problem with this mining box, try place it again");
            }
        }

        return true;
    }
}
