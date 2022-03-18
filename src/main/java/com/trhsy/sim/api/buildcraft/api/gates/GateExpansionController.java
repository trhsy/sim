package com.trhsy.sim.api.buildcraft.api.gates;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */


import com.trhsy.sim.api.buildcraft.api.statements.IActionInternal;
import com.trhsy.sim.api.buildcraft.api.statements.IStatement;
import com.trhsy.sim.api.buildcraft.api.statements.IStatementParameter;
import com.trhsy.sim.api.buildcraft.api.statements.ITriggerInternal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName GateExpansionController
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:35
 * ========================================
 **/
public abstract class GateExpansionController {
    public final IGateExpansion type;
    public final TileEntity pipeTile;

    public GateExpansionController(IGateExpansion type, TileEntity pipeTile) {
        this.pipeTile = pipeTile;
        this.type = type;
    }

    public IGateExpansion getType() {
        return this.type;
    }

    public boolean isActive() {
        return false;
    }

    public void tick(IGate gate) {
    }

    public void startResolution() {
    }

    public boolean resolveAction(IStatement action, int count) {
        return false;
    }

    public boolean isTriggerActive(IStatement trigger, IStatementParameter[] parameters) {
        return false;
    }

    public void addTriggers(List<ITriggerInternal> list) {
    }

    public void addActions(List<IActionInternal> list) {
    }

    public void writeToNBT(NBTTagCompound nbt) {
    }

    public void readFromNBT(NBTTagCompound nbt) {
    }
}
