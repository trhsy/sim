package com.trhsy.sim.api.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.EnumColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IInjectable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:59
 * ========================================
 **/
public interface IInjectable {
    boolean canInjectItems(ForgeDirection var1);

    int injectItem(ItemStack var1, boolean var2, ForgeDirection var3, EnumColor var4);
}
