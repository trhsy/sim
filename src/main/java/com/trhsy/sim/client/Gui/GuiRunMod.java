package com.trhsy.sim.client.Gui;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiRunMod
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 3:37
 * ========================================
 **/
public class GuiRunMod extends GuiScreen {

    public boolean running = true;
    private int mouseCount = 0;

    public boolean func_73868_f() {
        return true;
    }

    public GuiRunMod() {
    }

    public void func_73866_w_() {
        System.out.println("Initializing GUI");
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, 40, "Do NOT run Sim-U-Kraft"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 75, 90, "Normal Mode"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 75, 140, "Creative Mode"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 75, 190, "Hardcore Mode"));
    }

    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "Please choose the game mode for Sim-U-Kraft", this.field_146294_l / 2, 20, 16777215);
            this.func_73732_a(this.field_146289_q, "This mode switches off Sim-U-kraft for this world", this.field_146294_l / 2, 60, 16776960);
            this.func_73732_a(this.field_146289_q, "Ideal for beginners and experts. Not too challenging.", this.field_146294_l / 2, 110, 16776960);
            this.func_73732_a(this.field_146289_q, "No money needed, everything free, no blocks required, be creative!", this.field_146294_l / 2, 160, 16776960);
            this.func_73732_a(this.field_146289_q, "Builders require ALL blocks, harder gameplay", this.field_146294_l / 2, 210, 16776960);
        } catch (Exception var5) {
            System.out.println("Caught Exception while drawing strings/screen");
        }

        super.func_73863_a(i, j, f);
    }

    protected void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146127_k == 0) {
            ModSimukraft.states.gameModeNumber = 10;
            System.out.println("Turning off Sim-U-Kraft Reloaded");
        } else if (guibutton.field_146127_k == 1) {
            ModSimukraft.states.gameModeNumber = 0;
            System.out.println("Playing Sim-U-Kraft Reloaded in normal mode");
            FolkData.generateNewFolk(FMLCommonHandler.instance().getMinecraftServerInstance().func_130014_f_());
        } else if (guibutton.field_146127_k == 2) {
            ModSimukraft.states.gameModeNumber = 1;
        } else if (guibutton.field_146127_k == 3) {
            ModSimukraft.states.gameModeNumber = 2;
        }

        ModSimukraft.states.saveStates();
        this.running = false;
        this.field_146297_k.field_71462_r = null;
        this.field_146297_k.func_71381_h();
    }
}
