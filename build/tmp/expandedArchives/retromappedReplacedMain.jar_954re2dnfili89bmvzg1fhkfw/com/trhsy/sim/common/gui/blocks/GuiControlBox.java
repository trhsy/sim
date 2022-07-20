package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.entity.functionality.Marker;
import com.trhsy.sim.common.gui.folk.GuiCourierTasks;
import com.trhsy.sim.common.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.gui.folk.GuiMerchant;
import com.trhsy.sim.common.gui.folk.GuiShowEmployees;
import com.trhsy.sim.common.gui.other.GuiBeamPlayerTo;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ========================================
 *
 * @ClassName GuiControlBox
 * @Description todo Gui控制盒
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
public class GuiControlBox extends GuiScreen {
    //鼠标计数
    private int mouseCount = 0;
    //定位/位置信息
    public V3 location;
    //建筑物
    public Building theBuilding = null;
    //模拟NPC
    public FolkData theFolk = null;
    //员工人数
    public int employeeCount = 0;
    //雇员
    private HashMap employees = new HashMap();
    //点击编辑的玩家
    private EntityPlayer playerWhoClickedIt = null;

    /**
     *初始化Gui控制盒 实体玩家
     * @param location
     * @param thePlayer
     */
    public GuiControlBox(V3 location, EntityPlayer thePlayer) {
        //位子信息克隆
        this.location = location.clone();
        //装载所有建筑物
        Building.loadAllBuildings();
        //获取建筑信息
        this.theBuilding = Building.getBuilding(location);
        //玩家
        this.playerWhoClickedIt = thePlayer;
    }

    /**
     * 初始化Gui控制盒 NPC
     * @param location
     * @param folk
     */
    public GuiControlBox(V3 location, FolkData folk) {
        this.location = location;
        Building.loadAllBuildings();
        this.theBuilding = Building.getBuilding(location);
        this.theFolk = folk;
        //是否白天
        if (ModSimReloaded.isDayTime()) {
            this.theFolk.gotoXYZ(location, (GotoMethod) null);
        }

    }

    /**
     * Gui暂停游戏吗
     * @return
     */
    @Override
    public boolean func_73868_f() {
        return false;
    }

    /**
     * 更新屏幕
     */
    @Override
    public void func_73876_c() {
    }

