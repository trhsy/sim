package com.trhsy.sim.common.util;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 更新检查器
 */
public class UpdateChecker {
    int highest = 0;
    int m1 = 0;

    public UpdateChecker(FMLPreInitializationEvent event) {
        try {
            File checks = new File(ModSimReloaded.getSimukraftFolder() + File.separator + "/buildings");
            String baseURL = "https://trhsy.github.io/sim/1.8.9/version.txt";
            if (!checks.exists()) {
                onUpdate();
            }
            String ver = downloadFile(baseURL, ModSimReloaded.getSimukraftFolder() + File.separator + "version.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("")) {
                    if (!ModSim.VERSION.contentEquals(ver)) {
                        ModSimReloaded.sendChat(I18n.format("container.sim.update_checker1") + ver + I18n.format("container.sim.update_checker2") );
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("检查sim建筑包出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    public void onUpdate() {
        try {
//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String baseURL = "https://trhsy.github.io/sim/1.8.9/Simukraft_zh_CN.zip";
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            if ("en_US".equals(lang)) {
                baseURL = "https://trhsy.github.io/sim/1.8.9/Simukraft_en_US.zip";
            }
            String unzipFilePath = ModSimReloaded.getSimukraftFolder();
            File checks = new File(unzipFilePath + File.separator);
            File[] checkss = checks.listFiles();

            for (File f : checkss) {
                deleteFile(f);
            }
            checks.mkdir();
            String simFile = unzipFilePath + File.separator + "Simukraft.zip";
            String ver = this.downloadFile(baseURL, simFile);
            if (ver != null) {
                File zipFile = new File(ver);
                //开始解压
                ModSimReloaded.log.info("开始解压：", zipFile.getName());
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
                            ModSimReloaded.log.info("创建解压文件夹：", entryDir.getName());
                        }
                    }

                }

            }

            new File(simFile).deleteOnExit();

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("检查sim建筑包出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            //e.printStackTrace();
        }


    }

    public static void deleteFile(File file) {
        ModSimReloaded.log.info("开始删除文件/文件夹");
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

    public String downloadFile(String url, String localFile) {
        File f = new File(localFile);
        if (f.exists()) {
            deleteFile(f);
        }
        ModSimReloaded.log.info("将从此链接下载文件：\n" + url);
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
}
