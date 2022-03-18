package com.trhsy.sim.api.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import com.trhsy.sim.api.buildcraft.api.core.IBox;
import com.trhsy.sim.api.buildcraft.api.core.Position;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName IBuilderContext
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:29
 * ========================================
 **/
public interface IBuilderContext {
    Position rotatePositionLeft(Position var1);

    IBox surroundingBox();

    World world();

    MappingRegistry getMappingRegistry();
}