    /**
     * 初始化Gui
     */
    @Override
    public void func_73866_w_() {
        //从该列表中删除所有元素（可选操作）。此呼叫返回后，列表将为空
        this.field_146292_n.clear();
        //添加 完成 按钮
        this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, I18n.func_135052_a("container.sim.sim_gui_BC_Done")));
        //如果当前建筑物为空
        if (this.theBuilding == null) {
            //添加 修理房子 按钮
            this.field_146292_n.add(new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Fix_House")));
        } else {
            //街区位置不等于空 并且 大小大于0 并且建筑完工
            if (this.theBuilding.blockLocations != null && this.theBuilding.blockLocations.size() > 0 && this.theBuilding.buildingComplete) {
                //拆除
                this.field_146292_n.add(new GuiButton(1000, this.field_146294_l - 110, 5, 100, 20, I18n.func_135052_a("container.sim.Demolish")));
                //旋转楼梯
                this.field_146292_n.add(new GuiButton(1001, this.field_146294_l - 110, 25, 100, 20, I18n.func_135052_a("container.sim.Rotate_Stairs")));
            }
            //显示员工
            this.field_146292_n.add(new GuiButton(21, this.field_146294_l - 110, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.sim_gui_BC_Show_Employees")));
            //将我传送到
            this.field_146292_n.add(new GuiButton(30, this.field_146294_l - 110, this.field_146295_m - 50, 100, 20, I18n.func_135052_a("container.sim.Beam_me_to")));
            int down;
            FolkData folk;
            //类型为商业或者工业
            if (this.theBuilding.type.contentEquals("commercial") || this.theBuilding.type.contentEquals("industrial")) {
                down = 70;
                int idx = 2;
                //雇佣人数为0
                this.employeeCount = 0;
                //员工清理
                this.employees.clear();

                for (int fc = 0; fc < ModSimReloaded.theFolks.size(); ++fc) {
                    folk = (FolkData) ModSimReloaded.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 140, down - 6, 130, 20, I18n.func_135052_a("container.sim.Fire") +" "+folk.name));
                        this.employees.put(idx + 100, folk.name);
                        if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Depot"))) {
                            this.field_146292_n.add(new GuiButton(idx + 100, this.field_146294_l - 190, down - 6, 50, 20, I18n.func_135052_a("container.sim.Tasks")));
                        }

                        down += 20;
                        ++this.employeeCount;
                        ++idx;
                    }
                }
            }

            GuiButton b;
            if (this.theBuilding.type.contentEquals("commercial")) {
                //System.out.println("***************************commercial*********************");
                //面包店
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Bakery"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire6")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //杂货铺
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Grocery_Store"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire9")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //肉铺
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Butchers"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire20")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //汉堡店
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Burgers"))) {
                    ArrayList<FolkData> employees = FolkData.getFolksByEmployedAt(this.theBuilding.primaryXYZ);
                    Boolean flag = false;
                    GuiButton b1;
                    this.field_146292_n.add(b1 = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire4")));
                    Iterator iterator = employees.iterator();

                    while(iterator.hasNext()) {
                        FolkData folkData = (FolkData)iterator.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSMANAGER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b1.field_146124_l = false;
                    }

                    flag = false;
                    GuiButton b2;
                    this.field_146292_n.add(b2 = new GuiButton(2, 10, this.field_146295_m - 50, 100, 20, I18n.func_135052_a("container.sim.Hire3")));
                    Iterator iterator1 = employees.iterator();

                    while(iterator1.hasNext()) {
                        FolkData folkData = (FolkData)iterator1.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSFRYCOOK) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b2.field_146124_l = false;
                    }

                    flag = false;
                    GuiButton b3;
                    this.field_146292_n.add(b3 = new GuiButton(3, 10, this.field_146295_m - 70, 100, 20, I18n.func_135052_a("container.sim.Hire2")));
                    Iterator iterator2 = employees.iterator();

                    while(iterator2.hasNext()) {
                        FolkData folkData = (FolkData)iterator2.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSWAITER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b3.field_146124_l = false;
                    }
                }
            }

            if (this.theBuilding.type.contentEquals("industrial")) {
                //伐木场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Lumbermill"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire19")));
                    if (this.employeeCount > 4) {
                        b.field_146124_l = false;
                    }

                    if (BlockMarker.markers.size() == 1) {
                        this.field_146292_n.add(new GuiButton(20, this.field_146294_l / 2 + 100, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Set_Lumber_area")));
                    }
                }
                //建筑商
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Builders_Merchant"))) {
                    //雇佣商人
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire11")));
                    GuiButton b2;
                    //买/卖
                    this.field_146292_n.add(b2 = new GuiButton(25, 10, this.field_146295_m - 50, 100, 20, I18n.func_135052_a("container.sim.Buy_Sell")));
                    if (!ModSimReloaded.isDayTime() || this.employeeCount == 0) {
                        b2.field_146124_l = false;
                    }

                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //军营
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Barracks"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire7")));
                    if (this.employeeCount > 9) {
                        b.field_146124_l = false;
                    }
                }
                //牧羊场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Sheep_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire8")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //鸡蛋农场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Egg_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire17")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //养牛场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Cattle_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire18")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //养猪场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Pig_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire16")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //养鸡场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Chicken_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire15")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //仓库
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Depot"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire10")));
                    if (this.employeeCount > 3) {
                        b.field_146124_l = false;
                    }
                }
                //玻璃工厂
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Glass_Factory"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire14")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //板砖厂
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Brick_factory"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire23")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //渔场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Fishing_Dock"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire13")));
                    if (this.employeeCount > 1) {
                        b.field_146124_l = false;
                    }
                }
                //奶牛场
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Dairy_Farm"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire12")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }
                //奶酪工厂
                if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Cheese_Factory"))) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, I18n.func_135052_a("container.sim.Hire5")));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                down = 70;
                int idx = 2;

                for (int i = 0; i < ModSimReloaded.theFolks.size(); ++i) {
                    folk = (FolkData) ModSimReloaded.theFolks.get(i);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Barracks"))) {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 210, down - 6, 200, 20, I18n.func_135052_a("container.sim.Dismiss") +" "+ folk.name));
                        } else if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_contains_Burgers"))) {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 210, down - 6, 200, 20, I18n.func_135052_a("container.sim.Fire") +" "+ folk.vocation.toString()));
                        } else {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 140, down - 6, 130, 20, I18n.func_135052_a("container.sim.Fire") +" "+ folk.name));
                        }

                        down += 20;
                    }
                }
            }

        }
    }

    /**
     * 移动检查
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
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Building_Control_Panel"), this.field_146294_l / 2, 17, 16777215);
            if (this.theBuilding == null) {
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.on_this_building") + "(" + this.location.toString() + ")", 5, 77, 16711680);
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.this_Building"), 5, 97, 16711680);
            } else {
                String author = "";
                if (this.theBuilding.author != null && !this.theBuilding.author.contentEquals("")) {
                    author = " by " + this.theBuilding.author;
                }

                String isComplete = I18n.func_135052_a("container.sim.Under_construction");
                if (this.theBuilding.buildingComplete) {
                    isComplete = I18n.func_135052_a("container.sim.Active_Building");
                }

                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.sim_Building") + " : " + this.theBuilding.displayNameWithoutPK + author, 5, 37, 16777088);
                this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.sim_Type") + " : " + this.theBuilding.type + " (" + isComplete + ")", 5, 47, 16777088);
                int down;
                if (!this.theBuilding.type.contentEquals("residential")) {
                    if (!this.theBuilding.type.contentEquals("industrial") && !this.theBuilding.type.contentEquals("commercial")) {
                        if (this.theBuilding.type.contentEquals("other")) {
                        }
                    } else {
                        this.field_146289_q.func_78276_b(I18n.func_135052_a("container.sim.Employees") + " :", 5, 57, 16777088);
                        down = 70;

                        for (down = 0; down < ModSimReloaded.theFolks.size(); ++down) {
                            FolkData folk = (FolkData) ModSimReloaded.theFolks.get(down);
                            if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                                this.field_146289_q.func_78276_b(folk.name + " (" + folk.age + ") - " + folk.vocation.toString(), 20, down, 16777120);
                                down += 20;
                            }
                        }
                    }
                } else {
                    String s = "";
                    if (this.theBuilding.tenants.size() > 1 || this.theBuilding.tenants.size() == 0) {
                        s = "s";
                    }

                    this.field_146289_q.func_78276_b(this.theBuilding.tenants.size() + I18n.func_135052_a("container.sim.Resident") + s + " :", 5, 57, 16777088);
                    down = 70;

                    for(int t = 0; t < this.theBuilding.tenants.size(); ++t) {
                        String folkname = (String)this.theBuilding.tenants.get(t);
                        this.field_146289_q.func_78276_b(folkname, 20, down, 16777120);
                        down += 20;
                    }
                }
            }

            super.func_73863_a(i, j, f);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    /**
     * 执行的动作
     * @param guibutton
     */
    @Override
    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                GuiEmployFolk ui;
                //雇佣伐木工人
                if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire19"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.LUMBERJACK);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣面包师
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire6"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BAKER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣士兵
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire7"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SOLDIER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣牧羊人
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire8"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SHEPHERD);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣杂货商
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire9"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GROCER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣快递员
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire10"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.COURIER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣商人
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire11"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.MERCHANT);
                    this.field_146297_k.func_147108_a(ui);
                    //屠夫
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire20"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BUTCHER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣蛋农
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire17"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.EGGFARMER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣养猪户
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire16"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.PIGFARMER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣养牛户
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire18"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CATTLEFARMER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣养鸡场主
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire15"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHICKENFARMER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣玻璃制造商
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire14"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GLASSMAKER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣板砖工
                }else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire23"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BRICKMAKER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣渔夫
                }  else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire13"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.FISHERMAN);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣奶农
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire12"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.DAIRYFARMER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣奶酪匠
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire5"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHEESEMAKER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣肯打鸡经理
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire4"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSMANAGER);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣油炸厨师
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire3"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSFRYCOOK);
                    this.field_146297_k.func_147108_a(ui);
                    //雇佣服务员
                } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Hire2"))) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSWAITER);
                    this.field_146297_k.func_147108_a(ui);
                } else {
                    int bindex;
                    String folkname;
                    // 解雇
                    if (!guibutton.field_146126_j.contains(I18n.func_135052_a("container.sim.Fire")) && !guibutton.field_146126_j.contains(I18n.func_135052_a("container.sim.Dismiss"))) {
                        //修理房子
                        if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Fix_House"))) {
                            Building b;
                            ModSimReloaded.theBuildings.add(b = new Building(I18n.func_135052_a("container.sim.Repaired_House"), "residential", this.location, this.location, true));
                            b.buildingComplete = true;
                            b.capacity = -1;
                            b.author = "Satscape";
                            this.theBuilding = b;
                            Building.saveAllBuildings();
                            //砍伐面积
                        } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_btn_name_Set_Lumber_area"))) {
                            this.theBuilding.lumbermillMarker = ((Marker) BlockMarker.markers.get(0)).toV3();
                            guibutton.field_146124_l = false;
                        } else if (guibutton.field_146127_k == 21) {
                            GuiScreen gui = new GuiShowEmployees();
                            this.field_146297_k.func_147108_a((GuiScreen)null);
                            this.field_146297_k.func_147108_a(gui);
                            //任务
                        } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_btn_name_Tasks"))) {
                            folkname = (String)this.employees.get(guibutton.field_146127_k);
                            GuiScreen gui = new GuiCourierTasks(this.location, folkname, this.playerWhoClickedIt);
                            this.field_146297_k.func_147108_a((GuiScreen)null);
                            this.field_146297_k.func_147108_a(gui);
                            //买卖
                        } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_btn_name_Buy_Sell"))) {
                            GuiScreen uiGuiScreen = new GuiMerchant();
                            this.field_146297_k.func_147108_a((GuiScreen)null);
                            this.field_146297_k.func_147108_a(uiGuiScreen);
                        } else if (guibutton.field_146127_k == 30) {
                            GuiScreen guiScreen = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                            this.field_146297_k.func_147108_a((GuiScreen)null);
                            this.field_146297_k.func_147108_a(guiScreen);
                            //旋转楼梯
                        } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.Rotate_Stairs"))) {
                            this.rotateStairs();
                            //拆除
                        } else if (guibutton.field_146126_j.contentEquals(I18n.func_135052_a("container.sim.gui_btn_name_Demolish"))) {
                            World theWorld = this.playerWhoClickedIt.field_70170_p;
                            bindex = 0;

                            for (int i = 0; i < ModSimReloaded.theBuildings.size(); ++i) {
                                Building build = (Building) ModSimReloaded.theBuildings.get(i);

                                try {
                                    if (build.primaryXYZ.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true)) {
                                        bindex = i;
                                        FolkData theFolk = FolkData.getFolkByEmployedAt(this.theBuilding.primaryXYZ);
                                        if (theFolk != null) {
                                            theFolk.selfFire();
                                        }
                                    }
                                } catch (Exception var7) {
                                }
                            }

                            ModSimReloaded.demolishWorld = theWorld;
                            Iterator iterator = this.theBuilding.blockLocations.iterator();

                            while(iterator.hasNext()) {
                                V3 blockLoc = (V3)iterator.next();
                                BlockPos blockPos=new BlockPos(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue());
                                Block l = theWorld.func_180495_p(blockPos).func_177230_c();
                                if (l != null && ModSimReloaded.demolishBlocks.size() < 500) {
                                    blockLoc.blockID = l;
                                    ModSimReloaded.demolishBlocks.add(blockLoc);
                                }

                                theWorld.func_180501_a(blockPos, Blocks.field_150350_a.func_176223_P(), 3);
                                this.field_146297_k.field_71441_e.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0, 0.30000001192092896D, 0);
                                this.field_146297_k.field_71441_e.func_175688_a(EnumParticleTypes.FLAME, (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0, 0.4000000059604645D, 0);
                            }

                            theWorld.func_72956_a(this.playerWhoClickedIt, "random.explode", 1.0F, 1.0F);
                            ModSimReloaded.theBuildings.remove(bindex);
                            this.field_146297_k.func_147108_a((GuiScreen) null);
                        }
                    } else {
                        String dis=guibutton.field_146126_j;
                        int index=dis.indexOf(" ");
                        folkname = dis.substring(index,dis.length()).trim();
                        guibutton.field_146124_l = false;
                        //仓库
                        if (this.theBuilding.displayName.contains(I18n.func_135052_a("container.sim.gui_btn_name_Depot"))) {
                            for(bindex = 0; bindex < this.field_146292_n.size(); ++bindex) {
                                GuiButton but = (GuiButton)this.field_146292_n.get(bindex);
                                if (but.field_146129_i == guibutton.field_146129_i) {
                                    but.field_146124_l = false;
                                }
                            }
                        }

                        FolkData folk = FolkData.getFolkByName(folkname);
                        if (folk != null) {
                            folk.selfFire();
                        }
                    }
                }

            }
        }
    }

    /**
     * 在Gui上关闭
     */
    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    /**
     * 键入密钥
     * @param c
     * @param i
     */
    @Override
    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }

    /**
     * 旋转楼梯
     */
    private void rotateStairs() {

        World theWorld = this.field_146297_k.func_71401_C().func_71218_a(this.theBuilding.primaryXYZ.theDimension);
        theWorld.func_72908_a(this.theBuilding.primaryXYZ.x, this.theBuilding.primaryXYZ.y, this.theBuilding.primaryXYZ.z, ModSim.MODID + ":computer", 1.0F, 2.0F);
        Iterator iterator = this.theBuilding.blockLocations.iterator();

        while(true) {
            while(true) {
                while(true) {

                    while(iterator.hasNext()) {
                        //得到方块
                        V3 blockLoc = (V3)iterator.next();
                        BlockPos blockPos=new BlockPos(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue());
                        Block id=theWorld.func_180495_p(blockPos).func_177230_c();
                        Chunk chunk = theWorld.func_175726_f(blockPos);
                        ItemStack is = new ItemStack(theWorld.func_180495_p(blockPos).func_177230_c(), 1, id.func_176201_c(theWorld.func_180495_p(blockPos)));
                        int newmeta;
                        if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150400_ck && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150487_bG && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150401_cl && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150485_bF && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150446_ar) {
                            if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150478_aa && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150429_aA && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150429_aA) {
                                if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_150324_C) {
                                    newmeta = is.func_77960_j();
                                    ++newmeta;
                                    if (newmeta == 4) {
                                        newmeta = 0;
                                    }
                                    theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),2);
                                    //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                } else if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150331_J && Block.func_149634_a(is.func_77973_b()) != Blocks.field_180384_M && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150332_K) {
                                    if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_150444_as) {
                                        newmeta = is.func_77960_j();
                                        if (newmeta == 0) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 8;
                                        } else if (newmeta == 8) {
                                            newmeta = 12;
                                        } else if (newmeta == 12) {
                                            newmeta = 0;
                                        }
                                        theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),2);
                                        //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                    } else if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150444_as && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150468_ap) {
                                        if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150430_aB && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150471_bO) {
                                            if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_180390_bo) {
                                                newmeta = is.func_77960_j();
                                                ++newmeta;
                                                if (newmeta > 3) {
                                                    newmeta = 0;
                                                }
                                                theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                                                //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                            }
                                        } else {
                                            newmeta = is.func_77960_j();
                                            if (newmeta == 1) {
                                                newmeta = 3;
                                            } else if (newmeta == 3) {
                                                newmeta = 2;
                                            } else if (newmeta == 2) {
                                                newmeta = 4;
                                            } else if (newmeta == 4) {
                                                newmeta = 1;
                                            }
                                            theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                                            //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                        }
                                    } else {
                                        newmeta = is.func_77960_j();
                                        if (newmeta == 2) {
                                            newmeta = 5;
                                        } else if (newmeta == 5) {
                                            newmeta = 3;
                                        } else if (newmeta == 3) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 2;
                                        }
                                        theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                                        //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                    }
                                } else {
                                    newmeta = is.func_77960_j();
                                    if (newmeta == 2) {
                                        newmeta = 5;
                                    } else if (newmeta == 5) {
                                        newmeta = 3;
                                    } else if (newmeta == 3) {
                                        newmeta = 4;
                                    } else if (newmeta == 4) {
                                        newmeta = 2;
                                    }
                                    theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                                    //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                }
                            } else {
                                newmeta = is.func_77960_j();
                                if (newmeta == 1) {
                                    newmeta = 3;
                                } else if (newmeta == 3) {
                                    newmeta = 2;
                                } else if (newmeta == 2) {
                                    newmeta = 4;
                                } else if (newmeta == 4) {
                                    newmeta = 1;
                                }
                                theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                                //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                            }
                        } else {
                            newmeta = is.func_77960_j();
                            if (newmeta == 0) {
                                newmeta = 2;
                            } else if (newmeta == 1) {
                                newmeta = 3;
                            } else if (newmeta == 2) {
                                newmeta = 1;
                            } else if (newmeta == 3) {
                                newmeta = 0;
                            }
                            theWorld.markAndNotifyBlock(blockPos,chunk,id.func_176223_P(),id.func_176223_P(),3);
                            //theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                        }
                    }

                    return;
                }
            }
        }
    }
}
