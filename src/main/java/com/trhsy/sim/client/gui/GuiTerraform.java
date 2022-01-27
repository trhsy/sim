package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.jobs.JobTerraformer;
import com.trhsy.sim.common.jobs.TerraformerType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

/**
 * ========================================
 *
 * @ClassName GuiTerraform
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:37
 * ========================================
 **/
public class GuiTerraform extends GuiScreen {
    FolkData theFolk;
    GuiTextField tfRadius;
    private int mouseCount = 0;
    private String errorText = "";

    public GuiTerraform(FolkData folk) {
        this.theFolk = folk;
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Cancel Request"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 200, 30, 200, 20, "'Sealand' (water to land)"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 200, 50, 200, 20, "'Nature' (plants saplings)"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 200, 70, 200, 20, "'Lawnmower' (cuts all long grass)"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 200, 90, 200, 20, "'Flattenizer' (flatten area)"));
        this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 200, 110, 200, 20, "'Value Pack' (single layer of dirt)"));
        this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2, 30, 200, 20, "'Glacial' (Freeze water, add snow)"));
        this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2, 50, 200, 20, "'Moisturizer' (Adds water to lava)"));
        this.field_146292_n.add(new GuiButton(8, this.field_146294_l / 2, 70, 200, 20, "'Thermalizer' (Collects lava)"));
        this.field_146292_n.add(new GuiButton(9, this.field_146294_l / 2, 90, 200, 20, "'De-icer' (Removes snow)"));
        this.tfRadius = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - 50, this.field_146295_m - 55, 100, 20);
        this.tfRadius.func_146203_f(5);
        this.tfRadius.func_146180_a("30");
    }

    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, "Terraforming - Please choose a Terraforming theme", this.field_146294_l / 2, 17, 16777215);
        this.func_73732_a(this.field_146289_q, "Radius (1 to 60)", this.field_146294_l / 2, this.field_146295_m - 70, 16777215);
        this.func_73732_a(this.field_146289_q, this.errorText, this.field_146294_l / 2, this.field_146295_m - 80, 16744576);
        this.tfRadius.func_146194_f();
        super.func_73863_a(i, j, f);
    }

    public void func_73876_c() {
        this.tfRadius.func_146178_a();
    }

    public void func_146284_a(GuiButton guibutton) {
        try {
            JobTerraformer var2 = (JobTerraformer)this.theFolk.theirJob;
        } catch (Exception var4) {
            ModSimukraft.sendChat("Error: You must hire a terraformer, not a builder");
            return;
        }

        if (guibutton.field_146127_k == 0) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (Integer.parseInt(this.tfRadius.func_146179_b().trim()) > 60) {
            this.errorText = "ERROR: the radius must be 60 or less";
        } else if (guibutton.field_146127_k == 1) {
            this.theFolk.terraformerType = TerraformerType.WATERTODIRT;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 2) {
            this.theFolk.terraformerType = TerraformerType.NATURE;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 3) {
            this.theFolk.terraformerType = TerraformerType.LAWNMOWER;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 4) {
            this.theFolk.terraformerType = TerraformerType.FLATTENIZER;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 5) {
            this.theFolk.terraformerType = TerraformerType.VALUEPACK;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 6) {
            this.theFolk.terraformerType = TerraformerType.GLACIAL;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 7) {
            this.theFolk.terraformerType = TerraformerType.MOISTURIZER;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 8) {
            this.theFolk.terraformerType = TerraformerType.THERMALIZER;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else if (guibutton.field_146127_k == 9) {
            this.theFolk.terraformerType = TerraformerType.DEICER;
            this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.func_146179_b().trim());
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    protected void func_73864_a(int i, int j, int k) {
        this.tfRadius.func_146192_a(i, j, k);
        super.func_73864_a(i, j, k);
    }

    protected void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else {
            if (i >= 2 && i <= 11 || i == 14) {
                try {
                    if (this.tfRadius.func_146206_l()) {
                        this.tfRadius.func_146201_a(c, i);
                    }
                } catch (Exception var4) {
                    this.tfRadius.func_146201_a(c, i);
                }
            }

        }
    }
}

