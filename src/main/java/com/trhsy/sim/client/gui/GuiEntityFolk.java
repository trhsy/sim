package com.trhsy.sim.client.gui;/**
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
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.showPage();
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(myBackgroundTexture);
        int posX = (this.width - 256) / 2;
        this.drawTexturedModalRect(posX, 5, 0, 0, 256, 256);
        int left;
        int sec;
        if (this.currentPage == 0) {
            left = this.width / 2 - 120;
            sec = this.width / 2;
            String[] name = Minecraft.getMinecraft().thePlayer.toString().split("'");
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Folk_hello") + name[1] + " , "+I18n.format("container.sim.gui_Folk_Here_my")+"...", this.width / 2, 10, 16777215);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Name"), left, 27, 0);
            this.fontRendererObj.drawString(this.theFolk.name, sec, 27, 128);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Age"), left, 37, 0);
            if (this.theFolk.age > 1) {
                this.fontRendererObj.drawString(this.theFolk.age + I18n.format("container.sim.gui_Folk_years_old"), sec, 37, 128);
            } else if (this.theFolk.age == 1) {
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_1_year_old"), sec, 37, 128);
            } else {
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Less_than"), sec, 37, 128);
            }

            String words = "";
            if (this.theFolk.gender == 0) {
                words = I18n.format("container.sim.gui_Folk_Male");
            } else {
                words =I18n.format("container.sim.gui_Folk_Female");
            }

            if (this.theFolk.age >= 18) {
                words = words + I18n.format("container.sim.gui_Folk_adult");
            } else {
                words = words + I18n.format("container.sim.gui_Folk_child");
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Gender"), left, 47, 0);
            this.fontRendererObj.drawString(words, sec, 47, 128);
            if (this.theFolk.employedAt == null) {
                words = I18n.format("container.sim.gui_Folk_unemployed");
            } else {
                words = this.theFolk.vocation.toString();
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Job"), left, 57, 0);
            if (this.theFolk.age >= 18) {
                this.fontRendererObj.drawString(words, sec, 57, 128);
            } else {
                this.fontRendererObj.drawString("N/A", sec, 57, 128);
            }

            if (this.theFolk.age >= 18) {
                if (this.theFolk.getHome() == null) {
                    words = I18n.format("container.sim.gui_Folk_Homeless");
                } else {
                    words = I18n.format("container.sim.gui_Folk_Tenant");
                }
            } else {
                words = I18n.format("container.sim.gui_Folk_Living");
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Housing_status"), left, 67, 0);
            this.fontRendererObj.drawString(words, sec, 67, 128);
            if (!Relationship.isFolkLivingWithSomeone(this.theFolk)) {
                words = I18n.format("container.sim.gui_Folk_Single");
            } else {
                String who = I18n.format("container.sim.gui_Folk_You");
                FolkData whofd = Relationship.isFolkLivingWithSomeone(this.theFolk, true);
                if (whofd != null) {
                    who = whofd.name;
                }

                words = I18n.format("container.sim.gui_Folk_Living_with") + who;
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Relationship"), left, 77, 0);
            this.fontRendererObj.drawString(words, sec, 77, 128);
            words = I18n.format("container.sim.gui_Folk_Unknown");

            try {
                words = this.theFolk.action.toString();
            } catch (Exception var12) {
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Status"), left, 87, 0);
            this.fontRendererObj.drawString(words, sec, 87, 128);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Building_skill"), left, 97, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelBuilder + I18n.format("container.sim.gui_Folk_of_10"), sec, 97, 128);
            double w = 128.0D * ((double)(this.theFolk.levelBuilder % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 97, (int)w + sec, 105, 1358888960, 1358954240);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Mining_skill"), left, 107, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelMiner + I18n.format("container.sim.gui_Folk_of_10"), sec, 107, 128);
            w = 128.0D * ((double)(this.theFolk.levelMiner % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 107, (int)w + sec, 115, 1358888960, 1358954240);
            if (this.theFolk.levelSoldier < 1.0F) {
                this.theFolk.levelSoldier = 1.0F;
            }

            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Soldier_skill"), left, 117, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelSoldier + I18n.format("container.sim.gui_Folk_of_10"), sec, 117, 128);
            w = 128.0D * ((double)(this.theFolk.levelSoldier % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 117, (int)w + sec, 125, 1358888960, 1358954240);
            if (this.theFolk.pregnancyStage > 0.0F) {
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Medical_status"), left, 127, 0);
                String days = (int)(this.theFolk.pregnancyStage * 9.0F) + "";
                if (days.contentEquals("0")) {
                    days = I18n.format("container.sim.gui_Folk_Pregnant");
                } else if (days.contentEquals("1")) {
                    days = I18n.format("container.sim.gui_Folk_day_pregnant");
                } else {
                    days = days + I18n.format("container.sim.gui_Folk_days_pregnant");
                }

                this.fontRendererObj.drawString(days, sec, 127, 128);
            }
        } else if (this.currentPage == 1) {
            left = this.width / 2 - 125;
            sec = 30;
            this.drawCenteredString(this.fontRendererObj, this.theFolk.name + I18n.format("container.sim.gui_Folk_Relationships"), this.width / 2, 10, 16777215);

            for(int r = this.relOffset; r < this.folksRelationships.size(); ++r) {
                try {
                    Relationship rel = (Relationship)this.folksRelationships.get(r);
                    String[] sp = rel.toStringPersepctive(this.theFolk).split(": ");
                    this.fontRendererObj.drawString(sp[0], left, sec, 0);
                    this.fontRendererObj.drawString(sp[1], this.width / 2, sec, 128);
                    sec += 10;
                    if (sec > 200) {
                        break;
                    }
                } catch (Exception var13) {
                }
            }
        } else if (this.currentPage == 2) {
            left = this.width / 2;
            //int disoffset = true;
            this.drawCenteredString(this.fontRendererObj, this.theFolk.name + I18n.format("container.sim.gui_Folk_Needs"), this.width / 2, 10, 16777215);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Hunger")+":", left, 50, 0);
            this.fontRendererObj.drawString(this.theFolk.status4, this.width / 2, 30, 128);
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Fun"), left, 50, 0);
            this.fontRendererObj.drawString(this.theFolk.status5, this.width / 2, 40, 128);
        }

        super.drawScreen(i, j, f);
    }

    private void showPage() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 2, this.height - 22, 50, 20, I18n.format("container.sim.gui_Folk_Goodbye")+"!"));
        if (this.currentPage == 0) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 140, 100, 20, I18n.format("container.sim.gui_Folk_Relationshipss")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 180, 100, 20, I18n.format("container.sim.gui_Folk_Needss")));
        } else if (this.currentPage == 1) {
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
            if (this.relOffset > 0) {
                this.buttonList.add(new GuiButton(2, this.width / 2 - 125, 8, 20, 20, "<"));
            }

            int rels = this.folksRelationships.size();
            if (rels - this.relOffset > 18) {
                this.buttonList.add(new GuiButton(3, this.width / 2 + 105, 8, 20, 20, ">"));
            }
        } else if (this.currentPage == 2) {
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
        }

    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }

            if (this.currentPage == 0) {
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Relationshipss"))) {
                    this.currentPage = 1;
                    this.showPage();
                }

                if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Needss"))) {
                    this.currentPage = 2;
                    this.showPage();
                }
            } else if (this.currentPage == 1) {
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                    this.currentPage = 0;
                    this.showPage();
                }

                if (guibutton.displayString.contentEquals("<")) {
                    this.relOffset -= 18;
                    if (this.relOffset < 0) {
                        this.relOffset = 0;
                    }

                    this.showPage();
                }

                if (guibutton.displayString.contentEquals(">")) {
                    this.relOffset += 18;
                    this.showPage();
                }
            } else if (this.currentPage == 2 && guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                this.currentPage = 0;
                this.showPage();
            }

        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }
}
