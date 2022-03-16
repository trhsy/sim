package com.trhsy.sim.common.loader;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.fluid.FluidMilk;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @ClassName FluidLoader
 * @Description todo
 * @Author Tian
 * @Date 2022/3/1620:59
 **/
public class FluidLoader {
    public static Fluid fluidMilk = new FluidMilk();

    public FluidLoader(FMLPreInitializationEvent event) {
        if (FluidRegistry.isFluidRegistered(fluidMilk)) {
            event.getModLog().info("Found fluid {}, the registration is canceled. ", fluidMilk.getName());
            fluidMilk = FluidRegistry.getFluid(fluidMilk.getName());
        } else {
            FluidRegistry.registerFluid(fluidMilk);
        }
    }

    /*@SideOnly(Side.CLIENT)
    public static void registerRenders() {
        registerFluidRender((BlockFluidBase) BlockLoader.fluidMilk, "fluid_mercury");
    }
    @SideOnly(Side.CLIENT)
    public static void registerFluidRender(BlockFluidBase blockFluid, String blockStateName)
    {
        final String location = ModSim.MODID + ":" + blockStateName;
        final Item itemFluid = Item.getItemFromBlock(blockFluid);
        ModelLoader.setCustomMeshDefinition(itemFluid, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(location, "fluid");
            }
        });
        ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation(location, "fluid");
            }
        });
    }*/
}
