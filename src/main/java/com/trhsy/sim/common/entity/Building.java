package com.trhsy.sim.common.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.infrastructure.Infrastructure;
import com.trhsy.sim.common.infrastructure.InfrastructureElectricity;
import com.trhsy.sim.common.infrastructure.InfrastructureWater;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.util.GameMode;
import com.trhsy.sim.util.UpdateChecker;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName Building
 * @Description todo 建筑物
 * @Author Tian
 * @Date 2022/1/2319:49
 **/
public class Building implements Serializable {
    private static final long serialVersionUID = -1132989807904279141L;

    public String displayName;
    public String type;
    public String[] structure;
    public String description = "None";
    //基础设施
    public Infrastructure infrastructureRequirement = null;
    public Infrastructure infrastructureProducer = null;
    public int layerCount = 0;
    public int ftbCount = 0;
    public int ltrCount = 0;
    //尺寸
    public String dimensions = "";
    public int elevationLevel = 0;
    public V3 primaryXYZ;
    public V3 livingXYZ;
    public boolean buildingComplete = false;
    public int capacity = -1;
    public String buildDirection = "";
    public V3 lumbermillMarker = null;
    public int blocksInBuilding = 0;
    public String pk = "0";
    public String author = "Trhsy";
    public String displayNameWithoutPK = "";
    public Float rent = 0.0F;
    //租户
    public ArrayList<String> tenants = new ArrayList();
    public ArrayList<V3> blockLocations = new ArrayList();
    public transient HashMap<ItemStack, Integer> requirements = new HashMap();
    public transient V3 conBoxLocation = null;
    private static transient ArrayList<Building> buildingsRes = new ArrayList();
    private static transient ArrayList<Building> buildingsCom = new ArrayList();
    private static transient ArrayList<Building> buildingsInd = new ArrayList();
    private static transient ArrayList<Building> buildingsOth = new ArrayList();
    private static transient ArrayList<Building> buildingsSpec = new ArrayList();
    public ArrayList<V3> blockSpecial = new ArrayList();
    private static boolean runningInitThread = false;

    public Building() {
    }

    public Building(String fname, String theType) {
        this.type = theType;
        this.displayName = fname;
        this.buildingComplete = false;
        if (this.requirements == null) {
            this.requirements = new HashMap();
        }

    }

    public Building(String fname, String theType, V3 pxyz, V3 lxyz, boolean isComplete) {
        this.type = theType;
        this.displayName = fname;
        this.primaryXYZ = pxyz;
        this.livingXYZ = lxyz;
        this.buildingComplete = isComplete;
        if (this.requirements == null) {
            this.requirements = new HashMap();
        }

    }

    @Override
    public Building clone() {
        Building ret = new Building();
        ret.displayName = this.displayName;
        ret.type = this.type;
        ret.layerCount = this.layerCount;
        ret.infrastructureRequirement = this.infrastructureRequirement;
        ret.infrastructureProducer = this.infrastructureProducer;
        ret.ftbCount = this.ftbCount;
        ret.ltrCount = this.ltrCount;
        ret.dimensions = this.dimensions;
        ret.elevationLevel = this.elevationLevel;
        ret.description = this.description;

        try {
            ret.primaryXYZ = this.primaryXYZ.clone();
        } catch (Exception var4) {
            ret.primaryXYZ = null;
        }

        try {
            ret.livingXYZ = this.livingXYZ.clone();
        } catch (Exception var3) {
            ret.livingXYZ = null;
        }

        ret.buildingComplete = this.buildingComplete;
        ret.capacity = -1;
        ret.buildDirection = this.buildDirection;
        ret.blocksInBuilding = this.blocksInBuilding;
        ret.pk = this.pk;
        ret.author = this.author;
        ret.displayNameWithoutPK = this.displayNameWithoutPK;
        ret.rent = this.rent;
        ret.tenants = new ArrayList();
        ret.blockLocations = new ArrayList();
        ret.blockSpecial = new ArrayList();
        ret.loadStructure();
        return ret;
    }

    public ArrayList<V3> getSpecialBlocks(int meta) {
        ArrayList<V3> ret = new ArrayList();
        Iterator iterator = this.blockSpecial.iterator();

        while (iterator.hasNext()) {
            V3 v3 = (V3) iterator.next();
            if (v3.meta == meta) {
                ret.add(v3);
            }
        }

        return ret;
    }

