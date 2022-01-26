package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import org.apache.logging.log4j.Marker;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName BlockMarker
 * @Description todo 标记棒
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:41
 * ========================================
 **/
public class BlockMarker extends Block implements IExtendedEntityProperties {
    public static boolean hasPlaced = false;
    public static ArrayList<Marker> markers = new ArrayList();
    public V3 location;
    private IIcon[] icons;

    public BlockMarker() {
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78026_f);
        this.func_149676_a(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.func_149715_a(0.1F);
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:blockMarker");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        return this.icons[0];
    }

    public void func_149683_g() {
        this.func_149676_a(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
    }

    public boolean func_149686_d() {
        return false;
    }

    public boolean func_149662_c() {
        return false;
    }

    public void func_149664_b(World world, int i, int j, int k, int meta) {
        try {
            for(int m = 0; m < markers.size(); ++m) {
                Marker marker = (Marker)markers.get(m);

                for(int mm = 0; mm < 4; ++mm) {
                    try {
                        ((EntityAlignBeam)marker.beams.get(mm)).func_70106_y();
                    } catch (Exception var10) {
                    }
                }
            }
        } catch (Exception var11) {
        }

        markers.clear();
        super.func_149664_b(world, i, j, k, meta);
    }

    public void func_149689_a(World world, int i, int j, int k, EntityLivingBase player, ItemStack is) {
        hasPlaced = true;
        if (world.field_72995_K) {
            Marker ma;
            markers.add(ma = new Marker(i, j, k, world.field_73011_w.field_76574_g));
            String markerCaption = "";
            String helpText = "";
            if (markers.size() == 1) {
                markerCaption = "Front-Left";
                helpText = "You can place two more markers to mark out an area for a farm or mine etc. If you wish to do this, place another marker at the front-right position now";
                System.out.println(markers.size());
            } else if (markers.size() == 2) {
                markerCaption = "Front-Right";
                helpText = "Finally, place a marker at the Rear-Left position";
                System.out.println(markers.size());
            } else if (markers.size() == 3) {
                markerCaption = "Rear-Left";
                helpText = "You're done, now you can place down a mining box, farming box or right-click the front-left marker to copy a structure!";
                System.out.println(markers.size());
            } else {
                System.out.println(markers.size());
                markerCaption = "Too many Markers!";
            }

            if (markers.size() < 4) {
                V3 pos = new V3((double)i, (double)j, (double)k, world.field_73011_w.field_76574_g);
                pos.y = pos.y + 0.01D;
                if (ModSimukraft.configEnableMarkerAlignmentBeams) {
                    EntityAlignBeam beam = new EntityAlignBeam(world);
                    ma.caption = markerCaption;
                    beam.func_70012_b(pos.x, pos.y, pos.z, 0.0F, 0.0F);
                    beam.yaw = 0.0F;
                    if (!world.field_72995_K) {
                        world.func_72838_d(beam);
                    }

                    ma.beams.add(beam);
                    EntityAlignBeam beam2 = new EntityAlignBeam(world);
                    beam2.func_70012_b(pos.x, pos.y, pos.z, 90.0F, 0.0F);
                    beam2.yaw = 90.0F;
                    if (!world.field_72995_K) {
                        world.func_72838_d(beam2);
                    }

                    ma.beams.add(beam2);
                    EntityAlignBeam beam3 = new EntityAlignBeam(world);
                    beam3.func_70012_b(pos.x, pos.y, pos.z, 180.0F, 0.0F);
                    beam3.yaw = 180.0F;
                    if (!world.field_72995_K) {
                        world.func_72838_d(beam3);
                    }

                    ma.beams.add(beam3);
                    EntityAlignBeam beam4 = new EntityAlignBeam(world);
                    beam4.func_70012_b(pos.x, pos.y, pos.z, 270.0F, 0.0F);
                    beam4.yaw = 270.0F;
                    if (!world.field_72995_K) {
                        world.func_72838_d(beam4);
                    }

                    ma.beams.add(beam4);
                }
            }

            if (!helpText.contentEquals("")) {
                ModSimukraft.sendChat(helpText);
            }

            super.func_149689_a(world, i, j, k, player, is);
        }

    }

    public static Marker getMarker(V3 position) {
        Marker ret = null;

        for(int i = 0; i < markers.size(); ++i) {
            Marker m = (Marker)markers.get(i);
            if ((double)m.x == position.x && (double)m.y == position.y && (double)m.z == position.z) {
                ret = m;
                break;
            }
        }

        return ret;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149727_a(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        this.location = new V3((double)i, (double)j, (double)k, entityplayer.field_71093_bK);
        world.func_72908_a((double)i, (double)j, (double)k, "satscapesimukraft:computer", 1.0F, 1.0F);
        GuiMarker ui = new GuiMarker(this.location, entityplayer);
        Minecraft mc = Minecraft.func_71410_x();
        mc.func_147108_a(ui);
        return true;
    }

    public void saveNBTData(NBTTagCompound compound) {
    }

    public void loadNBTData(NBTTagCompound compound) {
    }

    public void init(Entity entity, World world) {
    }
}