package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName ThreadGuiMerchant
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:34
 * ========================================
 **/
public class ThreadGuiMerchant implements Runnable {
    ThreadGuiMerchant(GuiMerchant var1) {
        this.this$0 = var1;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000L);
        } catch (Exception var2) {
        }

        this.this$0.mc.theWorld.playSound(this.this$0.mc.thePlayer.posX, this.this$0.mc.thePlayer.posY, this.this$0.mc.thePlayer.posZ, ModSimukraft.MODID + ":merchm", 1.0F, 1.0F, false);
    }
}
