package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.BuildingBlueprint;
import com.trhsy.sim.npcCode.block.FarmBox;
import com.trhsy.sim.npcCode.block.MineBox;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.build.TerrainType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 职业工厂类：封装职业创建逻辑，消除冗长 if-else，符合开闭原则
 */
public class JobFactory {
    // 常量：职业翻译键前缀
    private static final String TRANSLATION_PREFIX = "container.sim.";
    // 职业类型 -> 职业创建器映射（函数式接口存储创建逻辑）
    private static final Map<String, JobCreator> JOB_CREATOR_MAP = new HashMap<>();

    // 静态初始化：注册所有职业的创建逻辑
    static {
        // 1. 建筑工（Vocation1）
        registerJob("Vocation1", (data, pos, world, extraData) -> {
            BuildingBlueprint blueprint = extraData.getBlueprint();
            int direction = extraData.getDirection();
            return blueprint != null
                    ? new JobBuilder(data, blueprint, pos, direction, world)
                    : new JobBuilder(data, pos, direction, world);
        });

        // 2. 面包师（Vocation6）
        registerJob("Vocation6", (data, pos, world, extraData) ->
                new JobBaker(data, pos, world));

        // 3. 规划师（Vocation16）
        registerJob("Vocation16", (data, pos, world, extraData) -> {
            TerrainType terrainType = extraData.getTerrainType();
            return terrainType != null
                    ? new JobTerrainFormer(data, terrainType, pos, world)
                    : null;
        });

        // 4. 农民（Vocation5）
        registerJob("Vocation5", (data, pos, world, extraData) -> {
            FarmBox farm = ModSimLoader.getFarm(new V3(pos.getX(),pos.getY(),pos.getZ()));
            return new JobFarmer(data, pos, world, farm);
        });

        // 5. 养猪户（Vocation13）
        registerJob("Vocation13", (data, pos, world, extraData) ->
                new JobLivestockFarmer(data, pos, "container.sim.job_Livestock_pig", world));

        // 6. 养牛户（Vocation12）
        registerJob("Vocation12", (data, pos, world, extraData) ->
                new JobLivestockFarmer(data, pos, "container.sim.job_Livestock_cow", world));

        // 7. 养鸡户（Vocation14）
        registerJob("Vocation14", (data, pos, world, extraData) ->
                new JobLivestockFarmer(data, pos, "container.sim.job_Livestock_chicken", world));

        // 8. 养羊户（Vocation27）
        registerJob("Vocation27", (data, pos, world, extraData) ->
                new JobLivestockFarmer(data, pos, "container.sim.job_Livestock_sheep", world));

        // 9. 养兔户（Vocation29）
        registerJob("Vocation29", (data, pos, world, extraData) ->
                new JobLivestockFarmer(data, pos, "container.sim.job_Livestock_rabbit", world));

        // 10. 牛奶农（Vocation20）
        registerJob("Vocation20", (data, pos, world, extraData) ->
                new JobDairyFarmer(data, pos, world));

        // 11. 牧羊人（Vocation8）
        registerJob("Vocation8", (data, pos, world, extraData) ->
                new JobShepherd(data, pos, world));

        // 12. 鸡蛋农（Vocation3）
        registerJob("Vocation3", (data, pos, world, extraData) ->
                new JobEggFarmer(data, pos, world));

        // 13. 屠夫（Vocation15）
        registerJob("Vocation15", (data, pos, world, extraData) ->
                new JobButcher(data, pos, world));

        // 14. 渔夫（Vocation18）
        registerJob("Vocation18", (data, pos, world, extraData) ->
                new JobFisherman(data, pos, world));

        // 15. 食品商（Vocation26）
        registerJob("Vocation26", (data, pos, world, extraData) ->
                new JobGrocer(data, pos, world));

        // 16. 士兵（Vocation7）
        registerJob("Vocation7", (data, pos, world, extraData) ->
                new JobSoldier(data, pos, world));

        // 17. 伐木工（Vocation2）
        registerJob("Vocation2", (data, pos, world, extraData) ->
                new JobLumberjack(data, pos, world));

        // 18. 制糖师（Vocation30）
        registerJob("Vocation30", (data, pos, world, extraData) ->
                new JobSugar(data, pos, world));

        // 19. 矿工（Vocation4）
        registerJob("Vocation4", (data, pos, world, extraData) -> {
            MineBox mine = ModSimLoader.getMine(new V3(pos.getX(),pos.getY(),pos.getZ()));
            return new JobMiner(data, pos, world, mine);
        });

        // 20. 板砖工（Vocation25）
        registerJob("Vocation25", (data, pos, world, extraData) ->
                new JobBrickMaker(data, pos, world));

        // 21. 玻璃制造商（Vocation17）
        registerJob("Vocation17", (data, pos, world, extraData) ->
                new JobGlassMaker(data, pos, world));

        // 22. 建筑商（Vocation11）
        registerJob("Vocation11", (data, pos, world, extraData) ->
                new JobBuildersMerchant(data, pos, world));

        // 23. 行长（Vocation31）
        registerJob("Vocation31", (data, pos, world, extraData) ->
                new JobATM(data, pos, world));

        // 24. 杂货商（Vocation9）
        registerJob("Vocation9", (data, pos, world, extraData) ->
                new JobMerchant(data, pos, world));

        // 25. 插花师（Vocation32）
        registerJob("Vocation32", (data, pos, world, extraData) ->
                new JobFlower(data, pos, world));

        // 26. 赤脚大夫（Vocation33）
        registerJob("Vocation33", (data, pos, world, extraData) ->
                new JobBarefootDoctor(data, pos, world));

        // 27. 妇产科医生（Vocation34）
        registerJob("Vocation34", (data, pos, world, extraData) ->
                new JobAccoucheur(data, pos, world));

        // 28. 汉堡店经理（Vocation36）
        registerJob("Vocation36", (data, pos, world, extraData) ->
                new JobBurgers(data, pos, world));

        // 29. 奶酪匠（Vocation21）
        registerJob("Vocation21", (data, pos, world, extraData) ->
                new JobCheesemaker(data, pos, world));

        // 30. 麦当劳（Vocation35）
        registerJob("Vocation35", (data, pos, world, extraData) ->
                new JobMcDonald(data, pos, world));

        // 31. 酒馆（Vocation28）
        registerJob("Vocation28", (data, pos, world, extraData) ->
                new JobBartender(data, pos, world));

        // 32. 快递员（Vocation10）
        registerJob("Vocation10", (data, pos, world, extraData) ->
                new JobCourier(data, pos, world));
    }

