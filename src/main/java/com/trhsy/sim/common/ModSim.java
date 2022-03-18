package com.trhsy.sim.common;

import com.trhsy.sim.client.ClientProxy;
import com.trhsy.sim.client.gui.GuiRunMod;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.jobs.JobSoldier;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.packets.client.Handler;
import com.trhsy.sim.packets.client.UpdateFolkPositionMessage;
import com.trhsy.sim.packets.server.LoadBuildingMessage;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * @ClassName ModSimukraft
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2319:40
 **/
@Mod(modid = ModSim.MODID, name = ModSim.NAME, version = ModSim.VERSION, useMetadata = true, dependencies = "required-after:Forge@[9.10,)")
public class ModSim {
    public static final String MODID = "sim";
    public static final String NAME = "Simulated town";
    public static final String VERSION = "1.1.0 Beta";
    /**
     * 将生成该mod的实例注册到对应mod的id里面，也可以访问其他mod的，要注意这里的id和此mod的id相同
     */
    @Instance(ModSim.MODID)
    public static ModSim instance = new ModSim();

    @SidedProxy(
            clientSide = "com.trhsy.sim.client.ClientProxy",
            serverSide = "com.trhsy.sim.common.CommonProxy"
    )
    public static CommonProxy proxy;
    public static ClientProxy clientProxy;
    public static Logger log;
    /*
    用于检测我们何时进入世界（非主菜单）以及玩家何时更改世界/地图
     */
    public static String currentSavePath = "";
    public static boolean configUseExpensiveRecipies = false;

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
    /*
    配置文件设置
     */
    public static Configuration config;
    /*
    配置人口限制
     */
    public static int configPopulationLimit = 100;
    /*
    配置木材面积
     */
    public static int configLumberArea = 30;
    /*
    配置禁用光束效果
     */
    public static boolean configDisableBeamEffect = false;
    /*
    配置谈话
     */
    public static boolean configFolkTalking = true;
    /*
    配置启用标记对齐
     */
    public static boolean configEnableMarkerAlignmentBeams = true;

    /*
    配置物料提醒间隔
     */
    public static int configMaterialReminderInterval = 3;
    /*
    配置偏移量
     */
    public static int configHUDoffset = 0;
    /*
    停止降雨
     */
    public static boolean configStopRain = false;
    /*
    配置说英语
     */
    public static boolean configFolkTalkingEnglish = true;
    /*
    配置男性姓名
     */
    public static String[] configMaleNames;
    /*
    配置女性姓名
     */
    public static String[] configFemaleNames;
    /*
    姓氏
     */
    public static String[] configSurnames;
    /*
    游戏模式
     */
    public static GameMode gameMode = null;
    /*
    工作关系
     */
    public static SimpleNetworkWrapper network;
    private static GuiRunMod runModui = null;
    int highest = 0;
    int m1 = 0;
    public ModSim() {

    }

    /**
     * 游戏模式
     *
     * @return
     */
    public static int getGameModeNumber() {
        if (gameMode == GameMode.DONOTRUN) {
            //不运行
            return -1;
        } else if (gameMode == GameMode.NORMAL) {
            //正常的
            return 0;
        } else if (gameMode == GameMode.CREATIVE) {
            //创造
            return 1;
        } else {
            //
            return gameMode == GameMode.HARDCORE ? 2 : 0;
        }
    }

    public static void setGameModeFromNumber(int gm) {
        if (gm == -1) {
            gameMode = GameMode.DONOTRUN;
        } else if (gm == 0) {
            gameMode = GameMode.NORMAL;
        } else if (gm == 1) {
            gameMode = GameMode.CREATIVE;
        } else if (gm == 2) {
            gameMode = GameMode.HARDCORE;
        }

    }

