package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.File;

/**
 * ========================================
 *
 * @ClassName BlueprintDeployer
 * @Description todo 设计图部署
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:26
 * ========================================
 **/
public abstract class BlueprintDeployer {
    public static  BlueprintDeployer instance;

    public BlueprintDeployer() {
    }

    public abstract void deployBlueprint(World var1, int var2, int var3, int var4, ForgeDirection var5, File var6);

    public abstract void deployBlueprintFromFileStream(World var1, int var2, int var3, int var4, ForgeDirection var5, byte[] var6);
}

