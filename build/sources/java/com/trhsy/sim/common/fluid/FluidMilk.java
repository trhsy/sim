package com.trhsy.sim.common.fluid;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * ========================================
 *
 * @ClassName FluidMilk
 * @Description todo 液态奶
 * @Author Administrator
 * @Date 2022/1/26 0026下午 4:49
 * ========================================
 **/
public class FluidMilk extends Fluid {
    public FluidMilk() {
        super("fluidMilk");
        this.setDensity(10);
        this.setViscosity(1000);
        FluidRegistry.registerFluid(this);
    }
}
