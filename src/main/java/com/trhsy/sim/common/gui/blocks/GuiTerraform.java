package com.trhsy.sim.common.gui.blocks;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.jobs.JobTerraformer;
import com.trhsy.sim.common.jobs.TerraformerType;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * ========================================
 *
 * @ClassName GuiTerraform
 * @Description todo 地形
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

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        try {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.Terraform1")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 200, 30, 200, 20, I18n.format("container.sim.Terraform2")));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 200, 50, 200, 20, I18n.format("container.sim.Terraform3")));
            this.buttonList.add(new GuiButton(3, this.width / 2 - 200, 70, 200, 20, I18n.format("container.sim.Terraform4")));
            this.buttonList.add(new GuiButton(4, this.width / 2 - 200, 90, 200, 20, I18n.format("container.sim.Terraform5")));
            this.buttonList.add(new GuiButton(5, this.width / 2 - 200, 110, 200, 20, I18n.format("container.sim.Terraform6")));
            this.buttonList.add(new GuiButton(6, this.width / 2, 30, 200, 20, I18n.format("container.sim.Terraform7")));
            this.buttonList.add(new GuiButton(7, this.width / 2, 50, 200, 20, I18n.format("container.sim.Terraform8")));
            this.buttonList.add(new GuiButton(8, this.width / 2, 70, 200, 20, I18n.format("container.sim.Terraform9")));
            this.buttonList.add(new GuiButton(9, this.width / 2, 90, 200, 20, I18n.format("container.sim.Terraform10")));
            this.tfRadius = new GuiTextField(0,this.fontRendererObj, this.width / 2 - 50, this.height - 55, 100, 20);
            this.tfRadius.setMaxStringLength(5);
            this.tfRadius.setText("30");
        } catch (Exception e) {
            ModSimReloaded.log.error("initGui出错了：" + e.getMessage());
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
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Terraform11"), this.width / 2, 17, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Terraform12"), this.width / 2, this.height - 70, 16777215);
            this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 80, 16744576);
            this.tfRadius.drawTextBox();
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage());
        }

    }

    @Override
    public void updateScreen() {
        try {
            this.tfRadius.updateCursorCounter();
        } catch (Exception e) {
            ModSimReloaded.log.error("updateScreen出错了：" + e.getMessage());
        }

    }
    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            try {
                JobTerraformer var2 = (JobTerraformer)this.theFolk.theirJob;
            } catch (Exception var4) {
                ModSimReloaded.sendChat(I18n.format("container.sim.Terraform13"));
                return;
            }

            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (Integer.parseInt(this.tfRadius.getText().trim()) > 60) {
                this.errorText = I18n.format("container.sim.Terraform14");
            } else if (guibutton.id == 1) {
                this.theFolk.terraformerType = TerraformerType.WATERTODIRT;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 2) {
                this.theFolk.terraformerType = TerraformerType.NATURE;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 3) {
                this.theFolk.terraformerType = TerraformerType.LAWNMOWER;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 4) {
                this.theFolk.terraformerType = TerraformerType.FLATTENIZER;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 5) {
                this.theFolk.terraformerType = TerraformerType.VALUEPACK;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 6) {
                this.theFolk.terraformerType = TerraformerType.GLACIAL;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 7) {
                this.theFolk.terraformerType = TerraformerType.MOISTURIZER;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 8) {
                this.theFolk.terraformerType = TerraformerType.THERMALIZER;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else if (guibutton.id == 9) {
                this.theFolk.terraformerType = TerraformerType.DEICER;
                this.theFolk.terraformerRadius = Integer.parseInt(this.tfRadius.getText().trim());
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("actionPerformed出错了：" + e.getMessage());
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        try {this.tfRadius.mouseClicked(i, j, k);
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            ModSimReloaded.log.error("mouseClicked出错了：" + e.getMessage());
            //e.printStackTrace();
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                if (i >= 2 && i <= 11 || i == 14) {
                    try {
                        if (this.tfRadius.isFocused()) {
                            this.tfRadius.textboxKeyTyped(c, i);
                        }
                    } catch (Exception var4) {
                        this.tfRadius.textboxKeyTyped(c, i);
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage());
        }
    }
}

