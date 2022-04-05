package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName IIconProvider
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:02
 * ========================================
 **/
public interface IIconProvider {
    @SideOnly(Side.CLIENT)
    IIcon getIcon(int var1);

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister var1);
}
