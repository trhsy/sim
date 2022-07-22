package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.block.BlockFarmingBox;
import com.trhsy.sim.common.block.BlockMiningBox;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.entity.functionality.MiningBox;
import com.trhsy.sim.common.gui.GuiRunMod;
import com.trhsy.sim.common.jobs.JobSoldier;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * 加载任务
 */
public class ModSimReloaded {
    /**
     * 日志
     **/
    public static Logger log;
    /*
    用于检测我们何时进入世界（非主菜单）以及玩家何时更改世界/地图
     */
    public static String currentSavePath = "";

    /*
    所有民众的数据（用于构建和维护 EntityFolk）
     */
    public static ArrayList<FolkData> theFolks = new ArrayList();
    /*
   所有的建筑对象
    */
    public static ArrayList<Building> theBuildings = new ArrayList();
    /*
    所有快递任务
     */
    public static ArrayList<CourierTask> theCourierTasks = new ArrayList();
    /*
    所有快递点
     */
    public static ArrayList<V3> theCourierPoints = new ArrayList();
    /*
    所有的采矿箱
     */
    public static ArrayList<MiningBox> theMiningBoxes = new ArrayList();
    /*
    所有养殖箱
     */
    public static ArrayList<FarmingBox> theFarmingBoxes = new ArrayList();
    /*
   所有情感关系
    */
    public static ArrayList<Relationship> theRelationships = new ArrayList();
    /*
    包含他们正在玩的这个关卡的所有游戏状态和设置
     */
    public static GameStates states = new GameStates();
    /*
    银行目前正在销售的商品列表，每天早上都会更新新商品
     */
    public static ArrayList<Commodity> theCommodities = new ArrayList();
    /*
    用于在update（）调用中升级作物农场
     */
    public static FarmingBox farmToUpgrade = null;
    /*
    升级作物农场计数
     */
    public static int farmToUpgradeCounter = 0;
    /*
    所有的农场升级点
     */
    private static ArrayList<V3> farmToUpgradePoints = null;
    /*
    白天
     */
    public static boolean isDay = true;
    /*
    所有的拆除
     */
    public static ArrayList<V3> demolishBlocks = new ArrayList();
    /*
    拆除
     */
    public static World demolishWorld = null;
    //Gui的运行模式
    private static GuiRunMod runModui = null;

    public ModSimReloaded() {

    }

