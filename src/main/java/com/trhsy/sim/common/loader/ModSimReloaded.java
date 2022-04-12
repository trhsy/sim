package com.trhsy.sim.common.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.gui.other.GuiRunMod;
import com.trhsy.sim.common.Commodity;
import com.trhsy.sim.common.block.functionality.FarmingBox;
import com.trhsy.sim.common.block.functionality.MiningBox;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.jobs.JobSoldier;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.util.GameMode;
import cpw.mods.fml.common.FMLCommonHandler;
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

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;
/**
 * 加载任务
 */
public class ModSimReloaded {
    public static Logger log = Logger.getLogger("Sim-U Trhsy Reloaded");
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

    private static GuiRunMod runModui = null;

    int highest = 0;
    int m1 = 0;

    public  ModSimReloaded(){

    }

    /**
     * 重置并加载新世界
     */
    public static void resetAndLoadNewWorld() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        ModSimReloaded.log.info("重置并加载世界 " + side.toString() + " SIDE");
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
                GameMode.setGameModeFromNumber(states.gameModeNumber);
            } catch (Exception var3) {
                GameMode.setGameModeFromNumber(0);
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
            if (ModSimReloaded.runModui == null) {
                GuiRunMod runModui = new GuiRunMod();
                Minecraft.getMinecraft().displayGuiScreen(runModui);
            }

        } else {
            if (states.gameModeNumber >= 0) {
                log.info("启动程序已经运行");
                //ModSimReloaded.log.info("Startup already been run");
                ModSim.proxy.ranStartup = true;
            }
            String welcome = I18n.format("container.sim.welcome");
            String welcomes = I18n.format("container.sim.welcomes");
            //欢迎来到SimCity
            sendChat(welcome + ModSim.VERSION + welcomes);
            theFolks.clear();
            Building.initialiseAllBuildings();
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
    }

    /**
     * 帮助功能，向所有世界/维度的所有玩家发送聊天信息
     * @param theText
     */
    public static void sendChat(String theText) {
        WorldServer[] worldServers = MinecraftServer.getServer().worldServers;
        int length = worldServers.length;

        for (int i = 0; i < length; ++i) {
            World w = worldServers[i];
            if (!w.isRemote) {
                for (int k = 0; k < w.playerEntities.size(); ++k) {
                    EntityPlayer p = (EntityPlayer) w.playerEntities.get(k);
                    p.addChatComponentMessage(new ChatComponentText(theText));
                }
            }
        }

    }

    /**
     * 以字符串形式获取“.minecraft/saves/CURRENTWORLD/simukraft/”文件夹 保存数据文件夹
     * @return
     */
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
     * 判断是否半天
     * @return
     */
    public static boolean isDayTime() {
        return MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000L <= 11999L;
    }

    /**
     * display Money
     * @param moneyin
     * @return
     */
    public static String displayMoney(float moneyin) {
        DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
        String output = myFormatter.format((double) moneyin);
        return output;
    }
    
    private String getTheirId() {
        return null;
    }

    public static void dayTransitionHandler() {
        //FolkData folk1;
        FolkData folk1;
        int homeless;
        int f1;
        if (isDayTime() && !isDay) {
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

            ++states.dayOfWeek;
            if (states.dayOfWeek > 6) {
                states.dayOfWeek = 0;
                homeless = 0;
                Iterator iterator = theFolks.iterator();

                while (iterator.hasNext()) {
                    folk1 = (FolkData) iterator.next();
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
            ModSimReloaded.log.info("渡过一晚");
            if (theFolks.size() > 1) {
                Random rand = new Random();
                homeless = rand.nextInt(theFolks.size());

                for (f1 = homeless; f1 == homeless; f1 = rand.nextInt(theFolks.size())) {
                }

                folk1 = (FolkData) theFolks.get(homeless);
                FolkData folk2 = (FolkData) theFolks.get(f1);
                Relationship.meddleWithRelationship(folk1, folk2);
            }

            Iterator iterator = theFolks.iterator();

            while (iterator.hasNext()) {
                FolkData folk = (FolkData) iterator.next();
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
            ModSimReloaded.log.info("进化的人");
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000L);
                    } catch (Exception var5) {
                    }

                    float totalRent = 0.0F;
                    float totalCorpTax = 0.0F;
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        for (int b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
                            Building building = (Building) ModSimReloaded.theBuildings.get(b);
                            if (building.type.contentEquals("residential") && building.tenants.size() > 0) {
                                if (building.rent == null || building.rent == 0.0F) {
                                    building.rent = 1.0F;
                                }

                                ModSimReloaded.log.info("房屋租金 " + building.displayNameWithoutPK + ": " + building.rent + "(" + building.blocksInBuilding + ")");
                                totalRent += building.rent;
                            }
                        }
                    }

                    if (totalRent > 0.0F) {
                        sendChat(I18n.format("container.sim.main_Collected") + ModSimReloaded.displayMoney(totalRent) + I18n.format("container.sim.main_rent_today"));
                        sendChat(I18n.format("container.sim.main_Collected") + ModSimReloaded.displayMoney(totalCorpTax) + I18n.format("container.sim.main_tax_today"));
                        GameStates var10000 = ModSimReloaded.states;
                        var10000.credits += totalRent;
                        var10000 = ModSimReloaded.states;
                        var10000.credits += totalCorpTax;
                        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                        if (p != null) {
                            ModSim.proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                        }
                    } else if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        sendChat(I18n.format("container.sim.main_No_rent"));
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

            if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
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
                    theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), BlockLoader.lightBox, 0, 3);
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
                    theWorld.setBlock(point.x.intValue(), point.y.intValue() - 2, point.z.intValue(), BlockLoader.lightBox, 0, 3);
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
                ModSimReloaded.log.info("完成农场升级");
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
                Iterator iterator = strings.iterator();

                while (iterator.hasNext()) {
                    String line = (String) iterator.next();
                    bw.write(line + "\r\n");
                }

                bw.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }


}
