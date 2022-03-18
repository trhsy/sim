package com.trhsy.sim.api.buildcraft.api.gates;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName IGateExpansion
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:34
 * ========================================
 **/
public interface IGateExpansion {
    String getUniqueIdentifier();

    String getDisplayName();

    GateExpansionController makeController(TileEntity var1);

    void registerBlockOverlay(IIconRegister var1);

    void registerItemOverlay(IIconRegister var1);

    IIcon getOverlayBlock();

    IIcon getOverlayItem();
}
