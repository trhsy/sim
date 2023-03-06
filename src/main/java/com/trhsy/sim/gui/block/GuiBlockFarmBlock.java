package com.trhsy.sim.gui.block;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.PacketFireFolk;
import com.trhsy.sim.network.server.PacketGetHireableFolks;
import com.trhsy.sim.network.server.PacketHireFolk;
import com.trhsy.sim.network.server.PacketUpdateFarmBox;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.util.FarmType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Mouse;

import java.util.UUID;

/**
 * @ClassName GuiBlockFarmBlock
 * @Description todo 养殖箱的gui
 * @Author TRHSY
 * @Date 2022/12/1013:44
 **/
public class GuiBlockFarmBlock extends GuiScreen {
    //NPCid
    public UUID id;
    //方向
    public EnumFacing facing;
    /**农场类型**/
    public FarmType farmType;
    //长
    public int x;
    //宽
    public int z;
    //地点
    public V3 loc;
    //鼠标统计
    private int mouseCount;
    //是否以雇佣
    public boolean hasEmployee;
    //NPC
    public NpcIdentity employee;
    //可雇佣的人
    public NpcIdentity[] hireableFolkNames;
    //分页
    int currentPage;
    //选中的NPC
    GuiButton selectedEmployee;

    public GuiBlockFarmBlock(UUID id, V3 loc, EnumFacing facing, FarmType farmType, int x, int z) {
        this.mouseCount = 0;
        this.hasEmployee = false;
        this.hireableFolkNames = new NpcIdentity[1000];
        this.currentPage = 0;
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.farmType=farmType;
        this.x = x;
        this.z = z;
        this.getHireableFolkNames();
        this.setDimensions();
    }

    public GuiBlockFarmBlock(UUID id, V3 loc, EnumFacing facing, FarmType farmType, int x, int z, NpcIdentity folk) {
        this.mouseCount = 0;
        this.hasEmployee = false;
        this.hireableFolkNames = new NpcIdentity[1000];
        this.currentPage = 0;
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.farmType=farmType;
        this.x = x;
        this.z = z;
        this.employee = folk;
        this.getHireableFolkNames();
        this.setDimensions();
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
        //预览
        ModSimLoader.previewPos1 = null;
        ModSimLoader.previewPos2 = null;
    }
    @Override
    public void initGui() {
        //显示分页
        this.showPage();
        //初始化gui
        super.initGui();
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 显示分页
     * @Date 13:49 2022/12/10
     * @Param []
     **/
    public void showPage() {
        //第零页
        if (this.currentPage == 0) {
            //清除所有按钮
            this.buttonList.clear();
            //添加按钮 完成
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
            if (this.employee == null) {
                //雇佣农民
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Hire25")));
            } else {
                //解雇 员工
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, I18n.format("container.sim.Fire") + " " + this.employee.name));
            }

            try {
                String fs_facing = I18n.format("container.sim.gui_west");
                //西
                if (this.facing.toString().equals("west")) {
                    fs_facing=I18n.format("container.sim.gui_west");
                    //东
                } else if (this.facing.toString().equals("east")) {
                    fs_facing=I18n.format("container.sim.gui_east");
                    //北
                } else if (this.facing.toString().equals("north")) {
                    fs_facing=I18n.format("container.sim.gui_north");
                    //南
                } else if (this.facing.toString().equals("south")) {
                    fs_facing=I18n.format("container.sim.gui_south");
                }
                this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 100, fs_facing));
                //小麦
                String fs_type =I18n.format("container.sim.FarmType5");
                String fs_farmType=this.farmType.toString();
                //小麦
                if(fs_farmType.equals(I18n.format("container.sim.FarmType5"))){

                    fs_type =I18n.format("container.sim.FarmType5");
                    //西瓜
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType2"))){
                    fs_type =I18n.format("container.sim.FarmType2");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType4"))){
                    fs_type =I18n.format("container.sim.FarmType4");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType3"))){
                    fs_type =I18n.format("container.sim.FarmType3");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType1"))){
                    fs_type =I18n.format("container.sim.FarmType1");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType6"))){
                    fs_type =I18n.format("container.sim.FarmType6");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType7"))){
                    fs_type =I18n.format("container.sim.FarmType7");
                }else if(fs_farmType.equals(I18n.format("container.sim.FarmType8"))){
                    fs_type =I18n.format("container.sim.FarmType8");
                }

