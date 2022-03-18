package com.trhsy.sim.api.buildcraft.api.statements;/**
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
 * @ClassName IStatement
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:52
 * ========================================
 **/
public interface IStatement {
    String getUniqueTag();

    @SideOnly(Side.CLIENT)
    IIcon getIcon();

    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister var1);

    int maxParameters();

    int minParameters();

    String getDescription();

    IStatementParameter createParameter(int var1);

    IStatement rotateLeft();
}
