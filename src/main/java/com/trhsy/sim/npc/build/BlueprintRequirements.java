package com.trhsy.sim.npc.build;

import java.util.UUID;

/**
 * @ClassName BlueprintRequirements
 * @Description todo 蓝图请求
 * @Author TRHSY
 * @Date 2022/10/2117:34
 **/
public class BlueprintRequirements {
    public UUID entityId;
    public String requirements;

    public BlueprintRequirements(UUID entityId, String requirements) {
        this.entityId = entityId;
        this.requirements = requirements;
    }

    public String[] getRequirements() {
        return this.requirements.split(";");
    }
}
