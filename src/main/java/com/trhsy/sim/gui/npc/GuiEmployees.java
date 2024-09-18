package com.trhsy.sim.gui.npc;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.server.PacketFireFolk;
import com.trhsy.sim.npcCode.NpcIdentity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @ClassName GuiEmployees
 * @Description todo 员工信息
 * @Author TRHSY
 * @Date 2022/10/2013:44
 **/
public class GuiEmployees extends GuiScreen {
    List<NpcIdentity> folks;
    private int mouseCount = 0;
    private int folkOffset = 0;
    private int folksOnAPage = 0;

    public GuiEmployees() {
    }
    @Override
    public void initGui() {
        this.folks = new CopyOnWriteArrayList<NpcIdentity>(ModSimClientLoader.tempHireableNpcNames);
        this.showPage();
        super.initGui();
    }

    private void showPage() {
        try {
            this.buttonList.clear();
            int y = 30;
            boolean more = false;
            int count = 0;
            if (this.folkOffset < 0) {
                this.folkOffset = 0;
            }

            for(int f = this.folkOffset; f < this.folks.size(); ++f) {
                //失业
                if (!((NpcIdentity)this.folks.get(f)).job.contentEquals(new TextComponentTranslation("container.sim.gui_Folk_unemployed",new Object[0]).getUnformattedText())) {
                    //解雇
                    this.buttonList.add(new GuiButton(f, this.width - 55, y, 50, 20, new TextComponentTranslation("container.sim.Fire",new Object[0]).getUnformattedText()));
                }

                y += 20;
                if (y + 20 > this.height - 30) {
                    more = true;
                    break;
                }

                ++count;
            }

            if (this.folksOnAPage == 0) {
                this.folksOnAPage = count + 1;
            }

            if (this.folkOffset > 0) {
                this.buttonList.add(new GuiButton(1000, 0, 0, 50, 20, "<"));
            }

            if (more) {
                this.buttonList.add(new GuiButton(1001, this.width - 50, 0, 50, 20, ">"));
            }
        } catch (Exception var5) {
            StackTraceElement element = var5.getStackTrace()[0];
            ModSimLoader.log.error("员工信息 GUI showPage 出错了：" + var5.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.drawDefaultBackground();
        //员工
        this.drawCenteredString(this.fontRenderer, new TextComponentTranslation("container.sim.employees",new Object[0]).getUnformattedText(), this.width / 2, 17, 16777215);
        int y = 35;
        if (this.folkOffset < 0) {
            this.folkOffset = 0;
        }

        for(int ff = this.folkOffset; ff < this.folks.size(); ++ff) {
            NpcIdentity folk = (NpcIdentity)this.folks.get(ff);
            this.drawString(this.fontRenderer, folk.name, 2, y, 10551295);
            String status;
            //失业的
            if (folk.job.contentEquals(new TextComponentTranslation("container.sim.gui_Folk_unemployed",new Object[0]).getUnformattedText())) {
                this.drawString(this.fontRenderer, new TextComponentTranslation("container.sim.gui_Folk_unemployed",new Object[0]).getUnformattedText(), 110, y, 16715792);
            } else {
                status = folk.job;
                this.drawString(this.fontRenderer, status, 110, y, 10551295);
            }

            status = "";

            try {
                status = folk.status;
            } catch (Exception var9) {
            }
            //在家
            if (status.contains(new TextComponentTranslation("container.sim.FolkAction9",new Object[0]).getUnformattedText())) {
                //在家放松
                status = new TextComponentTranslation("container.sim.folk_data_Relaxing_home",new Object[0]).getUnformattedText();
            }

            this.drawString(this.fontRenderer, status, 250, y, 10551295);
            y += 20;
            if (y + 20 > this.height - 20) {
                break;
            }
        }

        super.drawScreen(i, j, f);
    }
    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 1000) {
            this.folkOffset -= this.folksOnAPage;
            this.showPage();
        } else if (guibutton.id == 1001) {
            this.folkOffset += this.folksOnAPage;
            this.showPage();
        } else {
            NpcIdentity folk = (NpcIdentity)this.folks.get(guibutton.id);
            NetWorkLoader.net.sendToServer(new PacketFireFolk(folk.id));
            guibutton.enabled = false;
        }

    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
}
