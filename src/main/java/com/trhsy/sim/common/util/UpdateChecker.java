package com.trhsy.sim.common.util;

import com.trhsy.sim.ModSim;
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

        File checks = new File(getSimukraftFolder()+ File.separator);
        if(!checks.exists()){
            onUpdate();
        }
    }

    public void onUpdate() {
        try {
//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String baseURL = "https://trhsy.github.io/sim/Simukraft_zh_CN.zip";
            String lang= FMLCommonHandler.instance().getCurrentLanguage();
            if("en_US".equals(lang)) {
                baseURL = "https://trhsy.github.io/sim/Simukraft_en_US.zip";
            }
            String unzipFilePath= getSimukraftFolder();
            File checks = new File(unzipFilePath+ File.separator);
            File[] checkss = checks.listFiles();

            for (File f : checkss) {
                deleteFile(f);
            }
            checks.mkdir();
            String simFile=unzipFilePath+ File.separator + "Simukraft.zip";
            String ver = this.downloadFile(baseURL,  simFile);
            if (ver != null) {
                File zipFile = new File(ver);
                //开始解压
                ModSim.log.info("开始解压：",zipFile.getName());
                ZipEntry entry = null;
                String entryFilePath = null, entryDirPath = null;
                File entryFile = null, entryDir = null;
                int index = 0, count = 0;
                byte[] buffer = new byte[1024];
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                ZipFile zip = new ZipFile(zipFile);
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();
                //循环对压缩包里的每一个文件进行解压
                while(entries.hasMoreElements()) {

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
                        //ModSim.log.info("创建解压文件：",entryFile.getName());
                    }else {
                        entryDirPath = entryFilePath.substring(0, entryFilePath.length()-1);
                        entryDir = new File(entryDirPath);
                        //如果文件夹路径不存在，则创建文件夹
                        if (!entryDir.exists() || !entryDir.isDirectory()) {
                            entryDir.mkdirs();
                            ModSim.log.info("创建解压文件夹：",entryDir.getName());
                        }
                    }

                }

            }

            new File(simFile).deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }


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
            if(!checks.exists()&& !checks.isDirectory()){
                ModSim.log.warn("SimCity error - Mod未正确安装, ./minecraft/mods/sim/ 文件夹丢失了 - 重新创建此文件夹");
                checks.mkdir();
            }
            return (checks).getAbsolutePath();
        } catch (Exception var1) {
            return "";
        }
    }
    public static void deleteFile(File file){
        //ModSim.log.info("开始删除文件/文件夹：",file.getName());
        if(file.exists()){
            file.delete();
        }
        if(file.exists()){
            File[] paths = file.listFiles();
            for(File str:paths){
                deleteFile(str);
            }
            file.delete();
            paths = null;	// lets gc do its works
        }
        //ModSim.log.info("完成删除文件/文件夹：",file.getName());
        file = null;	// lets gc do its works
    }
    public String downloadFile(String url, String localFile) {
        File f=new File(localFile);
        deleteFile(f);
        //String ret = "";
        ModSim.log.info("将从此链接下载文件：\n",url);
        //url = url.replace(" ", "%20");

        try {
            URL aURL =new URL(url);
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
        } catch (Exception var9) {
            //ret = "";
            var9.printStackTrace();
        }

        return localFile;
    }
}
