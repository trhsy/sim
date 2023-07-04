package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.network.client.PacketReturnHireableFolks;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.block.MineBox;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.NpcData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsBufferBuilder;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @ClassName EventLoader
 * @Description todo 事件交互
 * @Author Tian
 * @Date 2022/9/2921:05
 **/
public class EventLoader {
    /**
     * 已加载世界
     **/
    public boolean hasLoadedWorld;
    /**
     * 上次可以户连接的时间
     **/
    public long timeSinceLastClientUpdate = 0L;
    /**
     * 分钟计时器
     **/
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
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("EventLoader出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 当玩家加入的时候
     * @Date 19:19 2022/10/7
     * @Param [event]
     **/
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Thread skinThread = new Thread(() -> {
            try {
                if (event.player != null) {
                    ModSimLoader.log.info("*********************玩家加入*****************");
                    for (NpcData fd : ModSimLoader.folks) {
                        while (fd.entity == null && FMLCommonHandler.instance() != null) {
                            try {
                                fd.entity = (EntityFolk) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(fd.ID));
                            } catch (Exception var4) {
//                                ModSimLoader.log.error("玩家加入加载数据失败了" + var4.getMessage());
                            }
                        }

                        fd.sendSkinPathToClient();
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }


        });
        skinThread.start();
    }

    /**
     * 世界保存
     *
     * @param event
     */
    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        if (!event.getWorld().isRemote) {
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
     * 世界加载
     *
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
            ModSimLoader.farms.clear();
            ModSimLoader.mines.clear();
            ModSimLoader.buildings.clear();
            ModSimLoader.dayOfWeek = 0;
            ModSimLoader.gamemode = 999;
            ModSimLoader.money = 10.0F;
            this.newDay = true;
            this.timeSinceLastClientUpdate = 0L;
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
                ModSimLoader.log.info("获得保存的NPC");
                new DimensionManager();
                File npcFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "npc");
                if (!npcFolder.exists()) {
                    npcFolder.mkdirs();
                }
                buildingSaves = npcFolder.listFiles();
                for (int i = 0; i < buildingSaves.length; i++) {
                    File buildingFile = buildingSaves[i];
                    //ModSimLoader.log.info("得到Npc " + buildingFile.getName());
                    NpcData npcData=new NpcData(event.getWorld(), UUID.fromString(buildingFile.getName().split(".sk2")[0]));
                    ModSimLoader.folks.add(npcData);
                    ModSimLoader.log.info(npcData.race.skinName);
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
                    ModSimLoader.buildings.add(new Building(event.getWorld(), UUID.fromString(buildingFile.getName().split(".sk2")[0])));
                }

            } catch (Exception var7) {
                StackTraceElement element = var7.getStackTrace()[0];
                ModSimLoader.log.error("加载建筑文件出错了：" + var7.getMessage() + "行数：" + element.getLineNumber());
            }

            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
            hasLoadedWorld = true;
        }
    }

    /**
     * 钩子
     *
     * @param event
     */
    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        //每两秒更新一次检查
        if (!event.world.isRemote && System.currentTimeMillis() - this.timeSinceLastClientUpdate > 2000L) {
            this.timeSinceLastClientUpdate = System.currentTimeMillis();
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
        if (ModSimLoader.gamemode != 999 && !event.world.isRemote) {
            //是白天
            if (ModSimLoader.isDayTime(event.world)) {
                //60秒循环
                if (System.currentTimeMillis() - this.minuteTimer > 60000L) {
                    //已重生
                    boolean spawnNew = true;
                    for (NpcData starve : ModSimLoader.folks) {
                        //判断是否已死亡
                        if (starve.isDead) {
                            //若有房子异常房子
                            if (starve.home != null) {
                                starve.home.occupants.remove(starve);
                                starve.home = null;
                            }
                        } else if (starve.home == null) {
                            //只要有一个人没有住到房子里就不生成新的人
                            spawnNew = false;
                        }
                    }
                    if (spawnNew) {
                        if (event.world.playerEntities.size() > 0) {
                            ModSimLoader.log.info("所有人都有住宅，开始生成新的NPC");
                            new NpcData(event.world, false);
                        }
                    }
                    this.minuteTimer=System.currentTimeMillis();
                }
                if (!this.newDay) {
                    ModSimLoader.log.info("天亮了");
                    //播放 天亮了鸡叫
                    SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":rooster"));
                    //event.world.playSound(0,0,0,soundEvent, SoundCategory.RECORDS, 0.3F, 0.6F,false);
                    for (EntityPlayer entityPlayer : event.world.playerEntities) {
                        event.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 0.7F, 0.8F);
                    }

                    this.newDay = true;
                    if (ModSimLoader.dayOfWeek >= 6) {
                        ModSimLoader.dayOfWeek = 0;
                    } else {
                        ++ModSimLoader.dayOfWeek;
                    }
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
                        soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                        //event.world.playSound(0,0,0,soundEvent, SoundCategory.RECORDS, 0.3F, 0.6F,false);
                        for (EntityPlayer entityPlayer : event.world.playerEntities) {
                            event.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 0.3F, 0.6F);
                        }
                        //您已收集 今天的租金。
                        ModSimLoader.sendChat(I18n.format("container.sim.main_Collected") + ModSimLoader.displayMoney(rent) + I18n.format("container.sim.main_rent_today"));
                    }
                    Iterator iterator = ModSimLoader.folks.iterator();
                    NpcData f = null;
                    fs_lable:while (true) {
                        do {
                            if (!iterator.hasNext()) {
                                String hungerName = "";
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
                                        hungerName = f.getName();
                                        ++hungerCount;
                                    }
                                    if (hungerName != "") {
                                        //快饿死了！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                        String starving = I18n.format("container.sim.main_is_VERY");
                                        //其他人正在挨饿！你应该建立一个农场，杂货店，面包店或向他们扔一些食物。
                                        String others_starving = I18n.format("container.sim.others_starving");
                                        String message = hungerCount > 1 ? hungerName + starving : hungerName + I18n.format("container.sim.Mining13") + hungerCount + others_starving;
                                        ModSimLoader.sendChat(message);
                                    }
                                }
                                if (starve != null) {
                                    //设置死亡
                                    starve.entity.attackEntityFrom(DamageSource.starve, 999.0F);
                                }
                                break fs_lable;
                            }
                            f = (NpcData) iterator.next();
                        } while (f.entity == null);
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
                            String s = I18n.format("container.sim.main_is_now");
                            ModSimLoader.sendChat(f.getName() + s);
                        }
                        //超越寿命
                        if (f.age >= f.race.lifespan && this.rand.nextInt(10) == 5) {
                            //年纪大了,感觉不太好。。。哦不！
                            String s = I18n.format("container.sim.main_is_old");
                            ModSimLoader.sendChat(f.getName() + s);
                            //来自伤害999，死亡
                            f.entity.attackEntityFrom(DamageSource.starve, 999.0F);
                        }
                    }
                }
                //}
            } else if (this.newDay) {
                this.newDay = false;
            }
        }
        //实时更新人的状态
        if (!event.world.isRemote) {
            for (NpcData f : ModSimLoader.folks) {
                f.onUpdate();
            }
        }
        //停止下雨MOD-在我的世界里一直下雨的时候实现了这个！
        if (event.world != null) {
            if (event.world.isRaining() && event.world.getWorldInfo().getRainTime() > 1 && ConfigLoader.configStopRain) {
                event.world.getWorldInfo().setRaining(false);
                ModSimLoader.log.info("我讨厌下雨-停了吧");
            }
        }
    }

    /**
     * 渲染钩子
     *
     * @param e
     */
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent e) {
        ModSim.proxy.renderTick(e);
    }

    /**
     * 当实体加入世界赋予玩家手里第一个物品栏里一个模拟城市任命卷轴
     *
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World worldObj = event.getWorld();

        if (!event.getWorld().isRemote && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            //欢迎来到模拟城镇,由TRHSY重制，更多资讯请关注公众号: dasha5000
            String welcome = "【" + player.getName() + "】" + I18n.format("container.sim.welcome");
            String welcomes = I18n.format("container.sim.welcomes");
            ModSimLoader.sendChat(welcome + ModSim.VERSION + welcomes);
//            try {
//                //检查模组更新提醒
//                String baseURL = "https://trhsy.github.io/sim/1.9/version.txt";
//                String ver = ModSimLoader.downloadFile(baseURL, ModSimLoader.getSimFolder() + File.separator + "version.txt");
//                if (ver != null) {
//                    ver = ver.trim();
//                    if (!ver.contentEquals("")&&!"1.0.0 Beta".contentEquals(ver)) {
//                        if (!ModSim.VERSION.contentEquals(ver)) {
//                            ModSimLoader.sendChat(I18n.format("container.sim.update_checker1") + ver + I18n.format("container.sim.update_checker2") );
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
            boolean shouldGive = ItemLoader.itemSimULoader != null && ModSimLoader.gamemode == 999;
            if (shouldGive) {
                ItemStack starter = new ItemStack(ItemLoader.itemSimULoader);
                if (!player.inventory.addItemStackToInventory(starter)) {
                    float f = 0.7F;
                    float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(worldObj, player.posX + (double) d0, player.posY + (double) d1, player.posZ + (double) d2, new ItemStack(ItemLoader.itemSimULoader));
                    entityitem.setDefaultPickupDelay();
                    worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
    }

    /**
     * 客户端断开连接
     *
     * @param event
     */
    @SubscribeEvent
    public void clientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        hasLoadedWorld = false;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 世界渲染
     * @Date 17:12 2022/10/31
     * @Param [event]
     **/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        World world = Minecraft.getMinecraft().theWorld;
        for (EntityPlayer player : world.playerEntities) {
            if (ModSimClientLoader.previewPos1 != null && ModSimClientLoader.previewPos2 != null) {
                drawBoundingBox(player, ModSimClientLoader.previewPos1, ModSimClientLoader.previewPos2, true, 4.0F, event);
            }
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 预览程序
     * @Date 17:19 2022/10/31
     * @Param [player, posA, posB, smooth, width, event]
     **/
    public static void drawBoundingBox(EntityPlayer player, Vec3d posA, Vec3d posB, boolean smooth, float width, RenderWorldLastEvent event) {
        GL11.glPushAttrib(8192);
        GL11.glDisable(2884);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) event.getPartialTicks();
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) event.getPartialTicks();
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.getPartialTicks();
        Vec3d pos = new Vec3d(d0, d1, d2);
        GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
        Color c = new Color(255, 0, 0, 150);
        GL11.glColor4d((double) c.getRed(), (double) c.getGreen(), (double) c.getBlue(), (double) c.getAlpha());
        GL11.glLineWidth(width);
        GL11.glDepthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        double dx = posA.xCoord - posB.xCoord > 0.0D ? -Math.abs(posA.xCoord - posB.xCoord) : Math.abs(posA.xCoord - posB.xCoord);
        double dy = Math.abs(posA.yCoord - posB.yCoord);
        double dz = posA.zCoord - posB.zCoord > 0.0D ? -Math.abs(posA.zCoord - posB.zCoord) : Math.abs(posA.zCoord - posB.zCoord);
        double xOf = dx > 0.0D ? 0.0D : 1.0D;
        double zOf = dz > 0.0D ? 0.0D : 1.0D;
        posA = posA.addVector(xOf, 0.0D, zOf);
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.xCoord + dx, posA.yCoord + dy, posA.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }
}
