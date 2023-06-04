package com.trhsy.sim.loader;

import com.trhsy.sim.gui.block.*;
import com.trhsy.sim.gui.npc.GuiFolk;
import com.trhsy.sim.gui.GuiRunMod;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.block.MineBox;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.DynamicSkin;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.npc.build.TerrainTypeRequitrements;
import com.trhsy.sim.util.FarmType;
import com.trhsy.sim.entity.util.NpcIdentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * sim 服务器加载信息
 *
 * @author Administrator
 */
public class ModSimLoader {
    /**
     * 全局日志调用
     */
    public static Logger log;

    /**
     * npc 数据
     **/
    public static List<NpcData> folks = new CopyOnWriteArrayList();
    /**
     * 建筑
     **/
    public static List<Building> buildings = new CopyOnWriteArrayList();
    /*养殖箱*/
    public static List<FarmBox> farms = new CopyOnWriteArrayList();
    /*挖矿箱*/
    public static List<MineBox> mines = new CopyOnWriteArrayList();

    /**
     * 建筑蓝图
     */
    public static List<BuildingBlueprint> buildingBlueprints = new CopyOnWriteArrayList();

    public static List<TerrainTypeRequitrements> terrainTypeReqs = new CopyOnWriteArrayList();
    /**
     * 是否加载npc
     **/
    public static boolean hasLoadedFolks = false;
    /**
     * npc 皮肤
     **/
    public static List<DynamicSkin> skins = new CopyOnWriteArrayList();

