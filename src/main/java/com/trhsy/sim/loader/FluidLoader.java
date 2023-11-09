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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: FluidLoader
 * @Description: 流体加载
 * @date 2023/10/31 下午 1:59
 */
@Mod.EventBusSubscriber
public class FluidLoader {
    /**
     * 牛奶
     */
    public static Fluid fluidMilk=new FluidMilk();
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        try {
            registerFluidRender((BlockFluidClassic)BlockLoader.milk, "milk");
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    public FluidLoader(FMLPreInitializationEvent event){
        try {
            if (FluidRegistry.isFluidRegistered(fluidMilk)) {
                event.getModLog().info("发现流体{}，注册被取消。 ", fluidMilk.getName());
                fluidMilk = FluidRegistry.getFluid(fluidMilk.getName());
            } else {
                FluidRegistry.registerFluid(fluidMilk);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("FluidLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @SideOnly(Side.CLIENT)
    public static void registerFluidRender(BlockFluidClassic blockFluid, String blockStateName) {
        try {
            fluidMilk.setBlock(blockFluid).setUnlocalizedName(blockFluid.getUnlocalizedName());
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
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerFluidRender出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