                this.buttonList.add(new GuiButton(4, this.width / 2 - 100, 80, fs_type));
            } catch (Exception var7) {
                StackTraceElement element = var7.getStackTrace()[0];
                ModSimLoader.log.error("农田箱GUI showPage出错了：" + var7.getMessage() + "行数：" + element.getLineNumber());
            }

            try {
                //长
                GuiButton lButton = new GuiButton(3, this.width / 2 - 50, 140, 100, 20, I18n.format("container.sim.job_farmer_Length") + ": " + this.x);
                //宽
                GuiButton wButton = new GuiButton(3, this.width / 2 - 50, 165, 100, 20, I18n.format("container.sim.job_farmer_Width") + ": " + this.z);
                //设置按钮不可用
                lButton.enabled = false;
                wButton.enabled = false;
                this.buttonList.add(lButton);
                this.buttonList.add(wButton);
                this.buttonList.add(new GuiButton(4, this.width / 2 - 70, 140, 20, 20, "-"));
                this.buttonList.add(new GuiButton(5, this.width / 2 + 50, 140, 20, 20, "+"));
                this.buttonList.add(new GuiButton(6, this.width / 2 - 70, 165, 20, 20, "-"));
                this.buttonList.add(new GuiButton(7, this.width / 2 + 50, 165, 20, 20, "+"));
            } catch (Exception var6) {
                StackTraceElement element = var6.getStackTrace()[0];
                ModSimLoader.log.error("农田箱GUI showPage 按钮  出错了：" + var6.getMessage() + "行数：" + element.getLineNumber());
            }
        } else if (this.currentPage == 1) {
            this.buttonList.clear();
            //取消
            this.buttonList.add(new GuiButton(1000, this.width / 2 - 200, this.height - 30, I18n.format("container.sim.sim_gui_player_to_Cancel")));
            //好的
            this.buttonList.add(new GuiButton(1001, this.width / 2, this.height - 30, I18n.format("container.sim.gui_btn_name_OK")));

            try {
                int x = 10;
                int y = 80;
                int idx = 100;
                //获取失业人员
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
            } catch (Exception var8) {
                ModSimLoader.log.error("获取失业人员发生了错误");
            }
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 屏幕绘制
     * @Date 14:25 2022/12/10
     * @Param [i, j, f]
     **/
    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        try {
            this.drawDefaultBackground();
            if (this.currentPage == 0) {
                //农场
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.gui_Farm"), this.width / 2, 17, 8454016);
            }

            super.drawScreen(i, j, f);
        } catch (Exception var5) {
            ModSimLoader.log.error("养殖箱GUI屏幕绘制");
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 按钮被触发
     * @Date 14:50 2022/12/10
     * @Param [guibutton]
     **/
    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                this.onGuiClosed();
            } else {
                //雇佣农民
                if (guibutton.displayString.contentEquals(I18n.format("container.sim.Hire25"))) {
                    this.currentPage = 1;
                    //显示
                    this.showPage();
                    //解雇
                } else if (guibutton.displayString.startsWith(I18n.format("container.sim.Fire"))) {
                    NetWorkLoader.net.sendToServer(new PacketFireFolk(this.employee.id));
                    guibutton.enabled = false;
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                    this.onGuiClosed();
                } else if (guibutton.id == 2) {
                    this.facing = this.facing.rotateY();
                    String fs_facing = I18n.format("container.sim.gui_west");
                    //西
                    if (this.facing.toString().equals("west")) {
                        fs_facing=I18n.format("container.sim.gui_west");
                        //东
                    } else if (this.facing.toString().equals("east")) {
                        fs_facing=I18n.format("container.sim.gui_east");
                        //北
                    } else if (this.facing.toString().equals("north")) {
                        fs_facing=I18n.format("container.sim.gui_north");
                        //南
                    } else if (this.facing.toString().equals("south")) {
                        fs_facing=I18n.format("container.sim.gui_south");
                    }
                    guibutton.displayString = fs_facing;
                    this.updateFarm();
                } else if (guibutton.id == 3) {
                    float cash = 5.0F;
                    if (this.getUpgradeCost() > cash) {
                        //资金不足
                        guibutton.displayString = I18n.format("container.sim.gui_Farming_text_NOT_ENOUGH");
                        guibutton.enabled = false;
                    } else {
                        if (this.x >= 4 && this.z >= 4) {
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            this.onGuiClosed();
                            return;
                        }
                        //农场太小了
                        guibutton.displayString = I18n.format("container.sim.gui_Farming_text_TOO_SMALL");
                        guibutton.enabled = false;
                    }
                }else if(guibutton.id==4){
                    //农场类型
                    this.farmType = this.farmType.rotateY();
                    //小麦
                    String fs_type =I18n.format("container.sim.FarmType5");
                    String fs_farmType=this.farmType.toString();
                    if(fs_farmType.equals(I18n.format("container.sim.FarmType1"))){
                        fs_type =I18n.format("container.sim.FarmType1");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType2"))){
                        fs_type =I18n.format("container.sim.FarmType2");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType3"))){
                        fs_type =I18n.format("container.sim.FarmType3");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType4"))){
                        fs_type =I18n.format("container.sim.FarmType4");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType5"))){
                        fs_type =I18n.format("container.sim.FarmType5");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType6"))){
                        fs_type =I18n.format("container.sim.FarmType6");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType7"))){
                        fs_type =I18n.format("container.sim.FarmType7");
                    }else if(fs_farmType.equals(I18n.format("container.sim.FarmType8"))){
                        fs_type =I18n.format("container.sim.FarmType8");
                    }
                    guibutton.displayString = fs_type;
                    this.updateFarm();
                }

                if (this.currentPage == 0) {
                    if (guibutton.id == 4 && this.x > 6) {
                        --this.x;
                        this.showPage();
                        this.updateFarm();
                    } else if (guibutton.id == 5 && this.x < 32) {
                        ++this.x;
                        this.showPage();
                        this.updateFarm();
                    } else if (guibutton.id == 6 && this.z > 6) {
                        --this.z;
                        this.showPage();
                        this.updateFarm();
                    } else if (guibutton.id == 7 && this.z < 32) {
                        ++this.z;
                        this.showPage();
                        this.updateFarm();
                    }
                }

                if (this.currentPage == 1) {
                    if (guibutton.id == 1000) {
                        this.currentPage = 0;
                        this.showPage();
                    }
                    //雇佣农民
                    if (guibutton.id == 1001 && this.selectedEmployee != null) {
                        this.hasEmployee = true;
                        this.employee = this.hireableFolkNames[this.selectedEmployee.id];
                        this.currentPage = 0;
                        this.showPage();
                        NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, I18n.format("container.sim.Hire_farmer"), this.loc));
                    }

                    if (guibutton.id > 99 && guibutton.id < 1000) {
                        if (this.selectedEmployee != null) {
                            this.selectedEmployee.enabled = true;
                        }

                        this.selectedEmployee = guibutton;
                        this.selectedEmployee.enabled = false;
                    }
                }

            }
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更新农场
     * @Date 14:54 2022/12/10
     * @Param []
     **/
    private void updateFarm() {
        this.setDimensions();
        NetWorkLoader.net.sendToServer(new PacketUpdateFarmBox(this.id, this.x, this.z, this.facing,this.farmType));
    }

    private Float getUpgradeCost() {
        Float ret = (float) (this.x * this.z);
        ret = ret / 15.0F;
        return ret;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 预览
     * @Date 14:58 2022/12/10
     * @Param []
     **/
    public void setDimensions() {
        ModSimLoader.previewPos1 = new Vec3d(this.loc.toBlockPos().offset(this.facing));
        ModSimLoader.previewPos2 = (new Vec3d(this.loc.toBlockPos().offset(this.facing, this.x + 1).offset(this.facing.rotateY(), this.z))).add(new Vec3d(0.0D, 2.0D, 0.0D));
    }

    public void getHireableFolkNames() {
        //获取可以受雇佣的人
        NetWorkLoader.net.sendToServer(new PacketGetHireableFolks(true));
    }
}