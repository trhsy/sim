package com.trhsy.buildcraft.api.transport.pluggable;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.INBTStoreable;
import com.trhsy.buildcraft.api.core.ISerializable;
import com.trhsy.buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName PipePluggable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:06
 * ========================================
 **/
public abstract class PipePluggable implements INBTStoreable, ISerializable {
    public PipePluggable() {
    }

    public abstract ItemStack[] getDropItems(IPipeTile var1);

    public void update(IPipeTile pipe, ForgeDirection direction) {
    }

    public void onAttachedPipe(IPipeTile pipe, ForgeDirection direction) {
        this.validate(pipe, direction);
    }

    public void onDetachedPipe(IPipeTile pipe, ForgeDirection direction) {
        this.invalidate();
    }

    public abstract boolean isBlocking(IPipeTile var1, ForgeDirection var2);

    public void invalidate() {
    }

    public void validate(IPipeTile pipe, ForgeDirection direction) {
    }

    public boolean isSolidOnSide(IPipeTile pipe, ForgeDirection direction) {
        return false;
    }

    public abstract AxisAlignedBB getBoundingBox(ForgeDirection var1);

    @SideOnly(Side.CLIENT)
    public abstract IPipePluggableRenderer getRenderer();
}