    /**
     * 在所有mod初始化之前调用此函数，这里应该加载配置文件，实例化方块和物品，并注册它们
     *
     * @param event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        log = event.getModLog();

        //新的网络包装器
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        //注册客户端消息系统
        network.registerMessage(Handler.class, UpdateFolkPositionMessage.class, 1, Side.CLIENT);
        //注册服务端消息系统
        network.registerMessage(com.trhsy.sim.packets.server.Handler.class, LoadBuildingMessage.class, 0, Side.SERVER);

        File check = new File(getSimukraftFolder());
        if (!check.exists()) {
            // 模拟城市 error - Mod未正确安装，./minecraft/mods/Simukraft/文件夹丢失了 - 从提供的zip文件复制此文件
            //ModSim.log.info("SimCity error - Mod not correctly installed, ./minecraft/mods/Simukraft/ folder is missing - copy this file from the zip provided");
            ModSim.log.warn("SimCity error - Mod未正确安装, ./minecraft/mods/Simukraft/ 文件夹丢失了 - 从提供的zip文件复制此文件");
        }



        /*
*/
        EntityRegistry.registerGlobalEntityID(EntityAlignBeam.class, "AlignBeam", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityAlignBeam.class, "AlignBeam", 0, this, 250, 10, false);
        EntityRegistry.registerGlobalEntityID(EntityFolk.class, "Folk", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityFolk.class, "Folk", 1, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityConBox.class, "ConBox", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityConBox.class, "ConBox", 2, this, 250, 2, true);
        EntityRegistry.registerGlobalEntityID(EntityWindmill.class, "SUKWindmill", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityWindmill.class, "SUKWindmill", 3, this, 250, 1, false);
        proxy.registerRenderInfo();
        proxy.registerMisc();
        //updateCheck();
    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        //在此mod初始化时调用此函数，这里应该注册合成表和烧练系统，并向其他mod发送交互信息，注意不要在这里注册方块和物品等等操作，forge支持在preInit函数执行
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //在所有mod初始化后调用此函数，这里应该接收其他mod发送的交互信息，并完成设置mod
        proxy.postInit(event);
    }

    public static void resetAndLoadNewWorld() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        ModSim.log.info("重置并加载世界 " + side.toString() + " SIDE");
        theBuildings.clear();
        theCourierPoints.clear();
        theCourierTasks.clear();
        theMiningBoxes.clear();
        theFarmingBoxes.clear();
        theFolks.clear();
        theRelationships.clear();
        File f = new File(getSavesDataFolder() + "settings.sk2");
        if (!f.exists()) {
            f = new File(getSavesDataFolder() + "settings.suk");
        }

        if (f.exists()) {
            states.loadStates();
            try {
                setGameModeFromNumber(states.gameModeNumber);
            } catch (Exception var3) {
                setGameModeFromNumber(0);
            }
        } else {
            states = new GameStates();
            states.saveStates();
        }

        if (states == null) {
            (new File(getSavesDataFolder() + "settings.sk2")).delete();
            states = new GameStates();
            states.saveStates();
            //你的SimCity设置文件已损坏，我必须重新创建一个
            String sim_settings = I18n.format("container.sim.sim_settings");
            sendChat(sim_settings);
        }

        if (states.gameModeNumber == -1) {
            if (runModui == null) {
                GuiRunMod runModui = new GuiRunMod();
                Minecraft.getMinecraft().displayGuiScreen(runModui);
            }

        } else {
            if (states.gameModeNumber >= 0) {
                ModSim.log.info("启动程序已经运行");
                //ModSim.log.info("Startup already been run");
                proxy.ranStartup = true;
            }
            String welcome = I18n.format("container.sim.welcome");
            String welcomes = I18n.format("container.sim.welcomes");
            //欢迎来到SimCity
            sendChat(welcome + VERSION + welcomes);
            theFolks.clear();
            Building.initialiseAllBuildings();
            Building.loadAllBuildings();
            CourierTask.loadCourierTasksAndPoints();
            MiningBox.loadMiningBoxes();
            FarmingBox.loadFarmingBoxes();
            FolkData.loadAndSpawnFolks();
            Relationship.loadRelationships();
//            updateCheck();
            isDay = isDayTime();
            Building.checkTennants();
            proxy.ranStartup = true;
        }
    }

    public static void sendChat(String theText) {
        WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            World w = arr$[i$];
            if (!w.isRemote) {
                for (int i = 0; i < w.playerEntities.size(); ++i) {
                    EntityPlayer p = (EntityPlayer) w.playerEntities.get(i);
                    p.addChatComponentMessage(new ChatComponentText(theText));
                }
            }
        }

    }

    public static String getSavesDataFolder() {
        String worldname = MinecraftServer.getServer().getFolderName();
        String strmc = (new File(".")).getAbsolutePath();
        strmc = strmc.substring(0, strmc.length() - 1);
        File test = new File(strmc + "saves");
        String ret = "";
        if (test.exists()) {
            ret = (new File(strmc + File.separator + "saves" + File.separator + worldname + File.separator + "simukraft" + File.separator)).getAbsolutePath() + File.separator;
        } else {
            strmc = strmc + worldname + File.separator + "simukraft" + File.separator;
            ret = (new File(strmc)).getAbsolutePath();
        }

        File f = new File(ret);
        if (!f.exists()) {
            f.mkdirs();
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
            return (new File(strmc + File.separator + "mods" + File.separator + "Simukraft")).getAbsolutePath();
        } catch (Exception var1) {
            return "";
        }
    }

    public static boolean isDayTime() {
        return MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000L <= 11999L;
    }

    public static String displayMoney(float moneyin) {
        DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
        String output = myFormatter.format((double) moneyin);
        return output;
    }

    public static void updateCheck() {
        instance.ThreadUpdate();
    }

    public void ThreadUpdate() {
        this.start();
    }

    public void start() {
        try {
            Thread.sleep(15000L);
            File check = new File(getSimukraftFolder() + "/buildings/");
            if (!check.exists()) {
                sendChat(getSimukraftFolder() + "/buildings/ "+I18n.format("container.sim.main_buildings"));
                return;
            }

            String baseURL = "https://www.jianguoyun.com/p/DWFS4bwQ-bWvChj-prME";//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String ver = this.downloadFile(baseURL + "version.txt", getSimukraftFolder() + File.separator + "simukraft.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("") && !"1.1.0 Beta".contentEquals(ver)) {
                    if (!VERSION.contentEquals(ver)) {
                        sendChat(I18n.format("container.sim.main_available"));
                    }

                    Long now = System.currentTimeMillis();
                    states.lastUpdateCheck = now;
                    states.saveStates();
                }
            }

            int high = this.getHighestPKID("residential");
            int o = this.getHighestPKID("other");
            if (o > high) {
                high = o;
            }

            String newbs = this.downloadFile(baseURL, getSimukraftFolder() + File.separator + "simukraft.txt");
            if (newbs.length() == 0) {
                return;
            }

            String[] items = newbs.split("!END");

            /*for (int i = 0; i < items.length - 1; ++i) {
                String[] fields = items[i].split("!F");
                String url = baseURL + "catalogue/PKID" + fields[0] + "-" + fields[1] + ".txt";
                String local = getSimukraftFolder() + "/buildings/" + fields[3] + "/PKID" + fields[0] + "-" + fields[1] + ".txt";
                String ret = this.downloadFile(url, local);
                if (!ret.contentEquals("")) {
                    url = baseURL + "backend.php?cmd=got&pk=" + fields[0];
                    this.downloadFile(url, getSimukraftFolder() + File.separator + "cache.txt");
                    sendChat("SimCity: Downloaded new building - '" + fields[1] + "' by " + fields[2] + " (" + fields[3] + ")");
                }
            }*/
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    private String getTheirId() {
        return null;
    }

    public int getHighestPKID(String type) {
        File actual = new File(getSimukraftFolder() + File.separator + "buildings" + File.separator + type + File.separator);
        File[] arr$ = actual.listFiles();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            File f = arr$[i$];
            if (f.getName().startsWith("PKID")) {
                this.m1 = f.getName().indexOf("-");
                if (this.m1 > 0) {
                    String id = f.getName().substring(4, this.m1);
                    if (Integer.parseInt(id) > this.highest) {
                        this.highest = Integer.parseInt(id);
                    }
                }
            }
        }

        return this.highest;
    }

    public String downloadFile(String url, String localFile) {
        String ret = "";
        ModSim.log.info("下载文件" + url);
        url = url.replace(" ", "%20");

        try {
            BufferedInputStream in = new BufferedInputStream((new URL(url)).openStream());
            FileOutputStream fos = new FileOutputStream(localFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[4096];
            boolean var8 = false;

            int x;
            while ((x = in.read(data, 0, 4096)) >= 0) {
                bout.write(data, 0, x);
            }

            bout.flush();
            ret = new String(data);
            bout.close();
            in.close();
        } catch (Exception var9) {
            ret = "";
            var9.printStackTrace();
        }

        return ret;
    }

    public static void dayTransitionHandler() {
        //FolkData folk1;
        FolkData folk1;
        int homeless;
        int f1;
        if (isDayTime() && !isDay) {
            isDay = true;
            //Night to day transition
            ModSim.log.info("天亮了");
            World world = proxy.getClientWorld();
            if (world != null) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                if (p != null) {
                    proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":rooster", 1.0F, 1.0F, false);
                }
            }

            ++states.dayOfWeek;
            if (states.dayOfWeek > 6) {
                states.dayOfWeek = 0;
                homeless = 0;
                Iterator i$ = theFolks.iterator();

                while (i$.hasNext()) {
                    folk1 = (FolkData) i$.next();
                    if (folk1.getHome() == null) {
                        ++homeless;
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
                f1 = rand.nextInt(theFolks.size());

                int f2;
                for (f2 = f1; f2 == f1; f2 = rand.nextInt(theFolks.size())) {
                }

                folk1 = (FolkData) theFolks.get(f1);
                FolkData folk2 = (FolkData) theFolks.get(f2);
                Relationship.meddleWithRelationship(folk1, folk2);
            }
        }

        if (!isDayTime() && isDay) {
            isDay = false;
            //Day to Night transition
            ModSim.log.info("天黑了");
            if (theFolks.size() > 1) {
                Random rand = new Random();
                homeless = rand.nextInt(theFolks.size());

                for (f1 = homeless; f1 == homeless; f1 = rand.nextInt(theFolks.size())) {
                }

                folk1 = (FolkData) theFolks.get(homeless);
                folk1 = (FolkData) theFolks.get(f1);
                Relationship.meddleWithRelationship(folk1, folk1);
            }

            Iterator i$ = theFolks.iterator();

            while (i$.hasNext()) {
                FolkData folk = (FolkData) i$.next();
                folk.destination = null;
                if (folk.theEntity != null) {
                    folk.theEntity.getNavigator().clearPathEntity();
                }
            }
        }

    }

    private static void evolveFolks() {
        if (theFolks.size() > 0) {
            Random rand = new Random();
            //evolving folks
            ModSim.log.info("进化的人");
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000L);
                    } catch (Exception var5) {
                    }

                    float totalRent = 0.0F;
                    float totalCorpTax = 0.0F;
                    if (ModSim.gameMode != GameMode.CREATIVE) {
                        for (int b = 0; b < ModSim.theBuildings.size(); ++b) {
                            Building building = (Building) ModSim.theBuildings.get(b);
                            if (building.type.contentEquals("residential") && building.tennants.size() > 0) {
                                if (building.rent == null || building.rent == 0.0F) {
                                    building.rent = 1.0F;
                                }

                                ModSim.log.info("房屋租金 " + building.displayNameWithoutPK + ": " + building.rent + "(" + building.blocksInBuilding + ")");
                                totalRent += building.rent;
                            }
                        }
                    }

                    if (totalRent > 0.0F) {
                        ModSim.sendChat(I18n.format("container.sim.main_Collected") + ModSim.displayMoney(totalRent) + I18n.format("container.sim.main_rent_today"));
                        ModSim.sendChat(I18n.format("container.sim.main_Collected") + ModSim.displayMoney(totalCorpTax) + I18n.format("container.sim.main_tax_today"));
                        GameStates var10000 = ModSim.states;
                        var10000.credits += totalRent;
                        var10000 = ModSim.states;
                        var10000.credits += totalCorpTax;
                        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                        if (p != null) {
                            ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                        }
                    } else if (ModSim.gameMode != GameMode.CREATIVE) {
                        ModSim.sendChat(I18n.format("container.sim.main_No_rent"));
                    }

                }
            });
            t.start();

            FolkData folk;
            int fl;
            int f;
            for (fl = 0; fl < theFolks.size(); ++fl) {
                folk = (FolkData) theFolks.get(fl);
                folk.greetedToday = false;
                folk.shaggingStage = -1.0F;
                if (folk.pregnancyStage > 0.0F) {
                    folk.pregnancyStage += 0.1F;
                }

                f = folk.age;
                if (f >= 18) {
                    if (states.dayOfWeek == 6) {
                        ++folk.age;
                    }
                } else if (states.dayOfWeek == 3 || states.dayOfWeek == 6) {
                    ++folk.age;
                    if (f == 17 && folk.age == 18) {
                        folk.evictThem();
                        sendChat(folk.name + I18n.format("container.sim.main_is_now"));
                    }
                }

                if (folk.age > 110 && rand.nextInt(10) == 5) {
                    sendChat(folk.name + I18n.format("container.sim.main_is_old"));
                    folk.eventDied(DamageSource.generic);
                }
            }

            if (gameMode != GameMode.CREATIVE) {
                fl = rand.nextInt(theFolks.size());

                for (f = 0; f < theFolks.size(); ++f) {
                    folk = (FolkData) theFolks.get(f);
                    if (f == fl) {
                        --folk.levelFood;
                        if (folk.levelFood == 0) {
                            sendChat(folk.name + I18n.format("container.sim.main_is_VERY"));
                        }
                    }
                }

                for (f = 0; f < theFolks.size(); ++f) {
                    folk = (FolkData) theFolks.get(f);
                    if (folk.theirJob != null && folk.vocation == Vocation.SOLDIER) {
                        JobSoldier job = (JobSoldier) folk.theirJob;
                        float pay = (float) job.kills * 0.2F;
                        if (job.kills > 0) {
                            sendChat(I18n.format("container.sim.main_Paid1") + folk.name + " " + displayMoney(pay) + I18n.format("container.sim.main_Paid2") + job.kills + I18n.format("container.sim.main_Paid3"));
                            GameStates var10000 = states;
                            var10000.credits -= pay;
                            job.kills = 0;
                        }
                    }
                }

                boolean updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.planks, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.cobblestone, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.stone, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.glass, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.wool, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.brick_block, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.stonebrick, updown);
                updown = rand.nextBoolean();
                PricesForBlocks.adjustPrice(Blocks.fence, updown);
            }

            states.saveStates();
            Commodity.refreshAvailableCommoditities();
        }
    }

    public static void demolishBlocks() {
        if (demolishBlocks.size() >= 1) {
            int count = demolishBlocks.size();
            if (count > 10) {
                count = 10;
            }

            for (int i = 0; i < count; ++i) {
                V3 blockLoc = (V3) demolishBlocks.get(0);

                try {
                    Block block = Block.getBlockFromName(blockLoc.name);
                    block.dropBlockAsItem(demolishWorld, blockLoc.x.intValue(), blockLoc.y.intValue() + 10 + (new Random()).nextInt(20), blockLoc.z.intValue(), 0, 0);
                    demolishBlocks.remove(0);
                } catch (Exception var5) {
                }
            }

        }
    }

    public static void upgradeFarm() {
        if (farmToUpgrade.level == 0) {
            farmToUpgrade.level = 1;
        }

        V3 point;
        WorldServer theWorld;
        if (farmToUpgrade.level == 1) {
            if (farmToUpgradePoints == null) {
                farmToUpgradePoints = farmToUpgrade.getPerimeterPoints();
            }

            point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
            theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
            Block id = theWorld.getBlock(point.x.intValue(), point.y.intValue(), point.z.intValue());
            boolean destroy = false;
            if (id != null) {
                TileEntity te = theWorld.getTileEntity(point.x.intValue(), point.y.intValue(), point.z.intValue());
                if (te == null) {
                    destroy = true;
                } else if (!(te instanceof IInventory)) {
                    destroy = true;
                }
            } else {
                destroy = true;
            }

            if (destroy) {
                theWorld.breakBlock(point.x.intValue(), point.y.intValue(), point.z.intValue(), true);
                theWorld.setBlock(point.x.intValue(), point.y.intValue(), point.z.intValue(), Blocks.fence, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue(), point.z.intValue());
            }

            if (farmToUpgradeCounter % 6 == 0) {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), BlockLoader.blockLightBox, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
            }
        } else if (farmToUpgrade.level == 2) {
            if (farmToUpgradePoints == null) {
                farmToUpgradePoints = farmToUpgrade.getSoilBlockPoints();
            }

            point = (V3) farmToUpgradePoints.get(farmToUpgradeCounter);
            theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
            if (point.x.intValue() % 5 == 0 && point.z.intValue() % 5 == 0) {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), Blocks.water, 0, 3);
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 2, point.z.intValue(), BlockLoader.blockLightBox, 0, 3);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 2, point.z.intValue());
            }
        }

        ++farmToUpgradeCounter;
        if (farmToUpgradeCounter > farmToUpgradePoints.size() - 1) {
            ++farmToUpgrade.level;
            farmToUpgradePoints = null;
            farmToUpgrade = null;
            farmToUpgradeCounter = 0;
            ModSim.log.info("完成农场升级");
        }
    }

    public static String getDayOfWeek() {
        String simSun = I18n.format("container.sim.simSun");
        String simMon = I18n.format("container.sim.simMon");
        String simTue = I18n.format("container.sim.simTue");
        String simWed = I18n.format("container.sim.simWed");
        String simThu = I18n.format("container.sim.simThu");
        String simFri = I18n.format("container.sim.simFri");
        String simSat = I18n.format("container.sim.simSat");
        String[] dow = new String[]{simSun, simMon, simTue, simWed, simThu, simFri, simSat};

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
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return ret;
    }

    public static void saveSK2(String fullFilename, ArrayList<String> strings) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilename));
            Iterator i$ = strings.iterator();

            while (i$.hasNext()) {
                String line = (String) i$.next();
                bw.write(line + "\r\n");
            }

            bw.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

}
