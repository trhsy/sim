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
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73866_w_() {
        try {
            if (this.theFarmingBox.level == 0) {
                this.theFarmingBox.level = 1;
            }

            this.field_146292_n.clear();

            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, I18n.func_135052_a("container.sim.sim_gui_BC_Done")));
            if (this.theFolk == null) {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Hire25")));
            } else {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 40, I18n.func_135052_a("container.sim.Fire") + " " + this.theFolk.name));
            }

            //农场
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 100, this.theFarmingBox.farmType.toString() + I18n.func_135052_a("container.sim.gui_Farm")));
            GuiButton b;
            switch (this.theFarmingBox.level) {
                case 1:
                    if (this.theFarmingBox.farmType != FarmType.SUGAR && this.theFarmingBox.farmType != FarmType.CACTUS) {
                        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, I18n.func_135052_a("container.sim.gui_Farming_Upgrade") + (this.theFarmingBox.level + 1)));
                    } else {
                        //全面升级
                        this.field_146292_n.add(b = new GuiButton(3, this.field_146294_l / 2 - 100, 140, I18n.func_135052_a("container.sim.gui_Farming_Fully_upgraded")));
                        b.field_146124_l = false;
                    }
                    break;
                case 2:
                    this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, 140, I18n.func_135052_a("container.sim.gui_Farming_Upgrade") + (this.theFarmingBox.level + 1)));
                    break;
                case 3:
                    this.field_146292_n.add(b = new GuiButton(3, this.field_146294_l / 2 - 100, 140, I18n.func_135052_a("container.sim.gui_Farming_Fully_upgraded")));
                    b.field_146124_l = false;
            }
            super.func_73866_w_();
        } catch (Exception e) {
            ModSimReloaded.log.error("initGui出错了：" + e.getMessage());
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
    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }
            this.func_146276_q_();
            if (this.theFarmingBox == null) {
                //养殖箱出错,放置3个标记,然后放置养殖箱
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_ERROR_WITH"), this.field_146294_l / 2, 17, 16777215);
                return;
            }

            this.func_73732_a(this.field_146289_q, " " + this.theFarmingBox.level + I18n.func_135052_a("container.sim.gui_Level") + this.theFarmingBox.farmType.toString() + I18n.func_135052_a("container.sim.gui_Farm"), this.field_146294_l / 2, 17, 8454016);

            try {
                if (this.theFarmingBox.marker1XYZ == null) {
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_No"), this.field_146294_l / 2, 27, 16711680);
                }
            } catch (Exception var5) {
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_Place"), this.field_146294_l / 2, 27, 16711680);
            }

            if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.SUGAR) {
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_cactus"), this.field_146294_l / 2, 130, 16777215);
            } else if (this.theFarmingBox.level == 1 && this.theFarmingBox.farmType == FarmType.CACTUS) {
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_sugar"), this.field_146294_l / 2, 130, 16777215);
            } else if (this.theFarmingBox.level < 3) {
                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_Upgrade_will_cost") + ModSimReloaded.displayMoney(this.getUpgradeCost()) + I18n.func_135052_a("container.sim.gui_Farming_text_credits"), this.field_146294_l / 2, 130, 16777215);
                } else {
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Farming_text_Upgrade_is_Free"), this.field_146294_l / 2, 130, 16777215);
                }
            }

            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage());
        }

    }

    /**
     * 执行的操作
     *
     * @param guibutton
     */
    @Override
    public void func_146284_a(GuiButton guibutton) {
        try {

            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else {
                    //雇佣农民
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire25"))) {
                        GuiEmployFolk ui = new GuiEmployFolk(this.theFarmingBox, Vocation.CROPFARMER);
                        this.field_146297_k.func_147108_a(ui);
                        //解雇
                    } else if (guibutton.field_146126_j.contains(I18n.func_135052_a("container.sim.Fire"))) {
                        this.theFolk.selfFire();
                        guibutton.field_146124_l = false;
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                    } else if (guibutton.field_146127_k == 2) {
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
                        guibutton.field_146126_j = this.theFarmingBox.farmType.toString() + I18n.func_135052_a("container.sim.gui_Farm");
                    } else if (guibutton.field_146127_k == 3) {
                        //获取金币
                        float cash = ModSimReloaded.states.credits;
                        //如果游戏模式为创造 资金为1000
                        if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                            cash = 1000.0F;
                        }

                        if (this.getUpgradeCost() > cash) {
                            //资金不足
                            guibutton.field_146126_j = I18n.func_135052_a("container.sim.gui_Farming_text_NOT_ENOUGH");
                            guibutton.field_146124_l = false;
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
                                this.field_146297_k.field_71462_r = null;
                                //失去焦点
                                this.field_146297_k.func_71381_h();
                                return;
                            }
                            //农场太小了
                            guibutton.field_146126_j = I18n.func_135052_a("container.sim.gui_Farming_text_TOO_SMALL");
                            guibutton.field_146124_l = false;
                        }
                    }

                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
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
            ModSimReloaded.log.error("getUpgradeCost出错了：" + e.getMessage());
        }
        return ret;
    }
}

