package com.trhsy.sim.client.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.gui.*;
import com.trhsy.sim.common.entity.Marker;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
        if (ModSim.isDayTime()) {
            this.theFolk.gotoXYZ(location, (GotoMethod) null);
        }

    }

    /**
     * Gui暂停游戏吗
     * @return
     */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * 更新屏幕
     */
    @Override
    public void updateScreen() {
    }

    /**
     * 初始化Gui
     */
    @Override
    public void initGui() {
        //从该列表中删除所有元素（可选操作）。此呼叫返回后，列表将为空
        this.buttonList.clear();
        //添加 完成 按钮
        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
        //如果当前建筑物为空
        if (this.theBuilding == null) {
            //添加 修理房子 按钮
            this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Fix_House")));
        } else {
            //街区位置不等于空 并且 大小大于0 并且建筑完工
            if (this.theBuilding.blockLocations != null && this.theBuilding.blockLocations.size() > 0 && this.theBuilding.buildingComplete) {
                //拆除
                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, I18n.format("container.sim.Demolish")));
                //旋转楼梯
                this.buttonList.add(new GuiButton(1001, this.width - 110, 25, 100, 20, I18n.format("container.sim.Rotate_Stairs")));
            }
            //显示员工
            this.buttonList.add(new GuiButton(21, this.width - 110, this.height - 30, 100, 20, I18n.format("container.sim.sim_gui_BC_Show_Employees")));
            //将我传送到
            this.buttonList.add(new GuiButton(30, this.width - 110, this.height - 50, 100, 20, I18n.format("container.sim.Beam_me_to")));
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

                for (int fc = 0; fc < ModSim.theFolks.size(); ++fc) {
                    folk = (FolkData) ModSim.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        this.buttonList.add(new GuiButton(idx, this.width - 140, down - 6, 130, 20, I18n.format("container.sim.Fire") + folk.name));
                        this.employees.put(idx + 100, folk.name);
                        if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Depot"))) {
                            this.buttonList.add(new GuiButton(idx + 100, this.width - 190, down - 6, 50, 20, I18n.format("container.sim.Tasks")));
                        }

                        down += 20;
                        ++this.employeeCount;
                        ++idx;
                    }
                }
            }

            GuiButton b;
            if (this.theBuilding.type.contentEquals("commercial")) {
                System.out.println("***************************commercial*********************");
                //面包店
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Bakery"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Baker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //杂货铺
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Grocery_Store"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Grocer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //肉铺
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Butchers"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Butcher")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //汉堡店
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Burgers"))) {
                    ArrayList<FolkData> employees = FolkData.getFolksByEmployedAt(this.theBuilding.primaryXYZ);
                    Boolean flag = false;
                    GuiButton b1;
                    this.buttonList.add(b1 = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Manager")));
                    Iterator iterator = employees.iterator();

                    while(iterator.hasNext()) {
                        FolkData folkData = (FolkData)iterator.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSMANAGER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b1.enabled = false;
                    }

                    flag = false;
                    GuiButton b2;
                    this.buttonList.add(b2 = new GuiButton(2, 10, this.height - 50, 100, 20, I18n.format("container.sim.Hire_Fry_Cook")));
                    Iterator iterator1 = employees.iterator();

                    while(iterator1.hasNext()) {
                        FolkData folkData = (FolkData)iterator1.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSFRYCOOK) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b2.enabled = false;
                    }

                    flag = false;
                    GuiButton b3;
                    this.buttonList.add(b3 = new GuiButton(3, 10, this.height - 70, 100, 20, I18n.format("container.sim.Hire_Waiter")));
                    Iterator iterator2 = employees.iterator();

                    while(iterator2.hasNext()) {
                        FolkData folkData = (FolkData)iterator2.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSWAITER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b3.enabled = false;
                    }
                }
            }

            if (this.theBuilding.type.contentEquals("industrial")) {
                //伐木场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Lumbermill"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Lumberjack")));
                    if (this.employeeCount > 4) {
                        b.enabled = false;
                    }

                    if (BlockMarker.markers.size() == 1) {
                        this.buttonList.add(new GuiButton(20, this.width / 2 + 100, this.height - 30, 100, 20, I18n.format("container.sim.Set_Lumber_area")));
                    }
                }
                //建筑商
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Builders_Merchant"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Merchant")));
                    GuiButton b2;
                    this.buttonList.add(b2 = new GuiButton(25, 10, this.height - 50, 100, 20, I18n.format("container.sim.Buy_Sell")));
                    if (!ModSim.isDayTime() || this.employeeCount == 0) {
                        b2.enabled = false;
                    }

                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //军营
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Barracks"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Train_Soldier")));
                    if (this.employeeCount > 9) {
                        b.enabled = false;
                    }
                }
                //牧羊场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Sheep_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Shepherd")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //鸡蛋农场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Egg_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Egg_Farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //养牛场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Cattle_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Cattle_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //养猪场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Pig_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Pig_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //养鸡场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Chicken_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Chicken_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //仓库
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Depot"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Courier")));
                    if (this.employeeCount > 3) {
                        b.enabled = false;
                    }
                }
                //玻璃工厂
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Glass_Factory"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Glass_maker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //渔场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Fishing_Dock"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Fisherman")));
                    if (this.employeeCount > 1) {
                        b.enabled = false;
                    }
                }
                //奶牛场
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Dairy_Farm"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Dairy_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //奶酪工厂
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Cheese_Factory"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Cheesemaker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }
                //奶酪工厂2
                if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Cheese_Factory2"))) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Cheesemaker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                down = 70;
                int idx = 2;

                for (int i = 0; i < ModSim.theFolks.size(); ++i) {
                    folk = (FolkData) ModSim.theFolks.get(i);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Barracks"))) {
                            this.buttonList.add(new GuiButton(idx, this.width - 210, down - 6, 200, 20, I18n.format("container.sim.Dismiss") + folk.name));
                        } else if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_contains_Burgers"))) {
                            this.buttonList.add(new GuiButton(idx, this.width - 210, down - 6, 200, 20, I18n.format("container.sim.Fire") + folk.vocation.toString()));
                        } else {
                            this.buttonList.add(new GuiButton(idx, this.width - 140, down - 6, 130, 20, I18n.format("container.sim.Fire") + folk.name));
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
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Building_Control_Panel"), this.width / 2, 17, 16777215);
            if (this.theBuilding == null) {
                this.fontRendererObj.drawString(I18n.format("container.sim.on_this_building") + "(" + this.location.toString() + ")", 5, 77, 16711680);
                this.fontRendererObj.drawString(I18n.format("container.sim.this_Building"), 5, 97, 16711680);
            } else {
                String author = "";
                if (this.theBuilding.author != null && !this.theBuilding.author.contentEquals("")) {
                    author = " by " + this.theBuilding.author;
                }

                String isComplete = I18n.format("container.sim.Under_construction");
                if (this.theBuilding.buildingComplete) {
                    isComplete = I18n.format("container.sim.Active_Building");
                }

                this.fontRendererObj.drawString(I18n.format("container.sim.sim_Building") + " : " + this.theBuilding.displayNameWithoutPK + author, 5, 37, 16777088);
                this.fontRendererObj.drawString(I18n.format("container.sim.sim_Type") + " : " + this.theBuilding.type + " (" + isComplete + ")", 5, 47, 16777088);
                int down;
                if (!this.theBuilding.type.contentEquals("residential")) {
                    if (!this.theBuilding.type.contentEquals("industrial") && !this.theBuilding.type.contentEquals("commercial")) {
                        if (this.theBuilding.type.contentEquals("other")) {
                        }
                    } else {
                        this.fontRendererObj.drawString(I18n.format("container.sim.Employees") + " :", 5, 57, 16777088);
                        down = 70;

                        for (down = 0; down < ModSim.theFolks.size(); ++down) {
                            FolkData folk = (FolkData) ModSim.theFolks.get(down);
                            if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                                this.fontRendererObj.drawString(folk.name + " (" + folk.age + ") - " + folk.vocation.toString(), 20, down, 16777120);
                                down += 20;
                            }
                        }
                    }
                } else {
                    String s = "";
                    if (this.theBuilding.tenants.size() > 1 || this.theBuilding.tenants.size() == 0) {
                        s = "s";
                    }

                    this.fontRendererObj.drawString(this.theBuilding.tenants.size() + I18n.format("container.sim.Resident") + s + " :", 5, 57, 16777088);
                    down = 70;

                    for(int t = 0; t < this.theBuilding.tenants.size(); ++t) {
                        String folkname = (String)this.theBuilding.tenants.get(t);
                        this.fontRendererObj.drawString(folkname, 20, down, 16777120);
                        down += 20;
                    }
                }
            }

            super.drawScreen(i, j, f);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    /**
     * 执行的动作
     * @param guibutton
     */
    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                GuiEmployFolk ui;
                //雇佣伐木工人
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Lumberjack"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.LUMBERJACK);
                    this.mc.displayGuiScreen(ui);
                    //雇佣面包师
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Baker"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BAKER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣士兵
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Soldier"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SOLDIER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣牧羊人
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Shepherd"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SHEPHERD);
                    this.mc.displayGuiScreen(ui);
                    //雇佣杂货商
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Grocer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GROCER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣快递员
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Courier"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.COURIER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣商人
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Merchant"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.MERCHANT);
                    this.mc.displayGuiScreen(ui);
                    //屠夫
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire_Butcher"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BUTCHER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣蛋农
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Egg_Farmer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.EGGFARMER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣养猪户
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Pig_farmer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.PIGFARMER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣养牛户
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Cattle_farmer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CATTLEFARMER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣养鸡场主
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Chicken_farmer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHICKENFARMER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣玻璃制造商
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Glass_maker"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GLASSMAKER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣渔夫
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Fisherman"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.FISHERMAN);
                    this.mc.displayGuiScreen(ui);
                    //雇佣奶农
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Dairy_farmer"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.DAIRYFARMER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣奶酪匠
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Cheesemaker"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHEESEMAKER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣肯打鸡经理
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Manager"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSMANAGER);
                    this.mc.displayGuiScreen(ui);
                    //雇佣油炸厨师
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Fry_Cook"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSFRYCOOK);
                    this.mc.displayGuiScreen(ui);
                    //雇佣服务员
                } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Hire_Waiter"))) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSWAITER);
                    this.mc.displayGuiScreen(ui);
                } else {
                    int bindex;
                    String folkname;
                    //烧制 解雇
                    if (!guibutton.displayString.contains(I18n.format("container.sim.gui_btn_name_Fire")) && !guibutton.displayString.contains(I18n.format("container.sim.gui_btn_name_Dismiss"))) {
                        //修理房子
                        if (guibutton.displayString.contentEquals(I18n.format("container.sim.Fix_House"))) {
                            Building b;
                            ModSim.theBuildings.add(b = new Building(I18n.format("container.sim.Repaired_House"), "residential", this.location, this.location, true));
                            b.buildingComplete = true;
                            b.capacity = -1;
                            b.author = "Satscape";
                            this.theBuilding = b;
                            Building.saveAllBuildings();
                            //砍伐面积
                        } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Set_Lumber_area"))) {
                            this.theBuilding.lumbermillMarker = ((Marker) BlockMarker.markers.get(0)).toV3();
                            guibutton.enabled = false;
                        } else if (guibutton.id == 21) {
                            GuiScreen gui = new GuiShowEmployees();
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(gui);
                            //任务
                        } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Tasks"))) {
                            folkname = (String)this.employees.get(guibutton.id);
                            GuiScreen gui = new GuiCourierTasks(this.location, folkname, this.playerWhoClickedIt);
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(gui);
                            //买卖
                        } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Buy_Sell"))) {
                            GuiScreen uiGuiScreen = new GuiMerchant();
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(uiGuiScreen);
                        } else if (guibutton.id == 30) {
                            GuiScreen guiScreen = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(guiScreen);
                            //旋转楼梯
                        } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.Rotate_Stairs"))) {
                            this.rotateStairs();
                            //拆除
                        } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.gui_btn_name_Demolish"))) {
                            World theWorld = this.playerWhoClickedIt.worldObj;
                            bindex = 0;

                            for (int i = 0; i < ModSim.theBuildings.size(); ++i) {
                                Building build = (Building) ModSim.theBuildings.get(i);

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

                            ModSim.demolishWorld = theWorld;
                            Iterator i$ = this.theBuilding.blockLocations.iterator();

                            while(i$.hasNext()) {
                                V3 blockLoc = (V3)i$.next();
                                Block l = theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue());
                                if (l != null && ModSim.demolishBlocks.size() < 500) {
                                    blockLoc.blockID = l;
                                    ModSim.demolishBlocks.add(blockLoc);
                                }

                                theWorld.setBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), Blocks.air, 0, 3);
                                this.mc.theWorld.spawnParticle("explode", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.30000001192092896D, 0.0D);
                                this.mc.theWorld.spawnParticle("flame", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.4000000059604645D, 0.0D);
                            }

                            theWorld.playSoundAtEntity(this.playerWhoClickedIt, "random.explode", 1.0F, 1.0F);
                            ModSim.theBuildings.remove(bindex);
                            this.mc.displayGuiScreen((GuiScreen) null);
                        }
                    } else {
                        folkname = guibutton.displayString.substring(guibutton.displayString.indexOf(" ")).trim();
                        guibutton.enabled = false;
                        //仓库
                        if (this.theBuilding.displayName.contains(I18n.format("container.sim.gui_btn_name_Depot"))) {
                            for(bindex = 0; bindex < this.buttonList.size(); ++bindex) {
                                GuiButton but = (GuiButton)this.buttonList.get(bindex);
                                if (but.yPosition == guibutton.yPosition) {
                                    but.enabled = false;
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
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.setIngameFocus();
    }

    /**
     * 键入密钥
     * @param c
     * @param i
     */
    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    /**
     * 旋转楼梯
     */
    private void rotateStairs() {

        World theWorld = this.mc.getIntegratedServer().worldServerForDimension(this.theBuilding.primaryXYZ.theDimension);
        theWorld.playSoundEffect(this.theBuilding.primaryXYZ.x, this.theBuilding.primaryXYZ.y, this.theBuilding.primaryXYZ.z, ModSim.MODID + ":computer", 1.0F, 2.0F);
        Iterator iterator = this.theBuilding.blockLocations.iterator();

        while(true) {
            while(true) {
                while(true) {

                    while(iterator.hasNext()) {
                        //得到方块
                        V3 blockLoc = (V3)iterator.next();
                        ItemStack is = new ItemStack(theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()), 1, theWorld.getBlockMetadata(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()));
                        int newmeta;
                        if (Block.getBlockFromItem(is.getItem()) != Blocks.acacia_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.birch_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.dark_oak_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.spruce_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.stone_stairs) {
                            if (Block.getBlockFromItem(is.getItem()) != Blocks.torch && Block.getBlockFromItem(is.getItem()) != Blocks.redstone_torch && Block.getBlockFromItem(is.getItem()) != Blocks.redstone_torch) {
                                if (Block.getBlockFromItem(is.getItem()) == Blocks.bed) {
                                    newmeta = is.getMetadata();
                                    ++newmeta;
                                    if (newmeta == 4) {
                                        newmeta = 0;
                                    }

                                    theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                } else if (Block.getBlockFromItem(is.getItem()) != Blocks.piston && Block.getBlockFromItem(is.getItem()) != Blocks.piston_extension && Block.getBlockFromItem(is.getItem()) != Blocks.piston_head) {
                                    if (Block.getBlockFromItem(is.getItem()) == Blocks.wall_sign) {
                                        newmeta = is.getMetadata();
                                        if (newmeta == 0) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 8;
                                        } else if (newmeta == 8) {
                                            newmeta = 12;
                                        } else if (newmeta == 12) {
                                            newmeta = 0;
                                        }

                                        theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                    } else if (Block.getBlockFromItem(is.getItem()) != Blocks.wall_sign && Block.getBlockFromItem(is.getItem()) != Blocks.ladder) {
                                        if (Block.getBlockFromItem(is.getItem()) != Blocks.stone_button && Block.getBlockFromItem(is.getItem()) != Blocks.wooden_button) {
                                            if (Block.getBlockFromItem(is.getItem()) == Blocks.fence_gate) {
                                                newmeta = is.getMetadata();
                                                ++newmeta;
                                                if (newmeta > 3) {
                                                    newmeta = 0;
                                                }

                                                theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                            }
                                        } else {
                                            newmeta = is.getMetadata();
                                            if (newmeta == 1) {
                                                newmeta = 3;
                                            } else if (newmeta == 3) {
                                                newmeta = 2;
                                            } else if (newmeta == 2) {
                                                newmeta = 4;
                                            } else if (newmeta == 4) {
                                                newmeta = 1;
                                            }

                                            theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                        }
                                    } else {
                                        newmeta = is.getMetadata();
                                        if (newmeta == 2) {
                                            newmeta = 5;
                                        } else if (newmeta == 5) {
                                            newmeta = 3;
                                        } else if (newmeta == 3) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 2;
                                        }

                                        theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                    }
                                } else {
                                    newmeta = is.getMetadata();
                                    if (newmeta == 2) {
                                        newmeta = 5;
                                    } else if (newmeta == 5) {
                                        newmeta = 3;
                                    } else if (newmeta == 3) {
                                        newmeta = 4;
                                    } else if (newmeta == 4) {
                                        newmeta = 2;
                                    }

                                    theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                }
                            } else {
                                newmeta = is.getMetadata();
                                if (newmeta == 1) {
                                    newmeta = 3;
                                } else if (newmeta == 3) {
                                    newmeta = 2;
                                } else if (newmeta == 2) {
                                    newmeta = 4;
                                } else if (newmeta == 4) {
                                    newmeta = 1;
                                }

                                theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                            }
                        } else {
                            newmeta = is.getMetadata();
                            if (newmeta == 0) {
                                newmeta = 2;
                            } else if (newmeta == 1) {
                                newmeta = 3;
                            } else if (newmeta == 2) {
                                newmeta = 1;
                            } else if (newmeta == 3) {
                                newmeta = 0;
                            }

                            theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                        }
                    }

                    return;
                }
            }
        }
    }
}
