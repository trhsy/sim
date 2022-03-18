package com.trhsy.sim.api.buildcraft.api.gates;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */



import com.trhsy.sim.api.buildcraft.api.statements.IStatement;
import com.trhsy.sim.api.buildcraft.api.statements.IStatementParameter;
import com.trhsy.sim.api.buildcraft.api.statements.StatementSlot;
import com.trhsy.sim.api.buildcraft.api.transport.IPipe;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName IGate
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:34
 * ========================================
 **/
public interface IGate {
    /** @deprecated */
    @Deprecated
    void setPulsing(boolean var1);

    IPipe getPipe();

    List<IStatement> getTriggers();

    List<IStatement> getActions();

    List<StatementSlot> getActiveActions();

    List<IStatementParameter> getTriggerParameters(int var1);

    List<IStatementParameter> getActionParameters(int var1);
}
