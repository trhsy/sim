package com.trhsy.sim.gui.block;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.gui.npc.GuiEmployees;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.*;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.npc.build.TerrainType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName GuiBlockConstructorBlock
 * @Description todo 打开建筑箱gui
 * @Author TRHSY
 * @Date 2022/10/1916:23
 **/
public class GuiBlockConstructorBlock extends GuiScreen {
    /**
     * 有员工
     **/
    public boolean hasEmployee = false;
    /**
     * 雇佣的npc
     **/
    public NpcIdentity employee;
    /**
     * 建筑蓝图
     **/
    public BuildingBlueprint selectedBlueprint;
    /**
     * 蓝图集合
     **/
    public List<BuildingBlueprint> potentialBlueprints = new CopyOnWriteArrayList<>();
    /**
     * 可雇佣的人
     **/
    public NpcIdentity[] hireableFolkNames = new NpcIdentity[1000];
    /**
     * 雇佣地形师
     */
    public boolean hiringTerraformer = false;
    /**
     * 选定的员工
     **/
    GuiButton selectedEmployee;
    /**
     * 位置
     **/
    public BlockPos pos;
    /**
     * 建筑方向
     **/
    public int buildDirection;
    /**
     * 分页
     */
    public int currentPage = 0;
    /**
     * 上一页
     */
    public int previousPage = 1;
    /**
     * 搜索
     */
    private GuiTextField tfSearch;
    /**
     * 搜索文字
     **/
    private String search = "";
    /**
     * 建筑偏移量
     */
    private int buildingOffset;
    /**
     * 当前第几页
     */
    private int buildingsOnPage = 0;
    /**
     * 建筑的时间
     */
    long fingBodge = 0L;

    /**
     * @return 块，方向
     * @Author fan
     * @Description //TODO 初始化
     * @Date 13:33 2022/10/31
     * @Param [p, bDir]
     **/
    public GuiBlockConstructorBlock(BlockPos p, int bDir) {
        this.pos = p;
        this.buildDirection = bDir;
        this.hasEmployee = false;
        this.employee = null;
        this.getHireableFolkNames();
        if (ModSimLoader.previewConstructor == p) {
            //constructorPreviousPage
            ModSimLoader.constructorPreviousPage = 0;
            //savedBlueprint
            ModSimLoader.savedBlueprint = null;
            //要构建的
            ModSimLoader.previewConstructor = null;
            //预览位置
            ModSimLoader.previewPos1 = null;
            ModSimLoader.previewPos2 = null;
        }

    }

