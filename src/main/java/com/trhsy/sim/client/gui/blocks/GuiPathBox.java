package com.trhsy.sim.client.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.client.gui.folk.GuiEmployFolk;
import com.trhsy.sim.common.core.entity.functionality.Marker;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * ========================================
 *
 * @ClassName GuiPathBox
 * @Description todo PathBox
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:35
 * ========================================
 **/
@SideOnly(Side.CLIENT)
public class GuiPathBox extends GuiScreen {
    public V3 location;
    public EntityPlayer thePlayer = null;
    GuiTextField theGuiTextField1;
    public String errorText = "";
    private int mouseCount = 0;
    private int page = 0;

    public GuiPathBox(V3 location, EntityPlayer p) {
        try {
            this.location = location;
            this.thePlayer = p;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiPathBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        try {
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.updateCursorCounter();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void initGui() {
        try {
            this.buttonList.clear();
            //完成
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
            GuiButton b=new GuiButton(2, this.width / 2 - 100, 160, I18n.format("container.sim.sim_gui_Set_new"));
            //置新的快递点/传送点
            this.buttonList.add(b);
            this.theGuiTextField1 = new GuiTextField(0,this.fontRendererObj, this.width / 2 - this.width / 3 / 2, 138, this.width / 3, 20);
            this.theGuiTextField1.setMaxStringLength(23);


        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiPathBox-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            //路径箱
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox4"), this.width / 2, 17, 16777215);

            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.drawTextBox();
            }
//            try {
//                if (this.thePathBox.marker1XYZ == null) {
//                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox5"), this.width / 2, 27, 16711680);
//                }
//            } catch (Exception e) {
//                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox6"), this.width / 2, 27, 16711680);
//            }
            this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 50, 16711680);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GuiPathBox-drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var6.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    //设置新的快递点/传送点
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_Set_new"))) {
                        String name = this.theGuiTextField1.getText().trim();
                        if (name.length() == 0) {
                            //请输入此快递点/发送点的名称
                            this.errorText =I18n.format("container.sim.Markers5");
                            this.theGuiTextField1.isFocused();
                            return;
                        }

                        V3 point = this.location.clone();
                        List<IInventory> chestInvs = Job.inventoriesFindClosest(point, 5);
                        if (chestInvs.size() == 0) {
                            //错误：将至少一个箱子靠近标记。
                            this.errorText = I18n.format("container.sim.Markers6");
                            return;
                        }

                        point.name = name;

                        for (int p = 0; p < ModSimReloaded.theCourierPoints.size(); ++p) {
                            V3 epoint = ModSimReloaded.theCourierPoints.get(p);
                            if (epoint.name.contentEquals(name)) {
                                //错误：名称必须是唯一的,' ' 已被使用。
                                this.errorText = I18n.format("container.sim.Markers7") + name + I18n.format("container.sim.Markers8");
                                return;
                            }
                        }
                        ModSimReloaded.theCourierPoints.add(point);
                        //添加了快递点/传送点'
                        this.errorText = I18n.format("container.sim.Markers9") + name + I18n.format("container.sim.Markers10");
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIPATHBOX-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void keyTyped(char c, int i) {
        try {if (i == 1) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.textboxKeyTyped(c, i);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        try {
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.mouseClicked(i, j, k);
            }
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }
    }
}

