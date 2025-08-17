package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.network.client.PacketUpdateNPC;
import com.trhsy.sim.network.server.PacketSyncNpcData;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.block.FarmBox;
import com.trhsy.sim.npcCode.block.MineBox;
import com.trhsy.sim.npcCode.build.Building;
import com.trhsy.sim.util.Courier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: SimmodeStart
 * @Description: 启动
 * @date 2024/10/25 14:20
 */
public class SimmodeStart {
    /**
     * 已加载世界
     **/
    public static boolean hasLoadedWorld = false;
    //新的一天
    public static boolean newDay = true;
    //新的一天是否收租
    public static boolean newDayRentals = true;
    /**
     * 上次可以户连接的时间
     **/
    public static long timeSinceLastClientUpdate = 0L;
    public static long timeSinceLastClientUpdates = 0L;

    /**
     * 分钟计时器
     **/
    public static long minuteTimer = System.currentTimeMillis();
    //收租计时
    public static long rentalsTimer = System.currentTimeMillis();


    private static ExecutorService executorService = Executors.newCachedThreadPool();


    // 性能优化参数
    private static final int RENT_COLLECTION_DELAY = 60;         // 收租延迟 (3秒)
    private static final int NPCS_PER_UPDATE = 5;                // 每更新批次处理的NPC数量
    private static final int MONEY_UPDATE_INTERVAL = 5000;       // 资金同步间隔(毫秒)
    private static final int HIREABLE_FOLKS_UPDATE_INTERVAL = 5000; // 可雇佣NPC同步间隔(毫秒)

    // 计时器和状态变量
    private static long lastUpdateTime = 0;
    private static long lastMoneyUpdateTime = 0;
    private static long lastHireableUpdateTime = 0;
    private static int currentNpcBatch = 0;
    private static long currentNpcIndex = 0; // 当前处理的NPC批次索引
    private static float cachedTotalRent = 0.0F;
    private static boolean rentCacheDirty = true;
    // 新增：NPC住宅检查与新NPC生成的计时器
    private static long npcSpawnCheckTimer = 0;
    private static final long NPC_SPAWN_CHECK_INTERVAL = 60000L; // 60秒检查一次

    // 性能优化参数
    private static final int SERVER_UPDATE_INTERVAL = 100;         // 服务器更新间隔 (5秒，20ticks/秒)
    private static final int CLIENT_UPDATE_INTERVAL = 20;          // 客户端更新间隔 (1秒)

    // 计时器和状态变量
    private static long lastServerUpdateTime = 0;
    private static long lastClientUpdateTime = 0;

    // 客户端NPC缓存 (线程安全)
    private static Map<String, NpcData> clientNpcCache = new ConcurrentHashMap<>();
    // 任务调度配置
    private static final int MAX_CONCURRENT_TASKS = 4; // 最大并发任务数
    private static final int TASK_QUEUE_CAPACITY = 100; // 任务队列容量
    private static final int UPDATE_INTERVAL_TICKS = 5; // 主更新间隔(5 ticks = 0.25秒)
    // 任务队列和线程池
//    private static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(TASK_QUEUE_CAPACITY);
    // 计时器和状态变量
    private static int tickCounter = 0;
    private static boolean isProcessing = false;
    private static final Map<Integer, NpcData> pendingNpcUpdates = new ConcurrentHashMap<>();
    private static final Map<String, Building> pendingBuildingUpdates = new ConcurrentHashMap<>();

