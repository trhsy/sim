package com.trhsy.sim.fluid;

import com.trhsy.sim.ModSim;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.fluid
 * @ClassName: FluidMilk
 * @Description: 牛奶 牛奶流体类，继承自 Forge 的 Fluid 类，用于定义牛奶流体的属性和行为。
 * @date 2023/10/31 下午 2:17
 */
public class FluidMilk extends Fluid {
    // 日志记录器，用于记录代码运行过程中的信息和错误
    private static final Logger LOGGER = LogManager.getLogger(FluidMilk.class);
    // 牛奶静止状态的资源位置
    public static final ResourceLocation STILL = new ResourceLocation(ModSim.MODID, "fluid/milk_still");
    // 牛奶流动状态的资源位置
    public static final ResourceLocation FLOWING =new ResourceLocation(ModSim.MODID, "fluid/milk_flow");
    // 牛奶流体的密度，单位为千克每立方米
    private static final int DENSITY = 2000;
    // 牛奶流体的粘度，单位为千分之一平方米每秒
    private static final int VISCOSITY = 2000;
    // 牛奶流体的亮度，在 Minecraft 中的亮度值
    private static final int LUMINOSITY = 0;
    // 牛奶流体的温度，使用热力学温标，单位为开尔文
    private static final int TEMPERATURE = 300;

    /**
     * 构造函数，用于初始化牛奶流体的基本属性。
     */
    public FluidMilk() {
        super("milks", STILL, FLOWING);
        try {
            // 设置流体的未本地化名称
            this.setUnlocalizedName("fluidMilk");
            // 设置流体的密度
            this.setDensity(DENSITY);
            // 设置流体的粘度
            this.setViscosity(VISCOSITY);
            // 设置流体的亮度
            this.setLuminosity(LUMINOSITY);
            // 设置流体的温度
            this.setTemperature(TEMPERATURE);
        } catch (Exception e) {
            // 记录异常信息，方便调试
            LOGGER.error("初始化牛奶流体时出现错误: {}", e.getMessage());
        }
    }

    /**
     * 获取流体的本地化名称。
     * @param fs 流体栈，包含流体的信息
     * @return 流体的本地化名称
     */
    @Override
    public String getLocalizedName(FluidStack fs) {
        // 使用新的国际化方式获取本地化名称
//        return Component.translatable("tile.fluidMilk.name").getString();
        return I18n.translateToLocal("tile.fluidMilk.name");
    }
}
