package com.trhsy.sim.loader;

import com.trhsy.sim.block.BlockMarker;
import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.entity.util.NpcSkin;
import com.trhsy.sim.npc.block.Marker;
import com.trhsy.sim.npc.build.BlueprintRequirements;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.util.GameStates;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ModSimClientLoader
 * @Description todo
 * @Author TRHSY
 * @Date 2023/4/2910:57
 **/
public class ModSimClientLoader {
    //客户端皮肤
    public static List<NpcSkin> folkSkins = new CopyOnWriteArrayList();
    /**
     * 临时可雇佣Npc姓名
     **/
    public static List<NpcIdentity> tempHireableNpcNames = new CopyOnWriteArrayList();
    /**
     * @Author fan
     * @Description //TODO 蓝图需求
     * @Date 11:00 2023/4/29
     * @Param 
     * @return 
     **/
    public static List<BlueprintRequirements> blueprintReqs = new CopyOnWriteArrayList();
    /**
     * @Author fan
     * @Description //TODO 标记
     * @Date 11:05 2023/4/29
     * @Param 
     * @return 
     **/
    public static List<Marker> markers = new CopyOnWriteArrayList();

    /**
     * 构建上一页
     **/
    public static int constructorPreviousPage;

    /**
     * 蓝图
     */
    public static BuildingBlueprint savedBlueprint;

    /**
     * 要构建的
     **/
    public static BlockPos previewConstructor;

    /**
     * 预览位置
     */
    public static Vec3d previewPos1;
    public static Vec3d previewPos2;
    public static int gamemode;
    public static int dayOfWeek;
    public static float money;
    public ModSimClientLoader() {
    }
    /**
     * @return java.util.List<com.trhsy.sim.entity.util.NpcIdentity>
     * @Author fan
     * @Description //TODO 失业人员 获得失业人员
     * @Date 17:59 2022/10/19
     * @Param []
     **/
    public static List<NpcIdentity> getUnemployedFolks() {
        List<NpcIdentity> hireables = new CopyOnWriteArrayList<NpcIdentity>();
        for (NpcIdentity cfi : tempHireableNpcNames) {

            if (cfi.job.contentEquals(I18n.format("container.sim.folkData1")) && Integer.parseInt(cfi.age) >= Integer.parseInt(cfi.maturityAge)) {
                hireables.add(cfi);
            }
        }

        return hireables;
    }
    /**
     * @Author fan
     * @Description //TODO 从UUID获取皮肤路径
     * @Date 11:09 2023/4/29
     * @Param [UUID]
     * @return java.lang.String
     **/
    public static String getPathFromUUID(UUID UUID) {

        for(int i = 0; i < folkSkins.size(); ++i) {
            NpcSkin npcSkin=folkSkins.get(i);
            if (npcSkin!=null&&npcSkin.UUID.equals(UUID.toString())) {
                return (folkSkins.get(i)).skinPath;
            }
        }
        return "male0.png";
    }

    /**
     * @return com.trhsy.sim.entity.util.NpcIdentity
     * @Author fan
     * @Description //TODO 根据uid 获取NPC信息
     * @Date 14:05 2022/10/18
     * @Param [uuid]
     **/
    public static NpcIdentity getFolkByUUID(UUID uuid) {
        NpcIdentity npcIdentity = null;
        for (NpcIdentity npcIdentity1 : tempHireableNpcNames) {
            String id1=npcIdentity1.id;
            String id2=uuid.toString();
            if (id1.equals(id2)) {
                return npcIdentity1;
            }
        }
        return npcIdentity;
    }

    /**根据蓝图id找到蓝图*/
    public static BlueprintRequirements getRequirementsByUUID(UUID uuid) {
        BlueprintRequirements cbr=null;
        for (BlueprintRequirements cbr1:blueprintReqs){
            if(!cbr1.entityId.equals(uuid)){
                cbr=cbr1;
                return cbr;
            }
        }
        return cbr;
    }
}
