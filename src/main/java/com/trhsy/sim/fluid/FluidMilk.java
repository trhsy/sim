package com.trhsy.sim.fluid;

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.fluid
 * @ClassName: FluidMilk
 * @Description: 牛奶
 * @date 2023/10/31 下午 2:17
 */
public class FluidMilk extends Fluid {

    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID, "fluid/milk_still");
    public static final ResourceLocation flowing =new ResourceLocation(ModSim.MODID, "fluid/milk_flow");

    public FluidMilk() {
        super("milks", FluidMilk.still, FluidMilk.flowing);
        this.setUnlocalizedName("fluidMilk");
        //用于设置这个流体的密度，单位为千克每立方米，默认为水的密度，也就是1000
        this.setDensity(2000);
        //用于设置这个流体的粘度，单位为千分之一平方米每秒，使用运动粘度，默认为水的粘度，也就是1000
        this.setViscosity(2000);
        //用于设置这个流体的亮度，也就是在Minecraft中的亮度，默认为水的亮度，也就是0
        this.setLuminosity(0);
        //用于设置这个流体的温度，使用热力学温标，也就是开尔文，默认为室温，也就是300
        this.setTemperature(300);
    }

    @Override
    public String getLocalizedName(FluidStack fs) {
        return I18n.translateToLocal("tile.fluidMilk.name");
    }
}
