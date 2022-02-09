package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.Relationship;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
 * @Description todo
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

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void updateScreen() {
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.showPage();
    }

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
            this.drawCenteredString(this.fontRendererObj, "Hello " + name[1] + ", Here's my information...", this.width / 2, 10, 16777215);
            this.fontRendererObj.drawString("Name", left, 27, 0);
            this.fontRendererObj.drawString(this.theFolk.name, sec, 27, 128);
            this.fontRendererObj.drawString("Age", left, 37, 0);
            if (this.theFolk.age > 1) {
                this.fontRendererObj.drawString(this.theFolk.age + " years old", sec, 37, 128);
            } else if (this.theFolk.age == 1) {
                this.fontRendererObj.drawString("1 year old", sec, 37, 128);
            } else {
                this.fontRendererObj.drawString("Less than 1 year", sec, 37, 128);
            }

            String words = "";
            if (this.theFolk.gender == 0) {
                words = "Male ";
            } else {
                words = "Female ";
            }

            if (this.theFolk.age >= 18) {
                words = words + "adult";
            } else {
                words = words + "child";
            }

            this.fontRendererObj.drawString("Gender", left, 47, 0);
            this.fontRendererObj.drawString(words, sec, 47, 128);
            if (this.theFolk.employedAt == null) {
                words = "unemployed";
            } else {
                words = this.theFolk.vocation.toString();
            }

            this.fontRendererObj.drawString("Job", left, 57, 0);
            if (this.theFolk.age >= 18) {
                this.fontRendererObj.drawString(words, sec, 57, 128);
            } else {
                this.fontRendererObj.drawString("N/A", sec, 57, 128);
            }

            if (this.theFolk.age >= 18) {
                if (this.theFolk.getHome() == null) {
                    words = "Homeless";
                } else {
                    words = "Tennant";
                }
            } else {
                words = "Living with parents";
            }

            this.fontRendererObj.drawString("Housing status", left, 67, 0);
            this.fontRendererObj.drawString(words, sec, 67, 128);
            if (!Relationship.isFolkLivingWithSomeone(this.theFolk)) {
                words = "Single";
            } else {
                String who = "You";
                FolkData whofd = Relationship.isFolkLivingWithSomeone(this.theFolk, true);
                if (whofd != null) {
                    who = whofd.name;
                }

                words = "Living with " + who;
            }

            this.fontRendererObj.drawString("Relationship", left, 77, 0);
            this.fontRendererObj.drawString(words, sec, 77, 128);
            words = "Unknown";

            try {
                words = this.theFolk.action.toString();
            } catch (Exception var12) {
            }

            this.fontRendererObj.drawString("Status", left, 87, 0);
            this.fontRendererObj.drawString(words, sec, 87, 128);
            this.fontRendererObj.drawString("Building skill level", left, 97, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelBuilder + " of 10", sec, 97, 128);
            double w = 128.0D * ((double)(this.theFolk.levelBuilder % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 97, (int)w + sec, 105, 1358888960, 1358954240);
            this.fontRendererObj.drawString("Mining skill level", left, 107, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelMiner + " of 10", sec, 107, 128);
            w = 128.0D * ((double)(this.theFolk.levelMiner % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 107, (int)w + sec, 115, 1358888960, 1358954240);
            if (this.theFolk.levelSoldier < 1.0F) {
                this.theFolk.levelSoldier = 1.0F;
            }

            this.fontRendererObj.drawString("Soldier skill level", left, 117, 0);
            this.fontRendererObj.drawString((int)this.theFolk.levelSoldier + " of 10", sec, 117, 128);
            w = 128.0D * ((double)(this.theFolk.levelSoldier % 1.0F * 1000.0F) / 1000.0D);
            this.drawGradientRect(sec, 117, (int)w + sec, 125, 1358888960, 1358954240);
            if (this.theFolk.pregnancyStage > 0.0F) {
                this.fontRendererObj.drawString("Medical status", left, 127, 0);
                String days = (int)(this.theFolk.pregnancyStage * 9.0F) + "";
                if (days.contentEquals("0")) {
                    days = "Pregnant";
                } else if (days.contentEquals("1")) {
                    days = "one day pregnant";
                } else {
                    days = days + " days pregnant";
                }

                this.fontRendererObj.drawString(days, sec, 127, 128);
            }
        } else if (this.currentPage == 1) {
            left = this.width / 2 - 125;
            sec = 30;
            this.drawCenteredString(this.fontRendererObj, this.theFolk.name + "'s Relationships", this.width / 2, 10, 16777215);

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
            this.drawCenteredString(this.fontRendererObj, this.theFolk.name + "'s Needs", this.width / 2, 10, 16777215);
            this.fontRendererObj.drawString("Hunger:", left, 30, 0);
            this.fontRendererObj.drawString(this.theFolk.status4, this.width / 2, 30, 128);
            this.fontRendererObj.drawString("Fun", left, 40, 0);
            this.fontRendererObj.drawString(this.theFolk.status5, this.width / 2, 40, 128);
        }

        super.drawScreen(i, j, f);
    }

    private void showPage() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 2, this.height - 22, 50, 20, "Goodbye!"));
        if (this.currentPage == 0) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 140, 100, 20, "Relationships"));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 180, 100, 20, "Needs"));
        } else if (this.currentPage == 1) {
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, "Back"));
            if (this.relOffset > 0) {
                this.buttonList.add(new GuiButton(2, this.width / 2 - 125, 8, 20, 20, "<"));
            }

            int rels = this.folksRelationships.size();
            if (rels - this.relOffset > 18) {
                this.buttonList.add(new GuiButton(3, this.width / 2 + 105, 8, 20, 20, ">"));
            }
        } else if (this.currentPage == 2) {
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, "Back"));
        }

    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }

            if (this.currentPage == 0) {
                if (guibutton.displayString.contentEquals("Relationships")) {
                    this.currentPage = 1;
                    this.showPage();
                }

                if (guibutton.displayString.contentEquals("Needs")) {
                    this.currentPage = 2;
                    this.showPage();
                }
            } else if (this.currentPage == 1) {
                if (guibutton.displayString.contentEquals("Back")) {
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
            } else if (this.currentPage == 2 && guibutton.displayString.contentEquals("Back")) {
                this.currentPage = 0;
                this.showPage();
            }

        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }
}
