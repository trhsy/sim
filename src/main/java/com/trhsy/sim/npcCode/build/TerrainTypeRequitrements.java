package com.trhsy.sim.npcCode.build;

import java.util.UUID;

/**
 * @ClassName TerrainTypeRequitrements
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/269:58
 **/
public class TerrainTypeRequitrements {
    public UUID entityId;
    public String requirements;
    public TerrainTypeRequitrements (UUID entityId, String requirements){
        this.entityId = entityId;
        this.requirements = requirements;
    }
    public String[] getRequirements() {
        return this.requirements.split(";");
    }
}
