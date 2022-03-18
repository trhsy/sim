package com.trhsy.sim.api.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */



import java.util.List;

/**
 * ========================================
 *
 * @ClassName IOverrideDefaultStatements
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:51
 * ========================================
 **/
public interface IOverrideDefaultStatements {
    List<ITriggerExternal> overrideTriggers();

    List<IActionExternal> overrideActions();
}
