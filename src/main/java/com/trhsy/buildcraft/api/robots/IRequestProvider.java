package com.trhsy.buildcraft.api.robots;/**
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
    int getNumberOfRequests();

    StackRequest getAvailableRequest(int var1);

    boolean takeRequest(int var1, EntityRobotBase var2);

    ItemStack provideItemsForRequest(int var1, ItemStack var2);
}
