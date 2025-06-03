package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.network.client.PacketUpdateNPC;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.block.FarmBox;
import com.trhsy.sim.npcCode.block.MineBox;
import com.trhsy.sim.npcCode.build.Building;
import com.trhsy.sim.util.Courier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.util.Random;
import java.util.UUID;
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
    public static boolean hasLoadedWorld;
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

    /**
     * 加载模组
     */
    public static void simModLoad(World world) {
        ModSimLoader.log.info("检查是否应该加载人员");
        if (world != null) {
            if (world.isRemote) {
                ModSimLoader.log.info("世界遥远，正在取消");
            } else if (hasLoadedWorld) {
                ModSimLoader.log.info("世界尚未加载，正在取消");
            } else {
                ModSimLoader.log.info("清除旧的世界数据");
                ModSimLoader.folks.clear();
                ModSimLoader.farms.clear();
                ModSimLoader.mines.clear();
                ModSimLoader.buildings.clear();
                ModSimLoader.dayOfWeek = 0;
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
                        NpcData npcData = new NpcData(world, UUID.fromString(buildingFile.getName().split(".sk2")[0]));
                        if (!npcData.isDead) {
                            ModSimLoader.folks.add(npcData);
                            NetWorkLoader.net.sendToAll(new PacketUpdateNPC());
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
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
        long startTime = System.currentTimeMillis();
//                    World world = Minecraft.getMinecraft().world;

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
                        //检查建筑物
            /*for (int i = ModSimLoader.buildings.size(); i > 0; --i) {
                Building b = ModSimLoader.buildings.get(i - 1);
                BlockPos pos = new BlockPos(b.controlXYZ.x, b.controlXYZ.y, b.controlXYZ.z);
                Block block = event.world.getBlockState(pos).getBlock();
                //ModSimLoader.log.info("建筑物："+b.buildingName + "的控制箱在"+pos.toString()+"，识别到的方块名字："+block.getUnlocalizedName());
                if (block != BlockLoader.blockControlBox) {
//                    ModSimLoader.log.info(b.buildingName + " 没有控制块-正在销毁");
                    //b.demolish(event.world, false);
                }
            }*/
                    }

                    //检查游戏状态 && event.world.playerEntities.size() > 0

                    if (ModSimLoader.gamemode != 999) {
                        //是白天
                        if (ModSimLoader.isDayTime(world)) {
                            //60秒循环
                            if (System.currentTimeMillis() - minuteTimer > 60000L) {
                                //已重生
                                boolean spawnNew = true;
                                for (NpcData starve : ModSimLoader.folks) {
                                    //if (starve.entity == null) {
                                    //    NpcData npcData = new NpcData(event.world, UUID.fromString(starve.ID));
                                    //    //starve.loadFolk(event.world, UUID.fromString(starve.ID));
                                    //}
                                    //判断是否已死亡
                                    if (starve.isDead || starve.entity == null) {
                                        //若有房子异常房子
                                        if (starve.home != null) {
                                            starve.home.occupants.remove(starve);
                                            starve.home = null;
                                        }
                                        //starve.onDeath(DamageSource.GENERIC);
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
//                                NetWorkLoader.net.sendToServer(new PacketNewFolk(false));
                                }
                                minuteTimer = System.currentTimeMillis();
                            }
                            if (!newDay) {
                                ModSimLoader.log.info("天亮了");
                                //播放 天亮了鸡叫
                                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":rooster"));
                                EntityPlayer entityPlayer=world.playerEntities.get(0);
                                world.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F,true);


                                newDay = true;
                                if (ModSimLoader.dayOfWeek >= 6) {
                                    ModSimLoader.dayOfWeek = 0;
                                } else {
                                    ++ModSimLoader.dayOfWeek;
                                }
                                rentalsTimer = System.currentTimeMillis();
                                newDayRentals = true;

                            }
                            if (System.currentTimeMillis() - rentalsTimer > 3000L && newDayRentals) {
                                newDayRentals = false;
                                ModSimLoader.log.info("收租了");
                                if (ModSimLoader.gamemode != 0) {
                                    NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
                                } else {
                                    float rent = 0.0F;
                                    for (Building b : ModSimLoader.buildings) {
                                        if (b.occupants.size() > 0) {
                                            rent += b.rent;
                                        }
                                    }

                                    ModSimLoader.addMoney(rent);
                                    //播放钱到账
                                    SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                                    EntityPlayer entityPlayer=world.playerEntities.get(0);
                                    world.playSound( entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F,true);
                                    //你今天收了 今天的租金。
                                    ModSimLoader.sendChat(new TextComponentTranslation("container.sim.main_Collected", new Object[0]).getUnformattedText() + ModSimLoader.displayMoney(rent) + new TextComponentTranslation("container.sim.main_rent_today", new Object[0]).getUnformattedText());

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
                           /* Iterator iterator = ModSimLoader.folks.iterator();

                            NpcData f = null;
                            fs_lable:
                            while (true) {
                                do {
                                    if (!iterator.hasNext()) {

                                        //饥饿计数
                                        int hungerCount = 0;
                                        NpcData starve = null;
                                        Iterator iterator1 = ModSimLoader.folks.iterator();
                                        while (iterator1.hasNext()) {
                                            f = (NpcData) iterator1.next();
                                            if (f.hunger > 0) {
                                                --f.hunger;
                                            } else if (this.rand.nextInt(4) == 3) {
                                                starve = f;
                                            } else {
                                                hungerName += f.getName() + ",";
                                                ++hungerCount;
                                            }
                                            if (hungerName != "") {
                                                //快饿死了！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                                String starving = new TextComponentTranslation("container.sim.main_is_VERY", new Object[0]).getUnformattedText();
                                                //其他人正在挨饿！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                                String others_starving = new TextComponentTranslation("container.sim.others_starving", new Object[0]).getUnformattedText();
                                                String message = hungerCount > 1 ? hungerName + starving : hungerName + new TextComponentTranslation("container.sim.Mining13", new Object[0]).getUnformattedText() + others_starving;
                                                ModSimLoader.sendChat(message);
                                            }
                                        }
                                        if (starve != null) {
                                            //设置死亡
                                            starve.entity.attackEntityFrom(DamageSource.STARVE, 999.0F);
                                        }
                                        break fs_lable;
                                    }
                                    f = (NpcData) iterator.next();
                                } while (f.entity == null);

                            }*/
                            }
                            //}
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
                        /*try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
        }
//                }
//            }
//        });

    }

    public static void simModDisconnected() {
        World world = Minecraft.getMinecraft().world;
        //保存一下数据
        simModSave(world);
        hasLoadedWorld = false;
//        executorService.shutdown();
    }
}
