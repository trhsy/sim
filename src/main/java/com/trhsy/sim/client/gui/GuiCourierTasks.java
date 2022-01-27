package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.CourierTask;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName GuiCourierTasks
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
public class GuiCourierTasks extends GuiScreen {
    V3 controlBoxLocation;
    FolkData theFolk = null;
    HashMap tasks = new HashMap();
    String onPage = "main";
    CourierTask newtask = new CourierTask();
    private int mouseCount = 0;
    private EntityPlayer thePlayer = null;

    public GuiCourierTasks(V3 xyz, String folkname, EntityPlayer pl) {
        this.controlBoxLocation = xyz;
        this.theFolk = FolkData.getFolkByName(folkname);
        this.thePlayer = pl;
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
    }

    public void func_73866_w_() {
        this.initscreen();
    }

    private void initscreen() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, "Done"));
        int idx;
        int t;
        int y;
        if (this.onPage.contentEquals("main")) {
            this.field_146292_n.add(new GuiButton(1, 5, this.field_146295_m - 20, 50, 20, "Add"));
            idx = 2;
            int y = true;

            for(t = 0; t < ModSimukraft.theCourierTasks.size(); ++t) {
                CourierTask ct = (CourierTask)ModSimukraft.theCourierTasks.get(t);
                if (ct.folkname.contentEquals(this.theFolk.name)) {
                    y = 30 + (idx - 2) * 20;
                    if (y + 20 > this.field_146295_m) {
                        break;
                    }

                    this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 50, y, 50, 20, "Delete"));
                    this.tasks.put(idx, t);
                    ++idx;
                }
            }
        } else if (this.onPage.contentEquals("add")) {
            idx = 10;
            y = 40;
            t = 2;

            for(int f = 0; f < ModSimukraft.theCourierPoints.size(); ++f) {
                V3 cpoint = (V3)ModSimukraft.theCourierPoints.get(f);
                this.field_146292_n.add(new GuiButton(t, idx, y, 110, 20, cpoint.name));
                ++t;
                idx += 110;
                if (idx + 110 > this.field_146294_l) {
                    idx = 10;
                    y += 20;
                }

                if (y + 20 > this.field_146295_m - 50) {
                    break;
                }
            }

            GuiButton b;
            this.field_146292_n.add(b = new GuiButton(1, this.field_146294_l - 160, this.field_146295_m - 25, 150, 20, "Deliver back to depot"));
            b.field_146124_l = false;
        }

    }

    public void func_73863_a(int i, int j, float f) {
        this.func_146276_q_();
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        if (this.onPage.contentEquals("main")) {
            this.func_73732_a(this.field_146289_q, "Courier tasks for " + this.theFolk.name, this.field_146294_l / 2, 17, 16777215);
            int idx = 2;

            for(int t = 0; t < ModSimukraft.theCourierTasks.size(); ++t) {
                CourierTask ct = (CourierTask)ModSimukraft.theCourierTasks.get(t);
                if (ct.folkname.contentEquals(this.theFolk.name)) {
                    int y = 40 + (idx - 2) * 20;
                    if (y + 20 > this.field_146295_m) {
                        break;
                    }

                    try {
                        this.field_146289_q.func_78276_b(ct.pickup.name, 5, y - 5, 16777103);
                        this.field_146289_q.func_78276_b("->", this.field_146294_l / 3, y - 5, 16776960);
                        if (ct.dropoff == null) {
                            this.field_146289_q.func_78276_b("The Depot", this.field_146294_l / 2, y - 5, 15794063);
                        } else {
                            this.field_146289_q.func_78276_b(ct.dropoff.name, this.field_146294_l / 2, y - 5, 15794063);
                        }

                        ++idx;
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }
                }
            }
        } else if (this.onPage.contentEquals("add")) {
            if (this.newtask.pickup.name.contentEquals("")) {
                this.func_73732_a(this.field_146289_q, "Choose a pick up point", this.field_146294_l / 2, 17, 16777215);
            } else {
                this.func_73732_a(this.field_146289_q, "Pick up from " + this.newtask.pickup.name + " and drop off at...", this.field_146294_l / 2, 17, 16777215);
            }
        }

        super.func_73863_a(i, j, f);
    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, this.thePlayer);
                this.field_146297_k.displayGuiScreen(ui);
            } else {
                if (this.onPage.contentEquals("main")) {
                    if (guibutton.field_146127_k == 1) {
                        this.onPage = "add";
                        this.initscreen();
                    } else if (guibutton.field_146127_k >= 2) {
                        int tidx = (Integer)this.tasks.get(guibutton.field_146127_k);
                        String fn = "ct" + tidx + this.theFolk.name.replace(" ", "");
                        File file = new File(ModSimukraft.getSavesDataFolder() + "CourierTasks" + File.separator + fn + ".sk2");
                        file.delete();
                        ModSimukraft.theCourierTasks.remove(tidx);
                        this.field_146292_n.remove(guibutton.field_146127_k);
                        this.initscreen();
                    }
                } else if (this.onPage.contentEquals("add")) {
                    String name;
                    V3 v;
                    if (this.newtask.pickup.name.contentEquals("")) {
                        name = guibutton.field_146126_j.trim();
                        v = CourierTask.getCourierPoint(name);
                        this.newtask.pickup.name = name;
                        this.newtask.pickup.setVals(v);
                        guibutton.field_146124_l = false;
                        GuiButton but = this.getButtonWithId(1);
                        but.field_146124_l = true;
                    } else if (this.newtask.dropoff != null && this.newtask.dropoff.name.contentEquals("")) {
                        if (guibutton.field_146127_k == 1) {
                            this.newtask.dropoff.name = "Depot";
                            this.newtask.dropoff.setVals(this.controlBoxLocation);
                        } else {
                            name = guibutton.field_146126_j.trim();
                            v = CourierTask.getCourierPoint(name);
                            this.newtask.dropoff.name = name;
                            this.newtask.dropoff.setVals(v);
                        }

                        guibutton.field_146124_l = false;
                        this.newtask.folkname = this.theFolk.name;
                        this.newtask.name = "Task " + (ModSimukraft.theCourierTasks.size() + 1) + "";
                        ModSimukraft.theCourierTasks.add(this.newtask);
                        this.onPage = "main";
                        this.initscreen();
                    }
                }

            }
        }
    }

    public GuiButton getButtonWithId(int id) {
        for(int x = 0; x < this.field_146292_n.size(); ++x) {
            GuiButton retbut = (GuiButton)this.field_146292_n.get(x);
            if (retbut.field_146127_k == id) {
                return retbut;
            }
        }

        return null;
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, this.thePlayer);
            this.field_146297_k.displayGuiScreen(ui);
        }
    }
}

