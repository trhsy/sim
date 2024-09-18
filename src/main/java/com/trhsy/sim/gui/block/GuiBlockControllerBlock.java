package com.trhsy.sim.gui.block;

import com.trhsy.sim.gui.npc.GuiEmployees;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.*;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.NpcIdentity;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.Building;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;
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
    List<String> jobNames = new CopyOnWriteArrayList<String>();
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
    /**建筑描述**/
    public String desc;
    public int fs_y;
    /**
     * @return
     * @Author fan
     * @Description //TODO 初始化
     * @Date 14:40 2022/11/7
     * @Param [p, bId, bName, jName]
     **/
    public GuiBlockControllerBlock(V3 p, String bId, String bName, String jName, String bType, String author,String desc) {
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
        this.desc=desc;
        this.getHireableFolkNames();
        this.populateJobList();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiBlockControllerBlock出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public GuiBlockControllerBlock(V3 p, List<NpcData> occupant, String bId, String bName, String jName, String bType, String author,String desc) {
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
            this.desc=desc;
        this.getHireableFolkNames();
        this.populateJobList();
    }catch (Exception e){
        StackTraceElement element = e.getStackTrace()[0];
        ModSimLoader.log.error("GuiBlockControllerBlock1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
    }
    }

    public GuiBlockControllerBlock(V3 p, List<NpcData> occupants, String bId, Boolean isResidential, String bName, String jName, String bType, String author,String desc) {
        try{
        this.pos = p;
        this.occupants = occupants;
        ModSimLoader.log.info("控制箱ID:" + bId);
        this.buildingId = bId;
        this.buildingName = bName;
        this.jobName = jName;
        this.buildingType = bType;
        this.author = author;
            this.desc=desc;
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
        this.jobNames.add(new TextComponentTranslation("container.sim.Vocation15",new Object[0]).getUnformattedText());
        //面包师
        this.jobNames.add(new TextComponentTranslation("container.sim.Vocation6",new Object[0]).getUnformattedText());
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
        this.buttonList.add(new GuiButton(0, 2, 12, 50, 20, new TextComponentTranslation("container.sim.sim_gui_BC_Done",new Object[0]).getUnformattedText()));
        if (this.currentPage == 0) {
            //如果建筑id不存在则修复
            if (this.buildingId == null || this.buildingId == "") {
                //拆除
//                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, new TextComponentTranslation("container.sim.Demolish",new Object[0]).getUnformattedText()));
                //添加 修理房子 按钮
//                this.buttonList.add(new GuiButton(3, 10, this.height - 30, 100, 20, new TextComponentTranslation("container.sim.Fix_House",new Object[0]).getUnformattedText()));
            } else {
                //拆除
                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, new TextComponentTranslation("container.sim.Demolish",new Object[0]).getUnformattedText()));
                //旋转楼梯
                this.buttonList.add(new GuiButton(1001, this.width - 110, 25, 100, 20, new TextComponentTranslation("container.sim.Rotate_Stairs",new Object[0]).getUnformattedText()));
                //显示员工
                this.buttonList.add(new GuiButton(21, this.width - 110, this.height - 30, 100, 20, new TextComponentTranslation("container.sim.sim_gui_BC_Show_Employees",new Object[0]).getUnformattedText()));
                //显示是否有工作
                if (this.jobName != null && !"null".equals(this.jobName)) {

                    //是否已雇佣
                    if (!this.hasEmployee) {
                        //雇佣
                        this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, new TextComponentTranslation("container.sim.Hire0",new Object[0]).getUnformattedText() + WordUtils.capitalize(this.jobName)));
                        //((GuiButton)this.buttonList.get(2)).enabled = false;
                    } else {
                        //士兵
                        if (this.jobName.equals(new TextComponentTranslation("container.sim.Vocation7",new Object[0]).getUnformattedText())&&this.occupants!=null&&this.occupants.size()<5) {
                            this.hasEmployee = false;
                            //雇佣
                            this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, new TextComponentTranslation("container.sim.Hire0",new Object[0]).getUnformattedText() + WordUtils.capitalize(this.jobName)));
                        }
                        int fs_y1=67;
                        if(this.occupants!=null){
                            for (int k = 0; k < this.occupants.size(); k++) {
                                NpcData npcData = this.occupants.get(k);
                                if(npcData!=null){
                                    String employeeName= npcData.getName();
                                    //解雇
                                    this.buttonList.add(new GuiButton(2, this.width - 140, fs_y1, 100, 20, new TextComponentTranslation("container.sim.Fire",new Object[0]).getUnformattedText() +" "+ WordUtils.capitalize(employeeName)));
                                    fs_y1 +=20;
                                }
                            }
                        }
                    }
                }
            }

        } else if (this.currentPage == 1) {
            //取消
            this.buttonList.add(new GuiButton(1010, this.width / 2 - 200, this.height - 30, new TextComponentTranslation("container.sim.sim_gui_player_to_Cancel",new Object[0]).getUnformattedText()));
            //好的
            this.buttonList.add(new GuiButton(1002, this.width / 2, this.height - 30, new TextComponentTranslation("container.sim.gui_btn_name_OK",new Object[0]).getUnformattedText()));

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
            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.on_this_building",new Object[0]).getUnformattedText() + "(" + this.pos.toString() + ")", 5, 77, 16711680);
            //请雇佣建筑工重建
            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.this_Building",new Object[0]).getUnformattedText(), 5, 97, 16711680);
        } else {
            this.fontRenderer.drawString(this.buildingName, this.width / 2, 17, 16777215);
            String author = "";
            if (this.author != null && !this.author.contentEquals("")) {
                author = this.author;
            }
            //正在建设/修复中
            String isComplete = new TextComponentTranslation("container.sim.Under_construction",new Object[0]).getUnformattedText();
            if (this.buildingId != null) {
                //建筑可用
                isComplete = new TextComponentTranslation("container.sim.Active_Building",new Object[0]).getUnformattedText();
            }
            //建筑名称
            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.sim_Building",new Object[0]).getUnformattedText() + " : " + this.buildingName, 5, 37, 16777088);
            //作者
            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.sim_gui_BC5",new Object[0]).getUnformattedText() + " : " + author, 5, 47, 16777088);
            //类型
            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.sim_Type",new Object[0]).getUnformattedText() + " : " + this.buildingType + " (" + isComplete + ")", 5, 57, 16777088);
            //描述
            int trd1 = this.desc.length();

            //设置Y轴的起始位置
            this.fs_y=67;
           if (trd1 > 40) {
                int z=0;
                for (int k = 0; k < trd1; k++) {
                    String trds1;
                    if(k==0){
                        //获取desc字符串中从z位置开始，长度为40的字符串
                        trds1=this.desc.substring(z,z+40);
                        //在坐标(5,fs_y)处绘制文本，文本内容为container.sim.sim_desc，字体为16777088
                        this.fontRenderer.drawString(new TextComponentTranslation("container.sim.sim_desc",new Object[0]).getUnformattedText() + " : " + trds1, 5, fs_y, 16777088);
                    }else if (z+30 >= trd1) {
                        //获取desc字符串中从z位置开始，长度为trd1-1的字符串
                        trds1=this.desc.substring(z,trd1);
                        //在坐标(5,this.fs_y)处绘制文本，文本内容为trds1，字体为16777088，不换行
                        this.fontRenderer.drawString(trds1, 5, this.fs_y, 16777088,false);
                        //结束循环
                        break;
                    }else{
                        //获取desc字符串中从z位置开始，长度为40的字符串
                        trds1=this.desc.substring(z,z+40);
                        //在坐标(5,this.fs_y)处绘制文本，文本内容为trds1，字体为16777088，不换行
                        this.fontRenderer.drawString(trds1, 5, this.fs_y, 16777088,false);
                    }
                    //每次循环fs_y增加8
                    this.fs_y=this.fs_y+8;
                    //每次循环z增加40
                    z+=40;
                }
            }else{
                String trds1=this.desc;
                this.fontRenderer.drawString(new TextComponentTranslation("container.sim.sim_desc",new Object[0]).getUnformattedText() + " : " + trds1, 5, fs_y, 16777088);
            }
            this.fs_y=this.fs_y+8;
            if (this.isResidential) {
                if (this.occupants != null) {
                    String employeeName="";
                    for (int k = 0; k < this.occupants.size(); k++) {
                        NpcData npcData = this.occupants.get(k);
                        if(npcData==null){
                            this.occupants.remove(k);
                            break;
                        }
                        employeeName+= npcData.getName()+";";
                    }
                    if(!"".equals(employeeName)){
                        employeeName=employeeName.substring(0,employeeName.length()-1);
                    }
                    //拥有者
                    this.fontRenderer.drawString(new TextComponentTranslation("container.sim.gui.block_controller_Occupants",new Object[0]).getUnformattedText() + ":" + employeeName, 5, 77, 16777088);
                }
            } else {
                if (this.occupants != null) {
                    int fs_y1=this.fs_y;
                    for (int k = 0; k < this.occupants.size(); k++) {
                        NpcData npcData = this.occupants.get(k);
                        if(npcData!=null){
                            String employeeName= npcData.getName();
                            //员工
                            this.fontRenderer.drawString(new TextComponentTranslation("container.sim.employees",new Object[0]).getUnformattedText() + ":" + employeeName, 5, fs_y1, 16777088);
                            fs_y1 +=20;
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

    /**
     * 点击按钮
     * @param guibutton
     */
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
                                this.occupants=new CopyOnWriteArrayList<NpcData>();
                            }
                            this.occupants.add(ModSimLoader.getFolkDataByUID(this.employee.id));
                            //雇佣npc
                            NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, this.jobName, new V3(this.pos.x,this.pos.y+1,this.pos.z)));
                            this.showPage();
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
                        NpcData fs_npcData=null;
                        for (int i = 0; i <this.occupants.size() ; i++) {
                            fs_npcData=this.occupants.get(i);
                            String s=fs_npcData.getName();
                            if(text.contains(s)){
                                this.occupants.remove(i);
                            }
                        }
                        NetWorkLoader.net.sendToServer(new PacketFireFolk(fs_npcData.ID));
                        Building building = ModSimLoader.getBuildingByV3(this.pos);
                        if (building != null) {
                            building.occupants.remove(fs_npcData);
                            building.saveBuilding();
                        }

                        this.hasEmployee=false;
                        this.currentPage = 0;
                        this.showPage();
                    } else if (guibutton.id == 3) {
                        //修理房子
                        Building b = new Building(new TextComponentTranslation("container.sim.Repaired_House",new Object[0]).getUnformattedText(), (float) 10 * 0.01F, this.pos, this.pos);
                        //建筑物类型
                        b.buildingType = new TextComponentTranslation("container.sim.sim_gui_BC_Residential",new Object[0]).getUnformattedText();
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