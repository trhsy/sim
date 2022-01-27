package com.trhsy.buildcraft.api.filler;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName IFillerPattern
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:28
 * ========================================
 **/
public interface IFillerPattern {
    String getUniqueTag();

    @SideOnly(Side.CLIENT)
    IIcon getIcon();

    String getDisplayName();
}
