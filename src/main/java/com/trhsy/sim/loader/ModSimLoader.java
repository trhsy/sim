package com.trhsy.sim.loader;

import com.trhsy.sim.block.BlockMarker;
import com.trhsy.sim.gui.npc.GuiFolk;
import com.trhsy.sim.gui.GuiRunMod;
import com.trhsy.sim.gui.block.GuiBlockConstructorBlock;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.block.MineBox;
import com.trhsy.sim.npc.build.BlueprintRequirements;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.DynamicSkin;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.util.GameStates;
import com.trhsy.sim.entity.util.NpcSkin;
import com.trhsy.sim.entity.util.NpcIdentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * sim 加载信息
 *
 * @author Administrator
 */
public class ModSimLoader {
    /**
     * 全局日志调用
     */
    public static Logger log;
    /**
     * 包含他们正在玩的这个关卡的所有游戏状态和设置
     **/
    public static GameStates states = new GameStates();

    /**
     * npc 数据
     **/
    public static List<NpcData> folks = new CopyOnWriteArrayList();
    /**
     * 建筑
     **/
    public static List<Building> buildings = new CopyOnWriteArrayList();
    public static List<FarmBox> farms = new CopyOnWriteArrayList();
    public static List<MineBox> mines = new CopyOnWriteArrayList();
    public static List<BlockMarker> markers = new CopyOnWriteArrayList();
    /**
     * 临时可雇佣Npc姓名
     **/
    public static List<NpcIdentity> tempHireableNpcNames = new CopyOnWriteArrayList();
    /**
     * 建筑蓝图
     */
    public static List<BuildingBlueprint> buildingBlueprints = new CopyOnWriteArrayList();
    public static List<BlueprintRequirements> blueprintReqs = new CopyOnWriteArrayList();
    /**
     * 是否加载npc
     **/
    public static boolean hasLoadedFolks = false;
    /**
     * npc 皮肤
     **/
    public static List<NpcSkin> folkSkins = new CopyOnWriteArrayList();
    public static List<DynamicSkin> skins = new CopyOnWriteArrayList();
    /**
     * 要构建的
     **/
    public static BlockPos previewConstructor;
    /**
     * 构建上一页
     **/
    public static int constructorPreviousPage;
    /**
     * 蓝图
     */
    public static BuildingBlueprint savedBlueprint;
    /**
     * 预览位置
     */
    public static Vec3d previewPos1;
    public static Vec3d previewPos2;

