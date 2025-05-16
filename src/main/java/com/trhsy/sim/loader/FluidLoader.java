package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.fluid.FluidMilk;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: FluidLoader
 * @Description: 流体加载器，负责注册和渲染自定义流体，如牛奶流体。
 * @date 2023/10/31 下午 1:59
 */
@Mod.EventBusSubscriber
public class FluidLoader {
    // 日志记录器，用于记录代码运行过程中的信息和错误
    private static final Logger LOGGER = ModSimLoader.log;
    // 流体名称常量，避免硬编码
    private static final String MILK_FLUID_NAME = "milk";
    // 模型状态名称常量，避免硬编码
    private static final String MODEL_STATE_FLUID = "fluid";
    /**
     * 牛奶流体实例
     */
    public static Fluid fluidMilk=new FluidMilk();

    // 静态代码块，在类加载时执行，确保流体在启动时就完成注册
    static {
        try {
            if (FluidRegistry.isFluidRegistered(fluidMilk)) {
                LOGGER.info("发现流体{}，注册被取消。 ", fluidMilk.getName());
                fluidMilk = FluidRegistry.getFluid(fluidMilk.getName());
            } else {
                FluidRegistry.registerFluid(fluidMilk);
            }
        } catch (Exception e) {
            handleException(e, "流体注册过程中出现错误");
        }
    }

    /**
     * 客户端侧注册渲染方法，用于注册流体的渲染信息。
     */
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            registerFluidRender((BlockFluidClassic)BlockLoader.milk, MILK_FLUID_NAME);
        } catch (Exception e) {
            handleException(e, "注册渲染时出现错误");
        }
    }
    /**
     * 注册流体渲染信息的具体方法。
     *
     * @param blockFluid     流体方块
     * @param blockStateName 方块状态名称
     */
    @SideOnly(Side.CLIENT)
    public static void registerFluidRender(BlockFluidClassic blockFluid, String blockStateName) {
        try {
            // 设置流体对应的方块和未本地化名称
            fluidMilk.setBlock(blockFluid).setUnlocalizedName(blockFluid.getUnlocalizedName());
            // 构建模型资源位置的基础字符串
            final String location = ModSim.MODID + ":" + blockStateName;
            // 获取流体方块对应的物品
            final Item itemFluid = Item.getItemFromBlock(blockFluid);
            // 设置物品的自定义网格定义
            ModelLoader.setCustomMeshDefinition(itemFluid, new ItemMeshDefinition() {
                @Override
                public ModelResourceLocation getModelLocation(ItemStack stack) {
                    return new ModelResourceLocation(location, MODEL_STATE_FLUID);
                }
            });
            // 设置方块的自定义状态映射器
            ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return new ModelResourceLocation(location, MODEL_STATE_FLUID);
                }
            });
        } catch (Exception e) {
            handleException(e, "注册流体渲染信息时出现错误");
        }
    }
    /**
     * 统一处理异常的方法，记录错误信息。
     *
     * @param e       异常对象
     * @param message 错误信息描述
     */
    private static void handleException(Exception e, String message) {
        StackTraceElement element = e.getStackTrace()[0];
        LOGGER.error("{}：{} 行数：{}", message, e.getMessage(), element.getLineNumber());
    }

}
