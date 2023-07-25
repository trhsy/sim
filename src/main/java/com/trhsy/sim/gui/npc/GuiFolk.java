package com.trhsy.sim.gui.npc;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import org.lwjgl.opengl.GL11;

/**
 * @ClassName GuiFolk
 * @Description todo NPC 交互界面
 * @Author TRHSY
 * @Date 2022/10/1812:55
 **/
public class GuiFolk extends GuiScreen {
    // npc 名称
    String folkName;
    //年龄
    int folkAge;
    //性别
    int folkGender;
    //工作名称
    String jobName;
    //住房状态
    String housingStatus;
    //关系状态
    String relationshipStatus;
    //当前状态
    String status;
    //饥饿状态
    String hungerStatus;
    //种族
    String folkRaceName;
    String folkTrait1;
    String folkTrait2;
    String folkTrait3;
    //关系数据
    String relationshipData;
    //建筑等级
    String building;
    //农耕等级
    String farming;
    //挖矿等级
    String mining;
    float pregnancyStage;
    int page = 0;

    public GuiFolk(PacketOpenFolkGui message) {
        this.folkName = message.folkName;
        this.folkAge = message.folkAge;
        this.folkGender = message.folkGender;
        this.folkRaceName = message.folkRaceName;
        this.jobName = message.jobName;
        this.housingStatus = message.housingStatus;
        this.relationshipStatus = message.relationshipStatus;
        this.status = message.status;
        this.folkTrait1 = message.folkTrait1;
        this.folkTrait2 = message.folkTrait2;
        this.folkTrait3 = message.folkTrait3;
        this.relationshipData = message.relationshipData;
        this.building = message.building;
        this.farming = message.farming;
        this.mining = message.mining;
        this.hungerStatus = message.hungerStatus;
        this.pregnancyStage=message.pregnancyStage ;
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void initGui() {
        super.initGui();
        this.showPage();
    }

    public void showPage() {
        this.buttonList.clear();
        //完成 按鈕
        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
        if (this.page == 0) {
            //情感
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 130, 100, 20, I18n.format("container.sim.gui_Folk_Relationshipss")));
            //需要
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 150, 100, 20, I18n.format("container.sim.gui_Folk_Needss")));
            //特征
            this.buttonList.add(new GuiButton(1, width / 2 - 50, 170, 100, 20, I18n.format("container.sim.guiFolk.Traits")));
            //库存