    /**
     * 游戏模式编号
     **/
    public static int gamemode = 999;
    //金钱
    public static float money = 10.0F;
    /**
     * @Author fan
     * @Description //TODO 周的某天
     * @Date 11:15 2023/4/29
     * @Param
     * @return
     **/
    public static int dayOfWeek = 0;

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
            File checks = new File(strmc + File.separator + "resources" + File.separator + "sim");
            if (!checks.exists() && !checks.isDirectory()) {
                ModSimLoader.log.warn("SimCity error - Mod未正确安装, ./minecraft/resources/sim/ 文件夹丢失了 - 重新创建此文件夹");
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
        if (gamemode != 1) {
            money += amount;
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
            InputStream inputStream =new FileInputStream(new File(fullFilename));
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            for (String line = br.readLine(); line != null; line =br.readLine()) {
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
            //住宅的空房子
            if (b.buildingType.contentEquals(I18n.format("container.sim.sim_gui_BC_Residential")) && b.occupants.size() < 1) {
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
//            MinecraftServer.getServer().playerList.getPlayerList().sendChatMsg(new TextComponentString(theText));
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(theText));

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
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("下载version文件并返回内容出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
        for (NpcData npcData : ModSimLoader.folks) {
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
    public static void openConstructorGui(BlockPos pos, int bDir, int dimension) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockConstructorBlock(pos, bDir, dimension));
    }

    public static void openConstructorGui(BlockPos pos, int bDir, NpcIdentity folk, int dimension) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockConstructorBlock(pos, bDir, folk, dimension));
    }


    /**
     * @return java.util.List<com.trhsy.sim.npc.build.BuildingBlueprint>
     * @Author fan
     * @Description //TODO 得到建筑蓝图
     * @Date 14:41 2022/10/21
     * @Param [type, searchText]
     **/
    public static List<BuildingBlueprint> getBlueprintsByType(String type, String searchText) {
        List<BuildingBlueprint> typedBlues = new CopyOnWriteArrayList<>();
        for (BuildingBlueprint bb : buildingBlueprints) {

            if (searchText != "" && searchText != null) {
                if (bb.name.contains(searchText)) {
                    if (bb.buildingType.contentEquals(type)) {
                        typedBlues.add(bb);
                    }
                }
            } else {
                if (bb.buildingType.contentEquals(type)) {
                    typedBlues.add(bb);
                }
            }
        }
        return typedBlues;

    }

    /**
     * @return com.trhsy.sim.npc.build.BuildingBlueprint
     * @Author fan
     * @Description //TODO 根据名字获取蓝图
     * @Date 20:04 2022/11/4
     * @Param [name]
     **/
    public static BuildingBlueprint getBlueprintsByName(String name) {
        BuildingBlueprint buildingBlueprint = null;
        for (BuildingBlueprint bb : buildingBlueprints) {
            if (name.equals(bb.name)) {
                buildingBlueprint = bb;
            }
        }
        return buildingBlueprint;
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 是建筑中的块
     * @Date 14:46 2022/10/21
     * @Param [v3]
     **/
    public static boolean isBlockInBuilding(V3 v3) {
        Iterator var1 = buildings.iterator();

        while (var1.hasNext()) {
            Building b = (Building) var1.next();
            Iterator var3 = b.structure.iterator();

            while (var3.hasNext()) {
                V3 bv3 = (V3) var3.next();
                if (v3.equals(bv3)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return java.util.List<com.trhsy.sim.block.FarmBox>
     * @Author fan
     * @Description //TODO 获得最近的农场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     **/
    public static List<FarmBox> getClosestFarm(final V3 pos, String fType) {
        List<FarmBox> fs = new CopyOnWriteArrayList<>();
        for (FarmBox f : farms) {
            if (f.farmType.toString().equals(fType) && f.employee != null) {
                fs.add(f);
            }
        }
        Collections.sort(fs, new Comparator<FarmBox>() {
            @Override
            public int compare(FarmBox f1, FarmBox f2) {
                if (f1.loc.getDistanceTo(pos) > f2.loc.getDistanceTo(pos)) {
                    return 1;
                } else {
                    return f1.loc.getDistanceTo(pos) < f2.loc.getDistanceTo(pos) ? -1 : 0;
                }
            }
        });
        return (List) (fs.size() > 3 ? fs.subList(0, 2) : fs);
    }

    /**
     * @return com.trhsy.sim.block.MineBox
     * @Author fan
     * @Description //TODO 矿场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     **/
    public static MineBox getMine(V3 pos) {
        Iterator var1 = mines.iterator();
        MineBox f;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            f = (MineBox) var1.next();
        } while (!f.loc.equals(pos));

        return f;
    }

    /**
     * @return com.trhsy.sim.block.FarmBox
     * @Author fan
     * @Description //TODO 农场
     * @Date 14:46 2022/10/21
     * @Param [pos]
     **/
    public static FarmBox getFarm(V3 pos) {
        Iterator var1 = farms.iterator();

        FarmBox f;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            f = (FarmBox) var1.next();
        } while (!f.loc.equals(pos));

        return f;
    }

    /**
     * @return java.util.List<com.trhsy.sim.npc.build.Building>
     * @Author fan
     * @Description //TODO 根据工作获取最近的建筑
     * @Date 15:53 2022/10/21
     * @Param [jobType, pos]
     **/
    public static List<Building> getClosestBuildingByJob(String jobType, final V3 pos) {
        List<Building> bs = new CopyOnWriteArrayList<>();
        Iterator var3 = buildings.iterator();

        while (var3.hasNext()) {
            Building b = (Building) var3.next();
            if (b.jobType.contentEquals(jobType) && b.occupants != null) {
                bs.add(b);
                ModSimLoader.log.info("找到建筑： " + b.buildingName);
            }
        }

        Collections.sort(bs, new Comparator<Building>() {
            @Override
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
     * @return java.util.List<com.trhsy.sim.npc.build.Building>
     * @Author fan
     * @Description //TODO 获取最近的建筑
     * @Date 15:53 2022/10/21
     * @Param [buildIn, pos]
     **/
    public static List<Building> getClosestBuilding(String buildIn, final V3 pos) {
        List<Building> bs = new CopyOnWriteArrayList();
        Iterator var3 = buildings.iterator();

        while (var3.hasNext()) {
            Building b = (Building) var3.next();
            if (b.buildingName == buildIn) {
                bs.add(b);
            }
        }

        Collections.sort(bs, new Comparator<Building>() {
            @Override
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
     * @return com.trhsy.sim.npc.build.Building
     * @Author fan
     * @Description //TODO 建筑位置
     * @Date 15:53 2022/10/21
     * @Param [pos]
     **/
    public static Building getBuildingByV3(V3 pos) {
        for (int i = 0; i < buildings.size(); ++i) {
            if (((Building) buildings.get(i)).controlXYZ.toString().contentEquals(pos.toString())) {
                return (Building) buildings.get(i);
            }
        }

        return null;
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 加载建筑
     * @Date 15:53 2022/10/21
     * @Param []
     **/
    public static void loadAllBuildings() {
        //建筑文件检查
        File checks = new File(ModSimLoader.getSimFolder() + File.separator + "/buildings");
        if (!checks.exists()) {
            //onUpdate();
        }
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

        if (comBuildings != null) {
            var7 = comBuildings;
            var8 = comBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (decBuildings != null) {
            var7 = decBuildings;
            var8 = decBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (indBuildings != null) {
            var7 = indBuildings;
            var8 = indBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (othBuildings != null) {
            var7 = othBuildings;
            var8 = othBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (resBuildings != null) {
            var7 = resBuildings;
            var8 = resBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }

        if (speBuildings != null) {
            var7 = speBuildings;
            var8 = speBuildings.length;

            for (var9 = 0; var9 < var8; ++var9) {
                buildingFolder = var7[var9];
                b = new BuildingBlueprint(buildingFolder);
                ModSimLoader.buildingBlueprints.add(b);
            }
        }
        Collections.sort(ModSimLoader.buildingBlueprints);

    }

    public static void onUpdate() {
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
        }


    }

    /**
     * @return com.trhsy.sim.npc.build.Building
     * @Author fan
     * @Description //TODO 根据uid获取建筑
     * @Date 15:54 2022/10/21
     * @Param [uuid]
     **/
    public static Building getBuildingByUUID(String uuid) {
        Building b1 = null;
        for (Building b : buildings) {
            if (b.ID.toString().contentEquals(uuid)) {
                b1 = b;
                return b1;
            }
        }
        return b1;
    }

    /**
     * 打开控制箱
     */
    public static void openControlGui(V3 v3, String buildingId, String buildingName, String jobName, String bType, String author) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockControllerBlock(v3, buildingId, buildingName, jobName, bType, author));
    }

    /**
     * 打开控制箱
     */
    public static void openControlGui(V3 v3, String buildingId, NpcIdentity folk, String buildingName, String jobName, String bType, String author) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockControllerBlock(v3, folk, buildingId, buildingName, jobName, bType, author));
    }

    /**
     * 打开控制箱
     */
    public static void openControlGui(V3 v3, String buildingId, NpcIdentity folk, boolean isResidential, String buildingName, String jobName, String bType, String author) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockControllerBlock(v3, folk, buildingId, isResidential, buildingName, jobName, bType, author));
    }

    public static void openFarmGui(UUID id, V3 loc, EnumFacing facing, FarmType farmType, int x, int z) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockFarmBlock(id, loc, facing, farmType, x, z));
    }

    public static void openFarmGui(UUID id, V3 loc, EnumFacing facing, FarmType farmType, int x, int z, NpcIdentity folk) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockFarmBlock(id, loc, facing, farmType, x, z, folk));
    }


    public static void openMineGui(UUID id, V3 loc, EnumFacing facing, int x, int z) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockMineBlock(id, loc, facing, x, z));
    }

    public static void openMineGui(UUID id, V3 loc, EnumFacing facing, int x, int z, NpcIdentity folk) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockMineBlock(id, loc, facing, x, z, folk));
    }

