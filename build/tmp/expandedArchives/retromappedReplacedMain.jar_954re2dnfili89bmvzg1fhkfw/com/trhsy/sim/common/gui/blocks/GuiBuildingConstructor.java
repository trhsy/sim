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
import com.trhsy.sim.packets.PacketHandler;
import com.trhsy.sim.packets.server.LoadBuildingPacket;
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
import java.util.concurrent.CopyOnWriteArrayList;

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
    private CopyOnWriteArrayList<FolkData> theWorkers = new CopyOnWriteArrayList();
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
    public GuiBuildingConstructor(V3 location, String buildDirection, CopyOnWriteArrayList<FolkData> theFolks) {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiBuildingConstructor出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * Gui 暂停游戏
     * 如果此 GUI 在单人游戏中显示时应该暂停游戏，则返回 true
     * @return
     */
    @Override
    public boolean func_73868_f() {
        return false;
    }

    /**
     * 从主游戏循环调用以更新屏幕。
     */
    @Override
    public void func_73876_c() {
        try {
            //设置游戏内不聚焦
            this.field_146297_k.func_71364_i();
            //如果搜索不为空
            if (this.tfSearch != null) {
                //更新光标计数器
                this.tfSearch.func_146178_a();
            }
            //更新画面
            super.func_73876_c();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 将按钮（和其他控件）添加到相关屏幕。 在显示 GUI 和调整窗口大小时调用，会预先清除 buttonList。
     */
    @Override
    public void func_73866_w_() {
        try {
            //原始键盘启用重复事件
            Keyboard.enableRepeatEvents(true);
            //显示页面
            this.showPage();
            //初始化
            super.func_73866_w_();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 绘制屏幕和其中的所有组件。 参数：mouseX、mouseY、renderPartialTicks
     * @param i
     * @param j
     * @param f
     */
    @Override
    public void func_73863_a(int i, int j, float f) {

        try {
            //如果鼠标计数小于10
            if (this.mouseCount < 10) {
                //鼠标计数增加
                this.mouseCount++;
                //返回鼠标
                Mouse.setGrabbed(false);
            }
            //在背景屏幕上绘制渐变（如果存在）或在 background.png 上绘制平面渐变
            this.func_146276_q_();
            //建筑构建器
            String sim_gui_BC_Constructor = I18n.func_135052_a("container.sim.sim_gui_BC_Constructor");
            this.func_73732_a(this.field_146289_q, sim_gui_BC_Constructor, this.field_146294_l / 2, 17, 16777215);
            //闲置
            String sim_gui_BC_Idle = I18n.func_135052_a("container.sim.sim_gui_BC_Idle");
            //没有选择吗
            String sim_gui_BC_chosen = I18n.func_135052_a("container.sim.sim_gui_BC_chosen");
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
            } catch (Exception e) {
                //在路上
                String sim_gui_BC_their = I18n.func_135052_a("container.sim.sim_gui_BC_their");
                s = sim_gui_BC_their;
                t = "";
            }
            String sim_gui_BC_Current = I18n.func_135052_a("container.sim.sim_gui_BC_Current");//目前状态
            String sim_gui_BC_Building = I18n.func_135052_a("container.sim.sim_gui_BC_Building");//建筑类型
            this.func_73732_a(this.field_146289_q, sim_gui_BC_Current + s, this.field_146294_l / 2, 30, 11206655);
            this.func_73732_a(this.field_146289_q, sim_gui_BC_Building + t, this.field_146294_l / 2, 40, 11206655);
            switch (this.currentPage) {
                case 0:
                    String sim_gui_BC_building_constructor = I18n.func_135052_a("container.sim.sim_gui_BC_building_constructor");//请选择需要建造的项目
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_building_constructor, this.field_146294_l / 2, 100, 16777130);
                    break;
                case 1:
                    String sim_gui_BC_building = I18n.func_135052_a("container.sim.sim_gui_BC_building");//请选择一个建筑类型
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_building, this.field_146294_l / 2, 100, 16777130);
                    break;
                case 2:
                    String sim_gui_BC_residential = I18n.func_135052_a("container.sim.sim_gui_BC_residential");//现在选择住宅建筑
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_residential, this.field_146294_l / 2, 50, 16777130);
                    this.tfSearch.func_146194_f();
                    break;
                case 3:
                    String sim_gui_BC_unemployed = I18n.func_135052_a("container.sim.sim_gui_BC_unemployed");//选择一个你想雇佣的NPC
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_unemployed, this.field_146294_l / 2, 50, 16777130);
                    break;
                case 4:
                    String sim_gui_BC_employees = I18n.func_135052_a("container.sim.sim_gui_BC_employees");//这里是你所有的员工
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_employees, this.field_146294_l / 2, 50, 16777130);
                    break;
                case 5:
                    String sim_gui_BC_commercial = I18n.func_135052_a("container.sim.sim_gui_BC_commercial");//现在选择商业建筑的建筑
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_commercial, this.field_146294_l / 2, 50, 16777130);
                    this.tfSearch.func_146194_f();
                    break;
                case 6:
                    String sim_gui_BC_industrial = I18n.func_135052_a("container.sim.sim_gui_BC_industrial");//现在选择工业建筑的建筑
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_industrial, this.field_146294_l / 2, 50, 16777130);
                    this.tfSearch.func_146194_f();
                    break;
                case 7:
                    String sim_gui_BC_Now_choose = I18n.func_135052_a("container.sim.sim_gui_BC_Now_choose");//现在选择其他类型的建筑
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_Now_choose, this.field_146294_l / 2, 50, 16777130);
                    this.tfSearch.func_146194_f();
                    break;
                case 9:
                    //现在选择特殊类型的建筑
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC1"), this.field_146294_l / 2, 50, 16777130);
                    this.tfSearch.func_146194_f();
                    break;
                case 10:
                    String realCost = " (" + ModSimReloaded.displayMoney((float) this.selectedBuilding.blocksInBuilding * 0.02F * (float) this.theWorkers.size()) + ")";
                    //建筑细节
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC2") + this.selectedBuilding.displayNameWithoutPK, this.field_146294_l / 2, 50, 16777130);
                    //建筑名
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC3") + "：" + this.selectedBuilding.displayNameWithoutPK, this.field_146294_l / 2, 80, 16777130);
                    //说明
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC4") + "：" + this.selectedBuilding.description, this.field_146294_l / 2, 110, 16777130);
                    //作者
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC5") + "：" + this.selectedBuilding.author, this.field_146294_l / 2, 140, 16777130);
                    //费用
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC6") + "：" + realCost, this.field_146294_l / 2, 170, 16777130);
                    //尺寸
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC7") + "：" + this.selectedBuilding.dimensions, this.field_146294_l / 2, 200, 16777130);
                    //对应块构造数量应该是
                    this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.sim_gui_BC8") + "：" + this.selectedBuilding.elevationLevel, this.field_146294_l / 2, 230, 16777130);
                    break;
                case 8:
                    //建筑要求
                    String sim_gui_BC_requirements_for = I18n.func_135052_a("container.sim.sim_gui_BC_requirements_for");
                    this.func_73732_a(this.field_146289_q, sim_gui_BC_requirements_for + this.selectedBuilding.displayNameWithoutPK, this.field_146294_l / 2, 50, 16777130);
                    int y = 70;
                    Iterator it = this.selectedBuilding.requirements.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry) it.next();
                        ItemStack is = (ItemStack) pairs.getKey();
                        if (is != null) {
                            if (y + 20 > this.field_146295_m - 20) {
                                //...还有几种方块的类型
                                String sim_gui_BC_block_types = I18n.func_135052_a("container.sim.sim_gui_BC_block_types");
                                this.func_73731_b(this.field_146289_q, sim_gui_BC_block_types, 90, y, 16777215);
                            } else {
                                String itemName = is.func_82833_r();
                                //橡木
                                //System.out.println("****************************oak wood*****************************");
                                if (itemName.toLowerCase().contentEquals(I18n.func_135052_a("container.sim.sim_gui_BC9"))) {
                                    //木材
                                    itemName = I18n.func_135052_a("container.sim.sim_gui_BC10");
                                }
                                //橡木木板
                                if (itemName.toLowerCase().contains(I18n.func_135052_a("container.sim.sim_gui_BC11"))) {
                                    //木板
                                    itemName = I18n.func_135052_a("container.sim.sim_gui_BC12");
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimReloaded.log.warn("drawScreen出错了:"+e.getMessage()+"行数："+element.getLineNumber());
        }
        super.func_73863_a(i, j, f);
    }

    /**
     * 显示需求
     * @param block
     * @param qty
     * @param y
     */
    private void displayReq(String block, int qty, int y) {
        try {
            double stacks = Math.floor((double) (qty / 64));
            this.func_73731_b(this.field_146289_q, qty + "", 90, y, 16777215);
            this.func_73731_b(this.field_146289_q, "x", 125, y, 16777215);
            this.func_73731_b(this.field_146289_q, block, 150, y, 16777215);
            //(不到一组)
            String sim_gui_BC_less = I18n.func_135052_a("container.sim.sim_gui_BC_less");
            //(正好是一组)
            String sim_gui_BC_exactly = I18n.func_135052_a("container.sim.sim_gui_BC_exactly");
            //(大约两组)
            String sim_gui_BC_about_2 = I18n.func_135052_a("container.sim.sim_gui_BC_about_2");
            //(大约
            String sim_gui_BC_about = I18n.func_135052_a("container.sim.sim_gui_BC_about");
            //组)
            String sim_gui_BC_stacks = I18n.func_135052_a("container.sim.sim_gui_BC_stacks");
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

            this.func_73731_b(this.field_146289_q, st, 250, y, 16777215);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("displayReq出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     *显示分页
     */
    private void showPage() {
        try {
            this.field_146297_k.func_71364_i();
            //清除所有按钮
            this.field_146292_n.clear();
            //完成
            String sim_gui_BC_Done = I18n.func_135052_a("container.sim.sim_gui_BC_Done");
            this.field_146292_n.add(new GuiButton(0, 2, 12, 50, 20, sim_gui_BC_Done));
            //如果选定建筑物为空
            if (this.selectedBuilding == null) {
                //获得要构建的建筑
                this.selectedBuilding = Building.getBuildingByConBox(this.constructorLoc);
            }

            if (this.currentPage == 0) {
                //选择建筑
                String sim_gui_BC_Choose_building = I18n.func_135052_a("container.sim.sim_gui_BC_Choose_building");
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 60, 150, 120, 20, sim_gui_BC_Choose_building));
                //雇佣建筑工
                String sim_gui_BC_Hire_builder = I18n.func_135052_a("container.sim.Hire1");
                this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 180, 150, 120, 20, sim_gui_BC_Hire_builder));
                //员工
                String sim_gui_BC_worker = I18n.func_135052_a("container.sim.sim_gui_BC_worker");
                String w = sim_gui_BC_worker;
                //如果工人为1
                if (this.theWorkers.size() == 1) {
                    //获取员工名称
                    w = ((FolkData) this.theWorkers.get(0)).name;
                } else if (this.theWorkers.size() > 1) {
                    //工作人员
                    String sim_gui_BC_Staff = I18n.func_135052_a("container.sim.sim_gui_BC_Staff");
                    w = sim_gui_BC_Staff + "(" + this.theWorkers.size() + ")";
                }
                //解雇
                String sim_gui_BC_Fire = I18n.func_135052_a("container.sim.Fire");
                this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 60, 150, 120, 20, sim_gui_BC_Fire + w));
                //显示员工
                String sim_gui_BC_Show_Employees = I18n.func_135052_a("container.sim.sim_gui_BC_Show_Employees");
                this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 60, 170, 120, 20, sim_gui_BC_Show_Employees));
                //规划区域
                String sim_gui_BC_Terraform_area = I18n.func_135052_a("container.sim.sim_gui_BC_Terraform_area");
                this.field_146292_n.add(new GuiButton(5, -600, 170, 120, 20, "-"));
                this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 - 60, 170, 120, 20, sim_gui_BC_Terraform_area));
                //雇佣规划师
                this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2 - 180, 170, 120, 20, I18n.func_135052_a("container.sim.Hire22")));
                if (this.theWorkers.size() == 0) {
                    ((GuiButton) this.field_146292_n.get(1)).field_146124_l = false;
                    ((GuiButton) this.field_146292_n.get(2)).field_146124_l = true;
                    ((GuiButton) this.field_146292_n.get(3)).field_146124_l = false;
                    ((GuiButton) this.field_146292_n.get(6)).field_146124_l = false;
                    ((GuiButton) this.field_146292_n.get(7)).field_146124_l = true;
                } else {
                    ((GuiButton) this.field_146292_n.get(1)).field_146124_l = true;
                    ((GuiButton) this.field_146292_n.get(2)).field_146124_l = false;
                    ((GuiButton) this.field_146292_n.get(3)).field_146124_l = true;
                    ((GuiButton) this.field_146292_n.get(6)).field_146124_l = true;
                    ((GuiButton) this.field_146292_n.get(7)).field_146124_l = false;
                }
            } else if (this.currentPage == 1) {
                //现在选择要建造的住宅楼
                String sim_gui_BC_Residential = I18n.func_135052_a("container.sim.sim_gui_BC_Residential");
                //现在选择要建造的商业建筑
                String sim_gui_BC_Commercial = I18n.func_135052_a("container.sim.sim_gui_BC_Commercial");
                //现在选择要建造的工业建筑
                String sim_gui_BC_Industrial = I18n.func_135052_a("container.sim.sim_gui_BC_Industrial");
                //其他
                String sim_gui_BC_Other = I18n.func_135052_a("container.sim.sim_gui_BC_Other");
                //特别
                String sim_gui_BC_special = I18n.func_135052_a("container.sim.sim_gui_BC_special");
                this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 200, 150, 100, 20, sim_gui_BC_Residential));
                this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 - 100, 150, 100, 20, sim_gui_BC_Commercial));
                this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2, 150, 100, 20, sim_gui_BC_Industrial));
                this.field_146292_n.add(new GuiButton(8, this.field_146294_l / 2 + 100, 150, 100, 20, sim_gui_BC_Other));
                this.field_146292_n.add(new GuiButton(9, this.field_146294_l / 2 - 50, 180, 100, 20, sim_gui_BC_special));
            } else if (this.currentPage != 3) {
                if (this.currentPage == 10) {
                    //返回
                    this.field_146292_n.add(new GuiButton(1001, this.field_146294_l / 2 - 100, this.field_146295_m - 25, 100, 20, I18n.func_135052_a("container.sim.sim_gui_BC_Go_Back")));
                    //需求
                    this.field_146292_n.add(new GuiButton(969, this.field_146294_l / 2, this.field_146295_m - 25, 100, 20, I18n.func_135052_a("container.sim.sim_gui_BC_Go_demand")));
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
                                String sim_gui_BC_Fire = I18n.func_135052_a("container.sim.Fire");
                                this.field_146292_n.add(new GuiButton(idx, x, y, 100, 20, sim_gui_BC_Fire + folk.name));
                                ++x;
                                x += 100;
                                if (x + 100 > this.field_146294_l) {
                                    x = 10;
                                    y += 20;
                                }

                                if (y + 20 > this.field_146295_m - 50) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            //var18.printStackTrace();
                        }
                    } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                        if (this.currentPage == 8) {
                            //返回
                            String sim_gui_BC_Go_Back = I18n.func_135052_a("container.sim.sim_gui_BC_Go_Back");
                            this.field_146292_n.add(new GuiButton(1001, this.field_146294_l / 2 - 100, this.field_146295_m - 25, 100, 20, sim_gui_BC_Go_Back));
                            ///建造它
                            String sim_gui_BC_Build_it = I18n.func_135052_a("container.sim.sim_gui_BC_Build_it");
                            this.field_146292_n.add(new GuiButton(1000, this.field_146294_l / 2, this.field_146295_m - 25, 100, 20, sim_gui_BC_Build_it));
                        }
                    } else {
                        //房屋集合
                        CopyOnWriteArrayList<Building> houses = new CopyOnWriteArrayList();
                        String theType = "";
                        this.buildingsOnPage = 0;
                        this.tfSearch = new GuiTextField(0,this.field_146289_q, this.field_146294_l / 2 - 50, this.field_146295_m - 30, 100, 20);
                        this.tfSearch.func_146180_a(this.search);
                        this.tfSearch.func_146195_b(true);
                        this.tfSearch.func_146203_f(10);
                        if (this.currentPage == 2) {
                            //获取住宅蓝图
                            houses = Building.getBuildingBlueprints("residential", this.tfSearch.func_146179_b().trim());
                            theType = "residential";
                        } else if (this.currentPage == 5) {
                            //获取商业蓝图
                            houses = Building.getBuildingBlueprints("commercial", this.tfSearch.func_146179_b().trim());
                            theType = "commercial";
                        } else if (this.currentPage == 6) {
                            //获取工业蓝图
                            houses = Building.getBuildingBlueprints("industrial", this.tfSearch.func_146179_b().trim());
                            theType = "industrial";
                        } else if (this.currentPage == 7) {
                            //获取其他蓝图
                            houses = Building.getBuildingBlueprints("other", this.tfSearch.func_146179_b().trim());
                            theType = "other";
                        }else if (this.currentPage == 9) {
                            //获取特除蓝图
                            houses = Building.getBuildingBlueprints("special", this.tfSearch.func_146179_b().trim());
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
                                String sim_gui_BC_Page = I18n.func_135052_a("container.sim.sim_gui_BC_Page");
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
                                        this.field_146292_n.add(b3 = new GuiButton(idx + 300, x, y + 48, 120, 20, line4));
                                        GuiButton b2;
                                        //金额
                                        this.field_146292_n.add(b2 = new GuiButton(idx + 200, x, y + 32, 120, 20, line3));
                                        GuiButton b1;
                                        //范围
                                        this.field_146292_n.add(b1 = new GuiButton(idx + 100, x, y + 16, 120, 20, line2));
                                        //设置按钮不可用
                                        b1.field_146124_l = false;
                                        b2.field_146124_l = false;
                                        b3.field_146124_l = false;
                                        String pk = "";
                                        //去除建筑名字中的pkid
                                        if (building.displayName.startsWith("PKID")) {
                                            int hyphen = building.displayName.indexOf("-");
                                            pk = building.displayName.substring(0, hyphen + 1);
                                        }

                                        this.pkIndex.put(idx, pk);
                                        this.field_146292_n.add(new GuiButton(idx, x, y, 120, 20, building.displayNameWithoutPK));
                                        x += 120;
                                        if (x + 120 > this.field_146294_l) {
                                            x = 10;
                                            y += 71;
                                        }

                                        idx++;
                                        ++this.buildingsOnPage;
                                        if (this.buildingOffset > 0) {
                                            this.field_146292_n.add(new GuiButton(501, 5, this.field_146295_m - 20, 75, 20, "<" + sim_gui_BC_Page));
                                        }

                                        if (y + 20 + 20 + 20 + 20 > this.field_146295_m) {
                                            this.field_146292_n.add(new GuiButton(500, this.field_146294_l - 80, this.field_146295_m - 20, 75, 20, sim_gui_BC_Page + ">"));
                                            break;
                                        }
                                    }
                                } else {
                                    this.field_146292_n.add(new GuiButton(501, 5, this.field_146295_m - 20, 75, 20, "<" + sim_gui_BC_Page));
                                }
                            }

                            if (this.fixedBuildingCount == -1) {
                                this.fixedBuildingCount = this.buildingsOnPage;
                            }
                        } else {
                            String sim_gui_BC_Nothing_found = I18n.func_135052_a("container.sim.sim_gui_BC_Nothing_found");
                            this.field_146292_n.add(new GuiButton(1, 10, 60, 300, 20, sim_gui_BC_Nothing_found));
                        }
                    }
                }
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("showPage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 激活时由按钮列表中的控件调用。（鼠标按下按钮）
     * @param guibutton
     */
    @Override
    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void func_146284_a(GuiButton guibutton) {
        try {
            if (System.currentTimeMillis() - this.fuckingBodge >= 100L) {
                this.fuckingBodge = System.currentTimeMillis();
                if (guibutton.field_146124_l) {
                    if (guibutton.field_146127_k == 0) {
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                    } else {
                        if (this.currentPage == 0) {
                            String sim_gui_BC_Choose_building = I18n.func_135052_a("container.sim.sim_gui_BC_Choose_building");
                            if (guibutton.field_146126_j.contentEquals(sim_gui_BC_Choose_building)) {
                                this.currentPage = 1;
                                this.showPage();
                            } else {
                                GuiEmployFolk gui;
                                if (guibutton.field_146127_k == 2) {
                                    gui = new GuiEmployFolk(this.constructorLoc, this.buildDirection, Vocation.BUILDER);
                                    this.field_146297_k.func_147108_a((GuiScreen) null);
                                    this.field_146297_k.func_147108_a(gui);
                                } else if (guibutton.field_146127_k == 3) {
                                    this.fireAllFolksForThisBuilding();
                                    this.currentPage = 0;
                                    this.showPage();
                                } else if (guibutton.field_146127_k == 4) {
                                    GuiScreen guiScreen = new GuiShowEmployees();
                                    this.field_146297_k.func_147108_a((GuiScreen) null);
                                    this.field_146297_k.func_147108_a(guiScreen);
                                } else if (guibutton.field_146127_k != 5) {
                                    if (guibutton.field_146127_k == 6) {
                                        GuiScreen guiScreen = new GuiTerraform((FolkData) this.theWorkers.get(0));
                                        this.field_146297_k.func_147108_a((GuiScreen) null);
                                        this.field_146297_k.func_147108_a(guiScreen);
                                    } else if (guibutton.field_146127_k == 7) {
                                        gui = new GuiEmployFolk(this.constructorLoc, "N/A", Vocation.TERRAFORMER);
                                        this.field_146297_k.func_147108_a((GuiScreen) null);
                                        this.field_146297_k.func_147108_a(gui);
                                    }
                                }
                            }
                        } else if (this.currentPage == 1) {
                            if (guibutton.field_146127_k == 5) {
                                this.currentPage = 2;
                                this.showPage();
                            } else if (guibutton.field_146127_k == 6) {
                                this.currentPage = 5;
                                this.showPage();
                            } else if (guibutton.field_146127_k == 7) {
                                this.currentPage = 6;
                                this.showPage();
                            } else if (guibutton.field_146127_k == 8) {
                                this.currentPage = 7;
                                this.showPage();
                            }else if (guibutton.field_146127_k == 9) {
                                this.currentPage = 9;
                                this.showPage();
                            }
                        } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7 && this.currentPage != 9) {
                            if (this.currentPage == 8) {
                                //建造它
                                String sim_gui_BC_Build_it = I18n.func_135052_a("container.sim.sim_gui_BC_Build_it");
                                if (guibutton.field_146126_j.contentEquals(sim_gui_BC_Build_it)) {
                                    if (Building.getBuilding(this.selectedBuilding.primaryXYZ) != null) {
                                        ModSimReloaded.theBuildings.remove(this.selectedBuilding);
                                    }

                                    this.selectedBuilding.conBoxLocation = this.constructorLoc.clone();
                                    ModSimReloaded.theBuildings.add(this.selectedBuilding);
                                    this.selectedBuilding.saveThisBuilding();
                                    PacketHandler.net.sendToAll(new LoadBuildingPacket("GuiBuildingCon"));

                                    for (int i = 0; i < this.theWorkers.size(); i++) {
                                        FolkData theWorker = (FolkData) this.theWorkers.get(i);
                                        theWorker.theBuilding = this.selectedBuilding;
                                        theWorker.saveThisFolk();
                                    }

                                    this.field_146297_k.func_147108_a((GuiScreen) null);
                                    this.field_146297_k.func_71381_h();
                                    return;
                                }

                                if (guibutton.field_146127_k == 1001) {
                                    this.currentPage = this.previousPage;
                                    this.showPage();
                                }
                            } else if (this.currentPage == 10) {
                                if (guibutton.field_146127_k == 969) {
                                    this.currentPage = 8;
                                    this.showPage();
                                } else if (guibutton.field_146127_k == 1001) {
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
                            if (guibutton.field_146127_k == 500) {
                                this.buildingOffset += this.fixedBuildingCount;
                                this.showPage();
                                return;
                            }

                            if (guibutton.field_146127_k == 501) {
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
                            pkPrefix = (String) this.pkIndex.get(guibutton.field_146127_k);
                            this.selectedBuilding = Building.getFromAllBuildings(pkPrefix + guibutton.field_146126_j, type);

                            try {
                                this.selectedBuilding.buildDirection = this.buildDirection;
                            } catch (Exception e) {
                            }

                            this.currentPage = 8;
                            this.showPage();
                        }

                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIBUILDINGCONSTORUCTOR-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public void fireAllFolksForThisBuilding() {
        try {
            for (int i = 0; i < this.theWorkers.size(); i++) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("fireAllFolksForThisBuilding出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void func_73869_a(char c, int i) {
        try {
            if (i == 1) {
                this.field_146297_k.func_147108_a((GuiScreen) null);
                this.field_146297_k.func_71381_h();
            } else {
                if (this.tfSearch != null) {
                    this.tfSearch.func_146201_a(c, i);
                    this.search = this.tfSearch.func_146179_b();
                    if (!this.search.endsWith(":")) {
                        this.buildingOffset = 0;
                        this.showPage();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void func_73864_a(int i, int j, int k) {
        try {
        if (this.tfSearch != null) {
            this.tfSearch.func_146192_a(i, j, k);
        }
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }
    }
}

