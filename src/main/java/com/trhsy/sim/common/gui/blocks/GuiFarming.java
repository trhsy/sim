package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameMode;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiFarming
 * @Description todo 耕种
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:29
 * ========================================
 **/
public class GuiFarming extends GuiScreen {
    FolkData theFolk = null;
    FarmingBox theFarmingBox = null;
    private int mouseCount = 0;

    public GuiFarming(FarmingBox farmingBox, FolkData folk) {
        this.theFarmingBox = farmingBox;
        this.theFolk = folk;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        try {
            if (this.theFarmingBox.level == 0) {
                this.theFarmingBox.level = 1;
            }

            this.buttonList.clear();

            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
            if (this.theFolk == null) {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Hire25")));
            } else {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Fire") + " " + this.theFolk.name));
            }

            //农场
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 100, this.theFarmingBox.farmType.toString() + I18n.format("container.sim.gui_Farm")));
            GuiButton b;
            switch (this.theFarmingBox.level) {
                case 1:
                    if (this.theFarmingBox.farmType != FarmType.SUGAR && this.theFarmingBox.farmType != FarmType.CACTUS) {
                        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, I18n.format("container.sim.gui_Farming_Upgrade") + (this.theFarmingBox.level + 1)));
                    } else {
                        //全面升级
                        this.buttonList.add(b = new GuiButton(3, this.width / 2 - 100, 140, I18n.format("container.sim.gui_Farming_Fully_upgraded")));
                        b.enabled = false;
                    }
                    break;
                case 2:
                    this.buttonList.add(new GuiButton(3, this.width / 2 - 100, 140, I18n.format("container.sim.gui_Farming_Upgrade") + (this.theFarmingBox.level + 1)));
                    break;
                case 3:
                    this.buttonList.add(b = new GuiButton(3, this.width / 2 - 100, 140, I18n.format("container.sim.gui_Farming_Fully_upgraded")));
                    b.enabled = false;
            }
            super.initGui();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 绘制屏幕
     *
     * @param i
     * @param j
     * @param f
     */
    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            this.drawDefaultBackground();
            if (this.theFarmingBox == null) {
                //养殖箱出错,放置3个标记,然后放置养殖箱
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_ERROR_WITH"), this.width / 2, 17, 16777215);
                return;
            }

            this.drawCenteredString(this.fontRendererObj, " " + this.theFarmingBox.level + I18n.format("container.sim.gui_Level") + this.theFarmingBox.farmType.toString() + I18n.format("container.sim.gui_Farm"), this.width / 2, 17, 8454016);

            try {
                if (this.theFarmingBox.marker1XYZ == null) {
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_No"), this.width / 2, 27, 16711680);
                }
            } catch (Exception e) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_Place"), this.width / 2, 27, 16711680);
            }

            if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.SUGAR) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_cactus"), this.width / 2, 130, 16777215);
            } else if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.CACTUS) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_sugar"), this.width / 2, 130, 16777215);
            } else if (this.theFarmingBox.level < 3) {
                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_Upgrade_will_cost") + ModSimReloaded.displayMoney(this.getUpgradeCost()) + I18n.format("container.sim.gui_Farming_text_credits"), this.width / 2, 130, 16777215);
                } else {
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farming_text_Upgrade_is_Free"), this.width / 2, 130, 16777215);
                }
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 执行的操作
     *
     * @param guibutton
     */
    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {

            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    //雇佣农民
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire25"))) {
                        GuiEmployFolk ui = new GuiEmployFolk(this.theFarmingBox, Vocation.CROPFARMER);
                        this.mc.displayGuiScreen(ui);
                        //解雇
                    } else if (guibutton.displayString.contains(I18n.format("container.sim.Fire"))) {
                        this.theFolk.selfFire();
                        guibutton.enabled = false;
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    } else if (guibutton.id == 2) {
                        //马铃薯
                        if (this.theFarmingBox.farmType == FarmType.POTATO) {
                            this.theFarmingBox.farmType = FarmType.PUMPKIN;
                            //南瓜
                        } else if (this.theFarmingBox.farmType == FarmType.PUMPKIN) {
                            this.theFarmingBox.farmType = FarmType.MELON;
                            //西瓜
                        } else if (this.theFarmingBox.farmType == FarmType.MELON) {
                            this.theFarmingBox.farmType = FarmType.WHEAT;
                            //小麦
                        } else if (this.theFarmingBox.farmType == FarmType.WHEAT) {
                            this.theFarmingBox.farmType = FarmType.CARROT;
                            //胡萝卜
                        } else if (this.theFarmingBox.farmType == FarmType.CARROT) {
                            this.theFarmingBox.farmType = FarmType.CUSTOM;
                            //自定义
                        } else if (this.theFarmingBox.farmType == FarmType.CUSTOM) {
                            this.theFarmingBox.farmType = FarmType.SUGAR;
                            //甘蔗
                        } else if (this.theFarmingBox.farmType == FarmType.SUGAR) {
                            this.theFarmingBox.farmType = FarmType.CACTUS;
                            //仙人掌
                        } else if (this.theFarmingBox.farmType == FarmType.CACTUS) {
                            this.theFarmingBox.farmType = FarmType.POTATO;
                        }
                        //农场
                        guibutton.displayString = this.theFarmingBox.farmType.toString() + I18n.format("container.sim.gui_Farm");
                    } else if (guibutton.id == 3) {
                        //获取金币
                        float cash = ModSimReloaded.states.credits;
                        //如果游戏模式为创造 资金为1000
                        if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                            cash = 1000.0F;
                        }

                        if (this.getUpgradeCost() > cash) {
                            //资金不足
                            guibutton.displayString = I18n.format("container.sim.gui_Farming_text_NOT_ENOUGH");
                            guibutton.enabled = false;
                        } else {
                            //如果长 宽 都大于4
                            if (this.theFarmingBox.getSizeLength() >= 4 && this.theFarmingBox.getSizeWidth() >= 4) {
                                //如果游戏模式为创造
                                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                    GameStates var10000 = ModSimReloaded.states;
                                    var10000.credits -= this.getUpgradeCost();
                                }
                                //农场升级计数
                                ModSimReloaded.farmToUpgradeCounter = 0;
                                //要升级的农场
                                ModSimReloaded.farmToUpgrade = this.theFarmingBox;
                                //当前
                                this.mc.currentScreen = null;
                                //失去焦点
                                this.mc.setIngameFocus();
                                return;
                            }
                            //农场太小了
                            guibutton.displayString = I18n.format("container.sim.gui_Farming_text_TOO_SMALL");
                            guibutton.enabled = false;
                        }
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 获取升级成本
     *
     * @return
     */
    private Float getUpgradeCost() {
        Float ret =null;
        try {
            ret=(float) (this.theFarmingBox.getSizeLength() * this.theFarmingBox.getSizeWidth());
            ret = ret / 15.0F;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getUpgradeCost出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }
}

