package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.References;
import com.trhsy.sim.common.core.entity.Relationship;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.PacketHandler;
import com.trhsy.sim.packets.toServer.OpenFolkInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private FolkData theFolk=new FolkData();;
    private EntityPlayer entityplayer;
    private List<Relationship> folksRelationships;
    private int relOffset = 0;

    public GuiEntityFolk(FolkData f, EntityPlayer entityplayer) {
        try {
            this.theFolk = f;
            this.entityplayer = entityplayer;
            this.folksRelationships = Relationship.getRelationshipsFor(this.theFolk);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiEntityFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        // theGuiTextField1.updateCursorCounter();
    }

    @Override
    public void initGui() {
        try {
            Keyboard.enableRepeatEvents(true);
            this.showPage();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {

        try {
            if (this.mouseCount < 10) {
                this.mouseCount++;
                Mouse.setGrabbed(false);
            }

            GL11.glColor4f(1, 1, 1, 1);
            //1.6.2 中的新功能
            this.mc.renderEngine.bindTexture(myBackgroundTexture);
            int posX = (this.width - 256) / 2;
            this.drawTexturedModalRect(posX, 5, 0, 0, 256, 256);
            if (this.currentPage == 0) {
                int left = this.width / 2 - 120;
                int sec = this.width / 2;
                String[] name = Minecraft.getMinecraft().thePlayer.toString().split("'");
                //你好 这是我的信息
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Folk_hello") + name[1] + " , " + I18n.format("container.sim.gui_Folk_Here_my") + "...", this.width / 2, 10, 16777215);
                //姓名
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Name"), left, 27, 0);
                this.fontRendererObj.drawString(this.theFolk.name, sec, 27, 128);
                //年龄
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
                    words = I18n.format("container.sim.gui_Folk_Female");
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
                        //无家可归
                        words = I18n.format("container.sim.gui_Folk_Homeless");
                    } else {
                        //租户
                        words = I18n.format("container.sim.gui_Folk_Tenant");
                    }
                } else {
                    //与父母同住
                    words = I18n.format("container.sim.gui_Folk_Living");
                }
                //住房状况
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Housing_status"), left, 67, 0);
                this.fontRendererObj.drawString(words, sec, 67, 128);
                if (!Relationship.isFolkLivingWithSomeone(this.theFolk)) {
                    //单身狗
                    words = I18n.format("container.sim.gui_Folk_Single");
                } else {
                    //你
                    String who = I18n.format("container.sim.gui_Folk_You");
                    FolkData whofd = Relationship.isFolkLivingWithSomeone(this.theFolk, true);
                    if (whofd != null) {
                        who = whofd.name;
                    }
                    //和...一起生活
                    words = I18n.format("container.sim.gui_Folk_Living_with") + who;
                }
                //情感
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Relationship"), left, 77, 0);
                this.fontRendererObj.drawString(words, sec, 77, 128);
                words = I18n.format("container.sim.gui_Folk_Unknown");

                try {
                    words = this.theFolk.action.toString();
                } catch (Exception e) {
                }
                //状态
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Status"), left, 87, 0);
                this.fontRendererObj.drawString(words, sec, 87, 128);
                //建设技能水平
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Building_skill"), left, 97, 0);
                this.fontRendererObj.drawString((int) this.theFolk.levelBuilder + I18n.format("container.sim.gui_Folk_of_10"), sec, 97, 128);
                double w = 128 * ((double) (this.theFolk.levelBuilder % 1 * 1000.0F) / 1000);
                this.drawGradientRect(sec, 97, (int) w + sec, 105, 1358888960, 1358954240);
                //挖矿技能等级
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Mining_skill"), left, 107, 0);
                this.fontRendererObj.drawString((int) this.theFolk.levelMiner + I18n.format("container.sim.gui_Folk_of_10"), sec, 107, 128);
                w = 128 * ((double) (this.theFolk.levelMiner % 1 * 1000.0F) / 1000);
                this.drawGradientRect(sec, 107, (int) w + sec, 115, 1358888960, 1358954240);
                if (this.theFolk.levelSoldier < 1) {
                    this.theFolk.levelSoldier = 1;
                }
                //士兵技能等级
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Soldier_skill"), left, 117, 0);
                this.fontRendererObj.drawString((int) this.theFolk.levelSoldier + I18n.format("container.sim.gui_Folk_of_10"), sec, 117, 128);
                w = 128 * ((double) (this.theFolk.levelSoldier % 1 * 1000.0F) / 1000);
                this.drawGradientRect(sec, 117, (int) w + sec, 125, 1358888960, 1358954240);
                if (this.theFolk.pregnancyStage > 0.0F) {
                    //医疗状况
                    this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Medical_status"), left, 127, 0);
                    String days = (int) (this.theFolk.pregnancyStage * 9.0F) + "";
                    if (days.contentEquals("0")) {
                        //孕
                        days = I18n.format("container.sim.gui_Folk_Pregnant");
                    } else if (days.contentEquals("1")) {
                        //怀孕一天
                        days = I18n.format("container.sim.gui_Folk_day_pregnant");
                    } else {
                        //怀孕天数
                        days = days + I18n.format("container.sim.gui_Folk_days_pregnant");
                    }

                    this.fontRendererObj.drawString(days, sec, 127, 128);
                }
            } else if (this.currentPage == 1) {
                int left = this.width / 2 - 125;
                int sec = 30;
                //的关系
                this.drawCenteredString(this.fontRendererObj, this.theFolk.name + I18n.format("container.sim.gui_Folk_Relationships"), this.width / 2, 10, 16777215);

                for (int r = this.relOffset; r < this.folksRelationships.size(); r++) {
                    try {
                        Relationship rel = (Relationship) this.folksRelationships.get(r);
                        String[] sp = rel.toStringPersepctive(this.theFolk).split(": ");
                        this.fontRendererObj.drawString(sp[0], left, sec, 0);
                        this.fontRendererObj.drawString(sp[1], this.width / 2, sec, 128);
                        sec += 10;
                        if (sec > 200) {
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
            } else if (this.currentPage == 2) {
                int left = this.width / 2;
                int disoffset = 0;
                //XX的需要
                this.drawCenteredString(this.fontRendererObj, this.theFolk.name + I18n.format("container.sim.gui_Folk_Needs"), this.width / 2, 10, 16777215);
                //饥饿
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Hunger") + ":", this.width / 3, 20, 0);
                this.fontRendererObj.drawString(this.theFolk.status4, this.width / 2, 20, 128);
                //乐趣
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Fun") + ":", this.width / 3, 30, 0);
                this.fontRendererObj.drawString(this.theFolk.funStatus, this.width / 2, 30, 128);
                //社交
                fontRendererObj.drawString(I18n.format("container.sim.guiFolk.Social") + ":", this.width / 3, 40, 0x000000);
                fontRendererObj.drawString(theFolk.socialStatus, this.width / 2, 40, 0x000080);
                //环境
                fontRendererObj.drawString(I18n.format("container.sim.guiFolk.Environment") + ":", this.width / 3, 50, 0x000000);
                fontRendererObj.drawString(theFolk.environmentStatus, this.width / 2, 50, 0x000080);
            } else if (currentPage == 3) {
                int left = this.width / 2 - 120;

                fontRendererObj.drawString(theFolk.trait1, left, 30, 0x000000);

                fontRendererObj.drawString(theFolk.trait2, left, 50, 0x000000);

                fontRendererObj.drawString(theFolk.trait3, left, 70, 0x000000);

                fontRendererObj.drawString(theFolk.trait4, left, 90, 0x000000);
            }
            // theGuiTextField1.drawTextBox();
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void showPage() {
        try {
            this.buttonList.clear();
            //再见
            this.buttonList.add(new GuiButton(0, 2, this.height - 22, 50, 20, I18n.format("container.sim.sim_gui_BC_bye")));
            if (this.currentPage == 0) {
                //关系
                this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 130, 100, 20, I18n.format("container.sim.gui_Folk_Relationshipss")));
                //需要
                this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 150, 100, 20, I18n.format("container.sim.gui_Folk_Needss")));
                //特征
                this.buttonList.add(new GuiButton(1, width / 2 - 50, 170, 100, 20, I18n.format("container.sim.guiFolk.Traits")));
                //库存
                this.buttonList.add(new GuiButton(1, width / 2 - 50, 190, 100, 20, I18n.format("container.sim.guiFolk.Inventory")));
            } else if (this.currentPage == 1) {
                //返回
                this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
                if (this.relOffset > 0) {
                    this.buttonList.add(new GuiButton(2, this.width / 2 - 125, 8, 20, 20, "<"));
                }

                int rels = this.folksRelationships.size();
                if (rels - this.relOffset > 18) {
                    this.buttonList.add(new GuiButton(3, this.width / 2 + 105, 8, 20, 20, ">"));
                }
            } else if (this.currentPage == 2) {
                //返回
                this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
            } else if (this.currentPage == 3) {
                //返回
                this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("showPage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                }

                if (this.currentPage == 0) {
                    //关系
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Relationshipss"))) {
                        this.currentPage = 1;
                        this.showPage();
                    }
                    //需要
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Needss"))) {
                        this.currentPage = 2;
                        this.showPage();
                    }
                    //特征
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.guiFolk.Traits"))) {
                        this.currentPage = 3;
                        this.showPage();
                    }
                    //库存
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.guiFolk.Inventory"))) {
                        //EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                        //player.openGui(ModSim.instance, 0, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                        //player.inventory.openInventory(player);
                        ModSimReloaded.packetPipeline.sendToServer(new OpenFolkInventoryPacket(References.GUI_FOLKINVENTORY));
                    }
                } else if (this.currentPage == 1) {
                    //情感分页  返回
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
                } else if (this.currentPage == 2) {
                    //需求分页 返回
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                        this.currentPage = 0;
                        this.showPage();
                    }

                } else if (currentPage == 3) {
                    // 特征 page
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                        this.currentPage = 0;
                        this.showPage();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIENTITYFOLK-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
    protected void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                //逃跑，不保存
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k){
        // theGuiTextField1.mouseClicked(i, j, k);
        try {
            super.mouseClicked(i, j, k);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
