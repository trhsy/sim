package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
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
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78026_f);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:blockFarming");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return this.icons[0];
    }

    public void func_149664_b(World par1World, int par2, int par3, int par4, int par5) {
        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double)par2, (double)par3, (double)par4, par1World.field_73011_w.field_76574_g));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        FarmingBox m = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double)par2, (double)par3, (double)par4, par1World.field_73011_w.field_76574_g));
        ModSimukraft.theFarmingBoxes.remove(m);
        par1World.func_72908_a((double)par2, (double)par3, (double)par4, "satscapesimukraft:powerdown", 1.0F, 1.0F);
        super.func_149664_b(par1World, par2, par3, par4, par5);
    }

    public void func_149726_b(World par1World, int par2, int par3, int par4) {
        if (BlockMarker.markers.isEmpty()) {
            ModSimukraft.sendChat("You need to place down 3 markers first to mark out the farming area");
        } else if (BlockMarker.markers.size() != 3) {
            ModSimukraft.sendChat("You need to place down 3 markers, not " + BlockMarker.markers.size());
        } else {
            FarmingBox m;
            ModSimukraft.theFarmingBoxes.add(m = new FarmingBox(new V3((double)par2, (double)par3, (double)par4, par1World.field_73011_w.field_76574_g)));

            try {
                int first = BlockMarker.markers.size() - 3;
                m.marker1XYZ = ((Marker)BlockMarker.markers.get(first)).toV3();
                m.marker2XYZ = ((Marker)BlockMarker.markers.get(first + 1)).toV3();
                m.marker3XYZ = ((Marker)BlockMarker.markers.get(first + 2)).toV3();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            super.func_149726_b(par1World, par2, par3, par4);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.func_72908_a((double)i, (double)j, (double)k, "satscapesimukraft:computer", 1.0F, 1.0F);

        try {
            FarmingBox farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK));
            farmingBlock.location.theDimension = entityplayer.field_71093_bK;
            FolkData folk = FolkData.getFolkByEmployedAt(new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK));
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_147108_a(new GuiFarming(farmingBlock, folk));
        } catch (Exception var13) {
            ModSimukraft.sendChat("Sorry, there was a problem with this farming box, try replacing it.");
        }

        return true;
    }
}
