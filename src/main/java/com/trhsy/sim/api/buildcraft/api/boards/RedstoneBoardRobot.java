package com.trhsy.sim.api.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import com.trhsy.sim.api.buildcraft.api.robots.AIRobot;
import com.trhsy.sim.api.buildcraft.api.robots.EntityRobotBase;

/**
 * ========================================
 *
 * @ClassName RedstoneBoardRobot
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:45
 * ========================================
 **/
public abstract class RedstoneBoardRobot extends AIRobot implements IRedstoneBoard<EntityRobotBase> {
    public RedstoneBoardRobot(EntityRobotBase iRobot) {
        super(iRobot);
    }

    @Override
    public abstract RedstoneBoardRobotNBT getNBTHandler();

    @Override
    public final void updateBoard(EntityRobotBase container) {
    }

    @Override
    public boolean canLoadFromNBT() {
        return true;
    }
}
