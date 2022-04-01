package com.trhsy.sim.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.client.resources.I18n;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * 更新检查器
 */
public class UpdateChecker {
    int highest = 0;
    int m1 = 0;

    public UpdateChecker() {
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            File check = new File(ModSim.getSimukraftFolder() + "/buildings/");
            if (!check.exists()) {
                ModSim.sendChat(ModSim.getSimukraftFolder() + "/buildings/ " + I18n.format("container.sim.main_buildings"));
                return;
            }
            String baseURL = "https://www.jianguoyun.com/d/home#/sandbox/14da907/3267f0bd2b7be3e3/%2F/?previewingFileName=version.txt";//"https://www.dropbox.com/s/i51v1lsq0u89elw/";
            String ver = this.downloadFile(baseURL, ModSim.getSimukraftFolder() + File.separator + "version.txt");
            if (ver != null) {
                ver = ver.trim();
                if (!ver.contentEquals("") && !ModSim.VERSION.contentEquals(ver)) {
                    ModSim.sendChat(I18n.format("container.sim.main_available"));
                    Long now = System.currentTimeMillis();
                    ModSim.states.lastUpdateCheck = now;
                    ModSim.states.saveStates();
                }
            }

            int high = getHighestPKID("residential");
            int o = getHighestPKID("other");
            if (o > high) {
                high = o;
            }

            /*String newbs = this.downloadFile(baseURL, ModSim.getSimukraftFolder() + File.separator + "version.txt");
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

        }


    }

    /**
     * 获得最高的PKID
     *
     * @param type
     * @return
     */
    public int getHighestPKID(String type) {
        File actual = new File(ModSim.getSimukraftFolder() + File.separator + "buildings" + File.separator + type + File.separator);
        File[] listFiles = actual.listFiles();

        for (int i = 0; i < listFiles.length; ++i) {
            File f = listFiles[i];
            if (f.getName().startsWith("PKID")) {
                this.m1 = f.getName().indexOf("-");
                if (this.m1 > 0) {
                    String id = f.getName().substring(4, this.m1);
                    if (Integer.parseInt(id) > this.highest) {
                        this.highest = Integer.parseInt(id);
                    }
                }
            }
        }

        return this.highest;
    }

    public String downloadFile(String url, String localFile) {
        String ret = "";
        ModSim.log.info("下载文件" + url);
        //url = url.replace(" ", "%20");

        try {
            BufferedInputStream in = new BufferedInputStream((new URL(url)).openStream());
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
        } catch (Exception var9) {
            ret = "";
            var9.printStackTrace();
        }

        return ret;
    }
}
