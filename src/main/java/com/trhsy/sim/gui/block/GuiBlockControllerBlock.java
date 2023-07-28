package com.trhsy.sim.gui.block;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.gui.npc.GuiEmployees;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.*;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName GuiBlockControllerBlock
 * @Description todo 控制箱
 * @Author TRHSY
 * @Date 2022/11/711:49
 **/
public class GuiBlockControllerBlock extends GuiScreen {
    /**
     * 是否雇佣
     **/
    public boolean hasEmployee = false;
    /**
     * npc信息
     **/
    public NpcIdentity employee;
    List<NpcData> occupants;
    /**
     * 可雇佣的人名
     **/
    public NpcIdentity[] hireableFolkNames = new NpcIdentity[1000];
    /**
     * 工作名称
     **/
    List<String> jobNames = new CopyOnWriteArrayList<>();
    /**
     * 建筑id
     **/
    public String buildingId;
    /**
     * 建筑名
     **/
    public String buildingName = "buildingName";
    /**
     * 工作
     **/
    public String jobName = "null";
    /**
     * 建筑类型
     **/
    private String buildingType;
    /**
     * 作者
     **/
    private String author;
    /**
     * 选择的NPC
     **/
    GuiButton selectedEmployee;
    /**
     * 当前页
     **/
    public int currentPage = 0;
    /**
     * 上一页
     **/
    public int previousPage = 1;
    /**
     * 位置
     **/
    public V3 pos;
    /**
     * 是否是住宅
     **/
    public boolean isResidential;
    /****/
    long fingBodge = 0L;

