package com.trhsy.sim.common;

import com.trhsy.sim.ModSim;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 更新检查器
 */
public class UpdateChecker {
    private static Logger logger;
    int highest = 0;
    int m1 = 0;

    public UpdateChecker(FMLPreInitializationEvent event) {
        onUpdate();
    }

    public void onUpdate() {
        try {
//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String baseURL = "https://trhsy.github.io/sim/Simukraft_zh_CN.zip";
            String lang= FMLCommonHandler.instance().getCurrentLanguage();
            if("en_US".equals(lang)) {
                baseURL = "https://trhsy.github.io/sim/Simukraft_en_US.zip";
            }
            String unzipFilePath= ModSim.getSimukraftFolder();
            String simFile=unzipFilePath+ File.separator + "Simukraft.zip";
            String ver = this.downloadFile(baseURL,  simFile);
            if (ver != null) {
                File zipFile = new File(ver);
                //创建解压缩文件保存的路径
                File unzipFileDir = new File(unzipFilePath);
                //开始解压
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
                    }else {
                        entryDirPath = entryFilePath.substring(0, entryFilePath.length()-1);
                        entryDir = new File(entryDirPath);
                        //如果文件夹路径不存在，则创建文件夹
                        if (!entryDir.exists() || !entryDir.isDirectory()) {
                            entryDir.mkdirs();
                        }
                    }



                    /*if (entryFile.exists()) {
                        //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                        SecurityManager securityManager = new SecurityManager();
                        securityManager.checkDelete(entryFilePath);
                        //删除已存在的目标文件
                        entryFile.delete();
                    }*/


                }
                File simFiles = new File(simFile);
                simFiles.delete();
                /*ver = ver.trim();
                if (!ver.contentEquals("") && !ModSim.VERSION.contentEquals(ver)) {
                    ModSim.sendChat(I18n.format("container.sim.main_available"));
                    Long now = System.currentTimeMillis();
                    ModSim.states.lastUpdateCheck = now;
                    ModSim.states.saveStates();
                    File check = new File(ModSim.getSimukraftFolder() + "/buildings/");
                    if(!check.exists()&& !check.isDirectory()){
                        logger.warn("SimCity error - Mod未正确安装, ./minecraft/mods/sim/buildings/ 文件夹丢失了 - 重新创建此文件夹");
                        check.mkdir();
                    }

                }*/

            }

            /*int high = getHighestPKID("residential");
            int o = getHighestPKID("other");
            if (o > high) {
                high = o;
            }

            String newbs = this.downloadFile(baseURL, ModSim.getSimukraftFolder() + File.separator + "version.txt");
            if (newbs.length() == 0) {
                return;
            }

            String[] items = newbs.split("!END");

            for (int i = 0; i < items.length - 1; ++i) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 获得最高的PKID
     *
     * @param type
     * @return
     */
    public int getHighestPKID(String type) {
        //查看当前路径下 的 文件
        File actual = new File(ModSim.getSimukraftFolder() + File.separator + "buildings" + File.separator + type + File.separator);
        //获得文件列表
        File[] listFiles = actual.listFiles();
        //循环
        for (int i = 0; i < listFiles.length; ++i) {
            File f = listFiles[i];
            //文件名开头是 PKID
            if (f.getName().startsWith("PKID")) {
                //查找名字中带 -
                this.m1 = f.getName().indexOf("-");
                //如果找到
                if (this.m1 > 0) {
                    //截取pkid 后面的数字
                    String id = f.getName().substring(4, this.m1);
                    //如果大于 则赋值
                    if (Integer.parseInt(id) > this.highest) {
                        this.highest = Integer.parseInt(id);
                    }
                }
            }
        }

        return this.highest;
    }

    public String downloadFile(String url, String localFile) {
        //String ret = "";
        //logger.info("将从此链接下载文件：\n" + url);
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
