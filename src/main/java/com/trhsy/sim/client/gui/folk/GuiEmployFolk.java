package com.trhsy.sim.client.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.gui.blocks.*;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameMode;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.core.entity.functionality.FarmingBox;
import com.trhsy.sim.common.core.entity.functionality.MiningBox;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;
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
@SideOnly(Side.CLIENT)
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
//    PathBox pathBox;
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
    private List<GuiButton> selectedFolks = new CopyOnWriteArrayList();
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
            //控制箱位置
            this.controlBoxLocation = controlBoxLocation;
            //建筑方向
            this.buildDirection = dir;
            //职业
            this.vocation = vocation;
            //如果是建筑工，最大雇员是1
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

    /*public GuiEmployFolk(PathBox thePathBox, Vocation v) {
        try {
            this.controlBoxLocation = thePathBox.location;
            this.vocation = v;
            this.pathBox = thePathBox;
            this.maxEmployees = 1;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var4.printStackTrace();
        }

    }*/

    @Override
    public void initGui() {
        try {
            this.buttonList.clear();
            //取消
            this.buttonList.add(new GuiButton(0, this.width / 2 - 200, this.height - 30, I18n.format("container.sim.sim_gui_player_to_Cancel")));
            //好
            this.buttonList.add(new GuiButton(1000, this.width / 2, this.height - 30, I18n.format("container.sim.gui_btn_name_OK")));
            List folks = FolkData.getFolkUnemployed(false);
            int x = 10;
            int y = 40;
            int idx = 1;

            for (int f = 0; f < folks.size(); ++f) {
                FolkData folk = (FolkData) folks.get(f);
                String xp = "";
                int ixp;
                if (this.vocation == Vocation.BUILDER) {
                    ixp = (int) Math.floor(folk.levelBuilder);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.MINER) {
                    ixp = (int) Math.floor( folk.levelMiner);
                    xp = " (" + ixp + ")";
                } else if (this.vocation == Vocation.SOLDIER) {
                    ixp = (int) Math.floor( folk.levelSoldier);
                    xp = " (" + ixp + ")";
                }

                this.buttonList.add(new GuiButton(idx, x, y, 110, 20, folk.name + xp));
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var9.printStackTrace();
        }

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            this.drawDefaultBackground();
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            //选择你想雇佣谁作为一名员工
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_btn_name_Choose_who_you") + this.vocation.toString(), this.width / 2, 17, 16777215);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEmployFolk-drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var5.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    //取消
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    if (guibutton.id > 0 && guibutton.id < 1000 && this.selectedFolks.size() < this.maxEmployees) {
                        this.selectedFolks.add(guibutton);
                        guibutton.enabled = false;
                    }
                    //好
                    if (guibutton.id == 1000) {
                        if (ModSimReloaded.states.credits <= 0.0F && GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            //你需要一些金币来雇佣员工。
                            ModSimReloaded.sendChat(I18n.format("container.sim.gui_sendChat_you_need"));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }

                        if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE && this.vocation == Vocation.MERCHANT) {
                            //建筑商的商人不能在创造性模式下雇佣
                            ModSimReloaded.sendChat(I18n.format("container.sim.gui_sendChat_Builder_merchant"));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }

                        List<FolkData> efolks = new CopyOnWriteArrayList();

                        for (int w = 0; w < this.selectedFolks.size(); ++w) {
                            GuiButton button = this.selectedFolks.get(w);
                            String folkname = button.displayString;
                            if (folkname.contains("(")) {
                                folkname = button.displayString.substring(0, button.displayString.indexOf(" (")).trim();
                            }

                            FolkData f = FolkData.getFolkByName(folkname);
                            //去我的新工作...
                            f.statusText = I18n.format("container.sim.gui.button_Going");
                            efolks.add(f);
                            //雇佣员工
                            this.hireFolks(efolks);
                        }
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIEMPLOYFOLK-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public void hireFolks(List<FolkData> efolks) {
        try {
            for (int i = 0; i < efolks.size(); i++) {
                //找到要雇佣的人
                FolkData efolk = efolks.get(i);
                //其雇佣地点为此建筑的控制箱
                efolk.employedAt = this.controlBoxLocation.clone();
                //职业为此建筑
                efolk.setTheirJob(this.vocation);
                //白天//此人是夜猫子
                if (ModSimReloaded.isDayTime()||efolk.isNightOwl()) {
                    V3 v3=new V3(efolk.employedAt.xCoord,efolk.employedAt.yCoord+1,efolk.employedAt.zCoord);
                    int dist = efolk.location.getDistanceTo(v3);
                    if(dist>10){
                        efolk.gotoXYZ(v3, GotoMethod.SHIFT);
                    }
                    //去雇佣地点
                    efolk.gotoXYZ(v3, null);
                }
            }
            this.mc.currentScreen = null;
            GuiBuildingConstructor ui;
            //建筑师
            if (this.vocation == Vocation.BUILDER) {
                ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
                this.mc.displayGuiScreen(ui);
                //地形师
            } else if (this.vocation == Vocation.TERRAFORMER) {
                ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
                this.mc.displayGuiScreen(ui);
                //矿工
            } else if (this.vocation == Vocation.MINER) {
                GuiMining uiGuiMining = new GuiMining(this.miningBox, efolks);
                this.mc.displayGuiScreen(uiGuiMining);
                //农作物种植者
            } else if (this.vocation == Vocation.CROPFARMER) {
                GuiFarming uiFarming = new GuiFarming(this.farmingBox, efolks.get(0));
                this.mc.displayGuiScreen(uiFarming);
                //路径生成器
            }/* else if (this.vocation == Vocation.PATHBUILDER) {
                GuiPathBox uiGuiPathBox = new GuiPathBox(this.pathBox, efolks);
                this.mc.displayGuiScreen(uiGuiPathBox);
            }*/ else {
                //控制箱
                GuiControlBox uiGuiControlBox = new GuiControlBox(this.controlBoxLocation, efolks.get(0));
                this.mc.displayGuiScreen(uiGuiControlBox);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("hireFolks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    public void onGuiClosed() {
        try {
            Keyboard.enableRepeatEvents(false);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        // TODO document why this method is empty
    }
}
