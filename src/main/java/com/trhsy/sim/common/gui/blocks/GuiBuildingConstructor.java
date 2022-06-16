package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.gui.folk.GuiShowEmployees;
import com.trhsy.sim.common.jobs.JobBuilder;
import com.trhsy.sim.common.jobs.Stage;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.server.LoadBuildingMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ========================================
 *
 * @ClassName GuiBuildingConstructor
 * @Description todo 建筑箱
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:27
 * ========================================
 **/
public class GuiBuildingConstructor extends GuiScreen {
    //鼠标计数
    private int mouseCount = 0;
    //当前页
    private int currentPage = 0;
    //工人集合
    private ArrayList<FolkData> theWorkers = new ArrayList();
    //要建筑的
    V3 constructorLoc;
    //建筑方向
    String buildDirection = "";
    //建筑偏移量
    private int buildingOffset = 0;
    //第几页建筑物
    private int buildingsOnPage = 0;
    //固定建筑计数
    private int fixedBuildingCount = -1;
    //pk 指数
    private HashMap pkIndex = new HashMap();
    //GUI 搜索文字
    private GuiTextField tfSearch;
    //搜索
    private String search = "";
    //选定的建筑
    private Building selectedBuilding = null;
    //上一页
    private int previousPage = 1;
    long fuckingBodge = 0L;

