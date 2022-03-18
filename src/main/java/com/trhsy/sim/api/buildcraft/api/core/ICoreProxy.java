package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

import java.lang.ref.WeakReference;

/**
 * ========================================
 *
 * @ClassName ICoreProxy
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:01
 * ========================================
 **/
public interface ICoreProxy {
    WeakReference<EntityPlayer> getBuildCraftPlayer(WorldServer var1);
}