    /**
     * @return 方块，建筑方向，npc
     * @Author fan
     * @Description //TODO 初始化
     * @Date 13:37 2022/10/31
     * @Param [p, bDir, folk]
     **/
    public GuiBlockConstructorBlock(BlockPos p, int bDir, NpcIdentity folk) {
        this.pos = p;
        this.buildDirection = bDir;
        this.employee = folk;
        this.hasEmployee = true;
        this.getHireableFolkNames();
        //
        if (p.equals(ModSimLoader.previewConstructor)) {
            this.selectedBlueprint = ModSimLoader.savedBlueprint;
            if (this.selectedBlueprint != null) {
                this.previousPage = ModSimLoader.constructorPreviousPage;
                this.currentPage = 11;
            }

            ModSimLoader.constructorPreviousPage = 0;
            ModSimLoader.savedBlueprint = null;
            ModSimLoader.previewConstructor = null;
            ModSimLoader.previewPos1 = null;
            ModSimLoader.previewPos2 = null;
        }

    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 如果此GUI在单人游戏中显示时应暂停游戏，则返回true
     * @Date 13:44 2022/10/31
     * @Param []
     **/
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 初始化GUI
     * @Date 13:46 2022/10/31
     * @Param []
     **/
    public void initGui() {
        super.initGui();
        //显示分页
        this.showPage();
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 显示分页
     * @Date 13:46 2022/10/31
     * @Param []
     **/
    public void showPage() {
        //设置不聚集
        this.mc.setIngameNotInFocus();
        //所有按钮清楚
        this.buttonList.clear();
        //完成按钮
        this.buttonList.add(new GuiButton(0, 2, 12, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
        if (this.currentPage == 0) {
            //选择建筑
            this.buttonList.add(new GuiButton(1, this.width / 2 - 60, 150, 120, 20, I18n.format("container.sim.sim_gui_BC_Choose_building")));
            //雇佣建筑工
            this.buttonList.add(new GuiButton(2, this.width / 2 - 180, 150, 120, 20, I18n.format("container.sim.Hire1")));
            //员工
            String w = I18n.format("container.sim.sim_gui_BC_worker");
            //解雇
            this.buttonList.add(new GuiButton(3, this.width / 2 + 60, 150, 120, 20, I18n.format("container.sim.Fire") + w));
            //显示员工
            this.buttonList.add(new GuiButton(4, this.width / 2 + 60, 170, 120, 20, I18n.format("container.sim.sim_gui_BC_Show_Employees")));
            this.buttonList.add(new GuiButton(5, -600, 170, 120, 20, "-"));
            //规划区域
            this.buttonList.add(new GuiButton(6, this.width / 2 - 60, 170, 120, 20, I18n.format("container.sim.sim_gui_BC_Terraform_area")));
            //雇佣规划师
            this.buttonList.add(new GuiButton(7, this.width / 2 - 180, 170, 120, 20, I18n.format("container.sim.Hire22")));
            if (!this.hasEmployee) {
                //选择建筑隐藏
                ((GuiButton) this.buttonList.get(1)).enabled = false;
                //雇佣建筑工可用
                ((GuiButton) this.buttonList.get(2)).enabled = true;
                //解雇不可用
                ((GuiButton) this.buttonList.get(3)).enabled = false;
                //规划区域不可用
                ((GuiButton) this.buttonList.get(6)).enabled = false;
                //雇佣规划师不可用
                ((GuiButton) this.buttonList.get(7)).enabled = true;
            } else {
                if (this.hiringTerraformer) {
                    //选择建筑不可用
                    ((GuiButton) this.buttonList.get(1)).enabled = false;
                    //规划区域可用
                    ((GuiButton) this.buttonList.get(6)).enabled = true;
                } else {
                    //选择建筑可用
                    ((GuiButton) this.buttonList.get(1)).enabled = true;
                    //规划区域不可用
                    ((GuiButton) this.buttonList.get(6)).enabled = false;
                }
                //雇佣建筑工不可用
                ((GuiButton) this.buttonList.get(2)).enabled = false;
                //解雇不可用
                ((GuiButton) this.buttonList.get(3)).enabled = true;

                //雇佣规划师不可用
                ((GuiButton) this.buttonList.get(7)).enabled = false;
            }
        } else if (this.currentPage == 1) {
            //住宅
            this.buttonList.add(new GuiButton(5, this.width / 2 - 200, 150, 100, 20, I18n.format("container.sim.sim_gui_BC_Residential")));
            //商业
            this.buttonList.add(new GuiButton(6, this.width / 2 - 100, 150, 100, 20, I18n.format("container.sim.sim_gui_BC_Commercial")));
            //工业
            this.buttonList.add(new GuiButton(7, this.width / 2, 150, 100, 20, I18n.format("container.sim.sim_gui_BC_Industrial")));
            //其他
            this.buttonList.add(new GuiButton(8, this.width / 2 + 100, 150, 100, 20, I18n.format("container.sim.sim_gui_BC_Other")));
            //特殊
            this.buttonList.add(new GuiButton(9, this.width / 2 - 50, 180, 100, 20, I18n.format("container.sim.sim_gui_BC_special")));
            //装饰
            this.buttonList.add(new GuiButton(10, this.width / 2 - 150, 180, 100, 20, I18n.format("container.sim.sim_gui_BC_Decorative")));
            //管理
//            this.buttonList.add(new GuiButton(11, this.width / 2 + 50, 180, 100, 20, "Administrative"));
            //返回
            this.buttonList.add(new GuiButton(505, 52, 12, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
        } else {
            int y;
            int idx;
            int x;
            if (this.currentPage == 2) {
                //取消
                this.buttonList.add(new GuiButton(1000, this.width / 2 - 200, this.height - 30, I18n.format("container.sim.sim_gui_player_to_Cancel")));
                //好
                this.buttonList.add(new GuiButton(1001, this.width / 2, this.height - 30, I18n.format("container.sim.gui_btn_name_OK")));

                try {
                    x = 10;
                    y = 80;
                    idx = 100;
                    //显示所有失业人员
                    for (int f = 0; f < ModSimLoader.getUnemployedFolks().size(); ++f) {
                        NpcIdentity folk = (NpcIdentity) ModSimLoader.getUnemployedFolks().get(f);
                        this.buttonList.add(new GuiButton(idx, x, y, 110, 20, folk.name));
                        this.hireableFolkNames[idx] = folk;
                        ++idx;
                        x += 110;
                        if (x + 110 > this.width) {
                            x = 10;
                            y += 20;
                        }

                        if (y + 20 > this.height - 50) {
                            break;
                        }
                    }
                } catch (Exception var16) {
                }
            } else if (this.currentPage == 11) {
                //返回
                this.buttonList.add(new GuiButton(1000, this.width / 2 - 150, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Go_Back")));
                //预览
                this.buttonList.add(new GuiButton(1001, this.width / 2 - 50, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Preview")));
                //需求
                this.buttonList.add(new GuiButton(969, this.width / 2 + 50, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_requirements") + " ->"));
            } else if (this.currentPage == 12) {
                //返回
                this.buttonList.add(new GuiButton(1000, this.width / 2 - 150, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Go_Back")));
                //建造它
                this.buttonList.add(new GuiButton(970, this.width / 2 + 50, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Build_it")));
            } else if (this.currentPage == 13) {
                //规划区域
                //返回
                this.buttonList.add(new GuiButton(1000, this.width / 2 - 150, this.height - 25, 100, 20, I18n.format("container.sim.sim_gui_BC_Go_Back")));
                //'填海'(填为陆地)
                this.buttonList.add(new GuiButton(21, this.width / 2 - 200, 35, 200, 20, I18n.format("container.sim.Terraform2")));
                //'绿化' (铺草坪)
                this.buttonList.add(new GuiButton(22, this.width / 2 - 200, 55, 200, 20, I18n.format("container.sim.Terraform3")));
                //'除草' (割草)
                this.buttonList.add(new GuiButton(23, this.width / 2 - 200, 75, 200, 20, I18n.format("container.sim.Terraform4")));
                //'平整化' (整平地面)
                this.buttonList.add(new GuiButton(24, this.width / 2 - 200, 95, 200, 20, I18n.format("container.sim.Terraform5")));
                //'超值套装' (单层泥土)
                this.buttonList.add(new GuiButton(25, this.width / 2 - 200, 115, 200, 20, I18n.format("container.sim.Terraform6")));
                //'冰川' (将水冻住或雪地化)
                this.buttonList.add(new GuiButton(26, this.width / 2, 35, 200, 20, I18n.format("container.sim.Terraform7")));
                //'湿润化' (添加水)
                this.buttonList.add(new GuiButton(27, this.width / 2, 55, 200, 20, I18n.format("container.sim.Terraform8")));
                //'炎热化' (添加岩浆)
                this.buttonList.add(new GuiButton(28, this.width / 2, 75, 200, 20, I18n.format("container.sim.Terraform9")));
                //'除雪' (铲雪)
                this.buttonList.add(new GuiButton(29, this.width / 2, 95, 200, 20, I18n.format("container.sim.Terraform10")));
            } else if (this.currentPage > 3 && this.currentPage < 12) {
                //页在3和12页
                this.buildingsOnPage = 0;
                //搜索输入框
                this.tfSearch = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 50, this.height - 30, 100, 20);
                //输入框内容
                this.tfSearch.setText(this.search);
                //设置焦点
                this.tfSearch.setFocused(true);
                //设置最大字符串长度
                this.tfSearch.setMaxStringLength(10);

                if (this.currentPage == 4) {
                    //住宅
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_Residential"), this.tfSearch.getText().trim());
                } else if (this.currentPage == 5) {
                    //商业
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_Commercial"), this.tfSearch.getText().trim());
                } else if (this.currentPage == 6) {
                    //工业
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_Industrial"), this.tfSearch.getText().trim());
                } else if (this.currentPage == 7) {
                    //其他
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_Other"), this.tfSearch.getText().trim());
                } else if (this.currentPage == 8) {
                    //特除
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_special"), this.tfSearch.getText().trim());
                } else if (this.currentPage == 9) {
                    //                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType("Administrative", this.tfSearch.getText().trim());
                } else if (this.currentPage == 10) {
                    //装饰
                    this.potentialBlueprints = ModSimLoader.getBlueprintsByType(I18n.format("container.sim.sim_gui_BC_Decorative"), this.tfSearch.getText().trim());
                }
                //返回
                this.buttonList.add(new GuiButton(505, 52, 12, 50, 20, I18n.format("container.sim.gui_Folk_Back")));
                x = 10;
                y = 60;
                idx = 1;

                if (this.potentialBlueprints != null) {
                    boolean hasLeftArrow = false;
                    boolean hasRightArrow = false;
                    this.buildingsOnPage = 0;
                    //所有蓝图
                    for (int b = 0; b <= this.potentialBlueprints.size(); ++b) {
                        int boff = b + this.buildingOffset;
                        if (boff < 0) {
                            boff = 0;
                            this.buildingOffset = 0;
                        }

                        if (boff < this.potentialBlueprints.size()) {
                            if (this.potentialBlueprints.get(boff) != null) {
                                String line3 = "";
                                //建筑蓝图
                                BuildingBlueprint building = (BuildingBlueprint) this.potentialBlueprints.get(boff);
                                String realCost = "";
                                //建筑蓝图所需方块*0.02就是所需金钱
                                realCost = " (" + ModSimLoader.displayMoney((float) building.blockCount * 0.02F) + ")";
                                //长宽高
                                String line2 = building.length + " x " + building.width + " x " + building.height;
                                //显示金额
                                line3 = ModSimLoader.displayMoney((float) building.blockCount * 0.02F) + realCost;
                                //作者
                                String line4 = building.author;
                                GuiButton b3;
                                //作者
                                this.buttonList.add(b3 = new GuiButton(idx + 3000, x, y + 48, 120, 20, line4));
                                GuiButton b2;
                                //金额
                                this.buttonList.add(b2 = new GuiButton(idx + 2000, x, y + 32, 120, 20, line3));
                                GuiButton b1;
                                //长宽高
                                this.buttonList.add(b1 = new GuiButton(idx + 1000, x, y + 16, 120, 20, line2));
                                //设置按钮不可用
                                b1.enabled = false;
                                b2.enabled = false;
                                b3.enabled = false;
                                //建筑名字
                                this.buttonList.add(new GuiButton(idx, x, y, 120, 20, building.name));
                                x += 120;
                                if (x + 120 > this.width) {
                                    x = 10;
                                    y += 71;
                                }

                                ++idx;
                                ++this.buildingsOnPage;
                                if (this.buildingOffset > 0 && !hasLeftArrow) {
                                    hasLeftArrow = true;
                                    //页
                                    this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "< " + I18n.format("container.sim.sim_gui_BC_Page")));
                                }

                                if (y + 20 + 20 + 20 + 20 > this.height && !hasRightArrow) {
                                    hasRightArrow = true;
                                    //页
                                    this.buttonList.add(new GuiButton(500, this.width - 80, this.height - 20, 75, 20, I18n.format("container.sim.sim_gui_BC_Page") + " >"));
                                    break;
                                }
                            }
                        } else {
                            //页
                            this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "< " + I18n.format("container.sim.sim_gui_BC_Page")));
                        }
                    }
                }
            }
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 激活时由按钮列表中的控件调用。（鼠标按下按钮）
     * @Date 15:48 2022/10/31
     * @Param [guibutton]
     **/
    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void actionPerformed(GuiButton guibutton) {
        //时间限制
        if (System.currentTimeMillis() - this.fingBodge >= 100L) {

            this.fingBodge = System.currentTimeMillis();
            //按钮激活了
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                    //显示员工
                } else if (guibutton.id == 4 && this.currentPage == 0) {
                    //雇佣员工
                    Minecraft.getMinecraft().displayGuiScreen(new GuiEmployees());
                } else {
                    //返回
                    if (this.currentPage == 1 && guibutton.id == 505) {
                        this.previousPage = this.currentPage;
                        this.currentPage = 0;
                        this.showPage();
                    }

                    if (this.currentPage == 2) {
                        //第二页面的返回
                        if (guibutton.id == 1000) {
                            this.previousPage = this.currentPage;
                            this.currentPage = 0;
                            this.showPage();
                        }
                        //预览 并且选中的员工不为空
                        if (guibutton.id == 1001 && this.selectedEmployee != null) {
                            //已经雇佣
                            this.hasEmployee = true;
                            //雇佣的人
                            this.employee = this.hireableFolkNames[this.selectedEmployee.id];
                            //上一页数
                            this.previousPage = this.currentPage;
                            //当前页
                            this.currentPage = 0;
                            this.showPage();
                            if (this.hiringTerraformer) {
                                //雇佣NPC 规划师
                                NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, I18n.format("container.sim.Vocation16"), V3.fromBlockPos(this.pos), this.buildDirection));
                            } else {
                                //雇佣建筑师
                                NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, I18n.format("container.sim.Vocation1"), V3.fromBlockPos(this.pos), this.buildDirection));
                            }
                        }
                        //雇佣的人
                        if (guibutton.id > 99 && guibutton.id < 1000) {

                            if (this.selectedEmployee != null) {
                                this.selectedEmployee.enabled = true;
                            }

                            this.selectedEmployee = guibutton;
                            this.selectedEmployee.enabled = false;
                        }
                    }
                    //选择建筑
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Choose_building"))) {
                        this.previousPage = this.currentPage;
                        this.currentPage = 1;
                        this.showPage();
                    }
                    //规划区域
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Terraform_area"))) {
                        this.previousPage = this.currentPage;
                        this.currentPage = 13;
                        this.showPage();
                    }
                    if (this.currentPage == 11) {
                        //返回
                        if (guibutton.id == 1000) {
                            this.currentPage = this.previousPage;
                            this.showPage();
                        }

                        if (guibutton.id == 1001) {
                            //建筑位置
                            ModSimLoader.previewConstructor = this.pos;
                            //蓝图
                            ModSimLoader.savedBlueprint = this.selectedBlueprint;
                            //预览1
                            ModSimLoader.previewPos1 = this.selectedBlueprint.getFirstPoint(this.pos, this.buildDirection);
                            //预览2
                            ModSimLoader.previewPos2 = this.selectedBlueprint.getSecondPoint(this.pos, this.buildDirection);
                            ModSimLoader.constructorPreviousPage = this.previousPage;
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }
                        //需求
                        if (guibutton.id == 969) {
                            this.previousPage = this.currentPage;
                            this.currentPage = 12;
                            this.showPage();
                        }
                    } else if (this.currentPage == 12) {
                        //返回
                        if (guibutton.id == 1000) {
                            this.currentPage = this.previousPage;
                            this.showPage();
                        }
                        //建造它
                        if (guibutton.id == 970) {
                            //发送蓝图
                            NetWorkLoader.net.sendToServer(new PacketSendBlueprint(this.selectedBlueprint, this.employee.id, V3.fromBlockPos(this.pos).toString(), this.buildDirection));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }
                    } else if (this.currentPage == 13) {
                        //返回
                        if (guibutton.id == 1000) {
                            this.currentPage = this.previousPage;
                            this.showPage();
                        }
                        TerrainType terrainType = null;
                        if (guibutton.id == 21) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra1"),"1");
                            //填海
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 22) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra2"),"2");
                            //绿化
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 23) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra3"),"3");
                            //除草
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 24) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra4"),"4");
                            //平整化
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 25) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra5"),"5");
                            //超值套装
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 26) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra6"),"6");
                            //冰川
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 27) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra7"),"7");
                            //湿润化
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 28) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra8"),"8");
                            //炎热化
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        } else if (guibutton.id == 29) {
                            terrainType=new TerrainType(I18n.format("container.sim.packetTerra9"),"9");
                            //除雪
                            NetWorkLoader.net.sendToServer(new PacketSendTerrainType(terrainType, this.employee.id, V3.fromBlockPos(this.pos).toString()));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            return;
                        }
                    }

                    if (this.currentPage < 4 || this.currentPage > 12) {
                        //雇佣建筑工
                        if (guibutton.id == 2) {
                            this.hiringTerraformer = false;
                            this.previousPage = this.currentPage;
                            ModSimLoader.log.info(this.previousPage);
                            this.currentPage = 2;
                            this.showPage();
                            //雇佣规划师
                        } else if (guibutton.id == 7) {
                            this.hiringTerraformer = true;
                            this.previousPage = this.currentPage;
                            ModSimLoader.log.info(this.previousPage);
                            this.currentPage = 2;
                            this.showPage();
                        } else if (guibutton.id == 3) {
                            //解雇
                            NetWorkLoader.net.sendToServer(new PacketFireFolk(this.employee.id));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                        }
                    }
                    //住宅
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Residential"))) {
                        this.previousPage = this.currentPage;
                        this.currentPage = 4;
                        this.showPage();
                    } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Commercial"))) {
                        //商业
                        this.previousPage = this.currentPage;
                        this.currentPage = 5;
                        this.showPage();
                    } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Industrial"))) {
                        //工业
                        this.previousPage = this.currentPage;
                        this.currentPage = 6;
                        this.showPage();
                    } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Other"))) {
                        //其他
                        this.previousPage = this.currentPage;
                        this.currentPage = 7;
                        this.showPage();
                    } else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_special"))) {
                        //特除
                        this.previousPage = this.currentPage;
                        this.currentPage = 8;
                        this.showPage();
                    } /*else if (guibutton.displayString.contentEquals("Administrative")) {
                        this.previousPage = this.currentPage;
                        this.currentPage = 9;
                        this.showPage();
                    } */ else if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_BC_Decorative"))) {
                        //装饰
                        this.previousPage = this.currentPage;
                        this.currentPage = 10;
                        this.showPage();
                    }

                    if (this.currentPage > 3 && this.currentPage < 12) {
                        //返回
                        if (guibutton.id == 505) {
                            this.previousPage = this.currentPage;
                            this.currentPage = 1;
                            this.showPage();
                        }

                        if (guibutton.id > 0 && guibutton.id < 500) {
                            //建筑
                            for (BuildingBlueprint bb : this.potentialBlueprints) {
                                if (bb.name.contentEquals(guibutton.displayString)) {
                                    this.selectedBlueprint = bb;
                                    break;
                                }
                            }

                            if (this.selectedBlueprint != null) {
                                this.previousPage = this.currentPage;
                                this.currentPage = 11;
                                this.showPage();
                            }
                        }

                        int offsetChange;
                        //上一页
                        if (guibutton.id == 500) {
                            offsetChange = this.buildingsOnPage > 1 ? this.buildingsOnPage : 6;
                            this.buildingOffset += offsetChange;
                            this.showPage();
                            //下一页
                        } else if (guibutton.id == 501) {
                            offsetChange = this.buildingsOnPage > 1 ? this.buildingsOnPage : 6;
                            this.buildingOffset -= offsetChange;
                            this.showPage();
                        }
                    }

                }
            }
        }
    }

    public void drawScreen(int i, int j, float f) {
        try {
            this.drawDefaultBackground();
            //建筑施工人员
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_Constructor"), this.width / 2, 17, 16777215);
            if (this.currentPage == 0) {
                //请为此建筑构建器选择一项任务
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_building_constructor"), this.width / 2, 100, 16777130);
            } else if (this.currentPage == 1) {
                //请选择一种建筑类型
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_building"), this.width / 2, 100, 16777130);
            } else if (this.currentPage == 2) {
                //选择一个你想雇佣的NPC
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_unemployed"), this.width / 2, 50, 16777130);
            } else if (this.currentPage == 3) {
                //这是你的所有员工
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_employees"), this.width / 2, 50, 16777130);
            } else if (this.currentPage == 4) {
                //选择要键的住宅
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_residential1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } else if (this.currentPage == 5) {
                //选择要建的商业
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_commercial1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } else if (this.currentPage == 6) {
                //工业
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_industrial1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } else if (this.currentPage == 7) {
                //其他
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_Now_choose1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } else if (this.currentPage == 8) {
                //特殊
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_Now_special1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } /*else if (this.currentPage == 9) {
                this.drawCenteredString(this.fontRendererObj, "Now choose the administrative type of building to build", this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            }*/ else if (this.currentPage == 10) {
                //饰品
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_Now_decorative1"), this.width / 2, 50, 16777130);
                this.tfSearch.drawTextBox();
            } else if (this.currentPage == 11) {
                String realCost = " (" + ModSimLoader.displayMoney((float) this.selectedBlueprint.blockCount * 0.02F) + ")";
                //建筑详情
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC_Building_details") + this.selectedBlueprint.name, this.width / 2, 50, 16777130);
                //建筑名
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC3") + ": " + this.selectedBlueprint.name, this.width / 2, 80, 16777130);
                //描述
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC4") + ": " + this.selectedBlueprint.desc, this.width / 2, 110, 16777130);
                //作者
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC5") + ": " + this.selectedBlueprint.author, this.width / 2, 140, 16777130);
                //费用
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC6") + ": " + ModSimLoader.displayMoney((float) this.selectedBlueprint.blockCount * 0.02F) + realCost, this.width / 2, 170, 16777130);
                //规格尺寸
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_BC7") + ": " + this.selectedBlueprint.getDimensions(), this.width / 2, 200, 16777130);
            } else if (this.currentPage == 12) {
                //建筑需求
                int y = 70;
                String[] reqs = this.selectedBlueprint.getBuildingRequirementsString().split(";");
                if (reqs.length > 0) {
                    this.drawCenteredString(this.fontRendererObj, reqs[0], this.width / 2, 50, 16777130);

                    for (int req = 1; req < reqs.length; ++req) {
                        this.drawString(this.fontRendererObj, reqs[req], this.width / 3, y, 16777215);
                        y += 15;
                    }
                }
            } else if (this.currentPage == 13) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Terraform11"), this.width / 2, 25, 16777215);
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Terraform12"), this.width / 2, 30, 16777215);
//                this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 80, 16744576);
            }

            super.drawScreen(i, j, f);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 获取可雇佣的人名
     * @Date 16:20 2022/11/1
     * @Param []
     **/
    public void getHireableFolkNames() {
        NetWorkLoader.net.sendToServer(new PacketGetHireableFolks(true));
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 搜索框
     * @Date 16:36 2022/11/1
     * @Param [c, i]
     **/
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
}