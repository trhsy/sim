package com.trhsy.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IStripesPipe
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:02
 * ========================================
 **/
public interface IStripesPipe extends IPipe {
    void sendItem(ItemStack var1, ForgeDirection var2);

    void dropItem(ItemStack var1, ForgeDirection var2);
}
