package com.trhsy.sim.api.buildcraft.api.fuels;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraftforge.fluids.Fluid;

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName IFuelManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:32
 * ========================================
 **/
public interface IFuelManager {
    IFuel addFuel(IFuel var1);

    IFuel addFuel(Fluid var1, int var2, int var3);

    Collection<IFuel> getFuels();

    IFuel getFuel(Fluid var1);
}
