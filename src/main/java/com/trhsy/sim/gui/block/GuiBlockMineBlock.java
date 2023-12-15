package com.trhsy.sim.gui.block;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.PacketFireFolk;
import com.trhsy.sim.network.server.PacketGetHireableFolks;
import com.trhsy.sim.network.server.PacketHireFolk;
import com.trhsy.sim.network.server.PacketUpdateMineBox;
import com.trhsy.sim.npcCode.NpcIdentity;
import com.trhsy.sim.npcCode.V3;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Mouse;

import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui.block
 * @ClassName: GuiBlockMineBlock
 * @Description:
 * @date 2023/5/16 18:07
 */
public class GuiBlockMineBlock extends GuiScreen {
    //id
    public UUID id;
    //方向
    public EnumFacing facing;
    public int x;
    public int z;
    //地点
    public V3 loc;
    private int mouseCount;
    public boolean hasEmployee;
    public NpcIdentity employee;
    public NpcIdentity[] hireableFolkNames;
    int currentPage;
    GuiButton selectedEmployee;

    public GuiBlockMineBlock(UUID id, V3 loc, EnumFacing facing, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.mouseCount = 0;
        this.hasEmployee = false;
        this.hireableFolkNames = new NpcIdentity[1000];
        this.currentPage = 0;
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.x = x;
        this.z = z;
        this.getHireableFolkNames();
        this.setDimensions();
    }

    public GuiBlockMineBlock(UUID id, V3 loc, EnumFacing facing, int x, int z, NpcIdentity folk) {
        this.facing = EnumFacing.EAST;
        this.mouseCount = 0;
        this.hasEmployee = false;
        this.hireableFolkNames = new NpcIdentity[1000];
        this.currentPage = 0;
        this.id = id;
        this.loc = loc;
        this.facing = facing;
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
        ModSimClientLoader.previewPos1 = null;
        ModSimClientLoader.previewPos2 = null;
    }
    @Override
    public void initGui() {
        this.showPage();
        super.initGui();
    }

