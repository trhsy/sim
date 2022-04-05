package com.trhsy.sim.api.buildcraft.api.gates;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * ========================================
 *
 * @ClassName GateExpansions
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:34
 * ========================================
 **/
public final class GateExpansions {
    private static final Map<String, IGateExpansion> expansions = new HashMap();
    private static final ArrayList<IGateExpansion> expansionIDs = new ArrayList();
    private static final Map<IGateExpansion, ItemStack> recipes = HashBiMap.create();

    private GateExpansions() {
    }

    public static void registerExpansion(IGateExpansion expansion) {
        registerExpansion(expansion.getUniqueIdentifier(), expansion);
    }

    public static void registerExpansion(String identifier, IGateExpansion expansion) {
        expansions.put(identifier, expansion);
        expansionIDs.add(expansion);
    }

    public static void registerExpansion(IGateExpansion expansion, ItemStack addedRecipe) {
        registerExpansion(expansion.getUniqueIdentifier(), expansion);
        recipes.put(expansion, addedRecipe);
    }

    public static IGateExpansion getExpansion(String identifier) {
        return (IGateExpansion)expansions.get(identifier);
    }

    public static Set<IGateExpansion> getExpansions() {
        Set<IGateExpansion> set = new HashSet();
        set.addAll(expansionIDs);
        return set;
    }

    public static Map<IGateExpansion, ItemStack> getRecipesForPostInit() {
        return recipes;
    }

    public static IGateExpansion getExpansionByID(int id) {
        return (IGateExpansion)expansionIDs.get(id);
    }

    public static int getExpansionID(IGateExpansion expansion) {
        return expansionIDs.indexOf(expansion);
    }
}
