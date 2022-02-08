package com.trhsy.buildcraft.api.recipes;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import java.util.List;

/**
 * ========================================
 *
 * @ClassName IIntegrationRecipeManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:44
 * ========================================
 **/
public interface IIntegrationRecipeManager {
    void addRecipe(IIntegrationRecipe var1);

    List<? extends IIntegrationRecipe> getRecipes();
}
