package com.trhsy.sim.npc.traits;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.traits
 * @ClassName: Trait
 * @Description: 特征
 * @date 2022/10/11 14:45
 */
public class Trait {
    /**特征名称**/
    public String traitName;
    /**特征描述**/
    public String traitDesc;
    /**反特征特征**/
    public Trait traitOpposite;
    /****/
    public NpcData theFolk;

    public Trait() {

    }

    public static Trait getTraitFromName(String searchTerm) {
        Trait trait = null;
        try {
            for (int i = 0; i < Traits.traitList.length - 1; i++) {
                if (Traits.traitList[i].traitName.contains(searchTerm)) {
                    trait = Traits.traitList[i];
                    return trait;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("getTraitFromName出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return trait;
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
    public void setTraitIcon() {

    }

    /**
     * 告诉有这种特质的人他们的“特殊”建筑在哪里（如果他们有）
     */
    public void hasSpecialBuilding(String buildingName, String visitingText) {
        try {
            /*Building specialBuilding = Building.getBuildingBySearch(buildingName);

            if (specialBuilding != null) {
                theFolk.gotoXYZ(specialBuilding.primaryXYZ, null);
                theFolk.destination.doNotTimeout = true;
                theFolk.statusText = visitingText;
            }*/
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("hasSpecialBuilding出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 告诉有这种特质的人他们的“特殊”建筑在哪里（如果他们有）
     */
    public void hasSpecialBuilding(String buildingName) {
        try {
           /*Building specialBuilding = Building.getBuildingBySearch(buildingName);
            theFolk.gotoXYZ(specialBuilding.primaryXYZ, null);
            theFolk.destination.doNotTimeout = true;
            theFolk.statusText = I18n.format("container.sim.folk_data_Visiting") + specialBuilding.displayName;*/
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("hasSpecialBuilding出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
