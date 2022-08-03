package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.functionality.Marker;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        try {
            this.location = location;
            this.thePlayer = p;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiMarker出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void updateScreen() {
        try {
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.updateCursorCounter();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public void initGui() {
        try {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 100, I18n.format("container.sim.sim_gui_Copy_structure")));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 160, I18n.format("container.sim.sim_gui_Set_new")));

            for(int x = 1; x <= 2; ++x) {
                ((GuiButton)this.buttonList.get(x)).enabled = false;
            }

            if (BlockMarker.markers.size() == 3) {
                ((GuiButton)this.buttonList.get(1)).enabled = true;
            } else if (BlockMarker.markers.size() == 1) {
                ((GuiButton)this.buttonList.get(2)).enabled = true;
                this.theGuiTextField1 = new GuiTextField(0,this.fontRendererObj, this.width / 2 - this.width / 3 / 2, 138, this.width / 3, 20);
                this.theGuiTextField1.setMaxStringLength(23);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Markers"), this.width / 2, 30, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Markers1"), this.width / 2, 40, 10551295);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Markers2"), this.width / 2, 55, 10551295);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Markers3"), this.width / 2, 70, 10551295);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Markers4"), this.width / 2, 85, 10551295);
            this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 50, 16711680);
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.drawTextBox();
            }
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                //复制结构/建造
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_Copy_structure"))) {
                    new ThreadFacsimile();
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_Set_new"))) {
                    String s = ((Marker)BlockMarker.markers.get(0)).toString();
                    String[] ss = s.split(",");
                    String name = this.theGuiTextField1.getText().trim();
                    if (name.length() == 0) {
                        this.errorText =I18n.format("container.sim.Markers5");
                        this.theGuiTextField1.isFocused();
                        return;
                    }

                    V3 point = new V3(Double.parseDouble(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[2]), this.thePlayer.dimension);
                    List<IInventory> chestInvs = Job.inventoriesFindClosest(point, 5);
                    if (chestInvs.size() == 0) {
                        this.errorText = I18n.format("container.sim.Markers6");
                        return;
                    }

                    point.name = name;

                    for (int p = 0; p < ModSimReloaded.theCourierPoints.size(); ++p) {
                        V3 epoint = (V3) ModSimReloaded.theCourierPoints.get(p);
                        if (epoint.name.contentEquals(name)) {
                            this.errorText = I18n.format("container.sim.Markers7") + name + I18n.format("container.sim.Markers8");
                            return;
                        }
                    }

                    ModSimReloaded.theCourierPoints.add(point);
                    this.errorText = I18n.format("container.sim.Markers9") + name + I18n.format("container.sim.Markers10");
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIMARKER-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        try {
        if (this.theGuiTextField1 != null) {
            this.theGuiTextField1.mouseClicked(i, j, k);
        }
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            //e.printStackTrace();
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }

            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.textboxKeyTyped(c, i);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public class ThreadFacsimile extends Thread {

        public ThreadFacsimile() {
            this.start();
        }

        @Override
        public void run() {

            try {
                V3 cxyz = GuiMarker.this.location;
                V3 Lxyz = ((Marker) BlockMarker.markers.get(1)).toV3();
                V3 Bxyz = ((Marker)BlockMarker.markers.get(2)).toV3();
                V3 exyz = new V3(Math.floor(GuiMarker.this.mc.thePlayer.posX), Math.floor(GuiMarker.this.mc.thePlayer.posY), Math.floor(GuiMarker.this.mc.thePlayer.posZ), Bxyz.theDimension);
                int ltrCountx;
                if (cxyz.xCoord == Lxyz.xCoord) {
                    ltrCountx = (int) (Math.abs(Lxyz.zCoord - cxyz.zCoord) - 1);
                } else {
                    ltrCountx = (int) (Math.abs(Lxyz.xCoord - cxyz.xCoord) - 1);
                }

                int ftbCountx;
                if (cxyz.xCoord == Bxyz.xCoord) {
                    ftbCountx = (int) (Math.abs(Bxyz.zCoord - cxyz.zCoord) - 1);
                } else {
                    ftbCountx = (int) (Math.abs(Bxyz.xCoord - cxyz.xCoord) - 1);
                }

                if (ftbCountx != 0 && ltrCountx != 0) {
                    int cx = (int)cxyz.xCoord;
                    int cy = (int)cxyz.yCoord;
                    int cz = (int)cxyz.zCoord;
                    int ex = (int)exyz.xCoord;
                    int ey = (int)exyz.yCoord;
                    int ez = (int)exyz.zCoord;
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
                            GuiMarker.this.errorText = I18n.format("container.sim.Markers11");
                            ModSimReloaded.sendChat(I18n.format("container.sim.Markers12") + cx + ", cz=" + cz + ", ex=" + ex + ", ez=" + ez);
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
                                BlockPos blockPos =new BlockPos(xxx, yyy, zzz);
                                IBlockState blocks=GuiMarker.this.mc.getIntegratedServer().worldServerForDimension(GuiMarker.this.thePlayer.dimension).getBlockState(blockPos);
                                int iD = Block.getIdFromBlock(blocks.getBlock());
                                int meta = blocks.getBlock().getMetaFromState(blocks);
                                String letter = "";
                                if (iD == Block.getIdFromBlock(BlockLoader.blockControlBox)) {
                                    letter = "$";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 0) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 1) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 2) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 3) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 4) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 5) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 6) {
                                    letter = "ï¿½";
                                } else if (iD == Block.getIdFromBlock(BlockLoader.blockLightBox) && meta == 7) {
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
                        GuiMarker.this.errorText = I18n.format("container.sim.Markers13");
                        return;
                    }

                    File check = new File(ModSimReloaded.getSimukraftFolder() + "/buildings/");
                    if (!check.exists()) {
                        ModSimReloaded.sendChat(ModSimReloaded.getSimukraftFolder() + "/buildings/ " + I18n.format("container.sim.Markers14"));
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
                    GuiMarker.this.errorText = I18n.format("container.sim.Markers15") + f + I18n.format("container.sim.Markers16");
                    GuiMarker.this.mc.theWorld.playSoundEffect(GuiMarker.this.location.xCoord, GuiMarker.this.location.yCoord, GuiMarker.this.location.zCoord, ModSim.MODID + ":computer", 1, 1);
                    Building.initialiseAllBuildings();

                } else {
                    GuiMarker.this.errorText = I18n.format("container.sim.Markers17");
                }
            } catch (Exception e) {
                StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("ThreadFacsimile出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            }
        }
    }
}