    public void showPage() {
        if (this.currentPage == 0) {
            this.buttonList.clear();
            //完成
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, new TextComponentTranslation("container.sim.sim_gui_BC_Done",new Object[0]).getUnformattedText()));
            if (this.employee == null) {
                //雇佣矿工
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, new TextComponentTranslation("container.sim.Hire21",new Object[0]).getUnformattedText()));
            } else {
                //解雇
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 40, new TextComponentTranslation("container.sim.Fire",new Object[0]).getUnformattedText()+" " + this.employee.name));
            }

            try {
                String fs_facing = new TextComponentTranslation("container.sim.gui_west",new Object[0]).getUnformattedText();
                //西
                if (this.facing.toString().equals("west")) {
                    fs_facing=new TextComponentTranslation("container.sim.gui_west",new Object[0]).getUnformattedText();
                    //东
                } else if (this.facing.toString().equals("east")) {
                    fs_facing=new TextComponentTranslation("container.sim.gui_east",new Object[0]).getUnformattedText();
                    //北
                } else if (this.facing.toString().equals("north")) {
                    fs_facing=new TextComponentTranslation("container.sim.gui_north",new Object[0]).getUnformattedText();
                    //南
                } else if (this.facing.toString().equals("south")) {
                    fs_facing=new TextComponentTranslation("container.sim.gui_south",new Object[0]).getUnformattedText();
                }
                this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 100, fs_facing));
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            try {
                //长
                GuiButton lButton = new GuiButton(3, this.width / 2 - 50, 140, 100, 20, new TextComponentTranslation("container.sim.job_farmer_Length",new Object[0]).getUnformattedText()+": " + this.x);
                //宽
                GuiButton wButton = new GuiButton(3, this.width / 2 - 50, 165, 100, 20, new TextComponentTranslation("container.sim.job_farmer_Width",new Object[0]).getUnformattedText()+": " + this.z);
                lButton.enabled = false;
                wButton.enabled = false;
                this.buttonList.add(lButton);
                this.buttonList.add(wButton);
                this.buttonList.add(new GuiButton(4, this.width / 2 - 70, 140, 20, 20, "-"));
                this.buttonList.add(new GuiButton(5, this.width / 2 + 50, 140, 20, 20, "+"));
                this.buttonList.add(new GuiButton(6, this.width / 2 - 70, 165, 20, 20, "-"));
                this.buttonList.add(new GuiButton(7, this.width / 2 + 50, 165, 20, 20, "+"));
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        } else if (this.currentPage == 1) {
            this.buttonList.clear();
            //取消
            this.buttonList.add(new GuiButton(1000, this.width / 2 - 200, this.height - 30, new TextComponentTranslation("container.sim.sim_gui_player_to_Cancel",new Object[0]).getUnformattedText()));
            //好
            this.buttonList.add(new GuiButton(1001, this.width / 2, this.height - 30, new TextComponentTranslation("container.sim.gui_btn_name_OK",new Object[0]).getUnformattedText()));

            try {
                int x = 10;
                int y = 80;
                int idx = 100;

                for(int f = 0; f < ModSimClientLoader.getUnemployedFolks().size(); ++f) {
                    NpcIdentity folk = (NpcIdentity)ModSimClientLoader.getUnemployedFolks().get(f);
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
            }
        }

    }
    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        try {
            this.drawDefaultBackground();
            if (this.currentPage == 0) {
                //矿井
                this.drawCenteredString(this.fontRenderer, new TextComponentTranslation("container.sim.gui_mine2",new Object[0]).getUnformattedText(), this.width / 2, 17, 8454016);
            }

            super.drawScreen(i, j, f);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                this.onGuiClosed();
            } else {
                //雇佣矿工
                if (guibutton.displayString.contentEquals(new TextComponentTranslation("container.sim.Hire21",new Object[0]).getUnformattedText())) {
                    this.currentPage = 1;
                    this.showPage();
                    //解雇
                } else if (guibutton.displayString.startsWith(new TextComponentTranslation("container.sim.Fire",new Object[0]).getUnformattedText()+" ")) {
                    NetWorkLoader.net.sendToServer(new PacketFireFolk(this.employee.id));
                    guibutton.enabled = false;
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                    this.onGuiClosed();
                } else if (guibutton.id == 2) {
                    this.facing = this.facing.rotateY();
                    String fs_facing = new TextComponentTranslation("container.sim.gui_west",new Object[0]).getUnformattedText();
                    //西
                    if (this.facing.toString().equals("west")) {
                        fs_facing=new TextComponentTranslation("container.sim.gui_west",new Object[0]).getUnformattedText();
                        //东
                    } else if (this.facing.toString().equals("east")) {
                        fs_facing=new TextComponentTranslation("container.sim.gui_east",new Object[0]).getUnformattedText();
                        //北
                    } else if (this.facing.toString().equals("north")) {
                        fs_facing=new TextComponentTranslation("container.sim.gui_north",new Object[0]).getUnformattedText();
                        //南
                    } else if (this.facing.toString().equals("south")) {
                        fs_facing=new TextComponentTranslation("container.sim.gui_south",new Object[0]).getUnformattedText();
                    }
                    guibutton.displayString = fs_facing;
                    this.updateMine();
                } else if (guibutton.id == 3) {
                    float cash = 5.0F;
                    if (this.getUpgradeCost() > cash) {
                        //资金不足
                        guibutton.displayString = new TextComponentTranslation("container.sim.gui_Farming_text_NOT_ENOUGH",new Object[0]).getUnformattedText();
                        guibutton.enabled = false;
                    } else {
                        if (this.x >= 4 && this.z >= 4) {
                            this.mc.currentScreen = null;
                            this.mc.setIngameFocus();
                            this.onGuiClosed();
                            return;
                        }
                        //太小
                        guibutton.displayString = new TextComponentTranslation("container.sim.gui_mine1",new Object[0]).getUnformattedText();
                        guibutton.enabled = false;
                    }
                }

                if (this.currentPage == 0) {
                    if (guibutton.id == 4 && this.x > 6) {
                        --this.x;
                        this.showPage();
                        this.updateMine();
                    } else if (guibutton.id == 5 && this.x < 32) {
                        ++this.x;
                        this.showPage();
                        this.updateMine();
                    } else if (guibutton.id == 6 && this.z > 6) {
                        --this.z;
                        this.showPage();
                        this.updateMine();
                    } else if (guibutton.id == 7 && this.z < 32) {
                        ++this.z;
                        this.showPage();
                        this.updateMine();
                    }
                }

                if (this.currentPage == 1) {
                    if (guibutton.id == 1000) {
                        this.currentPage = 0;
                        this.showPage();
                    }

                    if (guibutton.id == 1001 && this.selectedEmployee != null) {
                        this.hasEmployee = true;
                        this.employee = this.hireableFolkNames[this.selectedEmployee.id];
                        this.currentPage = 0;
                        this.showPage();
                        //矿工
                        NetWorkLoader.net.sendToServer(new PacketHireFolk(this.employee.id, new TextComponentTranslation("container.sim.Vocation4",new Object[0]).getUnformattedText(), this.loc));
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

    private void updateMine() {
        this.setDimensions();
        NetWorkLoader.net.sendToServer(new PacketUpdateMineBox(this.id, this.x, this.z, this.facing));
    }

    private Float getUpgradeCost() {
        Float ret = (float)(this.x * this.z);
        ret = ret / 15.0F;
        return ret;
    }

    public void setDimensions() {
        ModSimClientLoader.previewPos1 = new Vec3d(this.loc.toBlockPos().offset(this.facing));
        ModSimClientLoader.previewPos2 = (new Vec3d(this.loc.toBlockPos().offset(this.facing, this.x + 1).offset(this.facing.rotateY(), this.z))).add(new Vec3d(0.0D, 2.0D, 0.0D));
    }

    public void getHireableFolkNames() {
        NetWorkLoader.net.sendToServer(new PacketGetHireableFolks(true));
    }
}
