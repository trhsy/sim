package com.trhsy.sim.common.gui.other;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.common.core.entity.CourierTask;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * ========================================
 *
 * @ClassName GuiBeamPlayerTo
 * @Description todo Beam 播放器
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:26
 * ========================================
 **/
public class GuiBeamPlayerTo extends GuiScreen {
    private EntityPlayer thePlayer = null;

    public GuiBeamPlayerTo(EntityPlayer thePlayer) {
        this.thePlayer = thePlayer;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void initGui() {
        this.initscreen();
    }

    private void initscreen() {
        try {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_player_to_Cancel")));
            int x = 10;
            int y = 40;
            int idx = 2;

            for (int f = 0; f < ModSimReloaded.theCourierPoints.size(); ++f) {
                V3 cpoint = (V3) ModSimReloaded.theCourierPoints.get(f);
                this.buttonList.add(new GuiButton(idx, x, y, 110, 20, cpoint.name));
                idx++;
                x += 110;
                if (x + 110 > this.width) {
                    x = 10;
                    y += 20;
                }

                if (y + 20 > this.height - 50) {
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initscreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            this.drawDefaultBackground();
            //选择要投射到的点...
            String sim_gui_BPT_Choose = I18n.format("container.sim.sim_gui_BPT_Choose");
            this.drawCenteredString(this.fontRendererObj, sim_gui_BPT_Choose, this.width / 2, 17, 16777215);
            if (ModSimReloaded.theCourierPoints.size() == 0) {
                //你没有任何信使/光束点！
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_You"), this.width / 2, 37, 16752800);
                //向下放置一个标记棒并右键单击它
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_Place"), this.width / 2, 57, 16752800);
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_to"), this.width / 2, 77, 16752800);
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.displayGuiScreen((GuiScreen)null);
                } else {
                    String name = guibutton.displayString.trim();
                    V3 v = CourierTask.getCourierPoint(name);
                    V3 safePoint = v.clone();
                    Double var6 = safePoint.y;
                    Double var7 = safePoint.y = safePoint.y + 1;
                    ModSimReloaded.sendChat(I18n.format("container.sim.sim_gui_Beaming") + name);
                    this.mc.displayGuiScreen((GuiScreen) null);
                    ClientTickHandler.beamingPlayer = this.thePlayer;
                    ClientTickHandler.beamingStage = 1;
                    ClientTickHandler.beamingStartedAt = System.currentTimeMillis();
                    ClientTickHandler.beamingTo = safePoint.clone();
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIBEAMPLAYERTO-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public GuiButton getButtonWithId(int id) {
        try {
            for(int x = 0; x < this.buttonList.size(); ++x) {
                GuiButton retbut = (GuiButton)this.buttonList.get(x);
                if (retbut.id == id) {
                    return retbut;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getButtonWithId出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }

    @Override
    public void onGuiClosed() {
        try {
            Keyboard.enableRepeatEvents(false);
            this.mc.setIngameFocus();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}