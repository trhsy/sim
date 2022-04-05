package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.inventory.IInventory;

/**
 * ========================================
 *
 * @ClassName ITileBuilder
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:30
 * ========================================
 **/
public interface ITileBuilder extends IInventory {
    boolean isBuildingMaterialSlot(int var1);
}