//            this.buttonList.add(new GuiButton(1, width / 2 - 50, 190, 100, 20, I18n.format("container.sim.guiFolk.Inventory")));
        }else if (this.page == 1) {
            //返回
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
//            if (this.relOffset > 0) {
//                this.buttonList.add(new GuiButton(2, this.width / 2 - 125, 8, 20, 20, "<"));
//            }
//
//            int rels = this.folksRelationships.size();
//            if (rels - this.relOffset > 18) {
//                this.buttonList.add(new GuiButton(3, this.width / 2 + 105, 8, 20, 20, ">"));
//            }
        }else if (this.page == 2) {
            //返回
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
        } else if (this.page == 3) {
            //返回
            this.buttonList.add(new GuiButton(1, 2, this.height - 42, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
        }


    }
    @Override
    public void drawScreen(int i, int j, float f) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(ModSim.MODID, "textures/gui/guiFolk.png"));
        int posX = (this.width - 256) / 2;
        int labelPos = this.width / 2 - 96;
        this.drawDefaultBackground();
        this.drawTexturedModalRect(posX, 5, 0, 0, 256, 256);
        if (this.page == 0) {
            //你好！这是我的信息：
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Here_my"), this.width / 2, 17, 0x000000);
            //姓名
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Name")+":", labelPos, 27, 0x000000);
            this.fontRendererObj.drawString( this.folkName, this.width / 2, 27, 0x000000);
            //年龄
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Age")+":", labelPos, 37, 0x000000);
            this.fontRendererObj.drawString( String.valueOf(this.folkAge), this.width / 2, 37, 0x000000);
            //性别
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Gender")+":", labelPos, 47, 0x000000);
            this.fontRendererObj.drawString( this.folkGender == 0 ? I18n.format("container.sim.gui_Folk_Male") : I18n.format("container.sim.gui_Folk_Female"), this.width / 2, 47, 0x000000);
            //种族
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Species")+":", labelPos, 57, 0x000000);
            this.fontRendererObj.drawString( this.folkRaceName, this.width / 2, 57, 0x000000);
            //工作
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Job")+":", labelPos, 67, 0x000000);
            this.fontRendererObj.drawString( this.jobName, this.width / 2, 67, 0x000000);
            //住房状态
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Housing_status")+":", labelPos, 77, 0x000000);
            this.fontRendererObj.drawString( this.housingStatus, this.width / 2, 77, 0x000000);
            //建筑等级
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Building_skill")+":", labelPos, 87, 0x000000);
            this.fontRendererObj.drawString( this.building, this.width / 2, 87, 0x000000);
            double w = 128 * ((double) (Double.valueOf(this.building) % 1 * 1000.0F) / 1000);
            this.drawGradientRect(this.width / 2, 87, (int) w + this.width / 2, 105, 1358888960, 1358954240);
            //农耕等级
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Farming_Skill")+":", labelPos, 97, 0x000000);
            this.fontRendererObj.drawString(this.farming, this.width / 2, 97, 0x000000);
            double far = 128 * ((double) (Double.valueOf(this.farming) % 1 * 1000.0F) / 1000);
            this.drawGradientRect(this.width / 2, 97, (int) far + this.width / 2, 105, 1358888960, 1358954240);
            //农耕等级
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Mining_skill")+":", labelPos, 107, 0x000000);
            this.fontRendererObj.drawString(this.mining, this.width / 2, 107, 0x000000);
            double min = 128 * ((double) (Double.valueOf(this.mining) % 1 * 1000.0F) / 1000);
            this.drawGradientRect(this.width / 2, 107, (int) min + this.width / 2, 105, 1358888960, 1358954240);
            //怀孕阶段
            if (this.pregnancyStage > 0.0F) {
                //医疗状况
                this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Medical_status"), labelPos, 117, 0x000000);
                String days = (int) (this.pregnancyStage * 9.0F) + "";
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

                this.fontRendererObj.drawString(days, this.width / 2, 117, 128);
            }

        } else if(this.page == 1) {
            //的关系
            this.fontRendererObj.drawString( I18n.format("container.sim.gui_Folk_Relationshipss")+":", this.width / 2, 17, 0x000000);
            int height = 50;
            String[] var7 = this.relationshipData.split(";");
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                String relDat = var7[var9];
                this.fontRendererObj.drawString( relDat, labelPos, height, 0x000000);
                height += 15;
            }
        }else if(this.page == 2) {
            //XX的需要
            this.fontRendererObj.drawString( this.folkName + I18n.format("container.sim.gui_Folk_Needs"), this.width / 2, 10, 0x000000);
            //当前状态
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Status") + ":", this.width / 3, 20, 0);
            this.fontRendererObj.drawString(this.status, this.width / 2, 20, 128);
            //饥饿状态
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Hunger") + ":", this.width / 3, 30, 0);
            this.fontRendererObj.drawString(this.hungerStatus, this.width / 2, 30, 128);

           /* //乐趣
            this.fontRendererObj.drawString(I18n.format("container.sim.gui_Folk_Fun") + ":", this.width / 3, 30, 0);
            this.fontRendererObj.drawString(this.funStatus, this.width / 2, 30, 128);
            //社交
            fontRendererObj.drawString(I18n.format("container.sim.guiFolk.Social") + ":", this.width / 3, 40, 0x000000);
            fontRendererObj.drawString(theFolk.socialStatus, this.width / 2, 40, 0x000080);
            //环境
            fontRendererObj.drawString(I18n.format("container.sim.guiFolk.Environment") + ":", this.width / 3, 50, 0x000000);
            fontRendererObj.drawString(theFolk.environmentStatus, this.width / 2, 50, 0x000080);*/
        }else if (this.page == 3) {
            int left = this.width / 2 - 120;

            fontRendererObj.drawString(this.folkTrait1, left, 30, 0x000000);
            fontRendererObj.drawString(this.folkTrait2, left, 50, 0x000000);
            fontRendererObj.drawString(this.folkTrait3, left, 70, 0x000000);

        }

        super.drawScreen(i, j, f);
    }
    @Override
    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }
            if (this.page == 0) {
                //关系
                if (button.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Relationshipss"))) {
                    this.page = 1;
                    this.showPage();
                }
                //需要
                if (button.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Needss"))) {
                    this.page = 2;
                    this.showPage();
                }
                //特征
                if (button.displayString.contentEquals(I18n.format("container.sim.guiFolk.Traits"))) {
                    this.page = 3;
                    this.showPage();
                }
                //库存
                if (button.displayString.contentEquals(I18n.format("container.sim.guiFolk.Inventory"))) {
                    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                    //FMLNetworkHandler.openGui(player, ModSim.instance, References.GUI_FOLKINVENTORY, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
                }
            } else if (this.page == 1) {
                //情感分页  返回
                if (button.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                    this.page = 0;
                    this.showPage();
                }

                if (button.displayString.contentEquals("<")) {
//                    this.relOffset -= 18;
//                    if (this.relOffset < 0) {
//                        this.relOffset = 0;
//                    }

                    this.showPage();
                }

                if (button.displayString.contentEquals(">")) {
//                    this.relOffset += 18;
                    this.showPage();
                }
            } else if (this.page == 2) {
                //需求分页 返回
                if (button.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                    this.page = 0;
                    this.showPage();
                }

            } else if (page == 3) {
                // 特征 page
                if (button.displayString.contentEquals(I18n.format("container.sim.gui_Folk_Back"))) {
                    this.page = 0;
                    this.showPage();
                }
            }

        }
    }
}
