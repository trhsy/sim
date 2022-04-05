package com.trhsy.sim.api.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName IRedstoneBoard
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:40
 * ========================================
 **/
public interface IRedstoneBoard<T> {
    void updateBoard(T var1);

    RedstoneBoardNBT<?> getNBTHandler();
}