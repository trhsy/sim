package com.trhsy.sim.api.buildcraft.api.tiles;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IControllable
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:56
 * ========================================
 **/
public interface IControllable {
    Mode getControlMode();

    void setControlMode(Mode var1);

    boolean acceptsControlMode(Mode var1);
}