    /*private static final ExecutorService asyncExecutor = Executors.newFixedThreadPool(
            MAX_CONCURRENT_TASKS,
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "SimMod-Task-" + threadNumber.getAndIncrement());
                }
            }
    );*/
    /**
     * 加载模组
     */
    public static void simModLoad(World world) {
        ModSimLoader.log.info("检查是否应该加载人员");
        if (world != null) {
//            if (world.isRemote) {
//                ModSimLoader.log.info("世界遥远(客户端)，正在取消");
//            }else

            if (hasLoadedWorld) {
                ModSimLoader.log.info(hasLoadedWorld + "世界尚未加载");
            } else {
                ModSimLoader.log.info(hasLoadedWorld + "世界尚未加载");
                ModSimLoader.log.info(hasLoadedWorld + "清除旧的世界数据");
                ModSimLoader.folks.clear();
                ModSimLoader.farms.clear();
                ModSimLoader.mines.clear();
                ModSimLoader.buildings.clear();
                ModSimLoader.dayOfWeek = 0;
                ModSimLoader.gameDay = 0;
                ModSimLoader.gamemode = 999;
                ModSimLoader.money = 10.0F;
                ModSimLoader.sim_is_running = false;
                newDay = true;
                timeSinceLastClientUpdate = 0L;
                File[] buildingSaves;

                ModSimLoader.log.info("加载世界...");
                ModSimLoader.loadStates();


                try {
                    ModSimLoader.log.info("加载农场");
                    new DimensionManager();
                    File farmsFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms");
                    if (!farmsFolder.exists()) {
                        farmsFolder.mkdirs();
                    }
                    buildingSaves = farmsFolder.listFiles();
                    for (int i = 0; i < buildingSaves.length; i++) {
                        File buildingFile = buildingSaves[i];
                        //ModSimLoader.log.info("打开农场文件: " + buildingFile.getName());
                        ModSimLoader.farms.add(new FarmBox(UUID.fromString(buildingFile.getName().split(".sk2")[0])));
                    }
                } catch (Exception var10) {
                    StackTraceElement element = var10.getStackTrace()[0];
                    ModSimLoader.log.error("加载农场文件出错了：" + var10.getMessage() + "行数：" + element.getLineNumber());
                }

                try {
                    ModSimLoader.log.info("加载矿场");
                    new DimensionManager();
                    File minesFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "mines");
                    if (!minesFolder.exists()) {
                        minesFolder.mkdirs();
                    }
                    buildingSaves = minesFolder.listFiles();
                    for (int i = 0; i < buildingSaves.length; i++) {
                        File buildingFile = buildingSaves[i];
                        ModSimLoader.log.info("打开矿场文件: " + buildingFile.getName());
                        ModSimLoader.mines.add(new MineBox(UUID.fromString(buildingFile.getName().split(".sk2")[0])));
                    }
                } catch (Exception var9) {
                    StackTraceElement element = var9.getStackTrace()[0];
                    ModSimLoader.log.error("加载矿场文件出错了：" + var9.getMessage() + "行数：" + element.getLineNumber());
                }
                try {
                    ModSimLoader.log.info("获得保存的NPC，开始加载");
                    new DimensionManager();
                    File npcFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "npc");
                    if (!npcFolder.exists()) {
                        npcFolder.mkdirs();
                    }
                    buildingSaves = npcFolder.listFiles();
                    for (int i = 0; i < buildingSaves.length; i++) {
                        File buildingFile = buildingSaves[i];
                        //ModSimLoader.log.info("得到Npc " + buildingFile.getName());
                        String uid = buildingFile.getName().split(".sk2")[0];
                        NpcData npcData = new NpcData(world, UUID.fromString(uid));
                        if (!npcData.isDead) {
                            ModSimLoader.folks.add(npcData);
                            NetWorkLoader.net.sendToAll(new PacketUpdateNPC());
                        } else {
                            try {
                                String worldPath = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + "sim";
                                Files.deleteIfExists((new File(worldPath + File.separator + "npc" + File.separator + uid + ".sk2")).toPath());
                                ModSimLoader.log.warn("npc已死不加载，重新，已删除[" + uid + "]");
                            } catch (Exception var5) {
                                StackTraceElement element = var5.getStackTrace()[0];
                                ModSimLoader.log.error("npcDeath-onDeath出错了：" + var5.getMessage() + "行数：" + element.getLineNumber());
                            }
                        }

//                    ModSimLoader.log.info(npcData.race.skinName);
                    }
                } catch (Exception e) {
                    StackTraceElement element = e.getStackTrace()[0];
                    ModSimLoader.log.error("获得保存的NPC出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
                }

                try {
                    ModSimLoader.log.info("加载建筑物");
                    new DimensionManager();
                    File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "buildings");
                    if (!buildingFolder.exists()) {
                        buildingFolder.mkdirs();
                    }
                    buildingSaves = buildingFolder.listFiles();
                    for (int i = 0; i < buildingSaves.length; i++) {
                        File buildingFile = buildingSaves[i];
                        //ModSimLoader.log.info("打开建筑文件: " + buildingFile.getName());
                        ModSimLoader.buildings.add(new Building(world, UUID.fromString(buildingFile.getName().split(".sk2")[0])));
                    }

                } catch (Exception var7) {
                    StackTraceElement element = var7.getStackTrace()[0];
                    ModSimLoader.log.error("加载建筑文件出错了：" + var7.getMessage() + "行数：" + element.getLineNumber());
                }
                try {
                    ModSimLoader.log.info("加载快递点");
                    new DimensionManager();
                    File buildingFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "CourierPoints");
                    if (!buildingFolder.exists()) {
                        buildingFolder.mkdirs();
                    }
                    buildingSaves = buildingFolder.listFiles();
                    for (int i = 0; i < buildingSaves.length; i++) {
                        File buildingFile = buildingSaves[i];
                        ModSimLoader.theCourierPoints.add(new Courier(buildingFile.getName().split(".sk2")[0]));
                    }
                } catch (Exception var7) {
                    StackTraceElement element = var7.getStackTrace()[0];
                    ModSimLoader.log.error("加载快递点文件出错了：" + var7.getMessage() + "行数：" + element.getLineNumber());
                }
                NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
                hasLoadedWorld = true;
            }


        }
    }

    /**
     * 保存
     */
    public static void simModSave(World world) {
        if (world != null) {
            if (hasLoadedWorld) {
                //配置文件保存
//                ModSimLoader.log.info("时间数据保存，准备保存模组信息");
                ModSimLoader.saveStates();
                //农场保存
//                ModSimLoader.log.info("农场保存，准备保存模组信息");
                for (FarmBox farmBox : ModSimLoader.farms) {
                    farmBox.saveFarm();
                }
                //矿场保存
//                ModSimLoader.log.info("农场保存，准备保存模组信息");
                for (MineBox mineBox : ModSimLoader.mines) {
                    mineBox.saveMine();
                }
                //NPC保存
//                ModSimLoader.log.info("NPC保存，准备保存模组信息");
                for (NpcData folks : ModSimLoader.folks) {
                    folks.saveFolk();
                }
                //建筑保存
//                ModSimLoader.log.info("建筑保存，准备保存模组信息");
                for (Building b : ModSimLoader.buildings) {
                    b.saveBuilding();
                }
            }
        }
    }

    /**
     * 更新
     */
    public static void simModupdate(World world) {
//        World worlds =  Minecraft.getMinecraft().world;
        //第三版
/*
        // 频率控制
        tickCounter++;
        if (tickCounter % UPDATE_INTERVAL_TICKS != 0) {
            return;
        }
        // 检查是否已有任务在处理中
        if (isProcessing) {
            return;
        }

        // 当前世界有玩家才执行
        if (world.playerEntities.isEmpty()) {
            return;
        }

        // 标记开始处理
        isProcessing = true;

        // 1. 异步处理NPC状态更新
        processNpcsAsync(world);

        // 2. 异步处理建筑和租金
        processBuildingsAsync(world);

        // 3. 处理昼夜循环和环境控制
        processEnvironmentAsync(world);

        // 4. 同步网络数据
        scheduleNetworkSync(world);
*/
        //第二版
        /*// 检查是否需要整体更新
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < UPDATE_INTERVAL_TICKS * 50) { // 50ms = 1tick
            return;
        }

        lastUpdateTime = currentTime;

        // 确保世界和玩家存在
        if (world == null || world.playerEntities.isEmpty()) {
            return;
        }

        ModSimLoader.sim_is_running = true;

        // =============== 分批处理NPC更新 ===============
        List<NpcData> folks = new ArrayList<>(ModSimLoader.folks);
        int totalBatches = Math.max(1, (int) Math.ceil(folks.size() / (double) NPCS_PER_UPDATE));
        // 计算当前批次的起止索引
        int startIndex = currentNpcBatch * NPCS_PER_UPDATE;
        int endIndex = Math.min(startIndex + NPCS_PER_UPDATE, folks.size());

        for (int i = (int)startIndex; i < endIndex; i++) {
            NpcData f = folks.get(i);
            if (f != null && f.entity != null && !f.entity.isDead) {
                f.onUpdate();
            }
        }
        // 更新批次索引
        currentNpcBatch = (currentNpcBatch + 1) % totalBatches;

        // =============== 定期同步网络数据 ===============
        //
        if (currentTime - lastMoneyUpdateTime > MONEY_UPDATE_INTERVAL) {
            lastMoneyUpdateTime = currentTime;
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
        }

        if (currentTime - lastHireableUpdateTime > HIREABLE_FOLKS_UPDATE_INTERVAL) {
            lastHireableUpdateTime = currentTime;
            NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
        }
        // =============== 白天逻辑处理 ===============
        if (ModSimLoader.isDayTime(world)) {
            // 新的一天初始化
            if (!newDay) {
                newDay = true;
                handleNewDay(world);
            }
            if(System.currentTimeMillis() - npcSpawnCheckTimer > NPC_SPAWN_CHECK_INTERVAL){
                checkAndSpawnNewNpc(world);
                npcSpawnCheckTimer = System.currentTimeMillis(); // 重置计时器
            }
            // 收租逻辑 (使用缓存值)
            if (currentTime - rentalsTimer > RENT_COLLECTION_DELAY * 50 && newDayRentals) {
                newDayRentals = false;
                collectRent(world);
            }

            // 检查饥饿和年龄 (分批处理)
            checkNpcStatus(world, startIndex, endIndex);
        }
        // 黑夜逻辑
        else if (newDay) {
            newDay = false;
        }
        // =============== 环境控制 ===============
        if (ConfigLoader.configStopRain && world.isRaining() && world.getWorldInfo().getRainTime() > 1) {
            world.getWorldInfo().setRaining(false);
            ModSimLoader.log.info("停止下雨");
            ModSimLoader.sendChat(new TextComponentTranslation("chat.sim.xiayu", new Object[0]).getUnformattedText());
        }*/


        //第一版

        long startTime = System.currentTimeMillis();
        if (world != null) {
            if (startTime - timeSinceLastClientUpdates > 5000L || ModSimLoader.sim_is_running) {
                //当前世界有玩家
                if (world.playerEntities.size() > 0) {
                    ModSimLoader.sim_is_running = true;
                    //实时更新人的状态
                    for (NpcData f : ModSimLoader.folks) {
                        f.onUpdate();
                    }
                    //每1秒更新一次检查
                    if (System.currentTimeMillis() - timeSinceLastClientUpdate > 1000L) {
                        timeSinceLastClientUpdate = System.currentTimeMillis();
                        //可雇佣的人
                        NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
                        //更新资金
                        NetWorkLoader.net.sendToAll(new PacketUpdateMoney());

                        if (ModSimLoader.gamemode != 999) {
                            //是白天
                            if (ModSimLoader.isDayTime(world)) {
                                //60秒循环
                                if (System.currentTimeMillis() - minuteTimer > 60000L) {
                                    //已重生
                                    boolean spawnNew = true;
                                    for (NpcData starve : ModSimLoader.folks) {
                                        //判断是否已死亡
                                        if (starve.isDead || starve.entity == null) {
                                            //若有房子异常房子
                                            if (starve.home != null) {
                                                starve.home.occupants.remove(starve);
                                                starve.home = null;
                                            }
                                        } else if (starve.home == null) {
                                            spawnNew = false;
                                            //未成年不算
                                            if (starve.home != null && starve.race != null && starve.age > starve.race.maturity) {
                                                //只要有一个人没有住到房子里就不生成新的人
                                                spawnNew = true;
                                            }
                                        }
                                    }
                                    if (spawnNew) {
                                        ModSimLoader.log.info("所有人都有住宅，开始生成新的NPC");
                                        new NpcData(world, false);
                                    }
                                    minuteTimer = System.currentTimeMillis();
                                }
                                if (!newDay) {
                                    ModSimLoader.log.info("天亮了");
                                    //播放 天亮了鸡叫
                                    SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":rooster"));
//                                    for (EntityPlayer entityPlayer : world.playerEntities) {
//                                        BlockPos pos=entityPlayer.getPosition();
//                                        ModSimLoader.log.info("播放 天亮了鸡叫:[x:" + pos.getX() + "],y:[" + pos.getY() + "],z:[" + pos.getZ() + "]");
////                                        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
//                                        world.playSound( pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F,true);
//                                    }
                                    for (EntityPlayer entityPlayer : world.playerEntities) {
                                        // 使用玩家的精确坐标（而非方块坐标）
                                        double x = entityPlayer.posX;
                                        double y = entityPlayer.posY;
                                        double z = entityPlayer.posZ;
                                        // 调整声音类别为BLOCKS（方块音效，更符合场景）
                                        world.playSound(
                                                x, y, z,
                                                soundEvent,
                                                SoundCategory.BLOCKS,  // 更适合短音效
                                                1.0F,  // 音量（0.0-1.0）
                                                1.0F,  // 音调（0.5-2.0）
                                                true   // 距离衰减（true=随距离减小音量）
                                        );
                                    }


                                    newDay = true;
                                    if (ModSimLoader.dayOfWeek >= 6) {
                                        ModSimLoader.dayOfWeek = 0;
                                    } else {
                                        ++ModSimLoader.dayOfWeek;
                                    }
                                    ++ModSimLoader.gameDay;
                                    rentalsTimer = System.currentTimeMillis();
                                    newDayRentals = true;

                                }
                                if (System.currentTimeMillis() - rentalsTimer > 3000L && newDayRentals) {
                                    newDayRentals = false;
                                    ModSimLoader.log.info("收租了");
                                    if (ModSimLoader.gamemode != 999) {
                                        float rent = 0.0F;
                                        for (Building b : ModSimLoader.buildings) {
                                            if (b.occupants.size() > 0) {
                                                rent += b.rent;
                                            }
                                        }

                                        ModSimLoader.addMoney(rent);
                                        float rents = rent;
                                        //播放钱到账
                                        SoundEvent cashSound = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                                        /*for (EntityPlayer entityPlayer : world.playerEntities) {
                                            BlockPos pos=entityPlayer.getPosition();
                                            ModSimLoader.log.info("播放钱到账:[x:" + pos.getX() + "],y:[" + pos.getY() + "],z:[" + pos.getZ() + "]");
//                                            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                            world.playSound( pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F,true);
                                        }*/
                                        for (EntityPlayer entityPlayer : world.playerEntities) {
                                            double x = entityPlayer.posX;
                                            double y = entityPlayer.posY;
                                            double z = entityPlayer.posZ;
                                            world.playSound(
                                                    x, y, z,
                                                    cashSound,
                                                    SoundCategory.PLAYERS,  // 与玩家交互相关的音效
                                                    1.0F,
                                                    1.0F,
                                                    true
                                            );
                                        }
                                        //你今天收了 今天的租金。
                                        ModSimLoader.sendChat(new TextComponentTranslation("container.sim.main_Collected", new Object[0]).getUnformattedText() + ModSimLoader.displayMoney(rents) + new TextComponentTranslation("container.sim.main_rent_today", new Object[0]).getUnformattedText());
                                        NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
                                    }
                                    String hungerName = "";
                                    for (NpcData f : ModSimLoader.folks) {
                                        if (f.entity != null) {
                                            if (f.hunger > 0) {
                                                --f.hunger;
                                            } else if (f.hunger < 0) {
                                                //设置死亡 饿死
                                                f.entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
                                            } else {
                                                hungerName += f.getName() + ",";
                                            }

                                        }

                                        //交配欲望重置
                                        f.matingStage = -1.0F;
                                        //妊娠期
                                        if (f.pregnancyStage > 0.0F) {
                                            f.pregnancyStage += 0.1F;
                                        }
                                        //当前年龄
                                        int currentAge = f.age;
                                        //年龄增长
                                        if (f.age >= f.race.maturity) {
                                            if (ModSimLoader.dayOfWeek == 6) {
                                                ++f.age;
                                            }
                                        } else if (ModSimLoader.dayOfWeek == 3 || ModSimLoader.dayOfWeek == 6) {
                                            ++f.age;
                                        }

                                        if (currentAge < f.race.maturity && f.age >= f.race.maturity) {
                                            f.evict();
                                            //现在18岁了,他们会开始找房子,你现在也可以雇佣他们了。
                                            String s = new TextComponentTranslation("container.sim.main_is_now", new Object[0]).getUnformattedText();
                                            ModSimLoader.sendChat(f.getName() + s);
                                        }
                                        //超越寿命
                                        if (f.age >= f.race.lifespan && new Random().nextInt(10) == 5) {
                                            //年纪大了,感觉不太好。。。哦不！
                                            String s = new TextComponentTranslation("container.sim.main_is_old", new Object[0]).getUnformattedText();
                                            ModSimLoader.sendChat(f.getName() + s);
                                            //来自伤害999，死亡
                                            f.entity.attackEntityFrom(DamageSource.STARVE, 999.0F);
                                        }
                                    }

                                    if (hungerName != "") {
                                        //快饿死了！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                        String starving = new TextComponentTranslation("container.sim.main_is_VERY", new Object[0]).getUnformattedText();
                                        //其他人正在挨饿！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                        String others_starving = new TextComponentTranslation("container.sim.others_starving", new Object[0]).getUnformattedText();
                                        String message = hungerName + starving;
                                        ModSimLoader.sendChat(message);
                                    }


                                }

                            } else if (newDay) {
                                newDay = false;
                            }
                        }

                        //停止下雨MOD-在我的世界里一直下雨的时候实现了这个！
                        if (world != null) {
                            if (world.isRaining() && world.getWorldInfo().getRainTime() > 1 && ConfigLoader.configStopRain) {
                                world.getWorldInfo().setRaining(false);
                                ModSimLoader.log.info("我讨厌下雨-停了吧");
                                ModSimLoader.sendChat(new TextComponentTranslation("chat.sim.xiayu", new Object[0]).getUnformattedText());
                            }
                        }
                    }
                }

            }
        }


    }
    // 异步处理NPC状态
    private static void processNpcsAsync(World world) {
//        asyncExecutor.submit(() -> {
            try {
                Map<Integer, NpcData> updatedNpcs = new HashMap<>();
                /*List<NpcData> folks = new ArrayList<>(ModSimLoader.folks);


                // 分批处理NPC (每批10个)
                final int batchSize = 10;
                final int totalBatches = (int) Math.ceil(folks.size() / (double) batchSize);
//                System.out.println("异步处理NPC状态-开始");
                for (int batch = 0; batch < totalBatches; batch++) {
                    final int startIdx = batch * batchSize;
                    final int endIdx = Math.min(startIdx + batchSize, folks.size());

                    // 提交每个批次作为独立任务
//                    taskQueue.put(() -> {
                        for (int i = startIdx; i < endIdx; i++) {
                            NpcData npc = folks.get(i);
                            if (npc != null && npc.entity != null && !npc.entity.isDead) {
                                // 处理NPC状态
                                processSingleNpc(npc);
                                updatedNpcs.put(npc.entity.getEntityId(), npc);
                            }
                        }
//                    });
                }*/

                for (NpcData npc:ModSimLoader.folks){
                    if (npc != null && npc.entity != null && !npc.entity.isDead) {
                        // 处理NPC状态
                        processSingleNpc(npc);
                        updatedNpcs.put(npc.entity.getEntityId(), npc);
                    }
                }


                // 将更新后的NPC数据提交到主线程应用
//                scheduleMainThreadTask(() -> {
                    pendingNpcUpdates.putAll(updatedNpcs);
                    // 清理无效NPC
                    cleanupInvalidNpcs(world);
//                });

            } catch (Exception e) {
                ModSimLoader.log.error("Error processing NPCs asynchronously", e);
            } finally {
                // 标记处理完成
                isProcessing = false;
            }
//        });
    }
    // 处理单个NPC
    private static void processSingleNpc(NpcData npc) {
        // 处理饥饿度
        if (npc.hunger > 0) {
            --npc.hunger;
        } else if (npc.hunger < 0) {
            npc.entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

        // 繁殖相关
        npc.matingStage = -1.0F;

        // 妊娠期
        if (npc.pregnancyStage > 0.0F) {
            npc.pregnancyStage += 0.1F;
        }

        // 年龄增长
        int currentAge = npc.age;
        boolean ageIncreased = false;

        if (npc.age >= npc.race.maturity) {
            if (ModSimLoader.dayOfWeek == 6) {
                ++npc.age;
                ageIncreased = true;
            }
        } else if (ModSimLoader.dayOfWeek == 3 || ModSimLoader.dayOfWeek == 6) {
            ++npc.age;
            ageIncreased = true;
        }

        // 成年处理
        if (ageIncreased && currentAge < npc.race.maturity && npc.age >= npc.race.maturity) {
            npc.evict();
            String message = new TextComponentTranslation("container.sim.main_is_now", new Object[0]).getUnformattedText();
            scheduleMainThreadTask(() -> ModSimLoader.sendChat(npc.getName() + message));
        }

        // 寿命处理
        if (npc.age >= npc.race.lifespan && new Random().nextInt(10) == 5) {
            String message = new TextComponentTranslation("container.sim.main_is_old", new Object[0]).getUnformattedText();
            scheduleMainThreadTask(() -> {
                ModSimLoader.sendChat(npc.getName() + message);
                npc.entity.attackEntityFrom(DamageSource.STARVE, 999.0F);
            });
        }
    }
    // 异步处理建筑和租金
    private static void processBuildingsAsync(World world) {
//        asyncExecutor.submit(() -> {
            try {
                List<Building> buildings = new ArrayList<>(ModSimLoader.buildings);
                Map<String, Building> updatedBuildings = new HashMap<>();
                float totalRent = 0.0F;

                // 分批处理建筑
                for (Building building : buildings) {
                    if (building != null && building.occupants.size() > 0) {
                        // 计算租金
                        totalRent += building.rent;
                        updatedBuildings.put(building.ID.toString(), building);
                    }
                }

                // 将结果提交到主线程
                final float finalRent = totalRent;
//                scheduleMainThreadTask(() -> {
                    pendingBuildingUpdates.putAll(updatedBuildings);

                    // 处理租金收集逻辑
                    if (ModSimLoader.isDayTime(world) &&
                            System.currentTimeMillis() - rentalsTimer > 3000L &&
                            newDayRentals) {
                        collectRent(world, finalRent);
                    }
//                });

            } catch (Exception e) {
                ModSimLoader.log.error("Error processing buildings asynchronously", e);
            } finally {
                // 标记处理完成
                isProcessing = false;
            }
//        });
    }

    // 收租逻辑（在主线程执行）
    private static void collectRent(World world, float rent) {
        ModSimLoader.log.info("收租了: " + rent);

        if (ModSimLoader.gamemode != 0) {
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
        } else {
            if (world instanceof WorldServer) {
                ((WorldServer) world).addScheduledTask(() -> {
                    if (!world.playerEntities.isEmpty()) {
                        EntityPlayer player = world.playerEntities.get(0);
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                        world.playSound(null, player.posX, player.posY, player.posZ,
                                soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);

                        ModSimLoader.sendChat(new TextComponentTranslation("container.sim.main_Collected", new Object[0]).getUnformattedText() +
                                ModSimLoader.displayMoney(rent) +
                                new TextComponentTranslation("container.sim.main_rent_today", new Object[0]).getUnformattedText());
                    }
                });
            }
        }

        // 更新租金状态
        newDayRentals = false;
        rentalsTimer = System.currentTimeMillis();
    }
    // 异步处理环境控制
    private static void processEnvironmentAsync(World world) {
//        asyncExecutor.submit(() -> {
            try {
                // 检查天气
                if (ConfigLoader.configStopRain && world.isRaining() && world.getWorldInfo().getRainTime() > 1) {
                    // 在主线程执行天气修改
//                    scheduleMainThreadTask(() -> {
                        world.getWorldInfo().setRaining(false);
                        ModSimLoader.log.info("停止下雨");
                        ModSimLoader.sendChat(new TextComponentTranslation("chat.sim.xiayu", new Object[0]).getUnformattedText());
//                    });
                }

                // 处理昼夜循环
                handleDayNightCycle(world);

            } catch (Exception e) {
                ModSimLoader.log.error("Error processing environment asynchronously", e);
            } finally {
                // 标记处理完成
                isProcessing = false;
            }
//        });
    }

    // 处理昼夜循环
    private static void handleDayNightCycle(World world) {
        if (ModSimLoader.isDayTime(world)) {
            // 新的一天初始化
            if (!newDay) {
//                scheduleMainThreadTask(() -> {
                    newDay = true;
                    handleNewDay(world);
//                });
            }
        } else if (newDay) {
            newDay = false;
        }
    }

    // 处理新的一天开始
    private static void handleNewDay(World world) {
        ModSimLoader.log.info("天亮了");

        // 播放鸡叫音效
        if (world instanceof WorldServer) {
            ((WorldServer) world).addScheduledTask(() -> {
                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":rooster"));
                for (EntityPlayer player : world.playerEntities) {
                    world.playSound(null, player.posX, player.posY, player.posZ,
                            soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                }
            });
        }

        // 更新星期和游戏天数
        if (ModSimLoader.dayOfWeek >= 6) {
            ModSimLoader.dayOfWeek = 0;
        } else {
            ++ModSimLoader.dayOfWeek;
        }
        ++ModSimLoader.gameDay;

        // 重置收租标记
        newDayRentals = true;
        rentalsTimer = System.currentTimeMillis();
    }

    // 清理无效NPC
    private static void cleanupInvalidNpcs(World world) {
        Iterator<NpcData> iterator = ModSimLoader.folks.iterator();
        while (iterator.hasNext()) {
            NpcData npc = iterator.next();
            if (npc.entity == null || npc.entity.isDead) {
                // 清理住宅关联
                if (npc.home != null) {
                    npc.home.occupants.remove(npc);
                }
                iterator.remove();
            }
        }
    }

    // 调度网络同步任务
    private static void scheduleNetworkSync(World world) {
        if (world instanceof WorldServer) {
//            ((WorldServer) world).addScheduledTask(() -> {
                try {
                    // 发送NPC更新
                    for (NpcData npc : pendingNpcUpdates.values()) {
                        NetWorkLoader.net.sendToAll(new PacketSyncNpcData(npc));
                    }
                    pendingNpcUpdates.clear();

                    // 发送建筑更新
                    for (Building building : pendingBuildingUpdates.values()) {
//                        NetWorkLoader.net.sendToAll(new PacketSyncBuilding(building));
                    }
                    pendingBuildingUpdates.clear();

                    // 每1秒更新一次可雇佣的人和资金
                    if (tickCounter % 20 == 0) {
                        NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
                        NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
                    }

                } catch (Exception e) {
                    ModSimLoader.log.error("同步网络数据时出错", e);
                }
//            });
        }
    }

    // 安全调度主线程任务
    private static void scheduleMainThreadTask(Runnable task) {
        // 1. 判断当前环境是客户端还是服务器
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            // 服务器端：直接获取服务器实例
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server != null && !server.isCallingFromMinecraftThread()) {
                server.addScheduledTask(task);
            } else {
                task.run(); // 已经在主线程
            }
        } else {
            // 客户端：使用 Minecraft 实例的主线程调度器
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
            mc.addScheduledTask(task);
        }
    }


    // 优雅关闭线程池
    /*public static void shutdown() {
        asyncExecutor.shutdown();
        try {
            if (!asyncExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            asyncExecutor.shutdownNow();
        }
    }*/



    // 补充：检查是否可以生成新NPC（核心逻辑）
    private static void checkAndSpawnNewNpc(World world) {
        boolean spawnNew = true; // 默认允许生成新NPC

        // 遍历所有NPC，检查是否有成年NPC无住宅
        List<NpcData> folks = new ArrayList<>(ModSimLoader.folks);
        for (NpcData npc : folks) {
            // 处理已死亡的NPC（清理住宅关联，避免内存泄漏）
            if (npc.isDead || npc.entity == null) {
                if (npc.home != null) {
                    npc.home.occupants.remove(npc);
                    npc.home = null;
                }
                continue;
            }

            // 核心判断：成年NPC无住宅 → 禁止生成新NPC
            if (npc.home == null) { // 无住宅
                // 过滤未成年NPC（未成年无住宅不影响）
                if (npc.race != null && npc.age >= npc.race.maturity) { // 已成年
                    spawnNew = false; // 发现成年NPC无住宅，标记为不可生成
                    ModSimLoader.log.info("NPC " + npc.getName() + " 已成年但无住宅，停止生成新NPC");
                    break; // 找到一个即可，无需继续遍历
                }
            }
        }

        // 只有所有成年NPC都有住宅时，才生成新NPC
        if (spawnNew) {
            ModSimLoader.log.info("所有成年NPC均有住宅，生成新NPC");
            new NpcData(world, false); // 生成新NPC
        }
    }

    public static void simModDisconnected() {
        World world = Minecraft.getMinecraft().world;
        //保存一下数据
        simModSave(world);
        hasLoadedWorld = false;
    }

    /**
     * 客户端专属更新（渲染、动画、本地状态）
     * @param world
     */
    public static void clientSimModupdate(World world) {
        // 客户端必需的逻辑：
        // - NPC渲染器初始化
        // - 接收服务器同步的NPC数据并更新本地缓存
        // - 动画播放（如挥动手臂）、HUD显示等
        // 1. 检查是否需要整体更新（客户端控制更新频率）
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClientUpdateTime < CLIENT_UPDATE_INTERVAL) {
            return;
        }
        lastClientUpdateTime = currentTime;

        // 2. 客户端渲染与状态更新
        updateLocalNpcRendering(world);  // 更新本地NPC渲染（位置、动画）
        displayClientEffects(world);     // 显示客户端特效（粒子、音效）
        updateHudInfo(world);            // 更新HUD信息（NPC状态显示）
    }

    /**
     *  服务器端专属更新（实体创建、数据同步、AI）
     * @param world
     */
    public static void serverSimModupdate(World world) {
        // 原simModupdate中的服务器逻辑：
        // - NPC生成、状态更新（年龄、饥饿等）
        // - 网络包发送（向客户端同步NPC数据）
        // - 经济系统、收租等核心逻辑

        // 1. 检查是否需要整体更新（服务器端控制更新频率）
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastServerUpdateTime < SERVER_UPDATE_INTERVAL) {
            return;
        }
        lastServerUpdateTime = currentTime;

        // 2. 确保世界和玩家存在
        if (world == null || world.playerEntities.isEmpty()) {
            return;
        }

        // 3. 服务器端核心逻辑
        // 服务器端核心逻辑
        manageNpcEntities(world);        // 管理NPC实体（生成、删除）
        updateNpcStatusInBatches(world); // 分批更新NPC状态
        handleDayNightCycle(world);      // 处理昼夜循环
