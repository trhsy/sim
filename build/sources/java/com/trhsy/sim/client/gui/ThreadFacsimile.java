package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.Marker;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.block.Block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName ThreadFacsimile
 * @Description todo
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

        V3 cxyz = guiMarker.location;
        V3 Lxyz = ((Marker) BlockMarker.markers.get(1)).toV3();
        V3 Bxyz = ((Marker)BlockMarker.markers.get(2)).toV3();
        V3 exyz = new V3(Math.floor(guiMarker.mc.thePlayer.posX), Math.floor(guiMarker.mc.thePlayer.posY), Math.floor(guiMarker.mc.thePlayer.posZ), Bxyz.theDimension);
        //int ftbCount = false;
        //int ltrCount = false;
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
            //int bx = false;
            //int by = false;
            //int bz = false;
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
                    guiMarker.errorText = "Please stand facing the primary marker with the rear marker in the distance.";
                    ModSimukraft.sendChat("Could not copy building, Technical info:cx=" + cx + ", cz=" + cz + ", ex=" + ex + ", ez=" + ez);
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
            //int iDx = false;
            //int metax = false;
            HashMap key = new HashMap();
            key.put("0:0", "A");
            ArrayList layerLines = new ArrayList();
            int ch = 66;
            boolean allAirBlocks = true;
            String keyString = "A=0:0;";

            try {
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
                            int iD = Block.getIdFromBlock(guiMarker.mc.getIntegratedServer().worldServerForDimension(guiMarker.thePlayer.dimension).getBlock(xxx, yyy, zzz));
                            int meta = guiMarker.mc.getIntegratedServer().worldServerForDimension(guiMarker.thePlayer.dimension).getBlockMetadata(xxx, yyy, zzz);
                            String letter = "";
                            if (iD == Block.getIdFromBlock(ModSimukraft.controlBox)) {
                                letter = "$";
                            } else {
                                letter = (String)key.get(iD + ":" + meta);
                                if (key.get(iD + ":" + meta) == null) {
                                    ++ch;
                                    key.put(iD + ":" + meta, (new Character((char)ch)).toString());
                                    keyString = keyString + (new Character((char)ch)).toString() + "=" + iD + ":" + meta + ";";
                                    letter = (new Character((char)ch)).toString();
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
                    guiMarker.errorText = "Error, could not capture all blocks, try standing closer to marker and try again";
                    return;
                }

                File check = new File(ModSimukraft.getSimukraftFolder() + "/buildings/");
                if (!check.exists()) {
                    ModSimukraft.sendChat(ModSimukraft.getSimukraftFolder() + "/buildings/  folder is missing, The mod is not correctly installed, please copy the simukraft folder AND the zip file.");
                    return;
                }

                String f = String.valueOf(System.currentTimeMillis());
                ltr = f.length();
                f = f.substring(ltr - 6);
                FileWriter fstream = new FileWriter(ModSimukraft.getSimukraftFolder() + "/buildings/other/My Build" + f + ".txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(ltrCountx + "x" + ftbCountx + "x" + layerLines.size() + "\r\n");
                out.write(keyString + "\r\n");

                for(zzz = 0; zzz < layerLines.size(); ++zzz) {
                    out.write(layerLines.get(zzz).toString() + "\r\n");
                }

                out.close();
                Thread.sleep(500L);
                guiMarker.errorText = "Building copied and stored as 'My Build" + f + "' in Other buildings.";
                guiMarker.mc.theWorld.playSoundEffect(guiMarker.location.x, guiMarker.location.y, guiMarker.location.z, ModSimukraft.MODID + ":computer", 1.0F, 1.0F);
                Building.initialiseAllBuildings();
            } catch (Exception var33) {
                var33.printStackTrace();
            }

        } else {
            guiMarker.errorText = "ERROR: Markers not placed correctly, try again.";
        }
    }
}
