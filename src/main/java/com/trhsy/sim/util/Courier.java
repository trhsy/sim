package com.trhsy.sim.util;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import net.minecraftforge.common.DimensionManager;

import java.io.*;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: Courier
 * @Description:
 * @date 2023/08/18 上午 10:44
 */
public class Courier {
    public String ID;
    public V3 loc;
    public String name;
    public Courier(String id, V3 pos, String name) {
        this.ID=id;
        this.loc=pos;
        this.name=name;
    }
    public Courier(String uuid) {
        this.loadMine(uuid);
    }
    public void loadMine(String loadID) {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File mineFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "CourierPoints");
        if(!mineFolder.exists()){
            mineFolder.mkdirs();
        }
        try {
            InputStream inputStream =new FileInputStream(new File(mineFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                int m1 = line.indexOf("|");
                line.substring(0, m1);
                String value = line.substring(m1 + 1);
                if (line.contains("loc")) {
                    this.loc = V3.fromString(value);
                } else if (line.contains("name")) {
                    this.name = value;
                }
            }
            reader.close();
        } catch (Exception var8) {
            StackTraceElement element = var8.getStackTrace()[0];
            ModSimLoader.log.error("Courier出错了：" + var8.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    public void removeFarm (String uid){
        ModSimLoader.buildings.remove(this);
        File logFile = new File(ModSimLoader.getSavesDataFolder() + File.separator + "CourierPoints" + File.separator + uid + ".sk2");
        if(logFile.delete()){
            logFile.deleteOnExit();
        }
    }
}
