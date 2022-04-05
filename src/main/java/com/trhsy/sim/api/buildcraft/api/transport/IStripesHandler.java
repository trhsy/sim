package com.trhsy.sim.api.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IStripesHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:02
 * ========================================
 **/
public interface IStripesHandler {
    IStripesHandler.StripesHandlerType getType();

    boolean shouldHandle(ItemStack var1);

    boolean handle(World var1, int var2, int var3, int var4, ForgeDirection var5, ItemStack var6, EntityPlayer var7, IStripesActivator var8);

    public static enum StripesHandlerType {
        ITEM_USE,
        BLOCK_BREAK;

        private StripesHandlerType() {
        }
    }
}
