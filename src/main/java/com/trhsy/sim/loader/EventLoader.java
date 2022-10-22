package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/9/2921:05
 **/
public class EventLoader {
    /**已加载世界**/
    public boolean hasLoadedWorld;
    /**上次可以户连接的时间**/
    public long timeSinceLastClientUpdate = 0L;
    /**分钟计时器**/
    long minuteTimer = System.currentTimeMillis();
    boolean newDay = true;
    Random rand = new Random();
    /**
     * 自定义的事件在这里被注册
     **/
    public static final EventBus EVENT_BUS = new EventBus();
    public EventLoader() {
        try {
            MinecraftForge.EVENT_BUS.register(this);
            EventLoader.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("EventLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 当玩家加入的时候
     * @Date 19:19 2022/10/7
     * @Param [event]
     * @return void
     **/
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Thread skinThread = new Thread(() -> {
            try{
                if (event.player != null) {
                    System.out.println("*********************玩家加入*****************");
                    for (NpcData fd:ModSimLoader.folks){
                        while(fd.entity == null && FMLCommonHandler.instance() != null) {
                            try {
                                fd.entity = (EntityFolk)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(fd.ID));
                            } catch (Exception var4) {
                            }
                        }

                        fd.sendSkinPathToClient();
                    }
                }
            }catch (Exception e){
                e.getMessage();
            }


        });
        skinThread.start();

        try{
            //建筑文件检查
            File checks = new File(ModSimLoader.getSimFolder() + File.separator + "/buildings");
            if (!checks.exists()) {
                onUpdate();
            }
            //检查模组更新提醒
            String baseURL = "https://trhsy.github.io/sim/1.9/version.txt";
            String ver = ModSimLoader.downloadFile(baseURL, ModSimLoader.getSimFolder() + File.separator + "version.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("")&&!"1.0.0 Beta".contentEquals(ver)) {
                    if (!ModSim.VERSION.contentEquals(ver)) {
                        ModSimLoader.sendChat(I18n.format("container.sim.update_checker1") + ver + I18n.format("container.sim.update_checker2") );
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void onUpdate() {
        try {
//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String baseURL = "https://trhsy.github.io/sim/1.9/Simukraft_zh_CN.zip";
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            if ("en_US".equals(lang)) {
                baseURL = "https://trhsy.github.io/sim/1.9/Simukraft_en_US.zip";
            }
            String unzipFilePath = ModSimLoader.getSimFolder();
            File checks = new File(unzipFilePath + File.separator);
            File[] checkss = checks.listFiles();

            for (File f : checkss) {
                ModSimLoader.deleteFile(f);
            }
            checks.mkdir();
            String simFile = unzipFilePath + File.separator + "Simukraft.zip";
            String ver = ModSimLoader.downloadSimFile(baseURL, simFile);
            if (ver != null) {
                File zipFile = new File(ver);
                //开始解压
                ModSimLoader.log.info("开始解压：", zipFile.getName());
                ZipEntry entry = null;
                String entryFilePath = null, entryDirPath = null;
                File entryFile = null, entryDir = null;
                int index = 0, count = 0;
                byte[] buffer = new byte[1024];
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                ZipFile zip = new ZipFile(zipFile);
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
                //循环对压缩包里的每一个文件进行解压
                while (entries.hasMoreElements()) {

                    entry = entries.nextElement();

                    //构建压缩包中一个文件解压后保存的文件全路径
                    entryFilePath = unzipFilePath + File.separator + entry.getName();
                    //构建解压后保存的文件夹路径
                    index = entryFilePath.lastIndexOf(".txt");
                    if (index != -1) {
                        //创建解压文件
                        entryFile = new File(entryFilePath);
                        //写入文件
                        bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                        bis = new BufferedInputStream(zip.getInputStream(entry));
                        while ((count = bis.read(buffer, 0, 1024)) != -1) {
                            bos.write(buffer, 0, count);
                        }
                        bos.flush();
                        bos.close();
                        //ModSimReloaded.log.info("创建解压文件：",entryFile.getName());
                    } else {
                        entryDirPath = entryFilePath.substring(0, entryFilePath.length() - 1);
                        entryDir = new File(entryDirPath);
                        //如果文件夹路径不存在，则创建文件夹
                        if (!entryDir.exists() || !entryDir.isDirectory()) {
                            entryDir.mkdirs();
                            ModSimLoader.log.info("创建解压文件夹：", entryDir.getName());
                        }
                    }

                }

            }

            new File(simFile).deleteOnExit();

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("检查sim建筑包出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            //e.printStackTrace();
        }


    }
    /**
     * 世界保存
     * @param event
     */
    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        if (!event.getWorld().isRemote) {
            if (hasLoadedWorld) {
                //配置文件保存
                ModSimLoader.log.info("时间数据保存，准备保存模组信息");
                ModSimLoader.states.saveStates();
                //农场保存
                ModSimLoader.log.info("农场保存，准备保存模组信息");
                //NPC保存
                ModSimLoader.log.info("NPC保存，准备保存模组信息");
                for (NpcData folks:ModSimLoader.folks){
                    folks.saveFolk();
                }
                //建筑保存
                ModSimLoader.log.info("建筑保存，准备保存模组信息");
                for (Building b:ModSimLoader.buildings){
                    b.saveBuilding();
                }
            }
        }
    }

    /**
     * 世界加载
     * @param event
     */
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        ModSimLoader.log.info("检查是否应该加载人员");
        if (event.getWorld().isRemote) {
            ModSimLoader.log.info("世界遥远，正在取消");
        } else if (hasLoadedWorld) {
            ModSimLoader.log.info("世界尚未加载，正在取消");
        } else {

            ModSimLoader.log.info("清除旧的世界数据");
            ModSimLoader.folks.clear();
            ModSimLoader.buildings.clear();
//            ModSimLoader.farms.clear();
            ModSimLoader.states.dayOfWeek=0;
            ModSimLoader.states.gameModeNumber=999;
            ModSimLoader.states.credits=10.0F;
            this.timeSinceLastClientUpdate = 0L;
            File[] buildingSaves;
            File buildingFile;

                ModSimLoader.log.info("加载世界...");
                ModSimLoader.states.loadStates();
                ModSimLoader.log.info("装载农场");
                ModSimLoader.log.info("装载矿场");
            try {
                ModSimLoader.log.info("获得保存的NPC");
                new DimensionManager();
                File npcFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "npc");
                if(!npcFolder.exists()){
                    npcFolder.mkdirs();
                }
                buildingSaves=npcFolder.listFiles();
                for (int i = 0; i <buildingSaves.length; i++) {
                    buildingFile=buildingSaves[i];
                    ModSimLoader.log.info("得到Npc " + buildingFile.getName());
                    ModSimLoader.folks.add(new NpcData(event.getWorld(), UUID.fromString(buildingFile.getName().split(".sk2")[0])));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
                ModSimLoader.log.info("加载建筑物");



            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
            hasLoadedWorld = true;
        }
    }

    /**
     * 钩子
     * @param event
     */
    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote && System.currentTimeMillis() - this.timeSinceLastClientUpdate > 2000L) {
            this.timeSinceLastClientUpdate = System.currentTimeMillis();
            //可雇佣的人
            NetWorkLoader.net.sendToAll(new PacketReturnHireableFolks());
            //更新资金
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
            //检查建筑物
            for (int i = 0; i <ModSimLoader.buildings.size(); --i) {
                Building b = ModSimLoader.buildings.get(i - 1);
                if (event.world.getBlockState(b.controlXYZ.toBlockPos()).getBlock() != BlockLoader.blockControlBox) {
                    ModSimLoader.log.info(b.buildingName + " 没有控制块-正在销毁");
                    b.demolish(event.world, false);
                }
            }
            //检查游戏状态
            if (ModSimLoader.states.gameModeNumber != 999 && !event.world.isRemote&&event.world.playerEntities.size()>0) {
                //是白天
                if(ModSimLoader.isDayTime(event.world)){
                    NpcData starves=null;
                    if (System.currentTimeMillis() - this.minuteTimer > 60000L) {
                        boolean spawnNew = true;
                        for (NpcData starve:ModSimLoader.folks){
                            starves=starve;
                            if (starves.isDead) {
                                if (starves.home != null) {
                                    starves.home.occupants.remove(starves);
                                    starves.home = null;
                                }
                            } else if (starves.home == null) {
                                spawnNew = false;
                            }
                        }
                        if (spawnNew) {
                            new NpcData(event.world, false);
                        }
                    }
                    if(!this.newDay){
                        this.newDay = true;
                        if (ModSimLoader.states.dayOfWeek >= 6) {
                            ModSimLoader.states.dayOfWeek = 0;
                        } else {
                            ++ModSimLoader.states.dayOfWeek;
                        }
                        if (ModSimLoader.states.gameModeNumber != 0) {
                            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
                        } else {
                            float rent = 0.0F;
                            for (Building b:ModSimLoader.buildings){
                                if (b.occupants.size() > 0) {
                                    rent += b.rent;
                                }
                            }

                            ModSimLoader.addMoney(rent);
                            //您已收集 今天的租金。
                            ModSimLoader.sendChat(I18n.format("container.sim.main_Collected") + ModSimLoader.displayMoney(rent) + I18n.format("container.sim.main_rent_today"));
                        }

                        label130:while (true){
                            for (NpcData f:ModSimLoader.folks) {
                                if (f.entity != null) {
                                    String hungerName = "";
                                    int hungerCount = 0;
                                    NpcData starve = null;
                                    for (NpcData f1:ModSimLoader.folks) {
                                        if (f1.hunger > 0) {
                                            --f1.hunger;
                                        } else if (this.rand.nextInt(4) == 3) {
                                            starve = f1;
                                        } else {
                                            hungerName = f1.getName();
                                            ++hungerCount;
                                        }
                                        if (hungerName != "") {
                                            String starving=I18n.format("container.sim.main_is_VERY");
                                            String others_starving=I18n.format("container.sim.others_starving");
                                            String message = hungerCount > 1 ? hungerName + starving : hungerName + I18n.format("container.sim.Mining13") + hungerCount + others_starving;
                                            ModSimLoader.sendChat(message);
                                        }
                                    }
                                    if(starve != null){
                                        starve.entity.attackEntityFrom(DamageSource.starve, 999.0F);
                                    }
                                    break label130;
                                }
                                f.matingStage = -1.0F;
                                if (f.pregnancyStage > 0.0F) {
                                    f.pregnancyStage += 0.1F;
                                }

                                int currentAge = f.age;
                                if (f.age >= f.race.maturity) {
                                    if (ModSimLoader.states.dayOfWeek == 6) {
                                        ++f.age;
                                    }
                                } else if (ModSimLoader.states.dayOfWeek == 3 || ModSimLoader.states.dayOfWeek == 6) {
                                    ++f.age;
                                }

                                if (currentAge < f.race.maturity && f.age >= f.race.maturity) {
                                    f.evict();
                                    //现在18岁了,他们会开始找房子,你现在也可以雇佣他们了。
                                    String s=I18n.format("container.sim.main_is_now");
                                    ModSimLoader.sendChat(f.getName() + s);
                                }

                                if (f.age >= f.race.lifespan && this.rand.nextInt(10) == 5) {
                                    //年纪大了,感觉不太好。。。哦不！
                                    String s=I18n.format("container.sim.main_is_old");
                                    ModSimLoader.sendChat(f.getName() + s);
                                    f.entity.attackEntityFrom(DamageSource.starve, 999.0F);
                                }
                            }
                        }
                    }
                }else if (this.newDay) {
                    this.newDay = false;
                }
            }
            //实时更新人的状态
            if (!event.world.isRemote) {
                for (NpcData f:ModSimLoader.folks){
                    f.onUpdate();
                }
            }
        }
    }

    /**
     * 渲染钩子
     * @param e
     */
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent e) {
        ModSim.proxy.renderTick(e);
    }

    /**
     * 当实体加入世界赋予玩家手里第一个物品栏里一个模拟城市任命卷轴
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World worldObj = event.getWorld();

        if (!event.getWorld().isRemote && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            //欢迎来到模拟城镇,由TRHSY重制，更多资讯请关注公众号: dasha5000
            String welcome ="【"+player.getName()+"】"+I18n.format("container.sim.welcome");
            String welcomes = I18n.format("container.sim.welcomes");
            ModSimLoader.sendChat(welcome + ModSim.VERSION + welcomes);
            boolean shouldGive = ItemLoader.itemSimULoader != null && ModSimLoader.states.gameModeNumber == 999;
            if (shouldGive) {
                ItemStack starter = new ItemStack(ItemLoader.itemSimULoader);
                if (!player.inventory.addItemStackToInventory(starter)) {
                    float f = 0.7F;
                    float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(worldObj, player.posX + (double)d0, player.posY + (double)d1, player.posZ + (double)d2, new ItemStack(ItemLoader.itemSimULoader));
                    entityitem.setDefaultPickupDelay();
                    worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
    }
    /**
     * 客户端断开连接
     * @param event
     */
    @SubscribeEvent
    public void clientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        hasLoadedWorld = false;
    }
}
