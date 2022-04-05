package com.trhsy.sim.common.fluid;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

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
    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID + ":" + "milk_still");
    public static final ResourceLocation flowing = new ResourceLocation(ModSim.MODID + ":" + "milk_flow");

    public FluidMilk() {
        super("fluidMilk");
        this.setUnlocalizedName("fluidMilk");
        this.setDensity(10);
        this.setViscosity(1000);
        //FluidRegistry.registerFluid(this);
    }
}