    /**
     * 重置并加载新世界
     */
    public static void resetAndLoadNewWorld() {
        try {
            //获取有效的线程
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            ModSimReloaded.log.info("重置并加载世界 " + side.toString() + " SIDE");
            //建筑对象清除
            theBuildings.clear();
            //快递点清除
            theCourierPoints.clear();
            //快递任务清除
            theCourierTasks.clear();
            //采矿箱清除
            theMiningBoxes.clear();
            //农田箱清除
            theFarmingBoxes.clear();
            //NPC清除
            theFolks.clear();
            //情感关系清除
            theRelationships.clear();
            //读取游戏设置文件
            File f = new File(getSavesDataFolder() + "settings.sk2");
            if (!f.exists()) {
                f = new File(getSavesDataFolder() + "settings.suk");
            }
            //文件存在则读取
            if (f.exists()) {
                //重新从配置列表读取信息读取信息
                states.loadStates();
                try {
                    //设置游戏模式为读取到的模式
                    GameMode.setGameModeFromNumber(states.gameModeNumber);
                } catch (Exception var3) {
                    //设置错误
                    GameMode.setGameModeFromNumber(0);
                }
            } else {
                //不存在则更新
                states = new GameStates();
                states.saveStates();
            }
            //如果游戏状态为空
            if (states == null) {
                (new File(getSavesDataFolder() + "settings.sk2")).delete();
                states = new GameStates();
                states.saveStates();
                //你的SimCity设置文件已损坏，我必须重新创建一个
                String sim_settings = I18n.format("container.sim.sim_settings");
                sendChat(sim_settings);
            }
            //如果未运行模拟城市，弹出GUI页面
            if (states.gameModeNumber == -1) {
                if (ModSimReloaded.runModui == null) {
                    GuiRunMod runModui = new GuiRunMod();
                    Minecraft.getMinecraft().displayGuiScreen(runModui);
                }

            } else {
                if (states.gameModeNumber >= 0) {
                    ModSimReloaded.log.info("模拟城市程序已经运行");
                    ModSim.proxy.ranStartup = true;
                }
                //欢迎来到模拟城镇,由TRHSY重制，更多资讯请关注公众号: dasha5000
                String welcome = I18n.format("container.sim.welcome");
                String welcomes = I18n.format("container.sim.welcomes");
                sendChat(welcome + ModSim.VERSION + welcomes);
                //清空线程池中的所有npc
                theFolks.clear();
                //从磁盘加载所有建筑并初始化它们
                Building.initialiseAllBuildings();
                //加载世界上的建筑
                Building.loadAllBuildings();
                CourierTask.loadCourierTasksAndPoints();
                MiningBox.loadMiningBoxes();
                FarmingBox.loadFarmingBoxes();
                FolkData.loadAndSpawnFolks();
                Relationship.loadRelationships();
                //updateCheck();
                isDay = isDayTime();
                Building.checkTenants();
                ModSim.proxy.ranStartup = true;
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("resetAndLoadNewWorld出错了：" + e.getMessage());
        }
    }

    /**
     * 帮助功能，向所有世界/维度的所有玩家发送聊天信息
     *
     * @param theText
     */
    public static void sendChat(String theText) {
        try {
            WorldServer[] worldServers = MinecraftServer.getServer().worldServers;
            int length = worldServers.length;
            for (World w : MinecraftServer.getServer().worldServers) {
                if (!w.isRemote) {
                    for (int k = 0; k < w.playerEntities.size(); ++k) {
                        EntityPlayer p = (EntityPlayer) w.playerEntities.get(k);
                        p.addChatComponentMessage(new ChatComponentText(theText));
                    }
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("sendChat出错了：" + e.getMessage());
        }
    }

    /**
     * 以字符串形式获取“.minecraft/saves/CURRENTWORLD/sim/”文件夹 保存数据文件夹
     *
     * @return
     */
    public static String getSavesDataFolder() {
        String ret = "";
        try {
            String worldname = MinecraftServer.getServer().getFolderName();
            String strmc = (new File(".")).getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            File test = new File(strmc + "saves");
            if (test.exists()) {
                //客户端
                ret = (new File(strmc + File.separator + "saves" + File.separator + worldname + File.separator + "sim" + File.separator)).getAbsolutePath() + File.separator;
            } else {
                //服务器端
                strmc = strmc + worldname + File.separator + "sim" + File.separator;
                ret = (new File(strmc)).getAbsolutePath();
            }

            File f = new File(ret);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("getSavesDataFolder出错了：" + e.getMessage());
        }
        return ret;
    }

    /**
     * 获取模拟城市建筑文文件夹
     *
     * @return
     */
    public static String getSimukraftFolder() {
        try {
            String strmc = (new File(".")).getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            File checks = new File(strmc + File.separator + "mods" + File.separator + "sim");
            if (!checks.exists() && !checks.isDirectory()) {
                ModSimReloaded.log.warn("SimCity error - Mod未正确安装, ./minecraft/mods/sim/ 文件夹丢失了 - 重新创建此文件夹");
                checks.mkdir();
            }
            return (checks).getAbsolutePath();
        } catch (Exception var1) {
            return "";
        }
    }

    /**
     * 判断是否白天 当世界上是白天时返回true，忽略其他世界时间
     *
     * @return
     */
    public static boolean isDayTime() {
        //if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000 <= 11999) {
        //    return true;
        //} else {
        //    return false;
        //}
        boolean falg=false;
        try {
            falg=MinecraftServer.getServer().worldServers[0].isDaytime();
        } catch (Exception e) {
            ModSimReloaded.log.error("isDayTime出错了：" + e.getMessage());
        }
        return falg;

    }

    /**
     * display Money 显示金钱
     * 返回格式良好的货币值
     *
     * @param moneyin
     * @return
     */
    public static String displayMoney(float moneyin) {
        String output =null;
        try {
            DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
            output =myFormatter.format((double) moneyin);
        } catch (Exception e) {
            ModSimReloaded.log.error("displayMoney出错了：" + e.getMessage());
        }
        return output;
    }

    private String getTheirId() {
        return null;
    }

    public static void dayTransitionHandler() {
        try {
            if (isDayTime() && isDay == false) {
                //日转换
                isDay = true;
                //Night to day transition
                ModSimReloaded.log.info("天亮了");
                World world = ModSim.proxy.getClientWorld();
                if (world != null) {
                    EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                    if (p != null) {
                        ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":rooster", 1.0F, 1.0F, false);
                    }
                }

                states.dayOfWeek++;
                if (states.dayOfWeek > 6) {
                    states.dayOfWeek = 0;
                    int homeless = 0;
                    for (FolkData folk1 : theFolks) {
                        if (folk1.getHome() == null) {
                            homeless++;
                        }
                    }

                    if (homeless > 1) {
                        String sim_residential = I18n.format("container.sim.sim_residential");
                        String sim_residentials = I18n.format("container.sim.sim_residentials");
                        sendChat(sim_residential + homeless + sim_residentials);
                    }
                }

                evolveFolks();
                if (theFolks.size() > 1) {
                    Random rand = new Random();
                    int f1 = rand.nextInt(theFolks.size());

                    int f2 = f1;
                    while (f2 == f1) {
                        f2 = rand.nextInt(theFolks.size());
                    }
                    FolkData folk1 = (FolkData) theFolks.get(f1);
                    FolkData folk2 = (FolkData) theFolks.get(f2);
                    Relationship.meddleWithRelationship(folk1, folk2);
                }
            }

            if (!isDayTime() && isDay == true) {
                isDay = false;
                //Day to Night transition
                ModSimReloaded.log.info("渡过一晚");
                if (theFolks.size() > 1) {
                    Random rand = new Random();
                    int f1 = rand.nextInt(theFolks.size());
                    int f2 = f1;
                    while (f2 == f1) {
                        f2 = rand.nextInt(theFolks.size());
                    }

                    FolkData folk1 = (FolkData) theFolks.get(f1);
                    FolkData folk2 = (FolkData) theFolks.get(f2);
                    Relationship.meddleWithRelationship(folk1, folk2);
                }
                for (FolkData folk : theFolks) {
                    folk.destination = null;
                    if (folk.theEntity != null) {
                        folk.theEntity.getNavigator().clearPathEntity();
                    }
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("dayTransitionHandler出错了：" + e.getMessage());
        }


    }

    /**
     * npc 年龄增长
     */
    private static void evolveFolks() {
        try {
            if (theFolks.size() > 0) {
                Random rand = new Random();
                //evolving folks
                ModSimReloaded.log.info("进化的人");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000L);
                        } catch (Exception var5) {
                        }
                        //总租金
                        float totalRent = 0.0F;
                        //总税务
                        float totalCorpTax = 0.0F;
                        //如果游戏模式不是创造模式
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            //循环所有的建筑
                            for (int b = 0; b < ModSimReloaded.theBuildings.size(); b++) {
                                //获得建筑
                                Building building = (Building) ModSimReloaded.theBuildings.get(b);
                                //如果建筑是住宅并且 住宅租户大于0/有租户
                                if (building.type.contentEquals("residential") && building.tenants.size() > 0) {
                                    //建筑的租金是空或者0
                                    if (building.rent == null || building.rent == 0.0F) {
                                        //租金赋值为1
                                        building.rent = 1.0F;
                                    }

                                    ModSimReloaded.log.info("房屋租金 " + building.displayNameWithoutPK + ": " + building.rent + "(" + building.blocksInBuilding + ")");
                                    //租金叠加
                                    totalRent += building.rent;
                                }
                                if (building.type.contentEquals("commercial") && FolkData.getFolkByEmployedAt(building.primaryXYZ) != null) {
                                    if (building.rent == null || building.rent == 0f) {
                                        building.rent = 1f;
                                    }

                                    log.info("建筑公司税 " + building.displayNameWithoutPK + ": " + building.rent + "(" + building.blocksInBuilding + ")");
                                    totalRent += building.rent;
                                }
                            }
                        }
                        //如果租金大于0
                        if (totalRent > 0.0F) {
                            //今天收了 金的房租。
                            sendChat(I18n.format("container.sim.main_Collected") + ModSimReloaded.displayMoney(totalRent) + I18n.format("container.sim.main_rent_today"));
                            //今天收了 金的税收。
                            sendChat(I18n.format("container.sim.main_Collected") + ModSimReloaded.displayMoney(totalCorpTax) + I18n.format("container.sim.main_tax_today"));
                            //游戏状态
                            GameStates var10000 = ModSimReloaded.states;
                            //金币
                            var10000.credits += totalRent;
                            //税收
                            var10000.credits += totalCorpTax;
                            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                            if (p != null) {
                                ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                            }
                        } else if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            //今天没有收到房租,你应该雇一个人来盖一栋住宅。
                            sendChat(I18n.format("container.sim.main_No_rent"));
                        }

                    }
                });
                //启动线程
                t.start();

