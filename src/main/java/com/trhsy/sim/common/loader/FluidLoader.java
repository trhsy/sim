package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.block.fluid.FluidMilk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @ClassName FluidLoader
 * @Description todo 流体 注冊加載
 * @Author Tian
 * @Date 2022/5/823:06
 **/
public class FluidLoader {
    /**流体牛奶**/
    public static Fluid fluidMilk =new FluidMilk();
    public FluidLoader(FMLPreInitializationEvent event)
    {
        if (FluidRegistry.isFluidRegistered(fluidMilk))
        {
            event.getModLog().info("Found fluid {}, the registration is canceled. ", fluidMilk.getName());
            fluidMilk = FluidRegistry.getFluid(fluidMilk.getName());
        }
        else
        {
            FluidRegistry.registerFluid(fluidMilk);
        }
    }
}
