package com.trhsy.sim.common.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.gui.folk.GuiMerchant;

/**
 * ========================================
 *
 * @ClassName ThreadGuiMerchant
 * @Description todo 商人
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:34
 * ========================================
 **/
public class ThreadGuiMerchant implements Runnable {
    private final GuiMerchant guiMerchant;

    ThreadGuiMerchant(GuiMerchant var1) {
        this.guiMerchant = var1;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000L);
        } catch (Exception var2) {
        }

        this.guiMerchant.mc.theWorld.playSound(this.guiMerchant.mc.thePlayer.posX, this.guiMerchant.mc.thePlayer.posY, this.guiMerchant.mc.thePlayer.posZ, ModSim.MODID + ":merchm", 1.0F, 1.0F, false);
    }
}
