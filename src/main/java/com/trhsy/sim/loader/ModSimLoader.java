package com.trhsy.sim.loader;

import com.trhsy.sim.gui.GuiFolk;
import com.trhsy.sim.gui.GuiRunMod;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import com.trhsy.sim.npc.Building;
import com.trhsy.sim.npc.DynamicSkin;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.util.GameStates;
import com.trhsy.sim.entity.util.NpcSkin;
import com.trhsy.sim.entity.util.NpcIdentity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * sim 加载信息
 * @author Administrator
 */
public class ModSimLoader {
    /**
     * 全局日志调用
     */
    public static Logger log;
    /**包含他们正在玩的这个关卡的所有游戏状态和设置**/
    public static GameStates states = new GameStates();
    /**天数**/
    public static int day;
    /**npc 数据**/
    public static List<NpcData> folks = new CopyOnWriteArrayList();
    /**建筑**/
    public static List<Building> buildings = new CopyOnWriteArrayList();
    /**临时可雇佣Npc姓名**/
    public static List<NpcIdentity> tempHireableNpcNames = new CopyOnWriteArrayList();

    /**是否加载npc**/
    public static boolean hasLoadedFolks = false;
    /**npc 皮肤**/
    public static List<NpcSkin> folkSkins = new CopyOnWriteArrayList();
    public static List<DynamicSkin> skins = new CopyOnWriteArrayList();
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
     * @Author fan
     * @Description //TODO 添加金额
     * @Date 14:05 2022/10/18
     * @Param [amount]
     * @return void
     **/
    public static void addMoney(float amount) {
        if (ModSimLoader.states.gameModeNumber == 0) {
            ModSimLoader.states.credits += amount;
            NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
        }

    }
    /**显示金额格式**/
    public static String displayMoney(float money) {
        String output = null;
        try {
            DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
            output = myFormatter.format( money);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("displayMoney出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return output;
    }
    /**
     * @Author fan
     * @Description //TODO 根据uid 获取NPC信息
     * @Date 14:05 2022/10/18
     * @Param [uuid]
     * @return com.trhsy.sim.entity.util.NpcIdentity
     **/
    public static NpcIdentity getFolkByUUID(UUID uuid) {
        NpcIdentity npcIdentity=null;
        for(NpcIdentity npcIdentity1:tempHireableNpcNames) {
            if(npcIdentity1.id.equals(uuid.toString())){
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
            File mainFolder = new File(worldPath.getAbsolutePath() + File.separator + "sim"+ File.separator);
            ret=mainFolder.getAbsolutePath();
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
     * @Author fan
     * @Description //TODO 找到空房子
     * @Date 14:05 2022/10/18
     * @Param []
     * @return com.trhsy.sim.npc.Building
     **/
    public static Building getEmptyHome() {
        Building empty = null;
        for (Building b:buildings){
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
     * @Author fan
     * @Description //TODO 下载version文件并返回内容
     * @Date 20:26 2022/10/8
     * @Param [url, localFile]
     * @return java.lang.String
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
    /**
     * @Author fan
     * @Description //TODO 删除旧文件
     * @Date 20:25 2022/10/8
     * @Param [file]
     * @return void
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
     * @param fullFilename
     * @param strings
     */
    public static void saveSK2(String fullFilename, List<String> strings) {
        try {
            File f=new File(fullFilename);
            if(!f.exists()){
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullFilename));
            for (String line:strings){
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
     * @Author fan
     * @Description //TODO 根据uid获取NPC数据
     * @Date 14:04 2022/10/18
     * @Param [uid]
     * @return com.trhsy.sim.npc.NpcData
     **/
    public static NpcData getFolkDataByUID(String uid) {
        NpcData npcDatas=null;
        for (NpcData npcData:folks){
            if(npcData.ID.contentEquals(uid.toLowerCase())){
                npcDatas=npcData;
            }
        }
        return npcDatas;
    }

    /**
     * @Author fan
     * @Description //TODO 打开npcUI
     * @Date 14:03 2022/10/18
     * @Param [message]
     * @return void
     **/
    public static void openFolkGui(PacketOpenFolkGui message) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiFolk(message));
    }
}
