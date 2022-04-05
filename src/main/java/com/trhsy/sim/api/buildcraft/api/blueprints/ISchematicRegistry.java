package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

/**
 * ========================================
 *
 * @ClassName ISchematicRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:30
 * ========================================
 **/
public interface ISchematicRegistry {
    void registerSchematicBlock(Block var1, Class<? extends Schematic> var2, Object... var3);

    void registerSchematicBlock(Block var1, int var2, Class<? extends Schematic> var3, Object... var4);

    void registerSchematicEntity(Class<? extends Entity> var1, Class<? extends SchematicEntity> var2, Object... var3);

    boolean isSupported(Block var1, int var2);
}
