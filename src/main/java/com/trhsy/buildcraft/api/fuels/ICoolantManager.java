package com.trhsy.buildcraft.api.fuels;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.StackKey;
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

    ISolidCoolant addSolidCoolant(StackKey var1, StackKey var2, float var3);

    Collection<ICoolant> getCoolants();

    Collection<ISolidCoolant> getSolidCoolants();

    ICoolant getCoolant(Fluid var1);

    ISolidCoolant getSolidCoolant(StackKey var1);
}
