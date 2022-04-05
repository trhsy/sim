package com.trhsy.sim.api.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


/**
 * ========================================
 *
 * @ClassName ITriggerInternal
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:53
 * ========================================
 **/
public interface ITriggerInternal extends IStatement {
    boolean isTriggerActive(IStatementContainer var1, IStatementParameter[] var2);
}
