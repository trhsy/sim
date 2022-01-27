package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import org.lwjgl.input.Mouse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName GuiMarker
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:30
 * ========================================
 **/
public class GuiMarker extends GuiScreen {
    V3 location;
    String errorText = "";
    GuiTextField theGuiTextField1;
    EntityPlayer thePlayer = null;
    private int mouseCount = 0;

    public GuiMarker(V3 location, EntityPlayer p) {
        this.location = location;
        this.thePlayer = p;
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146178_a();
        }

    }

    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Done"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 100, "Copy structure/building"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 160, "Set new Courier/Beaming point"));

        for(int x = 1; x <= 2; ++x) {
            ((GuiButton)this.field_146292_n.get(x)).field_146124_l = false;
        }

        if (BlockMarker.markers.size() == 3) {
            ((GuiButton)this.field_146292_n.get(1)).field_146124_l = true;
        } else if (BlockMarker.markers.size() == 1) {
            ((GuiButton)this.field_146292_n.get(2)).field_146124_l = true;
            this.theGuiTextField1 = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - this.field_146294_l / 3 / 2, 138, this.field_146294_l / 3, 20);
            this.theGuiTextField1.func_146203_f(23);
        }

    }

    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, "Sim-u-Markers", this.field_146294_l / 2, 30, 16777215);
        this.func_73732_a(this.field_146289_q, "Markers can be used to make a copy of a building or you can use them to", this.field_146294_l / 2, 40, 10551295);
        this.func_73732_a(this.field_146289_q, "mark out a mining or food-based farming area.", this.field_146294_l / 2, 55, 10551295);
        this.func_73732_a(this.field_146289_q, "A single marker can be used to designate a new lumberjack area and more!", this.field_146294_l / 2, 70, 10551295);
        this.func_73732_a(this.field_146289_q, "TIP: remove old markers after use, before marking a new area.", this.field_146294_l / 2, 85, 10551295);
        this.func_73732_a(this.field_146289_q, this.errorText, this.field_146294_l / 2, this.field_146295_m - 50, 16711680);
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146194_f();
        }

        super.func_73863_a(i, j, f);
    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146127_k == 0) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else {
            if (guibutton.field_146126_j.contentEquals("Copy structure/building")) {
                new GuiMarker.ThreadFacsimile();
            } else if (guibutton.field_146126_j.contentEquals("Set new Courier/Beaming point")) {
                String s = ((Marker)BlockMarker.markers.get(0)).toString();
                String[] ss = s.split(",");
                String name = this.theGuiTextField1.func_146179_b().trim();
                if (name.length() == 0) {
                    this.errorText = "Please type a name for this Courier/Beaming point";
                    this.theGuiTextField1.func_146206_l();
                    return;
                }

                V3 point = new V3(Double.parseDouble(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[2]), this.thePlayer.field_71093_bK);
                ArrayList<IInventory> chestInvs = Job.inventoriesFindClosest(point, 5);
                if (chestInvs.size() == 0) {
                    this.errorText = "Error: Place at least one chest near the marker.";
                    return;
                }

                point.name = name;

                for(int p = 0; p < ModSimukraft.theCourierPoints.size(); ++p) {
                    V3 epoint = (V3)ModSimukraft.theCourierPoints.get(p);
                    if (epoint.name.contentEquals(name)) {
                        this.errorText = "Error: The name must be unique, '" + name + "' is already used.";
                        return;
                    }
                }

                ModSimukraft.theCourierPoints.add(point);
                this.errorText = "Courier/Beaming point '" + name + "' has been added.";
            }

        }
    }

    protected void func_73864_a(int i, int j, int k) {
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146192_a(i, j, k);
        }

        super.func_73864_a(i, j, k);
    }

    protected void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }

        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146201_a(c, i);
        }

    }

    public class ThreadFacsimile extends Thread {
        public ThreadFacsimile() {
            this.start();
        }

        public void run() {
            V3 cxyz = GuiMarker.this.location;
            V3 Lxyz = ((Marker) BlockMarker.markers.get(1)).toV3();
            V3 Bxyz = ((Marker)BlockMarker.markers.get(2)).toV3();
            V3 exyz = new V3(Math.floor(GuiMarker.this.field_146297_k.thePlayer.posX), Math.floor(GuiMarker.this.field_146297_k.thePlayer.posY), Math.floor(GuiMarker.this.field_146297_k.thePlayer.posZ), Bxyz.theDimension);
            int ftbCount = false;
            int ltrCount = false;
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
                int bx = false;
                int by = false;
                int bz = false;
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
                        GuiMarker.this.errorText = "Please stand facing the primary marker with the rear marker in the distance.";
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
                int iDx = false;
                int metax = false;
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
                                int iD = Block.func_149682_b(GuiMarker.this.field_146297_k.func_71401_C().worldServerForDimension(GuiMarker.this.thePlayer.field_71093_bK).getBlock(xxx, yyy, zzz));
                                int meta = GuiMarker.this.field_146297_k.func_71401_C().worldServerForDimension(GuiMarker.this.thePlayer.field_71093_bK).func_72805_g(xxx, yyy, zzz);
                                String letter = "";
                                if (iD == Block.func_149682_b(ModSimukraft.controlBox)) {
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
                        GuiMarker.this.errorText = "Error, could not capture all blocks, try standing closer to marker and try again";
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
                    GuiMarker.this.errorText = "Building copied and stored as 'My Build" + f + "' in Other buildings.";
                    GuiMarker.this.field_146297_k.field_71441_e.func_72908_a(GuiMarker.this.location.x, GuiMarker.this.location.y, GuiMarker.this.location.z, "satscapesimukraft:computer", 1.0F, 1.0F);
                    Building.initialiseAllBuildings();
                } catch (Exception var33) {
                    var33.printStackTrace();
                }

            } else {
                GuiMarker.this.errorText = "ERROR: Markers not placed correctly, try again.";
            }
        }
    }
}

