package com.trhsy.sim.api.buildcraft.api.recipes;


import java.util.Collection;

public interface IRecipeManager<T> {
    void addRecipe(String var1, int var2, T var3, Object... var4);

    void addRecipe(String var1, int var2, int var3, T var4, Object... var5);

    void addRecipe(IFlexibleRecipe<T> var1);

    void removeRecipe(String var1);

    void removeRecipe(IFlexibleRecipe<T> var1);

    Collection<IFlexibleRecipe<T>> getRecipes();

    IFlexibleRecipe<T> getRecipe(String var1);
}