    /**
     * 注册职业创建器
     * @param translationKeySuffix 翻译键后缀（如 "Vocation1"）
     * @param creator 职业创建逻辑
     */
    private static void registerJob(String translationKeySuffix, JobCreator creator) {
        String fullKey = new TextComponentTranslation(TRANSLATION_PREFIX + translationKeySuffix).getUnformattedText();
        JOB_CREATOR_MAP.put(fullKey, creator);
    }

    /**
     * 创建职业实例
     * @param npcData NPC数据
     * @param jobValue 存档中的职业字符串（如 "建筑工;x,y,z;0"）
     * @param blueprint 建筑蓝图（仅建筑工需要）
     * @param world 游戏世界
     * @return 职业实例，失败返回null
     */
    public static Job createJob(NpcData npcData, String jobValue, BuildingBlueprint blueprint, World world) {
        if (jobValue == null || jobValue.isEmpty()) {
            return null;
        }

        // 分割职业数据（格式：职业名;额外参数1;额外参数2...）
        String[] jobParts = jobValue.split(";");
        if (jobParts.length == 0) {
            ModSimLoader.log.warn("NPC[{}] 职业数据格式错误：{}", npcData.ID, jobValue);
            return null;
        }

        String jobKey = jobParts[0];
        JobCreator creator = JOB_CREATOR_MAP.get(jobKey);
        if (creator == null) {
            ModSimLoader.log.warn("NPC[{}] 未知职业：{}", npcData.ID, jobKey);
            return null;
        }

        // 解析职业额外参数（位置、方向、地形类型等）
        JobExtraData extraData = parseJobExtraData(jobParts, blueprint, npcData, world);
        BlockPos jobPos = extraData.getJobPos();
        if (jobPos == null) {
            ModSimLoader.log.warn("NPC[{}] 职业位置无效：{}", npcData.ID, jobValue);
            return null;
        }

        // 创建职业实例
        return creator.create(npcData, jobPos, world, extraData);
    }