                for (int i = 0; i < theFolks.size(); i++) {
                    //获取npc
                    FolkData folk = (FolkData) theFolks.get(i);
                    //重置今天打招呼为否
                    folk.greetedToday = false;
                    //交配为负
                    folk.shaggingStage = -1.0F;
                    //如果怀孕则加一
                    if (folk.pregnancyStage > 0.0F) {
                        //增加怀孕-出生在FolkData中
                        folk.pregnancyStage += 0.1F;
                    }
                    //年龄
                    int age = folk.age;
                    //年龄大于18则
                    if (age >= 18) {
                        //如果星期六 大一岁
                        if (states.dayOfWeek == 6) {
                            //周六上午
                            folk.age++;
                        }
                        //小于18 则周三或者周六 年龄加一
                    } else{
                        if (states.dayOfWeek == 3 || states.dayOfWeek == 6) {
                            //让孩子一周两次上年纪
                            folk.age++;
                            if (age == 17 && folk.age == 18) {
                                //现在是成年人了（皮肤会自动变化）
                                //被赶出家 不在父母家
                                folk.evictThem();
                                //现在18岁了,他们会开始找房子,你现在也可以雇佣他们了。
                                sendChat(folk.name + I18n.format("container.sim.main_is_now"));
                            }
                        }
                    }
                    //年龄大于110 当他们超过110时杀死他们（随机1/10）
                    if (folk.age > 110 && rand.nextInt(10) == 5) {
                        //年纪大了,感觉不太好。。。哦不！
                        sendChat(folk.name + I18n.format("container.sim.main_is_old"));
                        //npc老死
                        folk.eventDied(DamageSource.generic);
                    }
                }
                //不是创造模式
                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                    //随机数 每个比赛周让他们老化一年
                    int fl = rand.nextInt(theFolks.size());
                    //循环所有
                    for (int f = 0; f < theFolks.size(); ++f) {
                        FolkData folk = (FolkData) theFolks.get(f);
                        if (f == fl) {
                            //饥饿等级
                            folk.levelFood--;
                            if (folk.levelFood == 0) {
                                //非常饿,你应该建一个农场、杂货店、面包店或向他们扔一些食物。
                                sendChat(folk.name + I18n.format("container.sim.main_is_VERY"));
                            }else if(folk.levelFood<0){
                                //饥饿等级小于-开始掉血
                            }
                        }
                        //付钱给士兵

                        if (folk.theirJob != null && folk.vocation == Vocation.SOLDIER) {
                            JobSoldier job = (JobSoldier) folk.theirJob;
                            //酬金
                            float pay = (float) job.kills * 0.2F;
                            if (job.kills > 0) {
                                //支付了 npc 酬金,昨天杀了 0 敌对暴徒。
                                sendChat(I18n.format("container.sim.main_Paid1") + folk.name + " " + displayMoney(pay) + I18n.format("container.sim.main_Paid2") + job.kills + I18n.format("container.sim.main_Paid3"));
                                GameStates var10000 = states;
                                var10000.credits -= pay;
                                job.kills = 0;
                            }
                        }
                    }
                    //大宗价格波动（建筑商-商户）
                    boolean updown = rand.nextBoolean();
                    //木板的价格
                    PricesForBlocks.adjustPrice(Blocks.planks, updown);
                    updown = rand.nextBoolean();
                    //圆石的价格
                    PricesForBlocks.adjustPrice(Blocks.cobblestone, updown);
                    updown = rand.nextBoolean();
                    //石头的价格
                    PricesForBlocks.adjustPrice(Blocks.stone, updown);
                    updown = rand.nextBoolean();
                    //玻璃的价格
                    PricesForBlocks.adjustPrice(Blocks.glass, updown);
                    updown = rand.nextBoolean();
                    //羊毛的价格
                    PricesForBlocks.adjustPrice(Blocks.wool, updown);
                    updown = rand.nextBoolean();
                    //砖块的价格
                    PricesForBlocks.adjustPrice(Blocks.brick_block, updown);
                    updown = rand.nextBoolean();
                    //石砖的价格
                    PricesForBlocks.adjustPrice(Blocks.stonebrick, updown);
                    updown = rand.nextBoolean();
                    //栅栏的价格
                    PricesForBlocks.adjustPrice(Blocks.oak_fence, updown);
                }
                //游戏状态保存
                states.saveStates();
                //刷新可用商品
                Commodity.refreshAvailableCommoditities();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("evolveFolks出错了：" + e.getMessage());
        }

    }

    //拆除
    public static void demolishBlocks() {
        try {
            if (demolishBlocks.size() >= 1) {
                int count = demolishBlocks.size();
                if (count > 10) {
                    count = 10;
                }

                for (int i = 0; i < count; i++) {
                    V3 blockLoc = (V3) demolishBlocks.get(0);

                    try {
                        Block block = Block.getBlockFromName(blockLoc.name);
                        BlockPos blockPos = new BlockPos(blockLoc.x.intValue(), blockLoc.y.intValue() + 10 + (new Random()).nextInt(20), blockLoc.z.intValue());
                        block.dropBlockAsItem(demolishWorld, blockPos, block.getDefaultState(), 0);
                        demolishBlocks.remove(0);
                    } catch (Exception var5) {
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("demolishBlocks出错了：" + e.getMessage());
        }
    }

    /**
     * 升级农场
     * 从commonTickHandler（）每次勾选调用，以将场从一个级别升级到下一个级别
     */
    public static void upgradeFarm() {
        try {

            //如果农场等级为0 则1
            if (farmToUpgrade.level == 0) {
                farmToUpgrade.level = 1;
            }

            V3 point;
            WorldServer theWorld;
            //升级至2级（围栏和灯光）
            if (farmToUpgrade.level == 1) {
                //如果农场为空则重新获取
                if (farmToUpgradePoints == null) {
                    farmToUpgradePoints = farmToUpgrade.getPerimeterPoints();
                }
                //获取元素
                point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
                theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
                BlockPos blockPos = new BlockPos(point.x.intValue(), point.y.intValue(), point.z.intValue());
                //如果该区域没有障碍物，则设置围栏
                Block id = theWorld.getBlockState(blockPos).getBlock();
                //摧毁
                boolean destroy = false;
                //方块不为空
                if (id != null) {
                    //获取实体
                    TileEntity te = theWorld.getTileEntity(blockPos);
                    if (te == null) {
                        destroy = true;
                    } else if (!(te instanceof IInventory)) {
                        destroy = true;
                    }
                } else {
                    destroy = true;
                }

                if (destroy) {
                    //System.out.println("farmToUpgradeCounter:" + farmToUpgradeCounter);
                    BlockPos blockPos2 = new BlockPos(point.x.intValue() - 1, point.y.intValue(), point.z.intValue() + 1);
                    //摧毁放快
                    theWorld.destroyBlock(blockPos2, true);
                    //把原来方块替换成 栅栏
                    theWorld.setBlockState(blockPos2, Blocks.oak_fence.getDefaultState(), 3);
                    //更新方块标记
                    theWorld.markBlockForUpdate(blockPos2);
                }
                //升级点除以六等于0 每隔6个街区放置一盏灯
                if (farmToUpgradeCounter % 6 == 0) {
                    BlockPos blockPos1 = new BlockPos(point.x.intValue() - 1, point.y.intValue() - 1, point.z.intValue() + 1);
                    theWorld.destroyBlock(blockPos1, true);
                    //把原来方块替换成 灯箱
                    theWorld.setBlockState(blockPos1, BlockLoader.blockLightBox.getDefaultState(), 3);
                    theWorld.markBlockForUpdate(blockPos1);
                }
                // 升级至3级（灌溉）
            } else if (farmToUpgrade.level == 2) {
                //如果农场为空则重新获取
                if (farmToUpgradePoints == null) {
                    farmToUpgradePoints = farmToUpgrade.getSoilBlockPoints();
                }

                point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
                theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
                if (point.x.intValue() % 5 == 0 && point.z.intValue() % 5 == 0) {

                    BlockPos blockPos1 = new BlockPos(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
                    theWorld.setBlockState(blockPos1, Blocks.water.getDefaultState(), 3);

                    BlockPos blockPos2 = new BlockPos(point.x.intValue(), point.y.intValue() - 2, point.z.intValue());
                    theWorld.setBlockState(blockPos2, BlockLoader.blockLightBox.getDefaultState(), 3);
                    theWorld.markBlockForUpdate(blockPos1);
                    theWorld.markBlockForUpdate(blockPos2);
                }
            }

            farmToUpgradeCounter++;

            if (farmToUpgradeCounter > farmToUpgradePoints.size() - 1) {
                farmToUpgrade.level++;
                farmToUpgradePoints = null;
                farmToUpgrade = null;
                farmToUpgradeCounter = 0;
                ModSimReloaded.log.info("完成农场升级");
            }
        } catch (Exception e) {
            log.error("升级农场出错了：" + e.getMessage());
        }
    }

    public static String getDayOfWeek() {
        String[] dow =null;
        try {
            String simSun = I18n.format("container.sim.simSun");
            String simMon = I18n.format("container.sim.simMon");
            String simTue = I18n.format("container.sim.simTue");
            String simWed = I18n.format("container.sim.simWed");
            String simThu = I18n.format("container.sim.simThu");
            String simFri = I18n.format("container.sim.simFri");
            String simSat = I18n.format("container.sim.simSat");
            dow =new String[]{simSun, simMon, simTue, simWed, simThu, simFri, simSat};
        } catch (Exception e) {
            ModSimReloaded.log.error("getDayOfWeek出错了：" + e.getMessage());
        }
        return dow[states.dayOfWeek];
    }

    public static ArrayList<String> loadSK2(String fullFilename) {
        ArrayList ret = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fullFilename));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                ret.add(line);
            }

            br.close();
        } catch (Exception e) {
            ModSimReloaded.log.error("loadSK2出错了：" + e.getMessage());
        }

        return ret;
    }

    public static void saveSK2(String fullFilename, ArrayList<String> strings) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilename));
            Iterator iterator = strings.iterator();

            while (iterator.hasNext()) {
                String line = (String) iterator.next();
                bw.write(line + "\r\n");
            }

            bw.close();
        } catch (Exception e) {
            ModSimReloaded.log.error("saveSK2出错了：" + e.getMessage());
            //var5.printStackTrace();
        }

    }

    /**
     * 保存对象
     *
     * @param filename
     * @param o
     */
    public void saveObject(String filename, Object o) {
    }

    /**
     * 加载对象
     *
     * @param filename
     * @return
     */
    public static Object loadObject(String filename) {
        Object o = null;

        try {
            FileInputStream fis2 = new FileInputStream(filename);
            ObjectInputStream in2 = new ObjectInputStream(fis2);
            o = in2.readObject();
            in2.close();
        } catch (Exception var5) {
            ModSimReloaded.log.info("旧加载程序-无法加载对象 " + var5.getMessage());
        }

        return o;
    }
}