    /**
     * @return
     * @Author fan
     * @Description //TODO 初始化
     * @Date 14:40 2022/11/7
     * @Param [p, bId, bName, jName]
     **/
    public GuiBlockControllerBlock(V3 p, String bId, String bName, String jName, String bType, String author) {
        try {
        this.pos = p;
        this.hasEmployee = false;
        this.occupants = null;
        this.buildingId = bId;
        ModSimLoader.log.info("控制箱ID:" + bId);
        this.buildingName = bName;
        this.jobName = jName;
        this.buildingType = bType;
        this.author = author;
        this.getHireableFolkNames();
        this.populateJobList();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiBlockControllerBlock出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public GuiBlockControllerBlock(V3 p, List<NpcData> occupant, String bId, String bName, String jName, String bType, String author) {
        try{
        this.pos = p;
        this.occupants = occupant;
        this.hasEmployee = true;
        ModSimLoader.log.info("控制箱ID:" + bId);
        this.buildingId = bId;
        this.buildingName = bName;
        this.jobName = jName;
        this.buildingType = bType;
        this.author = author;
        this.getHireableFolkNames();
        this.populateJobList();
    }catch (Exception e){
        StackTraceElement element = e.getStackTrace()[0];
        ModSimLoader.log.error("GuiBlockControllerBlock1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
    }
    }

    public GuiBlockControllerBlock(V3 p, List<NpcData> occupants, String bId, Boolean isResidential, String bName, String jName, String bType, String author) {
        try{
        this.pos = p;
        this.occupants = occupants;
        ModSimLoader.log.info("控制箱ID:" + bId);
        this.buildingId = bId;
        this.buildingName = bName;
        this.jobName = jName;
        this.buildingType = bType;
        this.author = author;
        this.isResidential = isResidential;
    }catch (Exception e){
        StackTraceElement element = e.getStackTrace()[0];
        ModSimLoader.log.error("GuiBlockControllerBlock2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
    }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 填充工作列表
     * @Date 14:41 2022/11/7
     * @Param []
     **/
    public void populateJobList() {
        //屠夫
        this.jobNames.add(I18n.format("container.sim.Vocation15"));
        //面包师
        this.jobNames.add(I18n.format("container.sim.Vocation6"));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 初始化GUI
     * @Date 15:18 2022/11/7
     * @Param []
     **/
    @Override
    public void initGui() {
        super.initGui();
        this.showPage();
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 显示分页
     * @Date 15:26 2022/11/7
     * @Param []
     **/
    public void showPage() {
        try{
        this.mc.setIngameNotInFocus();
        //清楚所有按钮
        this.buttonList.clear();
        //完成
        this.buttonList.add(new GuiButton(0, 2, 12, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
        if (this.currentPage == 0) {
            //如果建筑id不存在则修复
            if (this.buildingId == null || this.buildingId == "") {
                //拆除
//                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, I18n.format("container.sim.Demolish")));
                //添加 修理房子 按钮
//                this.buttonList.add(new GuiButton(3, 10, this.height - 30, 100, 20, I18n.format("container.sim.Fix_House")));
            } else {
                //拆除
                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, I18n.format("container.sim.Demolish")));
                //旋转楼梯
                this.buttonList.add(new GuiButton(1001, this.width - 110, 25, 100, 20, I18n.format("container.sim.Rotate_Stairs")));
                //显示员工
                this.buttonList.add(new GuiButton(21, this.width - 110, this.height - 30, 100, 20, I18n.format("container.sim.sim_gui_BC_Show_Employees")));
                //将我传送到
//                this.buttonList.add(new GuiButton(30, this.width - 110, this.height - 50, 100, 20, I18n.format("container.sim.Beam_me_to")));
                //显示是否有工作
                if (this.jobName != null && !"null".equals(this.jobName)) {

                    //是否已雇佣
                    if (!this.hasEmployee) {
                        //雇佣
                        this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire0") + WordUtils.capitalize(this.jobName)));
                        //((GuiButton)this.buttonList.get(2)).enabled = false;
                    } else {
                        //士兵
                        if (this.jobName.equals(I18n.format("container.sim.Vocation7"))&&this.occupants!=null&&this.occupants.size()<5) {
                            this.hasEmployee = false;
                            //雇佣
                            this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire0") + WordUtils.capitalize(this.jobName)));
                        }
                        if(this.occupants!=null){
                            int down=67;
                            for (int k = 0; k < this.occupants.size(); k++) {
                                NpcData npcData = this.occupants.get(k);
                                String employeeName= npcData.getName();
                                //解雇
                                this.buttonList.add(new GuiButton(2, this.width - 140, down, 100, 20, I18n.format("container.sim.Fire") +" "+ WordUtils.capitalize(employeeName)));
                                down +=20;
                            }
                        }
                        /*else{
                            //解雇
                            this.buttonList.add(new GuiButton(2, 10, this.height - 30, 100, 20, I18n.format("container.sim.Fire") + WordUtils.capitalize(this.employee.name)));
                        }*/
                        //((GuiButton)this.buttonList.get(2)).enabled = true;
                    }
                }
            }

        } else if (this.currentPage == 1) {
            //取消
            this.buttonList.add(new GuiButton(1010, this.width / 2 - 200, this.height - 30, I18n.format("container.sim.sim_gui_player_to_Cancel")));
            //好的
            this.buttonList.add(new GuiButton(1002, this.width / 2, this.height - 30, I18n.format("container.sim.gui_btn_name_OK")));

            try {
                int x = 10;
                int y = 80;
                int idx = 100;

                for (int f = 0; f < ModSimClientLoader.getUnemployedFolks().size(); ++f) {
                    NpcIdentity folk = ModSimClientLoader.getUnemployedFolks().get(f);
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
            } catch (Exception var6) {
            }
        }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("showPage出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try{
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int posX = (this.width - 256) / 2;
        this.drawDefaultBackground();
        if (this.buildingId == null || this.buildingId == "") {
            //错误：此建筑的信息丢失
            this.fontRendererObj.drawString(I18n.format("container.sim.on_this_building") + "(" + this.pos.toString() + ")", 5, 77, 16711680);
            //请雇佣建筑工重建
            this.fontRendererObj.drawString(I18n.format("container.sim.this_Building"), 5, 97, 16711680);
        } else {
            this.fontRendererObj.drawString(this.buildingName, this.width / 2, 17, 16777215);
            String author = "";
            if (this.author != null && !this.author.contentEquals("")) {
                author = this.author;
            }
            //正在建设/修复中
            String isComplete = I18n.format("container.sim.Under_construction");
            if (this.buildingId != null) {
                //建筑可用
                isComplete = I18n.format("container.sim.Active_Building");
            }
            //建筑名称
            this.fontRendererObj.drawString(I18n.format("container.sim.sim_Building") + " : " + this.buildingName, 5, 37, 16777088);
            //作者
            this.fontRendererObj.drawString(I18n.format("container.sim.sim_gui_BC5") + " : " + author, 5, 47, 16777088);
            //类型
            this.fontRendererObj.drawString(I18n.format("container.sim.sim_Type") + " : " + this.buildingType + " (" + isComplete + ")", 5, 57, 16777088);
            if (this.isResidential) {
                if (this.occupants != null) {
                    String employeeName="";
                    for (int k = 0; k < this.occupants.size(); k++) {
                        NpcData npcData = this.occupants.get(k);
                        employeeName+= npcData.getName()+";";
                    }
                    employeeName=employeeName.substring(0,employeeName.length()-1);
                    //拥有者
                    this.fontRendererObj.drawString(I18n.format("container.sim.gui.block_controller_Occupants") + ":" + employeeName, 5, 67, 16777088);
                }
            } else {
                if (this.occupants != null) {
                    int down=67;
                    for (int k = 0; k < this.occupants.size(); k++) {
                        NpcData npcData = this.occupants.get(k);
                        if(npcData!=null){
                            String employeeName= npcData.getName();
                            //员工
                            this.fontRendererObj.drawString(I18n.format("container.sim.Employees") + ":" + employeeName, 5, down, 16777088);
                            down +=20;
                        }
                    }
                }
            }

        }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("drawScreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

        super.drawScreen(i, j, f);
    }

    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    @Override
    public void actionPerformed(GuiButton guibutton) {
        try{
        ModSimLoader.log.info("点击按钮：" + guibutton.id);
//        ModSimLoader.log.info("建筑工作类型：" + this.buildings.jobType);
        if (System.currentTimeMillis() - this.fingBodge >= 100L) {
            this.fingBodge = System.currentTimeMillis();
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    if (this.currentPage == 0) {
                        //拆除
                        if (guibutton.id == 1000) {
                            //拆除
                            NetWorkLoader.net.sendToServer(new PacketDemolishBuilding(this.buildingId));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                        } else if (guibutton.id == 1001) {
                            //旋转楼梯
//                            this.rotateStairs();
                            NetWorkLoader.net.sendToServer(new PacketrotateStairs(this.buildingId));
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                        } else if (guibutton.id == 21) {
                            //显示员工
                            Minecraft.getMinecraft().displayGuiScreen(new GuiEmployees());
                            return;
                        }/*else if (guibutton.id == 30) {
                            //显将我传送到
//                            GuiScreen guiScreen = new GuiBeamPlayerTo(this.playerWhoClickedIt);
//                            this.mc.displayGuiScreen(guiScreen);
                            return;
                        }*/
                    }

                    if (this.currentPage == 1) {
                        //取消
                        if (guibutton.id == 1010) {
                            this.previousPage = this.currentPage;
                            this.currentPage = 0;
                            this.showPage();
                        }
                        //好的
                        if (guibutton.id == 1002 && this.selectedEmployee != null) {
                            this.hasEmployee = true;
                            this.employee = this.hireableFolkNames[this.selectedEmployee.id];
                            this.previousPage = this.currentPage;
                            this.currentPage = 0;
                            this.showPage();
                            if(this.occupants==null){
                                this.occupants=new CopyOnWriteArrayList<>();
                            }
                            this.occupants.add(ModSimLoader.getFolkDataByUID(this.employee.id));
                            //雇佣npc
                            NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, this.jobName, this.pos));
                        }

                        if (guibutton.id > 99 && guibutton.id < 1000) {
                            if (this.selectedEmployee != null) {
                                this.selectedEmployee.enabled = true;
                            }

                            this.selectedEmployee = guibutton;
                            this.selectedEmployee.enabled = false;
                        }
                    }
                    ////雇佣
                    if (guibutton.id == 1) {
                        this.previousPage = this.currentPage;
//                        ModSimLoader.log.info(this.previousPage);
                        this.currentPage = 1;
                        this.showPage();
                    } else if (guibutton.id == 2) {
                        //解雇
                        String text=guibutton.displayString;
                        String uuId="";
                        for (int i = 0; i <this.occupants.size() ; i++) {
                            NpcData npcData=this.occupants.get(i);
                            String s=npcData.getName();
                            if(text.contains(s)){
                                uuId=npcData.ID;
                                this.occupants.remove(i);
                            }
                        }
                        NetWorkLoader.net.sendToServer(new PacketFireFolk(uuId));
                        this.hasEmployee=false;
                        this.currentPage = 0;
                        this.showPage();
                    } else if (guibutton.id == 3) {
                        //修理房子
                        Building b = new Building(I18n.format("container.sim.Repaired_House"), (float) 10 * 0.01F, this.pos, this.pos);
                        //建筑物类型
                        b.buildingType = I18n.format("container.sim.sim_gui_BC_Residential");
                        //建筑物的结构
                        b.structure.add(this.pos);
                        //工作类型
                        b.jobType = null;
                        ModSimLoader.buildings.add(b);
                        //保存建筑
                        b.saveBuilding();
                        //作者
                        b.author = "Trhsy";
                        b.saveBuilding();
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    }

                }
            }
        }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("actionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }


    /**
     * @return void
     * @Author fan
     * @Description //TODO 获取可以受雇佣的人
     * @Date 11:54 2022/11/7
     * @Param []
     **/
    public void getHireableFolkNames() {
        NetWorkLoader.net.sendToServer(new PacketGetHireableFolks(true));
    }
}