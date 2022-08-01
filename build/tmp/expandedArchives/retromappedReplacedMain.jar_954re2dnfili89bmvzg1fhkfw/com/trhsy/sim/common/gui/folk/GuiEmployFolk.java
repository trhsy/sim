package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameMode;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.entity.functionality.MiningBox;
import com.trhsy.sim.common.entity.functionality.PathBox;
import com.trhsy.sim.common.gui.blocks.*;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName GuiEmployFolk
 * @Description todo 雇佣员工
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
public class GuiEmployFolk extends GuiScreen {
    FolkData theFolk;
    //**控制箱的位置**/
    V3 controlBoxLocation;
    /**
     * 建筑方向
     **/
    String buildDirection = "";
    /**
     * 采矿箱
     **/
    MiningBox miningBox;
    /**
     * 农田箱
     **/
    FarmingBox farmingBox;
    /**
     * 路径箱
     **/
    PathBox pathBox;
    /**
     * 职业
     **/
    Vocation vocation;
    /**
     * 鼠标计数
     **/
    private int mouseCount = 0;
    /**
     * 选定的NPC
     */
    private CopyOnWriteArrayList<GuiButton> selectedFolks = new CopyOnWriteArrayList();
    /**
     * 最大员工数
     */
    private int maxEmployees = 1;

