package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiMarker;
import com.trhsy.sim.common.entity.EntityAlignBeam;
import com.trhsy.sim.common.entity.Marker;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

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
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("MarkerBar");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setLightLevel(0.1F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":blockMarker");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int meta) {
        try {
            for (int m = 0; m < markers.size(); ++m) {
                Marker marker = (Marker) markers.get(m);

                for (int mm = 0; mm < 4; ++mm) {
                    try {
                        ((EntityAlignBeam) marker.beams.get(mm)).setDead();
                    } catch (Exception var10) {
                    }
                }
            }
        } catch (Exception var11) {
        }

        markers.clear();
        super.onBlockDestroyedByPlayer(world, i, j, k, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase player, ItemStack is) {
        hasPlaced = true;
        if (world.isRemote) {
            Marker ma;
            markers.add(ma = new Marker(i, j, k, world.provider.dimensionId));
            String markerCaption = "";
            String helpText = "";
            if (markers.size() == 1) {
                markerCaption = "Front-Left";
                helpText = I18n.format("container.sim.box_Marker_left");
                ModSim.log.info(markers.size());
            } else if (markers.size() == 2) {
                markerCaption = "Front-Right";
                helpText = I18n.format("container.sim.box_Marker_right");
                ModSim.log.info(markers.size());
            } else if (markers.size() == 3) {
                markerCaption = "Rear-Left";
                helpText = I18n.format("container.sim.box_Marker_Rear_Left");
                ModSim.log.info(markers.size());
            } else {
                ModSim.log.info(markers.size());
                markerCaption = I18n.format("container.sim.box_Marker_Markers");
            }

            if (markers.size() < 4) {
                V3 pos = new V3((double) i, (double) j, (double) k, world.provider.dimensionId);
                pos.y = pos.y + 0.01D;
                if (ModSim.configEnableMarkerAlignmentBeams) {
                    EntityAlignBeam beam = new EntityAlignBeam(world);
                    ma.caption = markerCaption;
                    beam.setLocationAndAngles(pos.x, pos.y, pos.z, 0.0F, 0.0F);
                    beam.yaw = 0.0F;
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(beam);
                    }

                    ma.beams.add(beam);
                    EntityAlignBeam beam2 = new EntityAlignBeam(world);
                    beam2.setLocationAndAngles(pos.x, pos.y, pos.z, 90.0F, 0.0F);
                    beam2.yaw = 90.0F;
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(beam2);
                    }

                    ma.beams.add(beam2);
                    EntityAlignBeam beam3 = new EntityAlignBeam(world);
                    beam3.setLocationAndAngles(pos.x, pos.y, pos.z, 180.0F, 0.0F);
                    beam3.yaw = 180.0F;
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(beam3);
                    }

                    ma.beams.add(beam3);
                    EntityAlignBeam beam4 = new EntityAlignBeam(world);
                    beam4.setLocationAndAngles(pos.x, pos.y, pos.z, 270.0F, 0.0F);
                    beam4.yaw = 270.0F;
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(beam4);
                    }

                    ma.beams.add(beam4);
                }
            }

            if (!helpText.contentEquals("")) {
                ModSim.sendChat(helpText);
            }

            super.onBlockPlacedBy(world, i, j, k, player, is);
        }

    }

    public static Marker getMarker(V3 position) {
        Marker ret = null;

        for (int i = 0; i < markers.size(); ++i) {
            Marker m = (Marker) markers.get(i);
            if ((double) m.x == position.x && (double) m.y == position.y && (double) m.z == position.z) {
                ret = m;
                break;
            }
        }

        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        this.location = new V3((double) i, (double) j, (double) k, entityplayer.dimension);
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);
        GuiMarker ui = new GuiMarker(this.location, entityplayer);
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(ui);
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        // TODO document why this method is empty
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
    }

    @Override
    public void init(Entity entity, World world) {
    }
}