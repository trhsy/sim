package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.fluid.FluidMilk;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @ClassName FluidLoader
 * @Description todo 流体 注冊加載
 * @Author Tian
 * @Date 2022/5/823:06
 **/
public class FluidLoader {
    /**
     * 流体牛奶
     **/
    public static Fluid fluidMilk = new FluidMilk();

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            registerFluidRender((BlockFluidBase) BlockLoader.blockFluidMilk, "fluid_milk");
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public FluidLoader(FMLPreInitializationEvent event) {
        try {
            if (FluidRegistry.isFluidRegistered(fluidMilk)) {
                event.getModLog().info("发现流体{}，注册被取消。 ", fluidMilk.getName());
                fluidMilk = FluidRegistry.getFluid(fluidMilk.getName());
            } else {
                FluidRegistry.registerFluid(fluidMilk);
                FluidRegistry.addBucketForFluid(fluidMilk);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("FluidLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidRender(BlockFluidBase blockFluid, String blockStateName) {
        try {
            final String location = ModSim.MODID + ":" + blockStateName;
            final Item itemFluid = Item.getItemFromBlock(blockFluid);
            ModelLoader.setCustomMeshDefinition(itemFluid, new ItemMeshDefinition() {
                @Override
                public ModelResourceLocation getModelLocation(ItemStack stack) {
                    return new ModelResourceLocation(location, "fluid");
                }
            });
            ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return new ModelResourceLocation(location, "fluid");
                }
            });
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("registerFluidRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
