package com.trhsy.sim.loader;

import com.trhsy.sim.gui.GuiRunMod;
import com.trhsy.sim.network.GameStates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;

/**
 * sim 加载信息
 * @author Administrator
 */
public class ModSimLoader {
    /**
     * 全局日志调用
     */
    public static Logger log;
    /*
       包含他们正在玩的这个关卡的所有游戏状态和设置
        */
    public static GameStates states = new GameStates();
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
    public static String getSimukraftFolder() {
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
            FMLCommonHandler.instance().getMinecraftServerInstance().getServer().addChatMessage(new TextComponentString(theText));
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
     * @Description //TODO 下载文件
     * @Date 20:26 2022/10/8
     * @Param [url, localFile]
     * @return java.lang.String
     **/
    public static String downloadFile(String url, String localFile) {
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
}
