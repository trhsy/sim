package com.trhsy.sim.api.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;

/**
 * ========================================
 *
 * @ClassName IRequestProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:47
 * ========================================
 **/
public interface IRequestProvider {
    int getRequestsCount();

    ItemStack getRequest(int var1);

    ItemStack offerItem(int var1, ItemStack var2);
}
