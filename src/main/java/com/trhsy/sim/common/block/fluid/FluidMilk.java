package com.trhsy.sim.common.block.fluid;

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * @ClassName FluidMilk
 * @Description todo
 * @Author Tian
 * @Date 2022/5/823:01
 **/
public class FluidMilk extends Fluid {
    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID + ":" + "milk_still");
    public static final ResourceLocation flowing = new ResourceLocation(ModSim.MODID + ":" + "milk_flow");

    public FluidMilk() {
        super("fluidMilk", FluidMilk.still, FluidMilk.flowing);
        this.setUnlocalizedName("fluidMilk");
        this.setDensity(10);
        this.setViscosity(1000);
        this.setLuminosity(0);
        this.setTemperature(300);
        //FluidRegistry.registerFluid(this);
    }
}