    public static void saveStates() {
        try {
            String folder = ModSimLoader.getSavesDataFolder();
            List<String> strings = new CopyOnWriteArrayList();
            //金额
            strings.add("credits|" + money);
            //游戏状态
            strings.add("gamemode|" + gamemode);
            //星期几
            strings.add("dayofweek|" + dayOfWeek);

            ModSimLoader.saveSK2(folder + "settings.sk2", strings);
            ModSimLoader.log.info("游戏状态: saveStates() called BOTH sides, 金额存储为 " + money);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("saveStates出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 加载配置文件
     */
    public static void loadStates() {
        try {
            File f = new File(ModSimLoader.getSavesDataFolder() + "settings.sk2");
            if (!f.exists()) {
                saveStates();
            } else {
                loadStates2();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("loadStates出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 从配置文件读取并写入
     */
    private static void loadStates2() {
        try {
            List<String> strings = ModSimLoader.loadSK2(ModSimLoader.getSavesDataFolder() + "settings.sk2");
            for (String line : strings) {
                if (line.contains("|")) {
                    int m1 = line.indexOf("|");
                    String name = line.substring(0, m1);
                    String value = line.substring(m1 + 1);
                    if ("credits".equals(name)) {
                        money = Float.parseFloat(value);
                    } else if ("gamemode".equals(name)) {
                        gamemode = Integer.parseInt(value);
                    } else if ("dayofweek".equals(name)) {
                        dayOfWeek = Integer.parseInt(value);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("loadStates2出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    public static void openMarkerGui(V3 v3, int dimension) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlockMarker(v3, dimension));
    }
}