    /**
     * 初始化
     *
     * @param controlBoxLocation
     * @param dir
     * @param vocation
     */
    public GuiEmployFolk(V3 controlBoxLocation, String dir, Vocation vocation) {
        try {
            this.controlBoxLocation = controlBoxLocation;
            this.buildDirection = dir;
            this.vocation = vocation;
            if (this.vocation == Vocation.BUILDER) {
                this.maxEmployees = 1;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public GuiEmployFolk(MiningBox b, Vocation v) {
        try {
            this.controlBoxLocation = b.location;
            this.vocation = v;
            this.miningBox = b;
            this.maxEmployees = 1;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var4.printStackTrace();
        }

    }

    public GuiEmployFolk(FarmingBox b, Vocation v) {
        try {
            this.controlBoxLocation = b.location;
            this.vocation = v;
            this.farmingBox = b;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var4.printStackTrace();
        }

    }

    public GuiEmployFolk(PathBox thePathBox, Vocation v) {
        try {
            this.controlBoxLocation = thePathBox.location;
            this.vocation = v;
            this.pathBox = thePathBox;
            this.maxEmployees = 1;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var4.printStackTrace();
        }

    }

    @Override
    public void func_73866_w_() {
        try {
            this.field_146292_n.clear();
            //取消
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 200, this.field_146295_m - 30, I18n.func_135052_a("container.sim.sim_gui_player_to_Cancel")));
            //好
            this.field_146292_n.add(new GuiButton(1000, this.field_146294_l / 2, this.field_146295_m - 30, I18n.func_135052_a("container.sim.gui_btn_name_OK")));
            CopyOnWriteArrayList folks = FolkData.getFolkUnemployed(false);
            int x = 10;
            int y = 40;
            int idx = 1;

            for (int f = 0; f < folks.size(); ++f) {
                FolkData folk = (FolkData) folks.get(f);
                String xp = "";
                //int ixp = false;
                int ixp;
                if (this.vocation == Vocation.BUILDER) {
                    ixp = (int) Math.floor((double) folk.levelBuilder);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.MINER) {
                    ixp = (int) Math.floor((double) folk.levelMiner);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.SOLDIER) {
                    ixp = (int) Math.floor((double) folk.levelSoldier);
                    xp = " (" + ixp + ")";
                }

                this.field_146292_n.add(new GuiButton(idx, x, y, 110, 20, folk.name + xp));
                idx++;
                x += 110;
                if (x + 110 > this.field_146294_l) {
                    x = 10;
                    y += 20;
                }

                if (y + 20 > this.field_146295_m - 50) {
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var9.printStackTrace();
        }

    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        try {
            this.func_146276_q_();
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_btn_name_Choose_who_you") + this.vocation.toString(), this.field_146294_l / 2, 17, 16777215);
            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var5.printStackTrace();
        }
    }

    @Override
    public void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else {
                    if (guibutton.field_146127_k > 0 && guibutton.field_146127_k < 1000 && this.selectedFolks.size() < this.maxEmployees) {
                        this.selectedFolks.add(guibutton);
                        guibutton.field_146124_l = false;
                    }

                    if (guibutton.field_146127_k == 1000) {
                        if (ModSimReloaded.states.credits <= 0.0F && GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.gui_sendChat_you_need"));
                            this.field_146297_k.field_71462_r = null;
                            this.field_146297_k.func_71381_h();
                            return;
                        }

                        if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE && this.vocation == Vocation.MERCHANT) {
                            ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.gui_sendChat_Builder_merchant"));
                            this.field_146297_k.field_71462_r = null;
                            this.field_146297_k.func_71381_h();
                            return;
                        }

                        CopyOnWriteArrayList<FolkData> efolks = new CopyOnWriteArrayList();

                        for (int w = 0; w < this.selectedFolks.size(); ++w) {
                            GuiButton button = (GuiButton) this.selectedFolks.get(w);
                            String folkname = button.field_146126_j;
                            if (folkname.contains("(")) {
                                folkname = button.field_146126_j.substring(0, button.field_146126_j.indexOf(" (")).trim();
                            }

                            FolkData f = FolkData.getFolkByName(folkname);
                            f.statusText = I18n.func_135052_a("container.sim.gui.button_Going");
                            efolks.add(f);
                            this.hireFolks(efolks);
                        }
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIEMPLOYFOLK-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public void hireFolks(CopyOnWriteArrayList<FolkData> efolks) {
        try {
            for (int i = 0; i < efolks.size(); i++) {
                FolkData efolk = (FolkData) efolks.get(i);
                efolk.employedAt = this.controlBoxLocation;
                efolk.setTheirJob(this.vocation);
                if (ModSimReloaded.isDayTime()) {
                    efolk.gotoXYZ(efolk.employedAt, GotoMethod.WALK);
                }
            }
            this.field_146297_k.field_71462_r = null;
            GuiBuildingConstructor ui;
            //建筑者
            if (this.vocation == Vocation.BUILDER) {
                ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
                this.field_146297_k.func_147108_a(ui);
                //地形师
            } else if (this.vocation == Vocation.TERRAFORMER) {
                ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
                this.field_146297_k.func_147108_a(ui);
                //矿工
            } else if (this.vocation == Vocation.MINER) {
                GuiMining uiGuiMining = new GuiMining(this.miningBox, efolks);
                this.field_146297_k.func_147108_a(uiGuiMining);
                //农作物种植者
            } else if (this.vocation == Vocation.CROPFARMER) {
                GuiFarming uiFarming = new GuiFarming(this.farmingBox, (FolkData) efolks.get(0));
                this.field_146297_k.func_147108_a(uiFarming);
                //路径生成器
            } else if (this.vocation == Vocation.PATHBUILDER) {
                GuiPathBox uiGuiPathBox = new GuiPathBox(this.pathBox, efolks);
                this.field_146297_k.func_147108_a(uiGuiPathBox);
            } else {
                GuiControlBox uiGuiControlBox = new GuiControlBox(this.controlBoxLocation, (FolkData) efolks.get(0));
                this.field_146297_k.func_147108_a(uiGuiControlBox);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("hireFolks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    public void func_146281_b() {
        try {
            Keyboard.enableRepeatEvents(false);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void func_73869_a(char c, int i) {
        try {
            if (i == 1) {
                this.field_146297_k.func_147108_a((GuiScreen) null);
                this.field_146297_k.func_71381_h();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
        // TODO document why this method is empty
    }
}
