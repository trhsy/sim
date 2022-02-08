package com.trhsy.buildcraft.api.transport.pluggable;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.transport.IPipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IPipePluggableItem
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:05
 * ========================================
 **/
public interface IPipePluggableItem {
    PipePluggable createPipePluggable(IPipe var1, ForgeDirection var2, ItemStack var3);
}
