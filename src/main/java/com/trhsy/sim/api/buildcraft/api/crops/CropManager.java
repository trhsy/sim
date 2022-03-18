package com.trhsy.sim.api.buildcraft.api.crops;


import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CropManager {
    private static List<ICropHandler> handlers = new ArrayList();
    private static ICropHandler defaultHandler;

    private CropManager() {
    }

    public static void registerHandler(ICropHandler cropHandler) {
        handlers.add(cropHandler);
    }

    public static void setDefaultHandler(ICropHandler cropHandler) {
        defaultHandler = cropHandler;
    }

    public static ICropHandler getDefaultHandler() {
        return defaultHandler;
    }

    public static boolean isSeed(ItemStack stack) {
        Iterator var1 = handlers.iterator();

        ICropHandler cropHandler;
        do {
            if (!var1.hasNext()) {
                return defaultHandler.isSeed(stack);
            }

            cropHandler = (ICropHandler)var1.next();
        } while(!cropHandler.isSeed(stack));

        return true;
    }

    public static boolean canSustainPlant(World world, ItemStack seed, int x, int y, int z) {
        Iterator var5 = handlers.iterator();

        while(var5.hasNext()) {
            ICropHandler cropHandler = (ICropHandler)var5.next();
            if (cropHandler.isSeed(seed) && cropHandler.canSustainPlant(world, seed, x, y, z)) {
                return true;
            }
        }

        return defaultHandler.isSeed(seed) && defaultHandler.canSustainPlant(world, seed, x, y, z);
    }

    public static boolean plantCrop(World world, EntityPlayer player, ItemStack seed, int x, int y, int z) {
        Iterator var6 = handlers.iterator();

        ICropHandler cropHandler;
        do {
            if (!var6.hasNext()) {
                return defaultHandler.plantCrop(world, player, seed, x, y, z);
            }

            cropHandler = (ICropHandler)var6.next();
        } while(!cropHandler.isSeed(seed) || !cropHandler.canSustainPlant(world, seed, x, y, z) || !cropHandler.plantCrop(world, player, seed, x, y, z));

        return true;
    }

    public static boolean isMature(IBlockAccess blockAccess, Block block, int meta, int x, int y, int z) {
        Iterator var6 = handlers.iterator();

        ICropHandler cropHandler;
        do {
            if (!var6.hasNext()) {
                return defaultHandler.isMature(blockAccess, block, meta, x, y, z);
            }

            cropHandler = (ICropHandler)var6.next();
        } while(!cropHandler.isMature(blockAccess, block, meta, x, y, z));

        return true;
    }

    public static boolean harvestCrop(World world, int x, int y, int z, List<ItemStack> drops) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        Iterator var7 = handlers.iterator();

        while(var7.hasNext()) {
            ICropHandler cropHandler = (ICropHandler)var7.next();
            if (cropHandler.isMature(world, block, meta, x, y, z)) {
                return cropHandler.harvestCrop(world, x, y, z, drops);
            }
        }

        return defaultHandler.isMature(world, block, meta, x, y, z) && defaultHandler.harvestCrop(world, x, y, z, drops);
    }
}