    public void removeTennant(String tennant) {
        for (int t = 0; t < this.tenants.size(); ++t) {
            String ten = (String) this.tenants.get(t);
            if (ten.contentEquals(tennant)) {
                this.tenants.remove(t);
                break;
            }
        }

    }

    private void loadStructure() {
        //int[] var10000 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        try {
            if (this.requirements != null) {
                this.requirements.clear();
            } else {
                this.requirements = new HashMap();
            }

            this.displayNameWithoutPK = this.displayName;
            if (this.displayName.startsWith("PKID")) {
                int hyphen = this.displayName.indexOf("-");
                this.pk = this.displayName.substring(4, hyphen);
                this.displayNameWithoutPK = this.displayName.substring(hyphen + 1);
            }

            this.blocksInBuilding = 0;
            File f = new File(UpdateChecker.getSimukraftFolder() + "/buildings/" + this.type + "/" + this.displayName + ".txt");
            if (!f.exists()) {
                return;
            }

            FileInputStream fstream = new FileInputStream(UpdateChecker.getSimukraftFolder() + "/buildings/" + this.type + "/" + this.displayName + ".txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = br.readLine().toString().toLowerCase().trim();
            //建筑尺寸 例如 5x5x3
            String[] d = strLine.split("x");
            int[] di = new int[]{Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2])};

            this.structure = new String[di[0] * di[1] * di[2]];
            //长
            this.ltrCount = di[0];
            //宽
            this.ftbCount = di[1];
            //高
            this.layerCount = di[2];
            this.dimensions = d[0] + "x" + d[1] + "x" + d[2];
            strLine = br.readLine().toString().trim();
            HashMap thekey = new HashMap();
            d = strLine.split(";");

            int acount;
            for (acount = 0; acount < d.length; ++acount) {
                //A=0:0;C=101:0;D=26:0;E=26:8;F=47:0;G=50:5;AU=Razor9119;
                String[] k = d[acount].split("=");
                //         A     0:0
                thekey.put(k[0], k[1]);
                if (k[0].toUpperCase().contentEquals("AU")) {
                    //Razor 9119
                    this.author = k[1].trim();
                } else if (k[0].toUpperCase().contentEquals("DESC")) {
                    this.description = k[1];
                } else if (k[0].toUpperCase().contentEquals("ELEV")) {
                    this.description = k[1];
                } else if (k[0].toUpperCase().contentEquals("INFREQ")) {
                    if (k[1].toLowerCase().contentEquals("water")) {
                        this.infrastructureRequirement = new InfrastructureWater();
                    }

                    if (k[1].toLowerCase().contentEquals("electricity")) {
                        this.infrastructureRequirement = new InfrastructureElectricity();
                    }
                }
            }

            acount = 0;
            //int bcount = false;

            for (int i = 0; i < this.layerCount; ++i) {
                strLine = br.readLine().trim();
                int bcount = 0;

                for (int ftb = 0; ftb < this.ftbCount; ++ftb) {
                    for (int ltr = 0; ltr < this.ltrCount; ++ltr) {
                        try {
                            String ch = strLine.substring(bcount, bcount + 1);
                            char cha = ch.charAt(0);
                            //结构
                            if (ch.contentEquals("!")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.livingBlock) + ":0";
                            } else if (ch.contentEquals("$")) {
                                //控制箱
                                //System.out.println("控制箱");
                                if(this.displayName.contentEquals(I18n.format("container.sim.ATMs"))){
                                    this.structure[acount] = "" + Block.getIdFromBlock(BlockLoader.blockATMControlBox)+":0";
                                }else if("other".equals(this.type)){
                                    this.structure[acount] = "" + Block.getIdFromBlock(BlockLoader.blockOtherControlBox)+":0";
                                }else{
                                    this.structure[acount] = "" + Block.getIdFromBlock(BlockLoader.blockControlBox)+":0";
                                }

                            } else if (ch.contentEquals("*")) {
                                //灯箱
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":0";
                            } else if (ch.contentEquals("+")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":3";
                            } else if (ch.contentEquals("-")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":5";
                            } else if (ch.contentEquals("0")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":0";
                            } else if (ch.contentEquals("1")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":1";
                            } else if (ch.contentEquals("2")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":2";
                            } else if (ch.contentEquals("3")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":3";
                            } else if (ch.contentEquals("4")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":4";
                            } else if (ch.contentEquals("5")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":5";
                            } else if (ch.contentEquals("6")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":6";
                            } else if (ch.contentEquals("7")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":7";
                            } else if (ch.contentEquals("8")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) + ":8";
                            } else if (cha >= '0' && cha <= '9') {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock) +":" + cha;
                            } else if (ch.contentEquals("Ã€")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":0";
                            } else if (ch.contentEquals("Ã†")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":1";
                            } else if (ch.contentEquals("Ã‡")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":2";
                            } else if (ch.contentEquals("Ãˆ")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":3";
                            } else if (ch.contentEquals("ÃŒ")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":4";
                            } else if (ch.contentEquals("Ã�")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":5";
                            } else if (ch.contentEquals("Ã‘")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":6";
                            } else if (ch.contentEquals("Ã’")) {
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.lightBox) + ":7";
                            }else if((int)cha >=48 && (int)cha <=57){
                                this.structure[acount] = Block.getIdFromBlock(BlockLoader.specialBlock)+":"+cha;
                            }else {

                                this.structure[acount] = (String) thekey.get(ch);
                                String[] sbid = this.structure[acount].split(":");
                                int bid = Integer.parseInt(sbid[0]);
                                this.addToRequirements(Block.getBlockById(bid), 1);
                            }

