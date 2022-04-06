package com.trhsy.sim.client.gui.other;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.ClientTickHandler;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.CourierTask;
import com.trhsy.sim.common.entity.V3;
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
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_player_to_Cancel")));
        int x = 10;
        int y = 40;
        int idx = 2;

        for (int f = 0; f < ModSim.theCourierPoints.size(); ++f) {
            V3 cpoint = (V3) ModSim.theCourierPoints.get(f);
            this.buttonList.add(new GuiButton(idx, x, y, 110, 20, cpoint.name));
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

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        String sim_gui_BPT_Choose = I18n.format("container.sim.sim_gui_BPT_Choose");
        this.drawCenteredString(this.fontRendererObj, sim_gui_BPT_Choose, this.width / 2, 17, 16777215);
        if (ModSim.theCourierPoints.size() == 0) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_You"), this.width / 2, 37, 16752800);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_Place"), this.width / 2, 57, 16752800);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.sim_gui_player_to_to"), this.width / 2, 77, 16752800);
        }

        super.drawScreen(i, j, f);
    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.displayGuiScreen((GuiScreen)null);
            } else {
                String name = guibutton.displayString.trim();
                V3 v = CourierTask.getCourierPoint(name);
                V3 safePoint = v.clone();
                Double var6 = safePoint.y;
                Double var7 = safePoint.y = safePoint.y + 1.0D;
                ModSim.sendChat(I18n.format("container.sim.sim_gui_Beaming") + name);
                this.mc.displayGuiScreen((GuiScreen) null);
                ClientTickHandler.beamingPlayer = this.thePlayer;
                ClientTickHandler.beamingStage = 1;
                ClientTickHandler.beamingStartedAt = System.currentTimeMillis();
                ClientTickHandler.beamingTo = safePoint.clone();
            }
        }
    }

    public GuiButton getButtonWithId(int id) {
        for(int x = 0; x < this.buttonList.size(); ++x) {
            GuiButton retbut = (GuiButton)this.buttonList.get(x);
            if (retbut.id == id) {
                return retbut;
            }
        }

        return null;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.setIngameFocus();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
}