    /**
     * 运行模组
     */
    public static void openSetupGui() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRunMod());
    }

    /**
     * 获取模拟城市建筑文文件夹
     *
     * @return
     */
    public static String getSimFolder() {
        try {
            String strmc = (new File(".")).getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            File checks = new File(strmc + File.separator + "mods" + File.separator + "sim");
            if (!checks.exists() && !checks.isDirectory()) {
                ModSimLoader.log.warn("SimCity error - Mod未正确安装, ./minecraft/mods/sim/ 文件夹丢失了 - 重新创建此文件夹");
                checks.mkdir();
            }
            return (checks).getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 添加金额
     * @Date 14:05 2022/10/18
     * @Param [amount]
     **/
    public static void addMoney(float amount) {
        if (ModSimLoader.states.gameModeNumber == 0) {
            ModSimLoader.states.credits += amount;
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
        }

    }

    /**
     * 显示金额格式
     **/
    public static String displayMoney(float money) {
        String output = null;
        try {
            DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
            output = myFormatter.format(money);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("displayMoney出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return output;
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
            if (npcIdentity1.id.equals(uuid.toString())) {
                return npcIdentity1;
            }
        }
        return npcIdentity;
    }

    /**
     * 以字符串形式获取“.minecraft/saves/游戏世界名称/sim/”文件夹 保存数据文件夹
     *
     * @return
     */
    public static String getSavesDataFolder() {
        String ret = "";
        try {
            File worldPath = DimensionManager.getCurrentSaveRootDirectory().getAbsoluteFile();
            File mainFolder = new File(worldPath.getAbsolutePath() + File.separator + "sim" + File.separator);
            ret = mainFolder.getAbsolutePath();
            /*String strmc = (new File(".")).getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            File test = new File(strmc + "saves");
            if (test.exists()) {
                //客户端
                ret = (new File(strmc + File.separator + "saves" + File.separator  + "sim" + File.separator)).getAbsolutePath() + File.separator;
            } else {
                //服务器端
                strmc = strmc + File.separator + "sim" + File.separator;
                ret = (new File(strmc)).getAbsolutePath();
            }
            File f = new File(ret);*/
            if (!mainFolder.exists()) {
                mainFolder.mkdirs();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("getSavesDataFolder出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return ret;
    }

    /**
     * 从配置文件中读取内容
     *
     * @param fullFilename
     * @return
     */
    public static List<String> loadSK2(String fullFilename) {
        CopyOnWriteArrayList ret = new CopyOnWriteArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fullFilename));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                ret.add(line);
            }

            br.close();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("loadSK2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

        return ret;
    }

    /**
     * @return com.trhsy.sim.npc.Building
     * @Author fan
     * @Description //TODO 找到空房子
     * @Date 14:05 2022/10/18
     * @Param []
     **/
    public static Building getEmptyHome() {
        Building empty = null;
        for (Building b : buildings) {
            if (b.buildingType.contentEquals("Residential") && b.occupants.size() < 1) {
                empty = b;
                break;
            }
        }
        return empty;
    }

    /**
     * 判断是否白天 当世界上是白天时返回true，忽略其他世界时间
     *
     * @return
     */
    public static boolean isDayTime(World world) {
        //if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000 <= 11999) {
        //    return true;
        //} else {
        //    return false;
        //}
        boolean falg = false;
        try {
            falg = world.getWorldInfo().getWorldTime() % 24000L <= 11999L;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("isDayTime出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return falg;

    }

    /**
     * 帮助功能，向所有世界/维度的所有玩家发送聊天信息
     *
     * @param theText
     */
    public static void sendChat(String theText) {
        try {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(theText));
           /* WorldServer[] worldServers = MinecraftServer.getServer().worldServers;
            int length = worldServers.length;
            for (World w : MinecraftServer.getServer().worldServers) {
                if (!w.isRemote) {
                    for (int k = 0; k < w.playerEntities.size(); ++k) {
                        EntityPlayer p = (EntityPlayer) w.playerEntities.get(k);
                        p.addChatComponentMessage(new ChatComponentText(theText));
                    }
                }
            }*/
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("sendChat出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 下载version文件并返回内容
     * @Date 20:26 2022/10/8
     * @Param [url, localFile]
     **/
    public static String downloadFile(String url, String localFile) {
        String ret = "";
        File f = new File(localFile);
        if (f.exists()) {
            deleteFile(f);
        }
        ModSimLoader.log.info("将从此链接下载文件：\n" + url);
        try {
            URL aURL = new URL(url);
            InputStream is = aURL.openStream();
            BufferedInputStream in = new BufferedInputStream(is);
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
        } catch (Exception e) {
            ret = "";
            e.printStackTrace();
        }

        return ret;
    }
    public static String downloadSimFile(String url, String localFile) {
        File f = new File(localFile);
        if (f.exists()) {
            deleteFile(f);
        }
        ModSimLoader.log.info("将从此链接下载文件：\n" + url);
        try {
            URL aURL = new URL(url);
            InputStream is = aURL.openStream();
            BufferedInputStream in = new BufferedInputStream(is);
            FileOutputStream fos = new FileOutputStream(localFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[4096];
            boolean var8 = false;

            int x;
            while ((x = in.read(data, 0, 4096)) >= 0) {
                bout.write(data, 0, x);
            }

            bout.flush();
            //ret = new String(data);
            bout.close();
            in.close();
        } catch (Exception e) {
            //ret = "";
            //var9.printStackTrace();
        }

        return localFile;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 删除旧文件
     * @Date 20:25 2022/10/8
     * @Param [file]
     **/
    public static void deleteFile(File file) {
        ModSimLoader.log.info("开始删除文件/文件夹");
        if (file.exists()) {
            file.delete();
        }
        if (file.exists()) {
            File[] paths = file.listFiles();
            for (File str : paths) {
                deleteFile(str);
            }
            file.delete();
            paths = null;    // lets gc do its works
        }
        file = null;    // lets gc do its works
    }

    /**
     * 保存配置文件
     *
     * @param fullFilename
     * @param strings
     */
    public static void saveSK2(String fullFilename, List<String> strings) {
        try {
            File f = new File(fullFilename);
            if (!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilename));
            for (String line : strings) {
                bw.write(line + "\r\n");
            }
            bw.close();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("saveSK2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            //var5.printStackTrace();
        }

    }

    /**
     * @return com.trhsy.sim.npc.NpcData
     * @Author fan
     * @Description //TODO 根据uid获取NPC数据
     * @Date 14:04 2022/10/18
     * @Param [uid]
     **/
    public static NpcData getFolkDataByUID(String uid) {
        NpcData npcDatas = null;
        for (NpcData npcData : folks) {
            if (npcData.ID.contentEquals(uid.toLowerCase())) {
                npcDatas = npcData;
            }
        }
        return npcDatas;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 打开npcUI
     * @Date 14:03 2022/10/18
     * @Param [message]
     **/
    public static void openFolkGui(PacketOpenFolkGui message) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiFolk(message));
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO d打开建筑箱的gui
     * @Date 16:23 2022/10/19
     * @Param [pos, bDir]
     **/
    public static void openConstructorGui(BlockPos pos, int bDir) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockConstructorBlock(pos, bDir));
    }

    public static void openConstructorGui(BlockPos pos, int bDir, NpcIdentity folk) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockConstructorBlock(pos, bDir, folk));
    }

    /**
     * @return java.util.List<com.trhsy.sim.entity.util.NpcIdentity>
     * @Author fan
     * @Description //TODO 失业人员
     * @Date 17:59 2022/10/19
     * @Param []
     **/
    public static List<NpcIdentity> getUnemployedFolks() {
        List<NpcIdentity> hireables = new ArrayList();
        for (NpcIdentity cfi : tempHireableNpcNames) {

            if (cfi.job.contentEquals(I18n.format("container.sim.folkData1")) && Integer.parseInt(cfi.age) >= Integer.parseInt(cfi.maturityAge)) {
                hireables.add(cfi);
            }
        }

        return hireables;
    }
    /**
     * @Author fan
     * @Description //TODO 得到建筑蓝图
     * @Date 14:41 2022/10/21
     * @Param [type, searchText]
     * @return java.util.List<com.trhsy.sim.npc.build.BuildingBlueprint>
     **/
    public static List<BuildingBlueprint> getBlueprintsByType(String type, String searchText) {
        List<BuildingBlueprint> typedBlues = new ArrayList();
        for (BuildingBlueprint bb : buildingBlueprints) {

            if (searchText != "" &&searchText != null) {
                if(bb.name.contains(searchText)){
                    if (bb.buildingType.contentEquals(type)) {
                        typedBlues.add(bb);
                    }
                }
            }else{
                if (bb.buildingType.contentEquals(type)) {
                    typedBlues.add(bb);
                }
            }
        }
        return typedBlues;

    }
    /**
     * @Author fan
     * @Description //TODO 是建筑中的块
     * @Date 14:46 2022/10/21
     * @Param [v3]
     * @return boolean
     **/
    public static boolean isBlockInBuilding(V3 v3) {
        Iterator var1 = buildings.iterator();

        while(var1.hasNext()) {
            Building b = (Building)var1.next();
            Iterator var3 = b.structure.iterator();

            while(var3.hasNext()) {
                V3 bv3 = (V3)var3.next();
                if (v3.equals(bv3)) {
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * @Author fan
     * @Description //TODO 获得最近的农场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     * @return java.util.List<com.trhsy.sim.block.FarmBox>
     **/
    public static List<FarmBox> getClosestFarm(final V3 pos) {
        List<FarmBox> fs = new CopyOnWriteArrayList<>();
        for (FarmBox f:farms){
            fs.add(f);
        }
        Collections.sort(fs, new Comparator<FarmBox>() {
            public int compare(FarmBox f1, FarmBox f2) {
                if (f1.loc.getDistanceTo(pos) > f2.loc.getDistanceTo(pos)) {
                    return 1;
                } else {
                    return f1.loc.getDistanceTo(pos) < f2.loc.getDistanceTo(pos) ? -1 : 0;
                }
            }
        });
        return (List)(fs.size() > 3 ? fs.subList(0, 2) : fs);
    }
    /**
     * @Author fan
     * @Description //TODO 矿场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     * @return com.trhsy.sim.block.MineBox
     **/
    public static MineBox getMine(V3 pos) {
        MineBox m1=null;
        for (MineBox m:mines){
            if(!m.loc.equals(pos)){
                m1=m;
                return m1;
            }
        }

        return m1;
    }
    /**
     * @Author fan
     * @Description //TODO 农场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     * @return com.trhsy.sim.block.FarmBox
     **/
    public static FarmBox getFarm(V3 pos) {
        Iterator var1 = farms.iterator();

        FarmBox f;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            f = (FarmBox)var1.next();
        } while(!f.loc.equals(pos));

        return f;
    }
    /**
     * @Author fan
     * @Description //TODO 根据工作获取最近的建筑
     * @Date 15:53 2022/10/21
     * @Param [jobType, pos]
     * @return java.util.List<com.trhsy.sim.npc.build.Building>
     **/
    public static List<Building> getClosestBuildingByJob(String jobType, final V3 pos) {
        List<Building> bs = new ArrayList();
        Iterator var3 = buildings.iterator();

        while(var3.hasNext()) {
            Building b = (Building)var3.next();
            if (b.jobType.contentEquals(jobType)) {
                bs.add(b);
                ModSimLoader.log.info("找到建筑： " + b.buildingName);
            }
        }

        Collections.sort(bs, new Comparator<Building>() {
            public int compare(Building b1, Building b2) {
                if (b1.controlXYZ.getDistanceTo(pos) > b2.controlXYZ.getDistanceTo(pos)) {
                    return 1;
                } else {
                    return b1.controlXYZ.getDistanceTo(pos) < b2.controlXYZ.getDistanceTo(pos) ? -1 : 0;
                }
            }
        });
        return bs;
    }
    /**
     * @Author fan
     * @Description //TODO 获取最近的建筑
     * @Date 15:53 2022/10/21
     * @Param [buildIn, pos]
     * @return java.util.List<com.trhsy.sim.npc.build.Building>
     **/
    public static List<Building> getClosestBuilding(String buildIn, final V3 pos) {
        List<Building> bs = new ArrayList();
        Iterator var3 = buildings.iterator();

        while(var3.hasNext()) {
            Building b = (Building)var3.next();
            if (b.buildingName == buildIn) {
                bs.add(b);
            }
        }

        Collections.sort(bs, new Comparator<Building>() {
            public int compare(Building b1, Building b2) {
                if (b1.controlXYZ.getDistanceTo(pos) > b2.controlXYZ.getDistanceTo(pos)) {
                    return 1;
                } else {
                    return b1.controlXYZ.getDistanceTo(pos) < b2.controlXYZ.getDistanceTo(pos) ? -1 : 0;
                }
            }
        });
        return bs;
    }
    /**
     * @Author fan
     * @Description //TODO 建筑位置
     * @Date 15:53 2022/10/21
     * @Param [pos]
     * @return com.trhsy.sim.npc.build.Building
     **/
    public static Building getBuildingByV3(V3 pos) {
        for(int i = 0; i < buildings.size(); ++i) {
            if (((Building)buildings.get(i)).controlXYZ.toString().contentEquals(pos.toString())) {
                return (Building)buildings.get(i);
            }
        }

        return null;
    }
    /**
     * @Author fan
     * @Description //TODO 加载建筑
     * @Date 15:53 2022/10/21
     * @Param []
     * @return void
     **/
    public static void loadAllBuildings() {
        //开始加载所有建筑
//        File[] admBuildings = (new File(getSimFolder() + File.separator + "Buildings" + File.separator + "Administrative")).listFiles(File::isDirectory);
        //商业
        File[] comBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "commercial")).listFiles();
        File[] decBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "decorative")).listFiles();
        File[] indBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "industrial")).listFiles();
        File[] othBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "other")).listFiles();
        File[] resBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "residential")).listFiles();
        File[] speBuildings = (new File(getSimFolder() + File.separator + "buildings" + File.separator + "special")).listFiles();
        File[] var7;
        int var8;
        int var9;
        File buildingFolder;
        BuildingBlueprint b;
        /*if (admBuildings != null) {
            var7 = admBuildings;
            var8 = admBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(building);
                if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }

                ModSimLoader.buildingBlueprints.add(b);
            }
        }*/

        if (comBuildings != null) {
            var7 = comBuildings;
            var8 = comBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (decBuildings != null) {
            var7 = decBuildings;
            var8 = decBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (indBuildings != null) {
            var7 = indBuildings;
            var8 = indBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (othBuildings != null) {
            var7 = othBuildings;
            var8 = othBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (resBuildings != null) {
            var7 = resBuildings;
            var8 = resBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (speBuildings != null) {
            var7 = speBuildings;
            var8 = speBuildings.length;

            for(var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                //building = new File(buildingFolder.getAbsolutePath() + File.separator + buildingFolder.getName() + ".txt");
                b = new BuildingBlueprint(buildingFolder);
                /*if ((new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).exists()) {
                    styles = (new File(buildingFolder.getAbsolutePath() + File.separator + "Styles")).listFiles();
                    var14 = styles;
                    var15 = styles.length;

                    for(var16 = 0; var16 < var15; ++var16) {
                        style = var14[var16];
                        if (style.getName().toLowerCase().endsWith(".txt")) {
                            b.styles.add(new BuildingBlueprint(style));
                        }
                    }
                }*/

                ModSimLoader.buildingBlueprints.add(b);
            }
        }

    }
    /**
     * @Author fan
     * @Description //TODO 根据uid获取建筑
     * @Date 15:54 2022/10/21
     * @Param [uuid]
     * @return com.trhsy.sim.npc.build.Building
     **/
    public static Building getBuildingByUUID(String uuid) {
        Building b1=null;
        for ( Building b:buildings){
            if(!b.ID.toString().contentEquals(uuid)){
                b1=b;
                return b1;
            }
        }
        return b1;
    }

    public static String getPathFromUUID(String UUID) {
        for(int i = 0; i < folkSkins.size(); ++i) {
            if ((folkSkins.get(i)).UUID.contentEquals(UUID)) {
                return (folkSkins.get(i)).skinPath;
            }
        }

        return "";
    }
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
