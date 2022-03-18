package com.trhsy.sim.api.buildcraft.api.recipes;


import java.util.Collection;

public interface IProgrammingRecipeManager {
    void addRecipe(IProgrammingRecipe var1);

    void removeRecipe(String var1);

    void removeRecipe(IProgrammingRecipe var1);

    IProgrammingRecipe getRecipe(String var1);

    Collection<IProgrammingRecipe> getRecipes();
}
