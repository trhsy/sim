package com.trhsy.sim.client.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.gui.blocks.GuiControlBox;
import com.trhsy.sim.common.core.entity.CourierTask;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName GuiCourierTasks
 * @Description todo 快递任务
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
@SideOnly(Side.CLIENT)
public class GuiCourierTasks extends GuiScreen {
    V3 controlBoxLocation;
    FolkData theFolk = new FolkData();
    HashMap tasks = new HashMap();
    String onPage = "main";
    CourierTask newtask = new CourierTask();
    private int mouseCount = 0;
    private EntityPlayer thePlayer = null;

    public GuiCourierTasks(V3 xyz, String folkname, EntityPlayer pl) {
        try {
            this.controlBoxLocation = xyz;
            this.theFolk = FolkData.getFolkByName(folkname);
            this.thePlayer = pl;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("GuiCourierTasks出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        //theGuiTextField1.updateCursorCounter();
    }

    @Override
    public void initGui() {
        this.initscreen();
    }

    private void initscreen() {
        try {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
            if (this.onPage.contentEquals("main")) {
                this.buttonList.add(new GuiButton(1, 5, this.height - 20, 50, 20, I18n.format("container.sim.gui_btn_name_Add")));
                int idx = 2;
                int y = 30;

                for (int t = 0; t < ModSimReloaded.theCourierTasks.size(); ++t) {
                    CourierTask ct = (CourierTask) ModSimReloaded.theCourierTasks.get(t);
                    if (ct.folkname.contentEquals(this.theFolk.name)) {
                        y = 30 + (idx - 2) * 20;
                        if (y + 20 > this.height) {
                            break;
                        }

                        this.buttonList.add(new GuiButton(idx, this.width - 50, y, 50, 20, I18n.format("container.sim.gui_btn_name_Delete")));
                        this.tasks.put(idx, t);
                        idx++;
                    }
                }
            } else if (this.onPage.contentEquals("add")) {
                int x = 10, y = 40, idx = 2;

                for (int f = 0; f < ModSimReloaded.theCourierPoints.size(); ++f) {
                    V3 cpoint = ModSimReloaded.theCourierPoints.get(f);
                    this.buttonList.add(new GuiButton(idx, x, y, 110, 20, cpoint.name));
                    idx++;
                    x += 110;
                    if (x + 110 > this.width) {
                        x = 10;
                        y += 20;
                    }

                    if (y + 20 > this.height - 50) {
                        break;
                    }
                }

                GuiButton b;
                this.buttonList.add(b = new GuiButton(1, this.width - 160, this.height - 25, 150, 20, I18n.format("container.sim.gui_btn_name_Deliver_back")));
                b.enabled = false;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("GuiCourierTasks-initscreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            this.drawDefaultBackground();
            if (this.mouseCount < 10) {
                this.mouseCount++;
                Mouse.setGrabbed(false);
            }

            if (this.onPage.contentEquals("main")) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_btn_name_Courier_tasks") + this.theFolk.name, this.width / 2, 17, 16777215);
                int idx = 2;

                for (int t = 0; t < ModSimReloaded.theCourierTasks.size(); ++t) {
                    CourierTask ct = ModSimReloaded.theCourierTasks.get(t);
                    if (ct.folkname.contentEquals(this.theFolk.name)) {
                        int y = 40 + (idx - 2) * 20;
                        if (y + 20 > this.height) {
                            break;
                        }

                        this.fontRendererObj.drawString(ct.pickup.name, 5, y - 5, 16777103);
                        this.fontRendererObj.drawString("->", this.width / 3, y - 5, 16776960);
                        if (ct.dropoff == null) {
                            this.fontRendererObj.drawString(I18n.format("container.sim.gui_btn_name_The_Depot"), this.width / 2, y - 5, 15794063);
                        } else {
                            this.fontRendererObj.drawString(ct.dropoff.name, this.width / 2, y - 5, 15794063);
                        }

                        idx++;
                    }
                }
            } else if (this.onPage.contentEquals("add")) {
                //选择一个提货点
                if (this.newtask.pickup == null || this.newtask.pickup.name.contentEquals("")) {
                    //选择一个提货点
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_btn_name_Choose_a_pick"), this.width / 2, 17, 16777215);
                } else {
                    //从   拿起,然后在
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_btn_name_Pick_up_from") + this.newtask.pickup.name + I18n.format("container.sim.gui_btn_name_and_drop_off") + "...", this.width / 2, 17, 16777215);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("GuiCourierTasks-drawScreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        super.drawScreen(i, j, f);
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.displayGuiScreen((GuiScreen) null);
                    GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, this.thePlayer);
                    this.mc.displayGuiScreen(ui);
                } else {
                    if (this.onPage.contentEquals("main")) {
                        if (guibutton.id == 1) {
                            this.onPage = "add";
                            this.initscreen();
                        } else if (guibutton.id >= 2) {
                            int tidx = (Integer) this.tasks.get(guibutton.id);
                            String fn = "ct" + tidx + this.theFolk.name.replace(" ", "");
                            File file = new File(ModSimReloaded.getSavesDataFolder() + "CourierTasks" + File.separator + fn + ".sk2");
                            file.delete();
                            ModSimReloaded.theCourierTasks.remove(tidx);
                            this.buttonList.remove(guibutton.id);
                            this.initscreen();
                        }
                    } else if (this.onPage.contentEquals("add")) {
                        String name;
                        V3 v;

                        if (this.newtask.pickup == null || this.newtask.pickup.name.contentEquals("")) {
                            name = guibutton.displayString.trim();
                            v = CourierTask.getCourierPoint(name);
                            this.newtask.pickup=v.clone();
                            this.newtask.pickup.name = name;
                            guibutton.enabled = false;
                            GuiButton but = this.getButtonWithId(1);
                            but.enabled = true;
                        } else if (this.newtask.dropoff == null || this.newtask.dropoff.name.contentEquals("")) {
                            if (guibutton.id == 1) {
                                //仓库
                                this.newtask.dropoff=this.controlBoxLocation.clone();
                                this.newtask.dropoff.name = I18n.format("container.sim.gui_btn_name_The_Depot");
                            } else {
                                name = guibutton.displayString.trim();
                                v = CourierTask.getCourierPoint(name);
                                this.newtask.dropoff=v.clone();
                                this.newtask.dropoff.name = name;
                            }

                            guibutton.enabled = false;
                            this.newtask.folkname = this.theFolk.name;
                            this.newtask.name = "Task " + (ModSimReloaded.theCourierTasks.size() + 1) + "";
                            ModSimReloaded.theCourierTasks.add(this.newtask);
                            this.onPage = "main";
                            this.initscreen();
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("GUICOURIERTASKS-actionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public GuiButton getButtonWithId(int id) {
        try {
            for (int x = 0; x < this.buttonList.size(); ++x) {
                GuiButton retbut = (GuiButton) this.buttonList.get(x);
                if (retbut.id == id) {
                    return retbut;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("getButtonWithId出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return null;
    }

    @Override
    public void onGuiClosed() {
        try {
            Keyboard.enableRepeatEvents(false);
            this.mc.setIngameFocus();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    @Override
    public void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.displayGuiScreen((GuiScreen) null);
                GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, this.thePlayer);
                this.mc.displayGuiScreen(ui);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
}

