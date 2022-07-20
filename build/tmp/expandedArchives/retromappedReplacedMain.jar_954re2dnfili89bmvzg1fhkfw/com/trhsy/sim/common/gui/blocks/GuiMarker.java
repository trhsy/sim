package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.common.util.UpdateChecker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName GuiMarker
 * @Description todo 标记
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:30
 * ========================================
 **/
public class GuiMarker extends GuiScreen {
    public V3 location;
    public String errorText = "";
    GuiTextField theGuiTextField1;
    public EntityPlayer thePlayer = null;
    private int mouseCount = 0;

    public GuiMarker(V3 location, EntityPlayer p) {
        this.location = location;
        this.thePlayer = p;
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }
    @Override
    public void func_73876_c() {
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146178_a();
        }

    }
    @Override
    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, I18n.func_135052_a("container.sim.sim_gui_BC_Done")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 100, I18n.func_135052_a("container.sim.sim_gui_Copy_structure")));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 160, I18n.func_135052_a("container.sim.sim_gui_Set_new")));

        for(int x = 1; x <= 2; ++x) {
            ((GuiButton)this.field_146292_n.get(x)).field_146124_l = false;
        }

        if (BlockMarker.markers.size() == 3) {
            ((GuiButton)this.field_146292_n.get(1)).field_146124_l = true;
        } else if (BlockMarker.markers.size() == 1) {
            ((GuiButton)this.field_146292_n.get(2)).field_146124_l = true;
            this.theGuiTextField1 = new GuiTextField(0,this.field_146289_q, this.field_146294_l / 2 - this.field_146294_l / 3 / 2, 138, this.field_146294_l / 3, 20);
            this.theGuiTextField1.func_146203_f(23);
        }

    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Markers"), this.field_146294_l / 2, 30, 16777215);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Markers1"), this.field_146294_l / 2, 40, 10551295);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Markers2"), this.field_146294_l / 2, 55, 10551295);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Markers3"), this.field_146294_l / 2, 70, 10551295);
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Markers4"), this.field_146294_l / 2, 85, 10551295);
        this.func_73732_a(this.field_146289_q, this.errorText, this.field_146294_l / 2, this.field_146295_m - 50, 16711680);
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146194_f();
        }

        super.func_73863_a(i, j, f);
    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146127_k == 0) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else {
            if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.sim_gui_Copy_structure"))) {
                new ThreadFacsimile();
            } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.sim_gui_Set_new"))) {
                String s = ((Marker)BlockMarker.markers.get(0)).toString();
                String[] ss = s.split(",");
                String name = this.theGuiTextField1.func_146179_b().trim();
                if (name.length() == 0) {
                    this.errorText =I18n.func_135052_a("container.sim.Markers5");
                    this.theGuiTextField1.func_146206_l();
                    return;
                }

                V3 point = new V3(Double.parseDouble(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[2]), this.thePlayer.field_71093_bK);
                ArrayList<IInventory> chestInvs = Job.inventoriesFindClosest(point, 5);
                if (chestInvs.size() == 0) {
                    this.errorText = I18n.func_135052_a("container.sim.Markers6");
                    return;
                }

                point.name = name;

                for (int p = 0; p < ModSimReloaded.theCourierPoints.size(); ++p) {
                    V3 epoint = (V3) ModSimReloaded.theCourierPoints.get(p);
                    if (epoint.name.contentEquals(name)) {
                        this.errorText = I18n.func_135052_a("container.sim.Markers7") + name + I18n.func_135052_a("container.sim.Markers8");
                        return;
                    }
                }

                ModSimReloaded.theCourierPoints.add(point);
                this.errorText = I18n.func_135052_a("container.sim.Markers9") + name + I18n.func_135052_a("container.sim.Markers10");
            }

        }
    }

    @Override
    protected void func_73864_a(int i, int j, int k) {
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.func_146192_a(i, j, k);
        }

        try {
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
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

        @Override
        public void run() {
            V3 cxyz = GuiMarker.this.location;
            V3 Lxyz = ((Marker) BlockMarker.markers.get(1)).toV3();
            V3 Bxyz = ((Marker)BlockMarker.markers.get(2)).toV3();
            V3 exyz = new V3(Math.floor(GuiMarker.this.field_146297_k.field_71439_g.field_70165_t), Math.floor(GuiMarker.this.field_146297_k.field_71439_g.field_70163_u), Math.floor(GuiMarker.this.field_146297_k.field_71439_g.field_70161_v), Bxyz.theDimension);
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
                        GuiMarker.this.errorText = I18n.func_135052_a("container.sim.Markers11");
                        ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Markers12") + cx + ", cz=" + cz + ", ex=" + ex + ", ez=" + ez);
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
                                BlockPos blockPos =new BlockPos(xxx, yyy, zzz);
                                IBlockState blocks=GuiMarker.this.field_146297_k.func_71401_C().func_71218_a(GuiMarker.this.thePlayer.field_71093_bK).func_180495_p(blockPos);
                                int iD = Block.func_149682_b(blocks.func_177230_c());
                                int meta = blocks.func_177230_c().func_176201_c(blocks);
                                String letter = "";
                                if (iD == Block.func_149682_b(BlockLoader.blockControlBox)) {
                                    letter = "$";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 0) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 1) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 2) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 3) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 4) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 5) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 6) {
                                    letter = "ï¿½";
                                } else if (iD == Block.func_149682_b(BlockLoader.blockLightBox) && meta == 7) {
                                    letter = "ï¿½";
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
                        GuiMarker.this.errorText = I18n.func_135052_a("container.sim.Markers13");
                        return;
                    }

                    File check = new File(UpdateChecker.getSimukraftFolder() + "/buildings/");
                    if (!check.exists()) {
                        ModSimReloaded.sendChat(UpdateChecker.getSimukraftFolder() + "/buildings/ " + I18n.func_135052_a("container.sim.Markers14"));
                        return;
                    }

                    String f = String.valueOf(System.currentTimeMillis());
                    ltr = f.length();
                    f = f.substring(ltr - 6);
                    FileWriter fstream = new FileWriter(UpdateChecker.getSimukraftFolder() + "/buildings/other/My Build" + f + ".txt");
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(ltrCountx + "x" + ftbCountx + "x" + layerLines.size() + "\r\n");
                    out.write(keyString + "\r\n");

                    for(zzz = 0; zzz < layerLines.size(); ++zzz) {
                        out.write(layerLines.get(zzz).toString() + "\r\n");
                    }

                    out.close();
                    Thread.sleep(500L);
                    GuiMarker.this.errorText = I18n.func_135052_a("container.sim.Markers15") + f + I18n.func_135052_a("container.sim.Markers16");
                    GuiMarker.this.field_146297_k.field_71441_e.func_72908_a(GuiMarker.this.location.x, GuiMarker.this.location.y, GuiMarker.this.location.z, ModSim.MODID + ":computer", 1.0F, 1.0F);
                    Building.initialiseAllBuildings();
                } catch (Exception var33) {
                    var33.printStackTrace();
                }

            } else {
                GuiMarker.this.errorText = I18n.func_135052_a("container.sim.Markers17");
            }
        }
    }
}

