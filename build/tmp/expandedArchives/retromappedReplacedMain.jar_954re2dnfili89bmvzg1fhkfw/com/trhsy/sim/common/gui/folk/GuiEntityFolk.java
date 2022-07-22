package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.References;
import com.trhsy.sim.common.entity.Relationship;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiEntityFolk
 * @Description todo 实体人
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:29
 * ========================================
 **/
public class GuiEntityFolk extends GuiScreen {
    private static final ResourceLocation myBackgroundTexture = new ResourceLocation(ModSim.MODID + "", "textures/gui/guiFolk.png");
    private int currentPage = 0;
    private int mouseCount = 0;
    private FolkData theFolk;
    private EntityPlayer entityplayer;
    private ArrayList<Relationship> folksRelationships;
    private int relOffset = 0;

    public GuiEntityFolk(FolkData f, EntityPlayer entityplayer) {
        try {
            this.theFolk = f;
            this.entityplayer = entityplayer;
            this.folksRelationships = Relationship.getRelationshipsFor(this.theFolk);
        } catch (Exception e) {
            ModSimReloaded.log.error("GuiEntityFolk出错了：" + e.getMessage());
        }

    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
        // theGuiTextField1.updateCursorCounter();
    }

    @Override
    public void func_73866_w_() {
        try {
            Keyboard.enableRepeatEvents(true);
            this.showPage();
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_73863_a(int i, int j, float f) {

        try {
            if (this.mouseCount < 10) {
                this.mouseCount++;
                Mouse.setGrabbed(false);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //1.6.2 中的新功能
            this.field_146297_k.field_71446_o.func_110577_a(myBackgroundTexture);
            int posX = (this.field_146294_l - 256) / 2;
            this.func_73729_b(posX, 5, 0, 0, 256, 256);
            if (this.currentPage == 0) {
                int left = this.field_146294_l / 2 - 120;
                int sec = this.field_146294_l / 2;
                String[] name = Minecraft.func_71410_x().field_71439_g.toString().split("'");
                //你好 这是我的信息
                this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Folk_hello") + name[1] + " , " + I18n.func_135052_a("container.sim.gui_Folk_Here_my") + "...", this.field_146294_l / 2, 10, 16777215);
                //姓名
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Name"), left, 27, 0);
                this.field_146289_q.func_78276_b(this.theFolk.name, sec, 27, 128);
                //年龄
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Age"), left, 37, 0);
                if (this.theFolk.age > 1) {
                    this.field_146289_q.func_78276_b(this.theFolk.age + I18n.func_135052_a("container.sim.gui_Folk_years_old"), sec, 37, 128);
                } else if (this.theFolk.age == 1) {
                    this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_1_year_old"), sec, 37, 128);
                } else {
                    this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Less_than"), sec, 37, 128);
                }

                String words = "";
                if (this.theFolk.gender == 0) {
                    words = I18n.func_135052_a("container.sim.gui_Folk_Male");
                } else {
                    words = I18n.func_135052_a("container.sim.gui_Folk_Female");
                }

                if (this.theFolk.age >= 18) {
                    words = words + I18n.func_135052_a("container.sim.gui_Folk_adult");
                } else {
                    words = words + I18n.func_135052_a("container.sim.gui_Folk_child");
                }

                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Gender"), left, 47, 0);
                this.field_146289_q.func_78276_b(words, sec, 47, 128);
                if (this.theFolk.employedAt == null) {
                    words = I18n.func_135052_a("container.sim.gui_Folk_unemployed");
                } else {
                    words = this.theFolk.vocation.toString();
                }

                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Job"), left, 57, 0);
                if (this.theFolk.age >= 18) {
                    this.field_146289_q.func_78276_b(words, sec, 57, 128);
                } else {
                    this.field_146289_q.func_78276_b("N/A", sec, 57, 128);
                }

                if (this.theFolk.age >= 18) {
                    if (this.theFolk.getHome() == null) {
                        //无家可归
                        words = I18n.func_135052_a("container.sim.gui_Folk_Homeless");
                    } else {
                        //租户
                        words = I18n.func_135052_a("container.sim.gui_Folk_Tenant");
                    }
                } else {
                    //与父母同住
                    words = I18n.func_135052_a("container.sim.gui_Folk_Living");
                }
                //住房状况
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Housing_status"), left, 67, 0);
                this.field_146289_q.func_78276_b(words, sec, 67, 128);
                if (!Relationship.isFolkLivingWithSomeone(this.theFolk)) {
                    //单身狗
                    words = I18n.func_135052_a("container.sim.gui_Folk_Single");
                } else {
                    //你
                    String who = I18n.func_135052_a("container.sim.gui_Folk_You");
                    FolkData whofd = Relationship.isFolkLivingWithSomeone(this.theFolk, true);
                    if (whofd != null) {
                        who = whofd.name;
                    }
                    //和...一起生活
                    words = I18n.func_135052_a("container.sim.gui_Folk_Living_with") + who;
                }
                //情感
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Relationship"), left, 77, 0);
                this.field_146289_q.func_78276_b(words, sec, 77, 128);
                words = I18n.func_135052_a("container.sim.gui_Folk_Unknown");

                try {
                    words = this.theFolk.action.toString();
                } catch (Exception var12) {
                }
                //状态
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Status"), left, 87, 0);
                this.field_146289_q.func_78276_b(words, sec, 87, 128);
                //建设技能水平
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Building_skill"), left, 97, 0);
                this.field_146289_q.func_78276_b((int) this.theFolk.levelBuilder + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 97, 128);
                double w = 128 * ((double) (this.theFolk.levelBuilder % 1.0F * 1000.0F) / 1000);
                this.func_73733_a(sec, 97, (int) w + sec, 105, 1358888960, 1358954240);
                //挖矿技能等级
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Mining_skill"), left, 107, 0);
                this.field_146289_q.func_78276_b((int) this.theFolk.levelMiner + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 107, 128);
                w = 128 * ((double) (this.theFolk.levelMiner % 1.0F * 1000.0F) / 1000);
                this.func_73733_a(sec, 107, (int) w + sec, 115, 1358888960, 1358954240);
                if (this.theFolk.levelSoldier < 1.0F) {
                    this.theFolk.levelSoldier = 1.0F;
                }
                //士兵技能等级
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Soldier_skill"), left, 117, 0);
                this.field_146289_q.func_78276_b((int) this.theFolk.levelSoldier + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 117, 128);
                w = 128 * ((double) (this.theFolk.levelSoldier % 1.0F * 1000.0F) / 1000);
                this.func_73733_a(sec, 117, (int) w + sec, 125, 1358888960, 1358954240);
                if (this.theFolk.pregnancyStage > 0.0F) {
                    //医疗状况
                    this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Medical_status"), left, 127, 0);
                    String days = (int) (this.theFolk.pregnancyStage * 9.0F) + "";
                    if (days.contentEquals("0")) {
                        //孕
                        days = I18n.func_135052_a("container.sim.gui_Folk_Pregnant");
                    } else if (days.contentEquals("1")) {
                        //怀孕一天
                        days = I18n.func_135052_a("container.sim.gui_Folk_day_pregnant");
                    } else {
                        //怀孕天数
                        days = days + I18n.func_135052_a("container.sim.gui_Folk_days_pregnant");
                    }

                    this.field_146289_q.func_78276_b(days, sec, 127, 128);
                }
            } else if (this.currentPage == 1) {
                int left = this.field_146294_l / 2 - 125;
                int sec = 30;
                //的关系
                this.func_73732_a(this.field_146289_q, this.theFolk.name + I18n.func_135052_a("container.sim.gui_Folk_Relationships"), this.field_146294_l / 2, 10, 16777215);

                for (int r = this.relOffset; r < this.folksRelationships.size(); r++) {
                    try {
                        Relationship rel = (Relationship) this.folksRelationships.get(r);
                        String[] sp = rel.toStringPersepctive(this.theFolk).split(": ");
                        this.field_146289_q.func_78276_b(sp[0], left, sec, 0);
                        this.field_146289_q.func_78276_b(sp[1], this.field_146294_l / 2, sec, 128);
                        sec += 10;
                        if (sec > 200) {
                            break;
                        }
                    } catch (Exception var13) {
                    }
                }
            } else if (this.currentPage == 2) {
                int left = this.field_146294_l / 2;
                int disoffset = 0;
                //XX的需要
                this.func_73732_a(this.field_146289_q, this.theFolk.name + I18n.func_135052_a("container.sim.gui_Folk_Needs"), this.field_146294_l / 2, 10, 16777215);
                //饥饿
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Hunger") + ":", this.field_146294_l / 3, 20, 0);
                this.field_146289_q.func_78276_b(this.theFolk.status4, this.field_146294_l / 2, 20, 128);
                //乐趣
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Fun") + ":", this.field_146294_l / 3, 30, 0);
                this.field_146289_q.func_78276_b(this.theFolk.funStatus, this.field_146294_l / 2, 30, 128);
                //社交
                field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.guiFolk.Social") + ":", this.field_146294_l / 3, 40, 0x000000);
                field_146289_q.func_78276_b(theFolk.socialStatus, this.field_146294_l / 2, 40, 0x000080);
                //环境
                field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.guiFolk.Environment") + ":", this.field_146294_l / 3, 50, 0x000000);
                field_146289_q.func_78276_b(theFolk.environmentStatus, this.field_146294_l / 2, 50, 0x000080);
            } else if (currentPage == 3) {
                int left = this.field_146294_l / 2 - 120;

                field_146289_q.func_78276_b(theFolk.trait1, left, 30, 0x000000);

                field_146289_q.func_78276_b(theFolk.trait2, left, 50, 0x000000);

                field_146289_q.func_78276_b(theFolk.trait3, left, 70, 0x000000);

                field_146289_q.func_78276_b(theFolk.trait4, left, 90, 0x000000);
            }
            // theGuiTextField1.drawTextBox();
            super.func_73863_a(i, j, f);
        } catch (Exception e) {
            ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage());
        }
    }

    private void showPage() {
        try {
            this.field_146292_n.clear();
            //再见
            this.field_146292_n.add(new GuiButton(0, 2, this.field_146295_m - 22, 50, 20, I18n.func_135052_a("container.sim.sim_gui_BC_bye")));
            if (this.currentPage == 0) {
                //关系
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 130, 100, 20, I18n.func_135052_a("container.sim.gui_Folk_Relationshipss")));
                //需要
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 150, 100, 20, I18n.func_135052_a("container.sim.gui_Folk_Needss")));
                //特征
                this.field_146292_n.add(new GuiButton(1, field_146294_l / 2 - 50, 170, 100, 20, I18n.func_135052_a("container.sim.guiFolk.Traits")));
                //库存
                this.field_146292_n.add(new GuiButton(1, field_146294_l / 2 - 50, 190, 100, 20, I18n.func_135052_a("container.sim.guiFolk.Inventory")));
            } else if (this.currentPage == 1) {
                //返回
                this.field_146292_n.add(new GuiButton(1, 2, this.field_146295_m - 42, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Back")));
                if (this.relOffset > 0) {
                    this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 125, 8, 20, 20, "<"));
                }

                int rels = this.folksRelationships.size();
                if (rels - this.relOffset > 18) {
                    this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 105, 8, 20, 20, ">"));
                }
            } else if (this.currentPage == 2) {
                //返回
                this.field_146292_n.add(new GuiButton(1, 2, this.field_146295_m - 42, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Back")));
            } else if (this.currentPage == 3) {
                //返回
                this.field_146292_n.add(new GuiButton(1, 2, this.field_146295_m - 42, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Back")));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }


    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {
        try {
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                }

                if (this.currentPage == 0) {
                    //关系
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Relationshipss"))) {
                        this.currentPage = 1;
                        this.showPage();
                    }
                    //需要
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Needss"))) {
                        this.currentPage = 2;
                        this.showPage();
                    }
                    //特征
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.guiFolk.Traits"))) {
                        this.currentPage = 3;
                        this.showPage();
                    }
                    //库存
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.guiFolk.Inventory"))) {
                        EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
                        player.openGui(ModSim.instance, 0, player.field_70170_p, (int) player.field_70165_t, (int) player.field_70163_u, (int) player.field_70161_v);
                        player.field_71071_by.func_174889_b(player);
                        //PacketHandler.net.sendToServer(new OpenFolkInventoryPacket(References.GUI_FOLKINVENTORY));
                    }
                } else if (this.currentPage == 1) {
                    //情感分页  返回
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Back"))) {
                        this.currentPage = 0;
                        this.showPage();
                    }

                    if (guibutton.field_146126_j.contentEquals("<")) {
                        this.relOffset -= 18;
                        if (this.relOffset < 0) {
                            this.relOffset = 0;
                        }

                        this.showPage();
                    }

                    if (guibutton.field_146126_j.contentEquals(">")) {
                        this.relOffset += 18;
                        this.showPage();
                    }
                } else if (this.currentPage == 2) {
                    //需求分页 返回
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Back"))) {
                        this.currentPage = 0;
                        this.showPage();
                    }

                } else if (currentPage == 3) {
                    // 特征 page
                    if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Back"))) {
                        this.currentPage = 0;
                        this.showPage();
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_146281_b() {
        try {
            Keyboard.enableRepeatEvents(false);
        } catch (Exception e) {
            ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage());
        }

    }

    @Override
    protected void func_73869_a(char c, int i) {
        try {
            if (i == 1) {
                //逃跑，不保存
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage());
        }
    }

    @Override
    protected void func_73864_a(int i, int j, int k){
        // theGuiTextField1.mouseClicked(i, j, k);
        try {
            super.func_73864_a(i, j, k);
        } catch (Exception e) {
            ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage());
        }

    }
}
