package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.Relationship;
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
        this.theFolk = f;
        this.entityplayer = entityplayer;
        this.folksRelationships = Relationship.getRelationshipsFor(this.theFolk);
    }
    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
    }

    @Override
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.showPage();
    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.field_71446_o.func_110577_a(myBackgroundTexture);
        int posX = (this.field_146294_l - 256) / 2;
        this.func_73729_b(posX, 5, 0, 0, 256, 256);
        int left;
        int sec;
        if (this.currentPage == 0) {
            left = this.field_146294_l / 2 - 120;
            sec = this.field_146294_l / 2;
            String[] name = Minecraft.func_71410_x().field_71439_g.toString().split("'");
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.gui_Folk_hello") + name[1] + " , "+I18n.func_135052_a("container.sim.gui_Folk_Here_my")+"...", this.field_146294_l / 2, 10, 16777215);
            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Name"), left, 27, 0);
            this.field_146289_q.func_78276_b(this.theFolk.name, sec, 27, 128);
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
                words =I18n.func_135052_a("container.sim.gui_Folk_Female");
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
                    words = I18n.func_135052_a("container.sim.gui_Folk_Homeless");
                } else {
                    words = I18n.func_135052_a("container.sim.gui_Folk_Tenant");
                }
            } else {
                words = I18n.func_135052_a("container.sim.gui_Folk_Living");
            }

            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Housing_status"), left, 67, 0);
            this.field_146289_q.func_78276_b(words, sec, 67, 128);
            if (!Relationship.isFolkLivingWithSomeone(this.theFolk)) {
                words = I18n.func_135052_a("container.sim.gui_Folk_Single");
            } else {
                String who = I18n.func_135052_a("container.sim.gui_Folk_You");
                FolkData whofd = Relationship.isFolkLivingWithSomeone(this.theFolk, true);
                if (whofd != null) {
                    who = whofd.name;
                }

                words = I18n.func_135052_a("container.sim.gui_Folk_Living_with") + who;
            }

            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Relationship"), left, 77, 0);
            this.field_146289_q.func_78276_b(words, sec, 77, 128);
            words = I18n.func_135052_a("container.sim.gui_Folk_Unknown");

            try {
                words = this.theFolk.action.toString();
            } catch (Exception var12) {
            }

            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Status"), left, 87, 0);
            this.field_146289_q.func_78276_b(words, sec, 87, 128);
            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Building_skill"), left, 97, 0);
            this.field_146289_q.func_78276_b((int)this.theFolk.levelBuilder + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 97, 128);
            double w = 128 * ((double)(this.theFolk.levelBuilder % 1.0F * 1000.0F) / 1000);
            this.func_73733_a(sec, 97, (int)w + sec, 105, 1358888960, 1358954240);
            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Mining_skill"), left, 107, 0);
            this.field_146289_q.func_78276_b((int)this.theFolk.levelMiner + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 107, 128);
            w = 128 * ((double)(this.theFolk.levelMiner % 1.0F * 1000.0F) / 1000);
            this.func_73733_a(sec, 107, (int)w + sec, 115, 1358888960, 1358954240);
            if (this.theFolk.levelSoldier < 1.0F) {
                this.theFolk.levelSoldier = 1.0F;
            }

            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Soldier_skill"), left, 117, 0);
            this.field_146289_q.func_78276_b((int)this.theFolk.levelSoldier + I18n.func_135052_a("container.sim.gui_Folk_of_10"), sec, 117, 128);
            w = 128 * ((double)(this.theFolk.levelSoldier % 1.0F * 1000.0F) / 1000);
            this.func_73733_a(sec, 117, (int)w + sec, 125, 1358888960, 1358954240);
            if (this.theFolk.pregnancyStage > 0.0F) {
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Medical_status"), left, 127, 0);
                String days = (int)(this.theFolk.pregnancyStage * 9.0F) + "";
                if (days.contentEquals("0")) {
                    days = I18n.func_135052_a("container.sim.gui_Folk_Pregnant");
                } else if (days.contentEquals("1")) {
                    days = I18n.func_135052_a("container.sim.gui_Folk_day_pregnant");
                } else {
                    days = days + I18n.func_135052_a("container.sim.gui_Folk_days_pregnant");
                }

                this.field_146289_q.func_78276_b(days, sec, 127, 128);
            }
        } else if (this.currentPage == 1) {
            left = this.field_146294_l / 2 - 125;
            sec = 30;
            this.func_73732_a(this.field_146289_q, this.theFolk.name + I18n.func_135052_a("container.sim.gui_Folk_Relationships"), this.field_146294_l / 2, 10, 16777215);

            for(int r = this.relOffset; r < this.folksRelationships.size(); ++r) {
                try {
                    Relationship rel = (Relationship)this.folksRelationships.get(r);
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
            left = this.field_146294_l / 2;
            //int disoffset = true;
            //XX的需要
            this.func_73732_a(this.field_146289_q, this.theFolk.name + I18n.func_135052_a("container.sim.gui_Folk_Needs"), this.field_146294_l / 2, 10, 16777215);
            //饥饿
            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Hunger")+":", this.field_146294_l /3, 20, 0);

            this.field_146289_q.func_78276_b(this.theFolk.status4, this.field_146294_l / 2, 20, 128);
            this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.gui_Folk_Fun")+":", this.field_146294_l / 3, 40, 0);
            this.field_146289_q.func_78276_b(this.theFolk.status5, this.field_146294_l / 2, 40, 128);
        }

        super.func_73863_a(i, j, f);
    }

    private void showPage() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 2, this.field_146295_m - 22, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Goodbye")+"!"));
        if (this.currentPage == 0) {
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 140, 100, 20, I18n.func_135052_a("container.sim.gui_Folk_Relationshipss")));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 180, 100, 20, I18n.func_135052_a("container.sim.gui_Folk_Needss")));
        } else if (this.currentPage == 1) {
            this.field_146292_n.add(new GuiButton(1, 2, this.field_146295_m - 42, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Back")));
            if (this.relOffset > 0) {
                this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 125, 8, 20, 20, "<"));
            }

            int rels = this.folksRelationships.size();
            if (rels - this.relOffset > 18) {
                this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 105, 8, 20, 20, ">"));
            }
        } else if (this.currentPage == 2) {
            this.field_146292_n.add(new GuiButton(1, 2, this.field_146295_m - 42, 50, 20, I18n.func_135052_a("container.sim.gui_Folk_Back")));
        }

    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            }

            if (this.currentPage == 0) {
                if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Relationshipss"))) {
                    this.currentPage = 1;
                    this.showPage();
                }

                if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Needss"))) {
                    this.currentPage = 2;
                    this.showPage();
                }
            } else if (this.currentPage == 1) {
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
            } else if (this.currentPage == 2 && guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_Folk_Back"))) {
                this.currentPage = 0;
                this.showPage();
            }

        }
    }

    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    @Override
    protected void func_73864_a(int i, int j, int k) throws IOException {
        super.func_73864_a(i, j, k);
    }
}
