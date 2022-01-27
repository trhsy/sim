package com.trhsy.buildcraft.api.boards;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

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

    public abstract RedstoneBoardRobotNBT getNBTHandler();

    public final void updateBoard(EntityRobotBase container) {
    }

    public boolean canLoadFromNBT() {
        return true;
    }
}
