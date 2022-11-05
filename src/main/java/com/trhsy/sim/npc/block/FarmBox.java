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
 * @ClassName FarmBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2117:25
 **/
public class FarmBox {
    public UUID ID;
    public V3 loc;
    public NpcData employee;
    public int level;
    public EnumFacing facing;
    public int x;
    public int z;

    public FarmBox(V3 pos, V3 start, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.x = x;
        this.z = z;
    }

    public FarmBox(V3 pos, NpcData f, V3 start, int x, int z) {
        this.facing = EnumFacing.EAST;
        this.ID = UUID.randomUUID();
        this.loc = pos;
        this.employee = f;
        this.x = x;
        this.z = z;
    }

    public FarmBox(UUID uuid) {
        this.facing = EnumFacing.EAST;
        this.loadFarm(uuid);
    }

    public BlockPos getCorner() {
        return this.loc.toBlockPos().offset(this.facing);
    }

    public void saveFarm() {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File farmFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms");
        if(!farmFolder.exists()){
            farmFolder.mkdirs();
        }
        BufferedWriter writer = null;

        try {
            File logFile = new File(farmFolder + File.separator + this.ID + ".sk2");
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("id|" + this.ID.toString() + "\n");
            writer.write("loc|" + this.loc.toString() + "\n");
            writer.write("dimension|" + this.loc.dimension + "\n");
            writer.write("facing|" + this.facing.toString() + "\n");
            writer.write("x|" + this.x + "\n");
            writer.write("z|" + this.z + "\n");
            writer.write("level|" + this.level + "\n");
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }

    }

    public void loadFarm(UUID loadID) {
        StringBuilder var10002 = new StringBuilder();
        new DimensionManager();
        File farmFolder = new File(ModSimLoader.getSavesDataFolder() + File.separator + "farms");
        if(!farmFolder.exists()){
            farmFolder.mkdirs();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(farmFolder.getAbsolutePath() + File.separator + loadID + ".sk2"));

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
            var8.printStackTrace();
        }

    }
}