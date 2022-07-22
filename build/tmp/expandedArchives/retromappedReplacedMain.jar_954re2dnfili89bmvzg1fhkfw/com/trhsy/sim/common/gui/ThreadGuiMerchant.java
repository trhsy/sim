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
            this.guiMerchant.field_146297_k.field_71441_e.func_72980_b(this.guiMerchant.field_146297_k.field_71439_g.field_70165_t, this.guiMerchant.field_146297_k.field_71439_g.field_70163_u, this.guiMerchant.field_146297_k.field_71439_g.field_70161_v, ModSim.MODID + ":merchm", 1.0F, 1.0F, false);
        } catch (Exception var2) {
        }


    }
}
