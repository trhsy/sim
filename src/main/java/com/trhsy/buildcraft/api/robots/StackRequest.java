package com.trhsy.buildcraft.api.robots;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * ========================================
 *
 * @ClassName StackRequest
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:49
 * ========================================
 **/
public class StackRequest {
    public ItemStack stack;
    public int index;
    public TileEntity requester;
    public IDockingStation station;

    public StackRequest() {
    }
}