                            ++acount;
                            ++bcount;
                            if (!ch.contentEquals("A")) {
                                ++this.blocksInBuilding;
                            }
                        } catch (Exception var19) {
                        }
                    }
                }
            }

            br.close();
            in.close();
            this.rent = (float) this.blocksInBuilding * 0.01F;
        } catch (Exception var20) {
            ModSimReloaded.log.warning("建筑 loadStructure() " + var20.getMessage());
        }

    }

    private void addToRequirements(Block block, int amount) {
        //int val = false;
        ItemStack theBlock = new ItemStack(block, 1, 0);
        String name;
        Iterator it;
        boolean got;
        Map.Entry pairs;
        ItemStack is;
        int val;
        if (GameMode.gameMode == GameMode.GAMEMODES.NORMAL) {
            name = "";

            try {
                name = theBlock.getDisplayName().toLowerCase();
            } catch (Exception var11) {
                name = "????";
            }

            if (name.contains("planks") || name.contentEquals("cobblestone") || name.contentEquals("glass") || name.contains("wool") || name.contentEquals("bricks") || name.contentEquals("dirt") || name.contentEquals("stone bricks") || name.contentEquals("fence") || name.contentEquals("stone") || name.contains("wood") && !name.contains("slab") && !name.contains("door") && !name.contains("stairs") && !name.contains("grass")) {
                it = this.requirements.entrySet().iterator();
                got = false;

                while (it.hasNext()) {
                    pairs = (Map.Entry) it.next();
                    is = (ItemStack) pairs.getKey();
                    if (is.getItem() == theBlock.getItem()) {
                        val = (Integer) pairs.getValue();
                        ++val;
                        pairs.setValue(val);
                        got = true;
                        break;
                    }
                }

                if (!got) {
                    this.requirements.put(theBlock, 1);
                }
            }
        } else {
            if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                return;
            }

            if (GameMode.gameMode == GameMode.GAMEMODES.HARDCORE) {
                name = "";

                try {
                    name = theBlock.getDisplayName().toLowerCase();
                } catch (Exception var10) {
                    name = "????";
                }

                if (!name.contains("grass") && !name.contains("bed")) {
                    it = this.requirements.entrySet().iterator();
                    got = false;

                    while (it.hasNext()) {
                        pairs = (Map.Entry) it.next();
                        is = (ItemStack) pairs.getKey();
                        if (is.getItem() == theBlock.getItem()) {
                            val = (Integer) pairs.getValue();
                            ++val;
                            pairs.setValue(val);
                            got = true;
                            break;
                        }
                    }

                    if (!got) {
                        this.requirements.put(theBlock, 1);
                    }
                }
            }
        }

    }

    private static void copyArrayList(ArrayList<Building> from, ArrayList<Building> to) {
        for (int i = 0; i < from.size(); ++i) {
            to.add(from.get(i));
        }

    }

    public static ArrayList<Building> getBuildingBlueprints(String theType, String searchWords) {
        ArrayList retBuildings = new ArrayList();

        try {
            int sizesearch = 0;
            if (theType.contentEquals("residential")) {
                copyArrayList(buildingsRes, retBuildings);
            } else if (theType.contentEquals("commercial")) {
                copyArrayList(buildingsCom, retBuildings);
            } else if (theType.contentEquals("industrial")) {
                copyArrayList(buildingsInd, retBuildings);
            } else if (theType.contentEquals("other")) {
                copyArrayList(buildingsOth, retBuildings);
            } else if (theType.contentEquals("special")) {
                copyArrayList(buildingsSpec, retBuildings);
            }

            if (!searchWords.contentEquals("")) {
                try {
                    sizesearch = Integer.parseInt(searchWords.substring(2));
                } catch (Exception var6) {
                }

                int i;
                Building build;
                if (searchWords.startsWith("w:")) {
                    for (i = retBuildings.size() - 1; i >= 0; --i) {
                        build = (Building) retBuildings.get(i);
                        if (build.ltrCount != sizesearch) {
                            retBuildings.remove(i);
                        }
                    }
                } else if (searchWords.startsWith("d:")) {
                    for (i = retBuildings.size() - 1; i >= 0; --i) {
                        build = (Building) retBuildings.get(i);
                        if (build.ftbCount != sizesearch) {
                            retBuildings.remove(i);
                        }
                    }
                } else if (searchWords.startsWith("h:")) {
                    for (i = retBuildings.size() - 1; i >= 0; --i) {
                        build = (Building) retBuildings.get(i);
                        if (build.layerCount != sizesearch) {
                            retBuildings.remove(i);
                        }
                    }
                } else {
                    for (i = retBuildings.size() - 1; i >= 0; --i) {
                        build = (Building) retBuildings.get(i);
                        if (!build.displayName.toLowerCase().contains(searchWords.toLowerCase())) {
                            retBuildings.remove(i);
                        }
                    }
                }
            }
        } catch (Exception var7) {
        }

        return retBuildings;
    }

    /**
     * 获取建筑物
     *
     * @param primaryXYZ
     * @return
     */
    public static Building getBuilding(V3 primaryXYZ) {
        Building b = null;
        if (ModSimReloaded.theBuildings.size() == 0) {
            loadAllBuildings();
        }

        for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
            try {
                b = (Building) ModSimReloaded.theBuildings.get(x);
                if (b.primaryXYZ.isSameCoordsAs(primaryXYZ, false, true)) {
                    return b;
                }
            } catch (Exception var4) {
            }
        }

        return null;
    }

    public static Building getBuildingBySearch(String searchWord) {
        Building b = null;

        for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
            b = (Building) ModSimReloaded.theBuildings.get(x);
            if (b.displayName.contains(searchWord)) {
                return b;
            }
        }

        return null;
    }

    public static ArrayList<Building> getBuildingBySearch(String searchWord, boolean findAll) {
        ArrayList<Building> ret = new ArrayList();

        for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
            Building b = (Building) ModSimReloaded.theBuildings.get(x);
            if (b.displayName.toLowerCase().contains(searchWord.toLowerCase())) {
                ret.add(b);
            }
        }

        return ret;
    }

    public static Building getBuildingByConBox(V3 conBoxLoc) {
        Building b = null;

        for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
            b = (Building) ModSimReloaded.theBuildings.get(x);

            try {
                if (b.conBoxLocation.isSameCoordsAs(conBoxLoc, true, true)) {
                    return b;
                }
            } catch (Exception var4) {
            }
        }

        return null;
    }

    public void saveThisBuilding() {
        ArrayList<String> strings = new ArrayList();
        strings.clear();
        if (this.primaryXYZ != null) {
            String xyz = "b" + this.primaryXYZ.toString().replaceAll(",", "_");
            strings.add("displayname|" + this.displayName);
            strings.add("type|" + this.type);
            strings.add("primaryxyz|" + this.primaryXYZ.toString());
            if (this.livingXYZ == null) {
                strings.add("livingxyz|null");
            } else {
                strings.add("livingxyz|" + this.livingXYZ.toString());
            }

            strings.add("buildingcomplete|" + this.buildingComplete);
            strings.add("capacity|" + this.capacity);
            strings.add("builddir|" + this.buildDirection);
            if (this.lumbermillMarker == null) {
                strings.add("lmarker|null");
            } else {
                strings.add("lmarker|" + this.lumbermillMarker.toString());
            }

            strings.add("blocksinbuilding|" + this.blocksInBuilding);
            String temp = "tenants|";
            Iterator i$ = this.tenants.iterator();

            while (i$.hasNext()) {
                String tennant = (String) i$.next();
                if (!tennant.trim().contentEquals("")) {
                    temp = temp + tennant.trim() + ",";
                }
            }

            strings.add(temp);
            temp = "blocklocs|";
            i$ = this.blockLocations.iterator();

            V3 block;
            while (i$.hasNext()) {
                block = (V3) i$.next();
                if (block != null & block.toString().contains(",")) {
                    temp = temp + block.toString() + "B";
                }
            }

            strings.add(temp);
            if (this.blockSpecial.size() > 0) {
                temp = "blockspecial|";
                i$ = this.blockSpecial.iterator();

                while (i$.hasNext()) {
                    block = (V3) i$.next();
                    if (block != null & block.toString().contains(",")) {
                        temp = temp + block.toString() + "," + block.meta + "B";
                    }
                }

                strings.add(temp);
            }

            ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator + xyz + ".sk2", strings);
        }

    }

    public static void saveAllBuildings() {
        Minecraft mc = Minecraft.getMinecraft();
        ArrayList<String> strings = new ArrayList();

        for (int b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
            strings.clear();
            Building building = (Building) ModSimReloaded.theBuildings.get(b);
            if (building != null && building.primaryXYZ != null) {
                V3 pxyz = building.primaryXYZ;
                World buildingWorld = MinecraftServer.getServer().worldServerForDimension(building.primaryXYZ.theDimension);
                Block id = buildingWorld.getBlock(pxyz.x.intValue(), pxyz.y.intValue(), pxyz.z.intValue());
                String xyz = "b" + building.primaryXYZ.toString().replaceAll(",", "_");
                if (id != BlockLoader.blockControlBox && id != BlockLoader.constructorBox) {
                    File f = new File(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator + xyz + ".sk2");
                    if (f.exists()) {
                        f.delete();
                    }
                } else {
                    strings.add("displayname|" + building.displayName);
                    strings.add("type|" + building.type);
                    strings.add("primaryxyz|" + building.primaryXYZ.toString());
                    if (building.livingXYZ == null) {
                        strings.add("livingxyz|null");
                    } else {
                        strings.add("livingxyz|" + building.livingXYZ.toString());
                    }

                    strings.add("buildingcomplete|" + building.buildingComplete);
                    strings.add("capacity|" + building.capacity);
                    strings.add("builddir|" + building.buildDirection);
                    if (building.lumbermillMarker == null) {
                        strings.add("lmarker|null");
                    } else {
                        strings.add("lmarker|" + building.lumbermillMarker.toString());
                    }

                    strings.add("blocksinbuilding|" + building.blocksInBuilding);
                    String temp = "tenants|";
                    Iterator i$ = building.tenants.iterator();

                    while (i$.hasNext()) {
                        String tennant = (String) i$.next();
                        if (!tennant.trim().contentEquals("")) {
                            temp = temp + tennant.trim() + ",";
                        }
                    }

                    strings.add(temp);

                    V3 block;
                    try {
                        temp = "blocklocs|";
                        i$ = building.blockLocations.iterator();

                        while (i$.hasNext()) {
                            block = (V3) i$.next();
                            if (block != null & block.toString().contains(",")) {
                                temp = temp + block.toString() + "B";
                            }
                        }

                        strings.add(temp);
                    } catch (Exception var12) {
                    }

                    try {
                        if (building.blockSpecial.size() > 0) {
                            temp = "blockspecial|";
                            i$ = building.blockSpecial.iterator();

                            while (i$.hasNext()) {
                                block = (V3) i$.next();
                                if (block != null & block.toString().contains(",")) {
                                    temp = temp + block.toString() + "," + block.meta + "B";
                                }
                            }

                            strings.add(temp);
                        }
                    } catch (Exception var11) {
                    }

                    ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator + xyz + ".sk2", strings);
                }
            }
        }

        ModSimReloaded.log.info("建筑物.saveAllBuildings " + ModSimReloaded.theBuildings.size() + " 建筑");
    }

    public static void loadAllBuildings() {
        File buildingsFolder = new File(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator);
        buildingsFolder.mkdirs();
        boolean useNewFormat = false;
        File[] files = buildingsFolder.listFiles();
        File f;
        for (int i = 0; i < files.length; ++i) {
            f = files[i];
            if (f.getName().endsWith(".sk2")) {
                useNewFormat = true;
                break;
            }
        }

        Building build;
        if (useNewFormat) {
            ModSimReloaded.theBuildings.clear();
            File[] arr = buildingsFolder.listFiles();
            label166:
            for (int i = 0; i < arr.length; ++i) {
                f = arr[i];
                if (f.getName().endsWith(".sk2")) {
                    ArrayList<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                    build = new Building();
                    Iterator iterator = strings.iterator();

                    while (true) {
                        while (true) {
                            String line;
                            do {
                                if (!iterator.hasNext()) {
                                    build.loadStructure();
                                    ModSimReloaded.theBuildings.add(build);
                                    continue label166;
                                }

                                line = (String) iterator.next();
                            } while (!line.contains("|"));

                            int m1 = line.indexOf("|");
                            String name = line.substring(0, m1);
                            String value = line.substring(m1 + 1);
                            if (name.contentEquals("displayname")) {
                                build.displayName = value;
                            } else if (name.contentEquals("type")) {
                                build.type = value;
                            } else if (name.contentEquals("primaryxyz")) {
                                build.primaryXYZ = new V3(value);
                            } else if (name.contentEquals("livingxyz")) {
                                if (!value.contentEquals("null")) {
                                    build.livingXYZ = new V3(value);
                                }
                            } else if (name.contentEquals("buildingcomplete")) {
                                build.buildingComplete = Boolean.parseBoolean(value);
                            } else if (name.contentEquals("capacity")) {
                                build.capacity = Integer.parseInt(value);
                            } else if (name.contentEquals("builddir")) {
                                build.buildDirection = value;
                            } else if (name.contentEquals("lmarker")) {
                                if (!value.contentEquals("null")) {
                                    build.lumbermillMarker = new V3(value);
                                }
                            } else if (name.contentEquals("blocksinbuilding")) {
                                build.blocksInBuilding = Integer.parseInt(value);
                            } else {
                                String[] blocks;
                                String[] array;
                                int lengths;
                                String block;
                                if (name.contentEquals("tenants")) {
                                    if (value.trim().contentEquals("")) {
                                        build.tenants.clear();
                                    } else {
                                        blocks = value.split(",");
                                        array = blocks;
                                        lengths = blocks.length;

                                        for (int i_j = 0; i_j < lengths; ++i_j) {
                                            block = array[i_j];
                                            if (!block.trim().contentEquals("")) {
                                                build.tenants.add(block);
                                            }
                                        }
                                    }
                                } else if (name.contentEquals("blocklocs")) {
                                    if (value.contains("B") && value.contains(",")) {
                                        blocks = value.split("B");
                                        array = blocks;
                                        lengths = blocks.length;

                                        for (int i1 = 0; i1 < lengths; ++i1) {
                                            block = array[i1];
                                            if (block.contains(",")) {
                                                build.blockLocations.add(new V3(block));
                                            }
                                        }
                                    }
                                } else if (name.contentEquals("blockspecial") && value.contains("B") && value.contains(",")) {
                                    blocks = value.split("B");
                                    array = blocks;

                                    for (int i_j = 0; i_j < blocks.length; ++i_j) {
                                        block = array[i_j];
                                        if (block.contains(",")) {
                                            int p1 = block.lastIndexOf(",");
                                            String v = block.substring(0, p1);
                                            String meta = block.substring(p1 + 1);
                                            V3 v3 = new V3(v);
                                            v3.meta = Integer.parseInt(meta);
                                            build.blockSpecial.add(v3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Minecraft mc = Minecraft.getMinecraft();
            ModSimReloaded.theBuildings.clear();
            File[] array = buildingsFolder.listFiles();
            for (int i = 0; i < array.length; ++i) {
                File fs = array[i];
                if (fs.getName().endsWith(".suk")) {
                    build = (Building) ModSim.proxy.loadObject(fs.getAbsoluteFile().toString());
                    if (build != null) {
                        V3 xyz = build.primaryXYZ;
                        World buildingWorld = MinecraftServer.getServer().worldServerForDimension(build.primaryXYZ.theDimension);
                        Block id = buildingWorld.getBlock(xyz.x.intValue(), xyz.y.intValue(), xyz.z.intValue());
                        Building dupe = null;
                        if (ModSimReloaded.theBuildings.size() > 0) {
                            dupe = getBuilding(xyz);
                        }

                        if (id == BlockLoader.blockControlBox && dupe == null) {
                            build.loadStructure();
                            ModSimReloaded.theBuildings.add(build);
                        } else {
                            fs.delete();
                            ModSimReloaded.log.info("Building: 已删除作为id的建筑=" + id + " or dupe");
                        }
                    }
                }
            }
        }

    }

    public static void checkTenants() {
        for (int b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
            Building building = (Building) ModSimReloaded.theBuildings.get(b);

            for (int t = 0; t < building.tenants.size(); ++t) {
                try {
                    String tennant = (String) building.tenants.get(t);
                    boolean exists = false;

                    for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                        FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                        if (folk.name.contentEquals(tennant)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        building.tenants.remove(tennant);
                    }
                } catch (Exception var7) {
                    //var7.printStackTrace();
                }
            }
        }

    }

    public static void initialiseAllBuildings() {
        if (!runningInitThread) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Building.runningInitThread = true;
                    Building.buildingsRes.clear();
                    Building.buildingsCom.clear();
                    Building.buildingsInd.clear();
                    Building.buildingsOth.clear();
                    Building.buildingsSpec.clear();
                    Building.initBuildingsOfType("residential");
                    Building.initBuildingsOfType("commercial");
                    Building.initBuildingsOfType("industrial");
                    Building.initBuildingsOfType("other");
                    Building.initBuildingsOfType("special");
                    Building.runningInitThread = false;
                    ModSimReloaded.log.info("Building: 线程已完成从磁盘初始化所有建筑物");
                }
            });
            t.start();
        }
    }

    public static Building getBuildingForFolk(String partialFilename, String type) {
        File f = new File(UpdateChecker.getSimukraftFolder() + "/buildings/" + type + "/" + partialFilename);
        if (f.exists()) {
            String name = f.getName().substring(0, f.getName().length() - 4);
            Building build = new Building(name, type);
            build.loadStructure();
            return build;
        } else {
            return null;
        }
    }

    private static void initBuildingsOfType(String type) {
        File f = new File(UpdateChecker.getSimukraftFolder() + "/buildings/" + type);

        for (int i = 0; i < f.list().length; ++i) {
            String name = f.list()[i];
            name = name.substring(0, name.length() - 4);
            Building build = new Building(name, type);
            build.loadStructure();
            if (type.contentEquals("residential")) {
                buildingsRes.add(build);
            } else if (type.contentEquals("commercial")) {
                buildingsCom.add(build);
            } else if (type.contentEquals("industrial")) {
                buildingsInd.add(build);
            } else if (type.contentEquals("other")) {
                buildingsOth.add(build);
            }else if (type.contentEquals("special")) {
                buildingsSpec.add(build);
            }

            try {
                Thread.sleep(30L);
            } catch (Exception var6) {
            }
        }

    }

    public static Building getFromAllBuildings(String fullname, String type) {
        Iterator i$;
        Building build;
        if (type.contentEquals("residential")) {
            i$ = buildingsRes.iterator();

            while (i$.hasNext()) {
                build = (Building) i$.next();
                if (build.displayName.contentEquals(fullname)) {
                    return build.clone();
                }
            }
        } else if (type.contentEquals("commercial")) {
            i$ = buildingsCom.iterator();

            while (i$.hasNext()) {
                build = (Building) i$.next();
                if (build.displayName.contentEquals(fullname)) {
                    return build.clone();
                }
            }
        } else if (type.contentEquals("industrial")) {
            i$ = buildingsInd.iterator();

            while (i$.hasNext()) {
                build = (Building) i$.next();
                if (build.displayName.contentEquals(fullname)) {
                    return build.clone();
                }
            }
        } else if (type.contentEquals("other")) {
            i$ = buildingsOth.iterator();

            while (i$.hasNext()) {
                build = (Building) i$.next();
                if (build.displayName.contentEquals(fullname)) {
                    return build.clone();
                }
            }
        }else if (type.contentEquals("special")) {
            i$ = buildingsSpec.iterator();

            while(i$.hasNext()) {
                build = (Building)i$.next();
                if (build.displayName.contentEquals(fullname)) {
                    return build.clone();
                }
            }
        }

        return null;
    }
}
