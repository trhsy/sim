package com.trhsy.sim.api.buildcraft.api.core.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName ITextureStates
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:16
 * ========================================
 **/
public interface ITextureStates extends ICullable {
    ITextureStateManager getTextureState();

    IIcon getIcon(int var1, int var2);

    Block getBlock();
}
