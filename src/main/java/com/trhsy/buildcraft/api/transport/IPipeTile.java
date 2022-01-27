package com.trhsy.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.EnumColor;
import com.trhsy.buildcraft.api.transport.pluggable.PipePluggable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName IPipeTile
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:00
 * ========================================
 **/
public interface IPipeTile extends IInjectable {
    PipeType getPipeType();

    World getWorldObj();

    int x();

    int y();

    int z();

    boolean isPipeConnected(ForgeDirection var1);

    Block getNeighborBlock(ForgeDirection var1);

    TileEntity getNeighborTile(ForgeDirection var1);

    IPipe getNeighborPipe(ForgeDirection var1);

    IPipe getPipe();

    int getPipeColor();

    PipePluggable getPipePluggable(ForgeDirection var1);

    boolean hasPipePluggable(ForgeDirection var1);

    boolean hasBlockingPluggable(ForgeDirection var1);

    void scheduleNeighborChange();

    @Override
    int injectItem(ItemStack var1, boolean var2, ForgeDirection var3, EnumColor var4);

    /** @deprecated */
    @Deprecated
    int injectItem(ItemStack var1, boolean var2, ForgeDirection var3);
        }
