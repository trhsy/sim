package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName BuildCraftAPI
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:53
 * ========================================
 **/

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public final class BuildCraftAPI {
    public static ICoreProxy proxy;
    public static final Set<Block> softBlocks = new HashSet();
    public static IWorldProperty isSoftProperty;
    public static IWorldProperty isWoodProperty;
    public static IWorldProperty isLeavesProperty;
    public static IWorldProperty[] isOreProperty;
    public static IWorldProperty isHarvestableProperty;
    public static IWorldProperty isFarmlandProperty;
    public static IWorldProperty isDirtProperty;
    public static IWorldProperty isShoveled;
    public static IWorldProperty isFluidSource;

    private BuildCraftAPI() {
    }

    public static boolean isSoftBlock(World world, int x, int y, int z) {
        return isSoftProperty.get(world, x, y, z);
    }
}
