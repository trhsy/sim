package com.trhsy.sim.api.buildcraft.api.statements;

import net.minecraftforge.common.util.ForgeDirection;

public interface ITriggerExternalOverride {
    Result override(ForgeDirection var1, IStatementContainer var2, IStatementParameter[] var3);

    public static enum Result {
        TRUE,
        FALSE,
        IGNORE;

        private Result() {
        }
    }
}
