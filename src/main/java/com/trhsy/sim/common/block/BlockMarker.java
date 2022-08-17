package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.EntityAlignBeam;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.functionality.Marker;
import com.trhsy.sim.client.gui.blocks.GuiMarker;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName BlockMarker
 * @Description todo 标记棒
 * @Author Tian
 * @Date 2022/5/523:22
 **/
public class BlockMarker extends Block implements IExtendedEntityProperties {
    public static boolean hasPlaced = false;
    public static List<Marker> markers = new CopyOnWriteArrayList();
    public V3 location;

    public BlockMarker() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setUnlocalizedName("markerBar");
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setLightLevel(0.1F);
        //this.setTextureName(ModSim.MODID + ":" + "marker_bar_block");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    /**
     * 为项目渲染设置块边界
     */
    @Override
    public void setBlockBoundsForItemRender() {
        try {
            this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("标记棒setBlockBoundsForItemRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 是否不透明立方体
     * @return
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     *是否不普通方块
     * @return
     */
    @Override
    public boolean isNormalCube() {
        return false;
    }
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            for (int m = 0; m < markers.size(); ++m) {
                Marker marker = (Marker) markers.get(m);

                for (int mm = 0; mm < 4; ++mm) {
                    try {
                        ((EntityAlignBeam) marker.beams.get(mm)).setDead();
                    } catch (Exception e) {
                    }
                }
            }
            markers.clear();
            super.onBlockDestroyedByPlayer(world,blockPos,iBlockState);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("标记棒onBlockDestroyedByPlayer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos,IBlockState iBlockState, EntityLivingBase player, ItemStack is) {
        try {
            hasPlaced = true;
            if (world.isRemote) {
                Marker ma;
                markers.add(ma = new Marker(blockPos.getX(),blockPos.getY(),blockPos.getZ(), world.provider.getDimensionId()));
                String markerCaption = "";
                String helpText = "";
                if (markers.size() == 1) {
                    markerCaption = "Front-Left";
                    helpText = I18n.format("container.sim.box_Marker_left");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else if (markers.size() == 2) {
                    markerCaption = "Front-Right";
                    helpText = I18n.format("container.sim.box_Marker_right");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else if (markers.size() == 3) {
                    markerCaption = "Rear-Left";
                    helpText = I18n.format("container.sim.box_Marker_Rear_Left");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else {
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                    markerCaption = I18n.format("container.sim.box_Marker_Markers");
                }

                if (markers.size() < 4) {
                    V3 pos = new V3(blockPos.getX(),blockPos.getY()+1,blockPos.getZ(), world.provider.getDimensionId());
                    if (ConfigLoader.configEnableMarkerAlignmentBeams) {
                        EntityAlignBeam beam = new EntityAlignBeam(world);
                        ma.caption = markerCaption;
                        beam.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, 0.0F, 0.0F);
                        beam.yaw = 0.0F;
                        if (!world.isRemote) {
                            world.spawnEntityInWorld(beam);
                        }

                        ma.beams.add(beam);
                        EntityAlignBeam beam2 = new EntityAlignBeam(world);
                        beam2.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, 90.0F, 0.0F);
                        beam2.yaw = 90.0F;
                        if (!world.isRemote) {
                            world.spawnEntityInWorld(beam2);
                        }

                        ma.beams.add(beam2);
                        EntityAlignBeam beam3 = new EntityAlignBeam(world);
                        beam3.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, 180.0F, 0.0F);
                        beam3.yaw = 180.0F;
                        if (!world.isRemote) {
                            world.spawnEntityInWorld(beam3);
                        }

                        ma.beams.add(beam3);
                        EntityAlignBeam beam4 = new EntityAlignBeam(world);
                        beam4.setLocationAndAngles(pos.xCoord, pos.yCoord, pos.zCoord, 270.0F, 0.0F);
                        beam4.yaw = 270.0F;
                        if (!world.isRemote) {
                            world.spawnEntityInWorld(beam4);
                        }

                        ma.beams.add(beam4);
                    }
                }

                if (!helpText.contentEquals("")) {
                    ModSimReloaded.sendChat(helpText);
                }

                super.onBlockPlacedBy(world, blockPos,iBlockState, player, is);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("标记棒onBlockPlacedBy出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static Marker getMarker(V3 position) {
        Marker ret = null;
        try {
            for (int i = 0; i < markers.size(); i++) {
                Marker m = (Marker) markers.get(i);
                if ((double) m.x == position.xCoord && (double) m.y == position.yCoord && (double) m.z == position.zCoord) {
                    ret = m;
                    break;
                }
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("标记棒getMarker出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            this.location = new V3(blockPos.getX(),blockPos.getY(),blockPos.getZ(), thePlayer.dimension);
            world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":computer", 1, 1);
            GuiMarker ui = new GuiMarker(this.location, thePlayer);
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(ui);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }
        return true;
    }
    @Override
    public void saveNBTData(NBTTagCompound compound) {

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

    }

    @Override
    public void init(Entity entity, World world) {

    }
}
