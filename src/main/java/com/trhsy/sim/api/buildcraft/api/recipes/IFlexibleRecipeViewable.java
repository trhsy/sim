package com.trhsy.sim.api.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import java.util.Collection;

/**
 * ========================================
 *
 * @ClassName IFlexibleRecipeViewable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:43
 * ========================================
 **/
public interface IFlexibleRecipeViewable {
    Object getOutput();

    Collection<Object> getInputs();

    long getCraftingTime();

    int getEnergyCost();
}
