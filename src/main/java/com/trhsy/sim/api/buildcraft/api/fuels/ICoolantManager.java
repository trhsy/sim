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
 * @ClassName ICoolantManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:31
 * ========================================
 **/
public interface ICoolantManager {
    ICoolant addCoolant(ICoolant var1);

    ICoolant addCoolant(Fluid var1, float var2);

    ISolidCoolant addSolidCoolant(ISolidCoolant var1);

    ISolidCoolant addSolidCoolant( com.trhsy.sim.api.buildcraft.api.core.StackKey var1,  com.trhsy.sim.api.buildcraft.api.core.StackKey var2, float var3);

    Collection<ICoolant> getCoolants();

    Collection<ISolidCoolant> getSolidCoolants();

    ICoolant getCoolant(Fluid var1);

    ISolidCoolant getSolidCoolant(com.trhsy.sim.api.buildcraft.api.core.StackKey var1);
}
