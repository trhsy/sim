package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.EntityAlignBeam;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.gui.blocks.GuiMarker;
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

import java.util.ArrayList;

/**
 * @ClassName BlockMarker
 * @Description todo 标记棒
 * @Author Tian
 * @Date 2022/5/523:22
 **/
public class BlockMarker extends Block implements IExtendedEntityProperties {
    public static boolean hasPlaced = false;
    public static ArrayList<Marker> markers = new ArrayList();
    public V3 location;

    public BlockMarker() {
        super(Material.field_151575_d);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(2.0F);
        this.func_149752_b(1.0F);
        this.func_149663_c("markerBar");
        this.func_149676_a(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.func_149715_a(0.1F);
        //this.setTextureName(ModSim.MODID + ":" + "marker_bar_block");
        this.func_149647_a(CreativeTabsLoader.tabSimU);

    }

    /**
     * 为项目渲染设置块边界
     */
    @Override
    public void func_149683_g() {
        try {
            this.func_149676_a(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        } catch (Exception e) {
            ModSimReloaded.log.error("标记棒setBlockBoundsForItemRender出错了：" + e.getMessage());
        }
    }

    /**
     * 是否不透明立方体
     * @return
     */
    @Override
    public boolean func_149662_c() {
        return false;
    }

    /**
     *是否不普通方块
     * @return
     */
    @Override
    public boolean func_149721_r() {
        return false;
    }
    @Override
    public void func_176206_d(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            for (int m = 0; m < markers.size(); ++m) {
                Marker marker = (Marker) markers.get(m);

                for (int mm = 0; mm < 4; ++mm) {
                    try {
                        ((EntityAlignBeam) marker.beams.get(mm)).func_70106_y();
                    } catch (Exception var10) {
                    }
                }
            }
            markers.clear();
            super.func_176206_d(world,blockPos,iBlockState);
        } catch (Exception e) {
            ModSimReloaded.log.error("标记棒onBlockDestroyedByPlayer出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_180633_a(World world, BlockPos blockPos,IBlockState iBlockState, EntityLivingBase player, ItemStack is) {
        try {
            hasPlaced = true;
            if (world.field_72995_K) {
                Marker ma;
                markers.add(ma = new Marker(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), world.field_73011_w.func_177502_q()));
                String markerCaption = "";
                String helpText = "";
                if (markers.size() == 1) {
                    markerCaption = "Front-Left";
                    helpText = I18n.func_135052_a("container.sim.box_Marker_left");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else if (markers.size() == 2) {
                    markerCaption = "Front-Right";
                    helpText = I18n.func_135052_a("container.sim.box_Marker_right");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else if (markers.size() == 3) {
                    markerCaption = "Rear-Left";
                    helpText = I18n.func_135052_a("container.sim.box_Marker_Rear_Left");
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                } else {
                    ModSimReloaded.log.info(String.valueOf(markers.size()));
                    markerCaption = I18n.func_135052_a("container.sim.box_Marker_Markers");
                }

                if (markers.size() < 4) {
                    V3 pos = new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), world.field_73011_w.func_177502_q());
                    pos.y = pos.y + 0.01;
                    if (ConfigLoader.configEnableMarkerAlignmentBeams) {
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
                    ModSimReloaded.sendChat(helpText);
                }

                super.func_180633_a(world, blockPos,iBlockState, player, is);
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("标记棒onBlockPlacedBy出错了：" + e.getMessage());
        }
    }

    public static Marker getMarker(V3 position) {
        Marker ret = null;
        try {
            for (int i = 0; i < markers.size(); i++) {
                Marker m = (Marker) markers.get(i);
                if ((double) m.x == position.x && (double) m.y == position.y && (double) m.z == position.z) {
                    ret = m;
                    break;
                }
            }

        } catch (Exception e) {
            ModSimReloaded.log.error("标记棒getMarker出错了：" + e.getMessage());
        }

        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            this.location = new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), thePlayer.field_71093_bK);
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":computer", 1.0F, 1.0F);
            GuiMarker ui = new GuiMarker(this.location, thePlayer);
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_147108_a(ui);
        } catch (Exception e) {
            ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage());
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
