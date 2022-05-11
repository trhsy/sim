package com.trhsy.sim.common.block.fluid;

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * @ClassName FluidMilk
 * @Description todo 流体牛奶
 * @Author Tian
 * @Date 2022/5/823:01
 **/
public class FluidMilk extends Fluid {
    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID + ":" + "fluid/milk_still");
    public static final ResourceLocation flowing = new ResourceLocation(ModSim.MODID + ":" + "fluid/milk_flow");

    public FluidMilk() {
        super("milk", FluidMilk.still, FluidMilk.flowing);
        this.setUnlocalizedName("fluidMilk");
        this.setDensity(1000);//用于设置这个流体的密度，单位为千克每立方米，默认为水的密度，也就是1000
        this.setViscosity(1000);//用于设置这个流体的粘度，单位为千分之一平方米每秒，使用运动粘度，默认为水的粘度，也就是1000
        this.setLuminosity(0);//用于设置这个流体的亮度，也就是在Minecraft中的亮度，默认为水的亮度，也就是0
        this.setTemperature(300);//用于设置这个流体的温度，使用热力学温标，也就是开尔文，默认为室温，也就是300
        //FluidRegistry.registerFluid(this);
    }
}