//        manageEconomySystem(world);      // 经济系统（收租等）
        handleEnvironmentControl(world); // 环境控制（停止下雨）

    }

    // 服务器端：管理NPC实体（生成、删除）
    private static void manageNpcEntities(World world) {
        // 1. 检查并清理无效NPC
        cleanupInvalidNpcs(world);

        // 2. 检查是否需要生成新NPC（原逻辑）
        if (System.currentTimeMillis() - npcSpawnCheckTimer > NPC_SPAWN_CHECK_INTERVAL) {
            checkAndSpawnNewNpc(world);
            npcSpawnCheckTimer = System.currentTimeMillis();
        }

        // 3. 同步NPC数据到客户端（通过网络包）
        sendNpcDataToClients(world);
    }

    // 服务器端：分批更新NPC状态
    private static void updateNpcStatusInBatches(World world) {
        List<NpcData> folks = new ArrayList<>(ModSimLoader.folks);
        int totalBatches = Math.max(1, (int) Math.ceil(folks.size() / (double) NPCS_PER_UPDATE));

        // 计算当前批次的起止索引
        int startIndex = currentNpcBatch * NPCS_PER_UPDATE;
        int endIndex = Math.min(startIndex + NPCS_PER_UPDATE, folks.size());

        // 处理当前批次的NPC状态
        for (int i = startIndex; i < endIndex; i++) {
            NpcData f = folks.get(i);
            if (f != null && f.entity != null && !f.entity.isDead) {
                updateNpcStatus(f, world); // 更新NPC饥饿、年龄等状态
            }
        }

        // 更新批次索引
        currentNpcBatch = (currentNpcBatch + 1) % totalBatches;
    }

    // 服务器端：更新单个NPC状态
    private static void updateNpcStatus(NpcData npc, World world) {
        // 处理饥饿度、年龄、繁殖等核心逻辑
        // （与原代码逻辑一致，略）
        // 处理饥饿度
        if (npc.hunger > 0) {
            --npc.hunger;
        } else if (npc.hunger < 0) {
            npc.entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

        // 繁殖相关
        npc.matingStage = -1.0F;

        // 妊娠期
        if (npc.pregnancyStage > 0.0F) {
            npc.pregnancyStage += 0.1F;
        }

        // 年龄增长
        int currentAge = npc.age;
        boolean ageIncreased = false;

        if (npc.age >= npc.race.maturity) {
            if (ModSimLoader.dayOfWeek == 6) {
                ++npc.age;
                ageIncreased = true;
            }
        } else if (ModSimLoader.dayOfWeek == 3 || ModSimLoader.dayOfWeek == 6) {
            ++npc.age;
            ageIncreased = true;
        }

        // 成年处理
        if (ageIncreased && currentAge < npc.race.maturity && npc.age >= npc.race.maturity) {
            npc.evict();
            String message = new TextComponentTranslation("container.sim.main_is_now", new Object[0]).getUnformattedText();
            ModSimLoader.sendChat(npc.getName() + message);
        }

        // 寿命处理
        if (npc.age >= npc.race.lifespan && new Random().nextInt(10) == 5) {
            String message = new TextComponentTranslation("container.sim.main_is_old", new Object[0]).getUnformattedText();
            ModSimLoader.sendChat(npc.getName() + message);
            npc.entity.attackEntityFrom(DamageSource.STARVE, 999.0F);
        }
    }


    // 服务器端：发送NPC数据到客户端
    private static void sendNpcDataToClients(World world) {
        // 1. 筛选需要同步的NPC（如在玩家视野内）
        // 筛选需要同步的NPC（在玩家视野内）
        /*List<NpcData> npcsToSync = new ArrayList<>();
        double syncRange = 64.0; // 同步范围
        for (EntityPlayer player : world.playerEntities) {
            for (NpcData npc : ModSimLoader.folks) {
                if (npc.entity != null && npc.entity.getDistance(player) <= syncRange) {
                    npcsToSync.add(npc);
                }
            }
        }
        // 2. 通过网络包发送NPC数据到客户端
        for (NpcData npc : npcsToSync) {
            NetWorkLoader.net.sendToAll(new PacketSyncNpcData(npc));
        }*/

        if (world instanceof WorldServer && !((WorldServer) world).isCallingFromMinecraftThread()) {
            ((WorldServer) world).addScheduledTask(() -> {
                // 限流：只发送可见范围内的NPC
                List<NpcData> visibleNpcs = getVisibleNpcs(world);
                for (NpcData npc : visibleNpcs) {
                    NetWorkLoader.net.sendToAll(new PacketSyncNpcData(npc));
                }
            });
        } else {
            // 直接执行（如果已经在主线程）
            List<NpcData> visibleNpcs = getVisibleNpcs(world);
            for (NpcData npc : visibleNpcs) {
                NetWorkLoader.net.sendToAll(new PacketSyncNpcData(npc));
            }
        }
    }
    // 获取可见范围内的NPC（优化性能）
    private static List<NpcData> getVisibleNpcs(World world) {
        List<NpcData> visibleNpcs = new ArrayList<>();
        double renderDistance = world.provider.getDimension() == 0 ? 32.0 : 16.0; // 根据维度调整

        for (EntityPlayer player : world.playerEntities) {
            for (NpcData npc : ModSimLoader.folks) {
                if (npc.entity != null && npc.entity.getDistance(player) <= renderDistance) {
                    visibleNpcs.add(npc);
                }
            }
        }

        return visibleNpcs;
    }
    // 服务器端：环境控制（停止下雨）
    private static void handleEnvironmentControl(World world) {
        if (ConfigLoader.configStopRain && world.isRaining() && world.getWorldInfo().getRainTime() > 1) {
            world.getWorldInfo().setRaining(false);
            ModSimLoader.log.info("停止下雨");
            ModSimLoader.sendChat(new TextComponentTranslation("chat.sim.xiayu", new Object[0]).getUnformattedText());
        }
    }
    // 客户端：更新本地NPC渲染
    private static void updateLocalNpcRendering(World world) {
        // 获取本地缓存的NPC数据
        List<NpcData> localNpcs = new ArrayList<>(clientNpcCache.values());

        // 更新每个NPC的渲染状态
        for (NpcData npc : localNpcs) {
            if (npc != null && npc.entity != null && npc.entity.isEntityAlive()) {
                updateNpcRenderProperties(npc);
            }
        }
    }
    // 客户端：更新NPC渲染属性
    private static void updateNpcRenderProperties(NpcData npc) {
        // 更新NPC位置和姿态
        updateNpcPosition(npc);

        // 更新NPC动画（如挥动手臂）
        updateNpcAnimation(npc);

        // 更新NPC显示名称和状态
        updateNpcDisplayName(npc);
    }
    // 客户端：更新NPC位置
    private static void updateNpcPosition(NpcData npc) {
        // 从网络包数据更新NPC位置
        // 示例：entity.setPosition(npc.posX, npc.posY, npc.posZ);
    }

    // 客户端：更新NPC动画
    private static void updateNpcAnimation(NpcData npc) {
        // 示例：根据NPC状态设置动画
//        if (npc.isSwingingArm) {
            // 设置挥动手臂动画
//        }
    }

    // 客户端：更新NPC显示名称
    private static void updateNpcDisplayName(NpcData npc) {
        // 示例：entity.setCustomNameTag(npc.getName() + " - " + npc.getStatus());
    }
    // 客户端：显示特效（粒子、音效）
    private static void displayClientEffects(World world) {
        // 显示NPC相关粒子效果
        spawnNpcParticles(world);

        // 播放NPC相关音效
        playNpcSounds(world);
    }

    // 客户端：生成NPC粒子效果
    private static void spawnNpcParticles(World world) {
        // 示例：在NPC周围生成粒子
        // world.spawnParticle(EnumParticleTypes.HEART, x, y, z, 0, 0, 0);
    }

    // 客户端：播放NPC音效
    private static void playNpcSounds(World world) {
        // 示例：播放NPC语音
        // world.playSound(x, y, z, soundEvent, category, volume, pitch, false);
    }
    // 客户端：更新HUD信息
    private static void updateHudInfo(World world) {
        // 更新NPC状态显示（如饥饿度、年龄）
        updateNpcStatusHud(world);

        // 更新NPC相关提示信息
        updateNpcTips(world);
    }

    // 客户端：更新NPC状态HUD
    private static void updateNpcStatusHud(World world) {
        // 示例：在屏幕上显示NPC状态
        // 这通常在渲染HUD时处理
    }

    // 客户端：更新NPC提示信息
    private static void updateNpcTips(World world) {
        // 示例：显示NPC相关提示
        // 如"点击NPC交谈"、"NPC需要食物"等
    }

    // 客户端：更新本地NPC缓存
    public static void updateClientNpcCache(String entityId, NpcData npcData) {
        clientNpcCache.put(entityId, npcData);
    }
}
