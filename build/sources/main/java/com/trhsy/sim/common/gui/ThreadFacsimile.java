package com.trhsy.sim.common.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.gui.blocks.GuiMarker;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.common.util.UpdateChecker;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockPos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName ThreadFacsimile
 * @Description todo 线程传真
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:30
 * ========================================
 **/
public class ThreadFacsimile extends Thread {
    private GuiMarker guiMarker;
    public ThreadFacsimile() {
        this.start();
    }

    @Override
    public void run() {
        try {
            V3 cxyz = guiMarker.location;
            V3 Lxyz = ((Marker) BlockMarker.markers.get(1)).toV3();
            V3 Bxyz = ((Marker)BlockMarker.markers.get(2)).toV3();
            V3 exyz = new V3(Math.floor(guiMarker.mc.thePlayer.posX), Math.floor(guiMarker.mc.thePlayer.posY), Math.floor(guiMarker.mc.thePlayer.posZ), Bxyz.theDimension);
            int ltrCountx;
            if (cxyz.x.intValue() == Lxyz.x.intValue()) {
                ltrCountx = Math.abs(Lxyz.z.intValue() - cxyz.z.intValue()) - 1;
            } else {
                ltrCountx = Math.abs(Lxyz.x.intValue() - cxyz.x.intValue()) - 1;
            }
            int ftbCountx;
            if (cxyz.x.intValue() == Bxyz.x.intValue()) {
                ftbCountx = Math.abs(Bxyz.z.intValue() - cxyz.z.intValue()) - 1;
            } else {
                ftbCountx = Math.abs(Bxyz.x.intValue() - cxyz.x.intValue()) - 1;
            }

            if (ftbCountx != 0 && ltrCountx != 0) {
                int cx = cxyz.x.intValue();
                int cy = cxyz.y.intValue();
                int cz = cxyz.z.intValue();
                int ex = exyz.x.intValue();
                int ey = exyz.y.intValue();
                int ez = exyz.z.intValue();
                int bxx = ex;
                int byx = ey;
                int bzx = ez;
                if (cz == ez) {
                    if (cx > ex) {
                        bxx = cx + 1;
                    } else {
                        bxx = cx - 1;
                    }
                } else {
                    if (cx != ex) {
                        guiMarker.errorText = I18n.format("container.sim.Facsimile1");
                        ModSimReloaded.sendChat(I18n.format("container.sim.Facsimile2") + cx + ", cz=" + cz + ", ex=" + ex + ", ez=" + ez);
                        return;
                    }

                    if (cz > ez) {
                        bzx = cz + 1;
                    } else {
                        bzx = cz - 1;
                    }
                }

                int xo = 0;
                int zo = 0;
                HashMap key = new HashMap();
                key.put("0:0", "A");
                CopyOnWriteArrayList layerLines = new CopyOnWriteArrayList();
                int ch = 66;
                boolean allAirBlocks = true;
                String keyString = "A=0:0;";

                int ltr;
                int zzz;
                for(int l = 0; l < 200; ++l) {
                    String layerLine = "";

                    for(int ftb = 0; ftb < ftbCountx; ++ftb) {
                        for(ltr = 1; ltr <= ltrCountx; ++ltr) {
                            if (cz == ez) {
                                if (cx > ex) {
                                    xo = ftb;
                                    zo = ltr;
                                } else {
                                    xo = -ftb;
                                    zo = -ltr;
                                }
                            } else if (cx == ex) {
                                if (cz > ez) {
                                    xo = -ltr;
                                    zo = ftb;
                                } else {
                                    xo = ltr;
                                    zo = -ftb;
                                }
                            }

                            int xxx = bxx + xo;
                            int yyy = byx + l - 1;
                            zzz = bzx + zo;
                            BlockPos blockPos=new BlockPos(xxx, yyy, zzz);
                            IBlockState blockState=guiMarker.mc.getIntegratedServer().worldServerForDimension(guiMarker.thePlayer.dimension).getBlockState(blockPos);
                            int iD = Block.getIdFromBlock(blockState.getBlock());
                            int meta = blockState.getBlock().getMetaFromState(blockState);
                            String letter = "";
                            if (iD == Block.getIdFromBlock(BlockLoader.blockControlBox)) {
                                letter = "$";
                            } else {
                                letter = (String) key.get(iD + ":" + meta);
                                if (key.get(iD + ":" + meta) == null) {
                                    ++ch;
                                    key.put(iD + ":" + meta, (new Character((char) ch)).toString());
                                    keyString = keyString + (new Character((char) ch)).toString() + "=" + iD + ":" + meta + ";";
                                    letter = (new Character((char) ch)).toString();
                                }
                            }

                            layerLine = layerLine + letter;
                            if (iD != 0) {
                                allAirBlocks = false;
                            }
                        }
                    }

                    if (allAirBlocks) {
                        break;
                    }

                    layerLines.add(layerLine);
                    allAirBlocks = true;
                }

                if (layerLines.size() == 0) {
                    guiMarker.errorText = I18n.format("container.sim.Facsimile3");
                    return;
                }

                File check = new File(ModSimReloaded.getSimukraftFolder() + "/buildings/");
                if (!check.exists()) {
                    ModSimReloaded.sendChat(ModSimReloaded.getSimukraftFolder() + "/buildings/ " + I18n.format("container.sim.Facsimile4"));
                    return;
                }

                String f = String.valueOf(System.currentTimeMillis());
                ltr = f.length();
                f = f.substring(ltr - 6);
                FileWriter fstream = new FileWriter(ModSimReloaded.getSimukraftFolder() + "/buildings/other/My Build" + f + ".txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(ltrCountx + "x" + ftbCountx + "x" + layerLines.size() + "\r\n");
                out.write(keyString + "\r\n");

                for(zzz = 0; zzz < layerLines.size(); ++zzz) {
                    out.write(layerLines.get(zzz).toString() + "\r\n");
                }

                out.close();
                Thread.sleep(500L);
                guiMarker.errorText = I18n.format("container.sim.Facsimile5") + f + I18n.format("container.sim.Facsimile1");
                guiMarker.mc.theWorld.playSoundEffect(guiMarker.location.x, guiMarker.location.y, guiMarker.location.z, ModSim.MODID + ":computer", 1.0F, 1.0F);
                Building.initialiseAllBuildings();

            } else {
                guiMarker.errorText = I18n.format("container.sim.Facsimile1");
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("ThreadFacsimile出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
