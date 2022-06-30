package com.trhsy.sim.common.entity.folk.traits;

import com.trhsy.sim.common.core.Unused;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;

public class Trait {
    public String traitName;
    public String traitDesc;
    public Trait traitOpposite;
    public FolkData theFolk;

    public Trait() {

    }

    public static Trait getTraitFromName(String searchTerm) {
        Trait trait;
        for (int i = 0; i < Traits.traitList.length - 1; i++) {
            if (Traits.traitList[i].traitName.contains(searchTerm)) {
                trait = Traits.traitList[i];
                return trait;
            }
        }
        return null;
    }

    public static String getTraitName(Trait trait) {
        return trait.traitName;
    }

    /**
     * 设置特征的名称
     */
    public void setTraitName(String name) {
        this.traitName = name;
    }

    /**
     * 设置特征的描述
     */
    public void setTraitDescription(String description) {
        this.traitDesc = description;
    }

    /**
     * 设置与此相反的特征。如果一个人有一个特点，他就不能有相反的特点。
     */
    public void setTraitOpposite(Trait opposite) {
        this.traitOpposite = opposite;
    }

    /**
     * 获取特征的名称
     */
    public String getTraitName() {
        return traitName;
    }

    /**
     * 获取特征的描述
     */
    public String getTraitDescription() {
        return traitDesc;
    }


    /**
     * 设置特征的图标
     */
    @Unused
    public void setTraitIcon() {

    }

    /**
     * 告诉有这种特质的人他们的“特殊”建筑在哪里（如果他们有）
     */
    public void hasSpecialBuilding(String buildingName, String visitingText) {
        Building specialBuilding = Building.getBuildingBySearch(buildingName);

        if (specialBuilding != null) {
            theFolk.gotoXYZ(specialBuilding.primaryXYZ, GotoMethod.WALK);
            theFolk.destination.doNotTimeout = true;
            theFolk.statusText = visitingText;
        }
    }

    /**
     * 告诉有这种特质的人他们的“特殊”建筑在哪里（如果他们有）
     */
    public void hasSpecialBuilding(String buildingName) {
        Building specialBuilding = Building.getBuildingBySearch(buildingName);

        theFolk.gotoXYZ(specialBuilding.primaryXYZ, GotoMethod.WALK);
        theFolk.destination.doNotTimeout = true;
        theFolk.statusText = "Visiting the " + specialBuilding.displayName;
    }

}
