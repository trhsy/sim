package com.trhsy.sim.api.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */



/**
 * ========================================
 *
 * @ClassName BuildcraftRecipeRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:41
 * ========================================
 **/
public final class BuildcraftRecipeRegistry {
    public static IAssemblyRecipeManager assemblyTable;
    public static IIntegrationRecipeManager integrationTable;
    public static IRefineryRecipeManager refinery;
    public static IProgrammingRecipeManager programmingTable;

    private BuildcraftRecipeRegistry() {
    }
}