    /**
     * Gui构建构造函数
     * @param location 建筑商
     * @param buildDirection 建筑方向
     * @param theFolks 建筑工
     */
    public GuiBuildingConstructor(V3 location, String buildDirection, ArrayList<FolkData> theFolks) {
        this.constructorLoc = location;
        this.buildDirection = buildDirection;
        //如果不为空则赋值
        if (theFolks != null) {
            this.theWorkers = theFolks;
        } else {
            //为空则清除
            this.theWorkers.clear();

            for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                //得到npc 数据
                FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                //NPC 被雇佣 并且 坐标是当前要建筑的地方
                if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.constructorLoc, true, true)) {
                    //如果 NPC职业是建筑师
                    if (folk.vocation == Vocation.BUILDER) {
                        //NPC当前的工作内容
                        JobBuilder theirJob = (JobBuilder) folk.theirJob;
                        //工作阶段是闲置
                        if (theirJob.theStage == Stage.IDLE) {
                            //设置阶段为已分配工作
                            theirJob.theStage = Stage.WORKERASSIGNED;
                        }
                    }
                    //把当前建筑工人添加进去
                    this.theWorkers.add(folk);
                }
            }
        }

    }

    /**
     * Gui 暂停游戏
     * 如果此 GUI 在单人游戏中显示时应该暂停游戏，则返回 true
     * @return
     */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * 从主游戏循环调用以更新屏幕。
     */
    @Override
    public void updateScreen() {
        //设置游戏内不聚焦
        this.mc.setIngameNotInFocus();
        //如果搜索不为空
        if (this.tfSearch != null) {
            //更新光标计数器
            this.tfSearch.updateCursorCounter();
        }
        //更新画面
        super.updateScreen();
    }

    /**
     * 将按钮（和其他控件）添加到相关屏幕。 在显示 GUI 和调整窗口大小时调用，会预先清除 buttonList。
     */
    @Override
    public void initGui() {
        //原始键盘启用重复事件
        Keyboard.enableRepeatEvents(true);
        //显示页面
        this.showPage();
        //初始化
        super.initGui();
    }

    /**
     * 绘制屏幕和其中的所有组件。 参数：mouseX、mouseY、renderPartialTicks
     * @param i
     * @param j
     * @param f
     */
    @Override
    public void drawScreen(int i, int j, float f) {

        try {
            //如果鼠标计数小于10
            if (this.mouseCount < 10) {
                //鼠标计数增加
                this.mouseCount++;
                //返回鼠标
                Mouse.setGrabbed(false);
            }
            //在背景屏幕上绘制渐变（如果存在）或在 background.png 上绘制平面渐变
            this.drawDefaultBackground();
            //建筑构建器
            String sim_gui_BC_Constructor = I18n.format("container.sim.sim_gui_BC_Constructor");
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Constructor, this.width / 2, 17, 16777215);
            //闲置
            String sim_gui_BC_Idle = I18n.format("container.sim.sim_gui_BC_Idle");
            //没有选择吗
            String sim_gui_BC_chosen = I18n.format("container.sim.sim_gui_BC_chosen");
            String s = sim_gui_BC_Idle;
            String t = sim_gui_BC_chosen;

            try {
                //如果NPC 大于0
                if (this.theWorkers.size() > 0) {
                    //获取工作
                    JobBuilder theirJob = (JobBuilder) ((FolkData) this.theWorkers.get(0)).theirJob;
                    s = theirJob.theStage.toString();
                    if (((FolkData) this.theWorkers.get(0)).theBuilding != null) {
                        //当前名称
                        t = ((FolkData) this.theWorkers.get(0)).theBuilding.displayName;
                    }
                }
            } catch (Exception var11) {
                //在路上
                String sim_gui_BC_their = I18n.format("container.sim.sim_gui_BC_their");
                s = sim_gui_BC_their;
                t = "";
            }
            String sim_gui_BC_Current = I18n.format("container.sim.sim_gui_BC_Current");//目前状态
            String sim_gui_BC_Building = I18n.format("container.sim.sim_gui_BC_Building");//建筑类型
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Current + s, this.width / 2, 30, 11206655);
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Building + t, this.width / 2, 40, 11206655);
            switch (this.currentPage) {
                case 0:
                    String sim_gui_BC_building_constructor = I18n.format("container.sim.sim_gui_BC_building_constructor");//请选择需要建造的项目
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_building_constructor, this.width / 2, 100, 16777130);
                    break;
                case 1:
                    String sim_gui_BC_building = I18n.format("container.sim.sim_gui_BC_building");//请选择一个建筑类型
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_building, this.width / 2, 100, 16777130);
                    break;
                case 2:
                    String sim_gui_BC_residential = I18n.format("container.sim.sim_gui_BC_residential");//现在选择住宅建筑
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_residential, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 3:
                    String sim_gui_BC_unemployed = I18n.format("container.sim.sim_gui_BC_unemployed");//选择一个你想雇佣的NPC
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_unemployed, this.width / 2, 50, 16777130);
                    break;
                case 4:
                    String sim_gui_BC_employees = I18n.format("container.sim.sim_gui_BC_employees");//这里是你所有的员工
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_employees, this.width / 2, 50, 16777130);
                    break;
                case 5:
                    String sim_gui_BC_commercial = I18n.format("container.sim.sim_gui_BC_commercial");//现在选择商业建筑的建筑
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_commercial, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 6:
                    String sim_gui_BC_industrial = I18n.format("container.sim.sim_gui_BC_industrial");//现在选择工业建筑的建筑
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_industrial, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 7:
                    String sim_gui_BC_Now_choose = I18n.format("container.sim.sim_gui_BC_Now_choose");//现在选择其他类型的建筑
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Now_choose, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 9:
                    //现在选择特殊类型的建筑
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC1"), this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 10:
                    String realCost = " (" + ModSimReloaded.displayMoney((float) this.selectedBuilding.blocksInBuilding * 0.02F * (float) this.theWorkers.size()) + ")";
                    //建筑细节
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC2") + this.selectedBuilding.displayNameWithoutPK, this.width / 2, 50, 16777130);
                    //建筑名
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC3") + "：" + this.selectedBuilding.displayNameWithoutPK, this.width / 2, 80, 16777130);
                    //说明
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC4") + "：" + this.selectedBuilding.description, this.width / 2, 110, 16777130);
                    //作者
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC5") + "：" + this.selectedBuilding.author, this.width / 2, 140, 16777130);
                    //费用
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC6") + "：" + realCost, this.width / 2, 170, 16777130);
                    //尺寸
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC7") + "：" + this.selectedBuilding.dimensions, this.width / 2, 200, 16777130);
                    //对应块构造数量应该是
                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC8") + "：" + this.selectedBuilding.elevationLevel, this.width / 2, 230, 16777130);
                    break;
                case 8:
                    //建筑要求
                    String sim_gui_BC_requirements_for = I18n.format("container.sim.sim_gui_BC_requirements_for");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_requirements_for + this.selectedBuilding.displayNameWithoutPK, this.width / 2, 50, 16777130);
                    int y = 70;
                    Iterator it = this.selectedBuilding.requirements.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry) it.next();
                        ItemStack is = (ItemStack) pairs.getKey();
                        if (is != null) {
                            if (y + 20 > this.height - 20) {
                                //...还有几种方块的类型
                                String sim_gui_BC_block_types = I18n.format("container.sim.sim_gui_BC_block_types");
                                this.drawString(this.fontRendererObj, sim_gui_BC_block_types, 90, y, 16777215);
                            } else {
                                String itemName = is.getDisplayName();
                                //橡木
                                //System.out.println("****************************oak wood*****************************");
                                if (itemName.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC9"))) {
                                    //木材
                                    itemName = I18n.format("container.sim.sim_gui_BC10");
                                }
                                //橡木木板
                                if (itemName.toLowerCase().contains(I18n.format("container.sim.sim_gui_BC11"))) {
                                    //木板
                                    itemName = I18n.format("container.sim.sim_gui_BC12");
                                }

                                this.displayReq(itemName, (Integer) pairs.getValue(), y);
                                y += 15;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception var12) {
            //var12.printStackTrace();
            ModSimReloaded.log.warn(var12.getMessage());
        }
        super.drawScreen(i, j, f);
    }

    /**
     * 显示需求
     * @param block
     * @param qty
     * @param y
     */
    private void displayReq(String block, int qty, int y) {
        double stacks = Math.floor((double) (qty / 64));
        this.drawString(this.fontRendererObj, qty + "", 90, y, 16777215);
        this.drawString(this.fontRendererObj, "x", 125, y, 16777215);
        this.drawString(this.fontRendererObj, block, 150, y, 16777215);
        //(不到一组)
        String sim_gui_BC_less = I18n.format("container.sim.sim_gui_BC_less");
        //(正好是一组)
        String sim_gui_BC_exactly = I18n.format("container.sim.sim_gui_BC_exactly");
        //(大约两组)
        String sim_gui_BC_about_2 = I18n.format("container.sim.sim_gui_BC_about_2");
        //(大约
        String sim_gui_BC_about = I18n.format("container.sim.sim_gui_BC_about");
        //组)
        String sim_gui_BC_stacks = I18n.format("container.sim.sim_gui_BC_stacks");
        String st = "";
        if (qty < 64) {
            st = sim_gui_BC_less;
        } else if (qty == 64) {
            st = sim_gui_BC_exactly;
        } else if (qty >= 64 && qty < 128) {
            st = sim_gui_BC_about_2;
        } else {
            st = sim_gui_BC_about + (int) (stacks + 1.0) + sim_gui_BC_stacks;
        }

        this.drawString(this.fontRendererObj, st, 250, y, 16777215);
    }

    /**
     *显示分页
     */
    private void showPage() {
        this.mc.setIngameNotInFocus();
        //清除所有按钮
        this.buttonList.clear();
        //完成
        String sim_gui_BC_Done = I18n.format("container.sim.sim_gui_BC_Done");
        this.buttonList.add(new GuiButton(0, 2, 12, 50, 20, sim_gui_BC_Done));
        //如果选定建筑物为空
        if (this.selectedBuilding == null) {
            //获得要构建的建筑
            this.selectedBuilding = Building.getBuildingByConBox(this.constructorLoc);
        }

        if (this.currentPage == 0) {
            //选择建筑
            String sim_gui_BC_Choose_building = I18n.format("container.sim.sim_gui_BC_Choose_building");
            this.buttonList.add(new GuiButton(1, this.width / 2 - 60, 150, 120, 20, sim_gui_BC_Choose_building));
            //雇佣建筑工
            String sim_gui_BC_Hire_builder = I18n.format("container.sim.Hire1");
            this.buttonList.add(new GuiButton(2, this.width / 2 - 180, 150, 120, 20, sim_gui_BC_Hire_builder));
            //员工
            String sim_gui_BC_worker = I18n.format("container.sim.sim_gui_BC_worker");
            String w = sim_gui_BC_worker;
            //如果工人为1
            if (this.theWorkers.size() == 1) {
                //获取员工名称
                w = ((FolkData) this.theWorkers.get(0)).name;
            } else if (this.theWorkers.size() > 1) {
                //工作人员
                String sim_gui_BC_Staff = I18n.format("container.sim.sim_gui_BC_Staff");
                w = sim_gui_BC_Staff + "(" + this.theWorkers.size() + ")";
            }
            //解雇
            String sim_gui_BC_Fire = I18n.format("container.sim.Fire");
            this.buttonList.add(new GuiButton(3, this.width / 2 + 60, 150, 120, 20, sim_gui_BC_Fire + w));
            //显示员工
            String sim_gui_BC_Show_Employees = I18n.format("container.sim.sim_gui_BC_Show_Employees");
            this.buttonList.add(new GuiButton(4, this.width / 2 + 60, 170, 120, 20, sim_gui_BC_Show_Employees));
            //规划区域
            String sim_gui_BC_Terraform_area = I18n.format("container.sim.sim_gui_BC_Terraform_area");
            this.buttonList.add(new GuiButton(5, -600, 170, 120, 20, "-"));
            this.buttonList.add(new GuiButton(6, this.width / 2 - 60, 170, 120, 20, sim_gui_BC_Terraform_area));
            //雇佣规划师
            this.buttonList.add(new GuiButton(7, this.width / 2 - 180, 170, 120, 20, I18n.format("container.sim.Hire22")));
            if (this.theWorkers.size() == 0) {
                ((GuiButton) this.buttonList.get(1)).enabled = false;
                ((GuiButton) this.buttonList.get(2)).enabled = true;
                ((GuiButton) this.buttonList.get(3)).enabled = false;
                ((GuiButton) this.buttonList.get(6)).enabled = false;
                ((GuiButton) this.buttonList.get(7)).enabled = true;
            } else {
                ((GuiButton) this.buttonList.get(1)).enabled = true;
                ((GuiButton) this.buttonList.get(2)).enabled = false;
                ((GuiButton) this.buttonList.get(3)).enabled = true;
                ((GuiButton) this.buttonList.get(6)).enabled = true;
                ((GuiButton) this.buttonList.get(7)).enabled = false;
            }
        } else if (this.currentPage == 1) {
            //现在选择要建造的住宅楼
            String sim_gui_BC_Residential = I18n.format("container.sim.sim_gui_BC_Residential");
            //现在选择要建造的商业建筑
            String sim_gui_BC_Commercial = I18n.format("container.sim.sim_gui_BC_Commercial");
            //现在选择要建造的工业建筑
            String sim_gui_BC_Industrial = I18n.format("container.sim.sim_gui_BC_Industrial");
            //其他
            String sim_gui_BC_Other = I18n.format("container.sim.sim_gui_BC_Other");
            //特别
            String sim_gui_BC_special = I18n.format("container.sim.sim_gui_BC_special");
            this.buttonList.add(new GuiButton(5, this.width / 2 - 200, 150, 100, 20, sim_gui_BC_Residential));
            this.buttonList.add(new GuiButton(6, this.width / 2 - 100, 150, 100, 20, sim_gui_BC_Commercial));
            this.buttonList.add(new GuiButton(7, this.width / 2, 150, 100, 20, sim_gui_BC_Industrial));
            this.buttonList.add(new GuiButton(8, this.width / 2 + 100, 150, 100, 20, sim_gui_BC_Other));
            this.buttonList.add(new GuiButton(9, this.width / 2 - 50, 180, 100, 20, sim_gui_BC_special));
        } else if (this.currentPage != 3) {
            if (this.currentPage == 10) {
                //返回
                this.buttonList.add(new GuiButton(1001, this.width / 2 - 100, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Go_Back")));
                //需求
                this.buttonList.add(new GuiButton(969, this.width / 2, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Go_demand")));
            } else {
                int x;
                int y;
                int idx;
                if (this.currentPage == 4) {
                    try {
                        x = 10;
                        y = 65;
                        idx = 1;

                        for (y = 0; y < ModSimReloaded.theFolks.size(); ++y) {
                            FolkData folk = (FolkData) ModSimReloaded.theFolks.get(y);
                            //解雇
                            String sim_gui_BC_Fire = I18n.format("container.sim.Fire");
                            this.buttonList.add(new GuiButton(idx, x, y, 100, 20, sim_gui_BC_Fire + folk.name));
                            ++x;
                            x += 100;
                            if (x + 100 > this.width) {
                                x = 10;
                                y += 20;
                            }

                            if (y + 20 > this.height - 50) {
                                break;
                            }
                        }
                    } catch (Exception var18) {
                        var18.printStackTrace();
                    }
                } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                    if (this.currentPage == 8) {
                        //返回
                        String sim_gui_BC_Go_Back = I18n.format("container.sim.sim_gui_BC_Go_Back");
                        this.buttonList.add(new GuiButton(1001, this.width / 2 - 100, this.height - 25, 100, 20, sim_gui_BC_Go_Back));
                        ///建造它
                        String sim_gui_BC_Build_it = I18n.format("container.sim.sim_gui_BC_Build_it");
                        this.buttonList.add(new GuiButton(1000, this.width / 2, this.height - 25, 100, 20, sim_gui_BC_Build_it));
                    }
                } else {
                    //房屋集合
                    ArrayList<Building> houses = new ArrayList();
                    String theType = "";
                    this.buildingsOnPage = 0;
                    this.tfSearch = new GuiTextField(0,this.fontRendererObj, this.width / 2 - 50, this.height - 30, 100, 20);
                    this.tfSearch.setText(this.search);
                    this.tfSearch.setFocused(true);
                    this.tfSearch.setMaxStringLength(10);
                    if (this.currentPage == 2) {
                        //获取住宅蓝图
                        houses = Building.getBuildingBlueprints("residential", this.tfSearch.getText().trim());
                        theType = "residential";
                    } else if (this.currentPage == 5) {
                        //获取商业蓝图
                        houses = Building.getBuildingBlueprints("commercial", this.tfSearch.getText().trim());
                        theType = "commercial";
                    } else if (this.currentPage == 6) {
                        //获取工业蓝图
                        houses = Building.getBuildingBlueprints("industrial", this.tfSearch.getText().trim());
                        theType = "industrial";
                    } else if (this.currentPage == 7) {
                        //获取其他蓝图
                        houses = Building.getBuildingBlueprints("other", this.tfSearch.getText().trim());
                        theType = "other";
                    }else if (this.currentPage == 9) {
                        //获取特除蓝图
                        houses = Building.getBuildingBlueprints("special", this.tfSearch.getText().trim());
                        theType = "special";
                    }

                    x = 10;
                    y = 60;
                    idx = 1;
                    //如果蓝图不为空
                    if (houses != null) {
                        //遍历蓝图
                        for (int b = 0; b <= houses.size(); ++b) {
                            //定义偏移量
                            int boff = b + this.buildingOffset;
                            if (boff < 0) {
                                //重置偏移量
                                boff = 0;
                                this.buildingOffset = 0;
                            }
                            //页
                            String sim_gui_BC_Page = I18n.format("container.sim.sim_gui_BC_Page");
                            //偏移量小于大小
                            if (boff < houses.size()) {
                                //如果当前不为空
                                if (houses.get(boff) != null) {
                                    //金额
                                    String line3 = "";
                                    //获得建筑
                                    Building building = (Building) houses.get(boff);
                                    //实际成本
                                    String realCost = "";
                                    //工作人员大于1
                                    if (this.theWorkers.size() > 1) {
                                        //计算成本
                                        realCost = " (" + ModSimReloaded.displayMoney((float) building.blocksInBuilding * 0.02F * (float) this.theWorkers.size()) + ")";
                                    }
                                    //建筑整体范围
                                    String line2 = building.ltrCount + " x " + building.ftbCount + " x " + building.layerCount;
                                    //金额
                                    line3 = ModSimReloaded.displayMoney((float) building.blocksInBuilding * 0.02F) + realCost;
                                    //作者
                                    String line4 = building.author;
                                    GuiButton b3;
                                    //作者
                                    this.buttonList.add(b3 = new GuiButton(idx + 300, x, y + 48, 120, 20, line4));
                                    GuiButton b2;
                                    //金额
                                    this.buttonList.add(b2 = new GuiButton(idx + 200, x, y + 32, 120, 20, line3));
                                    GuiButton b1;
                                    //范围
                                    this.buttonList.add(b1 = new GuiButton(idx + 100, x, y + 16, 120, 20, line2));
                                    //设置按钮不可用
                                    b1.enabled = false;
                                    b2.enabled = false;
                                    b3.enabled = false;
                                    String pk = "";
                                    //去除建筑名字中的pkid
                                    if (building.displayName.startsWith("PKID")) {
                                        int hyphen = building.displayName.indexOf("-");
                                        pk = building.displayName.substring(0, hyphen + 1);
                                    }

                                    this.pkIndex.put(idx, pk);
                                    this.buttonList.add(new GuiButton(idx, x, y, 120, 20, building.displayNameWithoutPK));
                                    x += 120;
                                    if (x + 120 > this.width) {
                                        x = 10;
                                        y += 71;
                                    }

                                    ++idx;
                                    ++this.buildingsOnPage;
                                    if (this.buildingOffset > 0) {
                                        this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "<" + sim_gui_BC_Page));
                                    }

                                    if (y + 20 + 20 + 20 + 20 > this.height) {
                                        this.buttonList.add(new GuiButton(500, this.width - 80, this.height - 20, 75, 20, sim_gui_BC_Page + ">"));
                                        break;
                                    }
                                }
                            } else {
                                this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "<" + sim_gui_BC_Page));
                            }
                        }

                        if (this.fixedBuildingCount == -1) {
                            this.fixedBuildingCount = this.buildingsOnPage;
                        }
                    } else {
                        String sim_gui_BC_Nothing_found = I18n.format("container.sim.sim_gui_BC_Nothing_found");
                        this.buttonList.add(new GuiButton(1, 10, 60, 300, 20, sim_gui_BC_Nothing_found));
                    }
                }
            }
        }

    }

    @Override
    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void actionPerformed(GuiButton guibutton) {
        if (System.currentTimeMillis() - this.fuckingBodge >= 100L) {
            this.fuckingBodge = System.currentTimeMillis();
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    if (this.currentPage == 0) {
                        String sim_gui_BC_Choose_building = I18n.format("container.sim.sim_gui_BC_Choose_building");
                        if (guibutton.displayString.contentEquals(sim_gui_BC_Choose_building)) {
                            this.currentPage = 1;
                            this.showPage();
                        } else {
                            GuiEmployFolk gui;
                            if (guibutton.id == 2) {
                                gui = new GuiEmployFolk(this.constructorLoc, this.buildDirection, Vocation.BUILDER);
                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.displayGuiScreen(gui);
                            } else if (guibutton.id == 3) {
                                this.fireAllFolksForThisBuilding();
                                this.currentPage = 0;
                                this.showPage();
                            } else if (guibutton.id == 4) {
                                GuiScreen guiScreen = new GuiShowEmployees();
                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.displayGuiScreen(guiScreen);
                            } else if (guibutton.id != 5) {
                                if (guibutton.id == 6) {
                                    GuiScreen guiScreen = new GuiTerraform((FolkData) this.theWorkers.get(0));
                                    this.mc.displayGuiScreen((GuiScreen) null);
                                    this.mc.displayGuiScreen(guiScreen);
                                } else if (guibutton.id == 7) {
                                    gui = new GuiEmployFolk(this.constructorLoc, "N/A", Vocation.TERRAFORMER);
                                    this.mc.displayGuiScreen((GuiScreen) null);
                                    this.mc.displayGuiScreen(gui);
                                }
                            }
                        }
                    } else if (this.currentPage == 1) {
                        if (guibutton.id == 5) {
                            this.currentPage = 2;
                            this.showPage();
                        } else if (guibutton.id == 6) {
                            this.currentPage = 5;
                            this.showPage();
                        } else if (guibutton.id == 7) {
                            this.currentPage = 6;
                            this.showPage();
                        } else if (guibutton.id == 8) {
                            this.currentPage = 7;
                            this.showPage();
                        }else if (guibutton.id == 9) {
                            this.currentPage = 9;
                            this.showPage();
                        }
                    } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7 && this.currentPage != 9) {
                        if (this.currentPage == 8) {
                            //建造它
                            String sim_gui_BC_Build_it = I18n.format("container.sim.sim_gui_BC_Build_it");
                            if (guibutton.displayString.contentEquals(sim_gui_BC_Build_it)) {
                                if (Building.getBuilding(this.selectedBuilding.primaryXYZ) != null) {
                                    ModSimReloaded.theBuildings.remove(this.selectedBuilding);
                                }

                                this.selectedBuilding.conBoxLocation = this.constructorLoc.clone();
                                ModSimReloaded.theBuildings.add(this.selectedBuilding);
                                this.selectedBuilding.saveThisBuilding();
                                ModSimReloaded.network.sendToAll(new LoadBuildingMessage("GuiBuildingCon"));

                                for (int i = 0; i < this.theWorkers.size(); ++i) {
                                    FolkData theWorker = (FolkData) this.theWorkers.get(i);
                                    theWorker.theBuilding = this.selectedBuilding;
                                    theWorker.saveThisFolk();
                                }

                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.setIngameFocus();
                                return;
                            }

                            if (guibutton.id == 1001) {
                                this.currentPage = this.previousPage;
                                this.showPage();
                            }
                        } else if (this.currentPage == 10) {
                            if (guibutton.id == 969) {
                                this.currentPage = 8;
                                this.showPage();
                            } else if (guibutton.id == 1001) {
                                this.currentPage = this.previousPage;
                                this.showPage();
                            }
                        }else if (this.currentPage != 3 && this.currentPage == 4) {
                            this.fireAllFolksForThisBuilding();
                            this.currentPage = 0;
                            this.showPage();
                        }
                    } else {
                        this.previousPage = this.currentPage;
                        if (guibutton.id == 500) {
                            this.buildingOffset += this.fixedBuildingCount;
                            this.showPage();
                            return;
                        }

                        if (guibutton.id == 501) {
                            this.buildingOffset -= this.fixedBuildingCount;
                            this.showPage();
                            return;
                        }

                        String type = "";
                        if (this.currentPage == 2) {
                            type = "residential";
                        }

                        if (this.currentPage == 5) {
                            type = "commercial";
                        }

                        if (this.currentPage == 6) {
                            type = "industrial";
                        }

                        if (this.currentPage == 7) {
                            type = "other";
                        }
                        if (this.currentPage == 9) {
                            type = "special";
                        }
                        String pkPrefix = "";
                        pkPrefix = (String) this.pkIndex.get(guibutton.id);
                        this.selectedBuilding = Building.getFromAllBuildings(pkPrefix + guibutton.displayString, type);

                        try {
                            this.selectedBuilding.buildDirection = this.buildDirection;
                        } catch (Exception var5) {
                        }

                        this.currentPage = 8;
                        this.showPage();
                    }

                }
            }
        }
    }

    public void fireAllFolksForThisBuilding() {
        for (int i = 0; i < this.theWorkers.size(); ++i) {
            //员工
            FolkData worker = (FolkData) this.theWorkers.get(i);
            if (worker.vocation == Vocation.BUILDER) {
                JobBuilder theirJob = (JobBuilder) worker.theirJob;
                theirJob.theStage = Stage.IDLE;
                worker.theBuilding = null;
            }

            worker.selfFire();
        }

        this.theWorkers.clear();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        } else {
            if (this.tfSearch != null) {
                this.tfSearch.textboxKeyTyped(c, i);
                this.search = this.tfSearch.getText();
                if (!this.search.endsWith(":")) {
                    this.buildingOffset = 0;
                    this.showPage();
                }
            }

        }
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        if (this.tfSearch != null) {
            this.tfSearch.mouseClicked(i, j, k);
        }

        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