    /**
     * 解析职业额外数据（位置、方向、地形类型等）
     */
    private static JobExtraData parseJobExtraData(String[] jobParts, BuildingBlueprint blueprint, NpcData npcData, World world) {
        JobExtraData extraData = new JobExtraData();
        extraData.setBlueprint(blueprint);

        // 解析职业位置（优先从职业参数取，其次从tempEmployLoc取）
        BlockPos jobPos = null;
        if (jobParts.length >= 2) {
            try {

                jobPos = V3.fromString(jobParts[1]).toBlockPos();
            } catch (Exception e) {
                ModSimLoader.log.debug("NPC[{}] 解析职业位置失败，使用雇佣位置", npcData.ID);
            }
        }
        if (jobPos == null && npcData.tempEmployLoc != null) {
            jobPos = npcData.tempEmployLoc.toBlockPos();
        }
        extraData.setJobPos(jobPos);

        // 解析建筑工方向（仅建筑工需要）
        if (jobParts.length >= 3 && "container.sim.Vocation1".equals(new TextComponentTranslation(jobParts[0]).getUnformattedText())) {
            try {
                extraData.setDirection(Integer.parseInt(jobParts[2]));
            } catch (NumberFormatException e) {
                ModSimLoader.log.warn("NPC[{}] 建筑工方向解析失败：{}", npcData.ID, jobParts[2]);
                extraData.setDirection(0); // 默认方向
            }
        }

        // 解析规划师地形类型（仅规划师需要）
        if (jobParts.length >= 4 && "container.sim.Vocation16".equals(new TextComponentTranslation(jobParts[0]).getUnformattedText())) {
            try {
                String terrainName = jobParts[2];
                String terrainType = jobParts[3];
                extraData.setTerrainType(new TerrainType(terrainName, terrainType));
            } catch (Exception e) {
                ModSimLoader.log.warn("NPC[{}] 规划师地形类型解析失败", npcData.ID, e);
            }
        }

        return extraData;
    }

    /**
     * 职业创建器函数式接口
     */
    @FunctionalInterface
    private interface JobCreator {
        Job create(NpcData data, BlockPos pos, World world, JobExtraData extraData);
    }

    /**
     * 职业额外数据封装类（存储位置、方向、蓝图等非通用参数）
     */
    public static class JobExtraData {
        private BlockPos jobPos;
        private int direction;
        private BuildingBlueprint blueprint;
        private TerrainType terrainType;

        // Getter & Setter
        public BlockPos getJobPos() { return jobPos; }
        public void setJobPos(BlockPos jobPos) { this.jobPos = jobPos; }
        public int getDirection() { return direction; }
        public void setDirection(int direction) { this.direction = direction; }
        public BuildingBlueprint getBlueprint() { return blueprint; }
        public void setBlueprint(BuildingBlueprint blueprint) { this.blueprint = blueprint; }
        public TerrainType getTerrainType() { return terrainType; }
        public void setTerrainType(TerrainType terrainType) { this.terrainType = terrainType; }
    }
}
