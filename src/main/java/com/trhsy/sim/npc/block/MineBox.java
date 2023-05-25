package com.trhsy.sim.npc.block;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.UUID;

/**
 * @ClassName MineBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2117:27
 **/
public class MineBox {
    public UUID ID;
    public V3 loc;
    public NpcData employee;
    public int level;
    public EnumFacing facing;
    public int x;
    public int z;

    public MineBox(V3 pos, V3 start, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.x = x;
        this.z = z;
    }

    public MineBox(V3 pos, NpcData f, V3 start, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.employee = f;
        this.x = x;
        this.z = z;
    }

    public MineBox(UUID uuid) {
        this.facing = EnumFacing.EAST;
        this.loadMine(uuid);
    }

    public BlockPos getCorner() {
        return this.loc.toBlockPos().offset(this.facing);
    }

    public void saveMine() {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File mineFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "mines");
        if(!mineFolder.exists()){
            mineFolder.mkdirs();
        }

        BufferedWriter writer = null;

        try {
            File logFile = new File(mineFolder + File.separator + this.ID + ".sk2");
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("id|" + this.ID.toString() + "\n");
            writer.write("loc|" + this.loc.toString() + "\n");
            writer.write("dimension|" + this.loc.dimension + "\n");
            writer.write("facing|" + this.facing.toString() + "\n");
            writer.write("x|" + this.x + "\n");
            writer.write("z|" + this.z + "\n");
            writer.write("level|" + this.level + "\n");
        } catch (Exception var12) {
            StackTraceElement element = var12.getStackTrace()[0];
            ModSimLoader.log.error("saveMine出错了：" + var12.getMessage() + "行数：" + element.getLineNumber());
        } finally {
            try {
                writer.close();
            } catch (Exception var11) {
                StackTraceElement element = var11.getStackTrace()[0];
                ModSimLoader.log.error("saveMine-writer出错了：" + var11.getMessage() + "行数：" + element.getLineNumber());
            }

        }

    }

    public void loadMine(UUID loadID) {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File mineFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "mines");
        if(!mineFolder.exists()){
            mineFolder.mkdirs();
        }

        try {
            //BufferedReader reader = new BufferedReader(new FileReader(mineFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            InputStream inputStream =new FileInputStream(new File(mineFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                int m1 = line.indexOf("|");
                line.substring(0, m1);
                String value = line.substring(m1 + 1);
                if (line.contains("id|")) {
                    this.ID = UUID.fromString(value);
                } else if (line.contains("loc")) {
                    this.loc = V3.fromString(value);
                } else if (line.contains("dimension")) {
                    this.loc.dimension = Integer.valueOf(value);
                } else if (line.contains("facing")) {
                    this.facing = EnumFacing.byName(value);
                } else if (line.contains("x|")) {
                    this.x = Integer.valueOf(value);
                } else if (line.contains("z|")) {
                    this.z = Integer.valueOf(value);
                } else if (line.contains("level|")) {
                    this.level = Integer.valueOf(value);
                }
            }

            reader.close();
        } catch (Exception var8) {
            StackTraceElement element = var8.getStackTrace()[0];
            ModSimLoader.log.error("loadMine出错了：" + var8.getMessage() + "行数：" + element.getLineNumber());
        }

    }
}