package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.entity.infrastructure.Infrastructure;
import com.trhsy.sim.common.entity.infrastructure.InfrastructureElectricity;
import com.trhsy.sim.common.entity.infrastructure.InfrastructureWater;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 建筑物
 */
public class Building implements Serializable {
    //显示名称
    public String displayName;
    //类型
    public String type;
    //结构体
    public String[] structure;
    //描述默认 无
    public String description = "None";
    //基础设施 需求
    public Infrastructure infrastructureRequirement = null;
    //基础设施 制作者
    public Infrastructure infrastructureProducer = null;
    //建筑层数
    public int layerCount = 0;
    //宽
    public int ftbCount = 0;
    //长
    public int ltrCount = 0;
    //尺寸/范围
    public String dimensions = "";
    //海拔高度
    public int elevationLevel = 0;
    //主体 xyz的坐标
    public V3 primaryXYZ;
    //生活区域的坐标
    public V3 livingXYZ;
    //建筑是否完成
    public boolean buildingComplete = false;
    //容量
    public int capacity = -1;
    //建筑方向
    public String buildDirection = "";
    //伐木厂标记
    public V3 lumbermillMarker = null;
    //建筑中的积木
    public int blocksInBuilding = 0;
    public String pk = "0";
    //作者 trhsy
    public String author = "Trhsy";
    //显示没有PK的名字
    public String displayNameWithoutPK = "";
    //租金
    public Float rent = 0.0F;
    //租户
    public CopyOnWriteArrayList<String> tenants = new CopyOnWriteArrayList();
    //块位置
    public CopyOnWriteArrayList<V3> blockLocations = new CopyOnWriteArrayList();
    //需求
    public transient HashMap<ItemStack, Integer> requirements = new HashMap();
    //控制箱位置
    public transient V3 conBoxLocation = null;
    //住宅建筑物
    private static transient CopyOnWriteArrayList<Building> buildingsRes = new CopyOnWriteArrayList();
    //商业建筑物
    private static transient CopyOnWriteArrayList<Building> buildingsCom = new CopyOnWriteArrayList();
    //工业建筑物
    private static transient CopyOnWriteArrayList<Building> buildingsInd = new CopyOnWriteArrayList();
    //其他建筑物
    private static transient CopyOnWriteArrayList<Building> buildingsOth = new CopyOnWriteArrayList();
    //特制建筑物
    private static transient CopyOnWriteArrayList<Building> buildingsSpec = new CopyOnWriteArrayList();
    //特除的空气方块
    public CopyOnWriteArrayList<V3> blockSpecial = new CopyOnWriteArrayList();
    //运行初始化线程
    private static boolean runningInitThread = false;

    public Building() {
    }

    /**
     * 初始化建筑物
     *
     * @param fname   名称
     * @param theType 类型
     */
    public Building(String fname, String theType) {
        try {
            //类型
            this.type = theType;
            //名称
            this.displayName = fname;
            //建筑未完成
            this.buildingComplete = false;
            //显示需求
            if (this.requirements == null) {
                this.requirements = new HashMap();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑Building出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 初始化建筑物
     *
     * @param fname      名称
     * @param theType    类型
     * @param pxyz       主体xyz
     * @param lxyz       生活区 xyz
     * @param isComplete 是否已经完成
     */
    public Building(String fname, String theType, V3 pxyz, V3 lxyz, boolean isComplete) {
        try {
            this.type = theType;
            this.displayName = fname;
            this.primaryXYZ = pxyz;
            this.livingXYZ = lxyz;
            this.buildingComplete = isComplete;
            if (this.requirements == null) {
                this.requirements = new HashMap();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑Building出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    /**
     * 建筑克隆
     *
     * @return
     */
    @Override
    public Building clone() {
        //重新声明建筑
        Building ret = new Building();
        try {
            //复制名称
            ret.displayName = this.displayName;
            //复制类型
            ret.type = this.type;
            //复制层数
            ret.layerCount = this.layerCount;
            //基础建筑需求
            ret.infrastructureRequirement = this.infrastructureRequirement;
            //基础建筑制作人
            ret.infrastructureProducer = this.infrastructureProducer;
            //建筑宽
            ret.ftbCount = this.ftbCount;
            //建筑搞
            ret.ltrCount = this.ltrCount;
            //建筑范围
            ret.dimensions = this.dimensions;
            //海拔高度
            ret.elevationLevel = this.elevationLevel;
            //建筑描述
            ret.description = this.description;
            if (this.primaryXYZ != null) {
                ret.primaryXYZ = this.primaryXYZ.clone();
            }
            if (this.livingXYZ != null) {
                ret.livingXYZ = this.livingXYZ.clone();
            }
            //建筑完成
            ret.buildingComplete = this.buildingComplete;
            //容量
            ret.capacity = -1;
            //建筑方向
            ret.buildDirection = this.buildDirection;
            //建筑中的积木
            ret.blocksInBuilding = this.blocksInBuilding;
            ret.pk = this.pk;
            ret.author = this.author;
            ret.displayNameWithoutPK = this.displayNameWithoutPK;
            //租金
            ret.rent = this.rent;
            //租户
            ret.tenants = new CopyOnWriteArrayList();
            //区块位置
            ret.blockLocations = new CopyOnWriteArrayList();
            //空
            ret.blockSpecial = new CopyOnWriteArrayList();
            //加载结构
            ret.loadStructure();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑clone出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            ret.primaryXYZ = null;
            ret.livingXYZ = null;
        }

        return ret;
    }

    /**
     * 获得空方块
     *
     * @param meta
     * @return
     */
    public CopyOnWriteArrayList<V3> getSpecialBlocks(int meta) {
        CopyOnWriteArrayList<V3> ret = new CopyOnWriteArrayList();
        try {
            for (V3 v3 : this.blockSpecial) {
                if (v3.meta == meta) {
                    ret.add(v3);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getSpecialBlocks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    /**
     * 移除租户
     *
     * @param tennant
     */
    public void removeTennant(String tennant) {
        try {
            for (int t = 0; t < this.tenants.size(); ++t) {
                String ten = (String) this.tenants.get(t);
                if (ten.contentEquals(tennant)) {
                    this.tenants.remove(t);
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑removeTennant出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 加载结构
     * 仅由 Building now 用于在线程中加载结构以减少延迟
     */
    private void loadStructure() {
        try {
            //需求不为空，清除
            if (this.requirements != null) {
                this.requirements.clear();
            } else {
                //重置
                this.requirements = new HashMap<ItemStack, Integer>();
            }
            //显示不带PK的名称
            this.displayNameWithoutPK = this.displayName;
            //如果是以PKID开始的
            if (this.displayName.startsWith("PKID")) {
                int hyphen = this.displayName.indexOf("-");
                this.pk = this.displayName.substring(4, hyphen);
                this.displayNameWithoutPK = this.displayName.substring(hyphen + 1);
            }
            //已经建筑的方块为0
            this.blocksInBuilding = 0;
            //加载所有建筑物
            File f = new File(ModSimReloaded.getSimukraftFolder() + "/buildings/" + this.type + "/" + this.displayName + ".txt");
            if (!f.exists()) {
                ModSimReloaded.log.info("建筑物不存在，加载失败");
                return;
            }
            //转换数据
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //读取信息
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
            //建筑信息 key P=5:0;A=23:2; etc
            strLine = br.readLine().toString().trim();
            HashMap thekey = new HashMap();
            d = strLine.split(";");

            for (int i = 0; i < d.length; i++) {
                //A=0:0;C=101:0;D=26:0;E=26:8;F=47:0;G=50:5;AU=Razor9119;
                String[] k = d[i].split("=");
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
            int acount = 0;
            //循环遍历高
            for (int i = 0; i < this.layerCount; i++) {
                //读一层
                strLine = br.readLine().trim();
                int bcount = 0;
                //宽
                for (int ftb = 0; ftb < this.ftbCount; ftb++) {
                    //长
                    for (int ltr = 0; ltr < this.ltrCount; ltr++) {
                        try {
                            String ch = strLine.substring(bcount, bcount + 1);
                            char cha = ch.charAt(0);
                            if ("!".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLiving) + ":0";
                            } else if ("$".equals(ch)) {//控制箱
                                if (this.displayName.contentEquals(I18n.func_135052_a("container.sim.ATMs"))) {
                                    this.structure[acount] = Block.func_149682_b(BlockLoader.blockControlBox) + ":1";
                                } else if ("other".equals(this.type) || "special".equals(this.type)) {
                                    this.structure[acount] = Block.func_149682_b(BlockLoader.blockControlBox) + ":2";
                                } else {
                                    this.structure[acount] = Block.func_149682_b(BlockLoader.blockControlBox) + ":0";
                                }
                            } else if ("*".equals(ch)) {//灯箱
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":0";
                            } else if ("+".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":3";
                            } else if ("-".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":5";
                            } else if ("0".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":0";
                            } else if ("1".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":1";
                            } else if ("2".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":2";
                            } else if ("3".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":3";
                            } else if ("4".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":4";
                            } else if ("5".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":5";
                            } else if ("6".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":6";
                            } else if ("7".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":7";
                            } else if ("8".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":8";
                            } else if ("9".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":9";
                            } else if ("Ã€".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":0";
                            } else if ("Ã†".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":1";
                            } else if ("Ã‡".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":2";
                            } else if ("Ãˆ".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":3";
                            } else if ("ÃŒ".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":4";
                            } else if ("Ã�".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":5";
                            } else if ("Ã‘".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":6";
                            } else if ("Ã’".equals(ch)) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockLightBox) + ":7";
                            } else if (cha >= '0' && cha <= '9') {
                                //生活区地毯
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":" + cha;
                            } else if ((int) cha >= 48 && (int) cha <= 57) {
                                this.structure[acount] = Block.func_149682_b(BlockLoader.blockSpecial) + ":" + cha;
                            } else {
                                String newCh = (String) thekey.get(ch);
                                if (newCh != null) {
                                    this.structure[acount] = newCh;
                                    String[] sbid = this.structure[acount].split(":");
                                    int bid = Integer.parseInt(sbid[0]);
                                    Block block = Block.func_149729_e(bid);
//                                    if(bid==3215){
//                                        System.out.println(block.getUnlocalizedName());
//                                    }
                                    this.addToRequirements(block, 1);
                                }

                            }

                            acount++;
                            bcount++;
                            if (!ch.contentEquals("A")) {
                                this.blocksInBuilding++;
                            }
                        } catch (Exception e) {
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error(displayName + "：--》建筑加载异常:" + e.getMessage()+"行数："+element.getLineNumber());
                        }
                    }
                }
            }

            br.close();
            in.close();
            //租金
            this.rent = (float) this.blocksInBuilding * 0.01F;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error(displayName + "：建筑异常:" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 添加到需求中
     *
     * @param block
     * @param amount
     */
    private void addToRequirements(Block block, int amount) {
        try {
            int val;
            //得到当前块
            ItemStack theBlock = new ItemStack(block, 1);
            String name = "";
            String planks = I18n.func_135052_a("container.sim.building.planks");
            String cobblestone = I18n.func_135052_a("container.sim.building.cobblestone");
            String glass = I18n.func_135052_a("container.sim.building.glass");
            String wool = I18n.func_135052_a(" container.sim.building.wool");
            String bricks = I18n.func_135052_a("container.sim.building.bricks");
            String dirt = I18n.func_135052_a("container.sim.building.dirt");
            String stone_bricks = I18n.func_135052_a("container.sim.building.stone_bricks");
            String fence = I18n.func_135052_a("container.sim.building.fence");
            String stone = I18n.func_135052_a("container.sim.building.stone");
            String wood = I18n.func_135052_a("container.sim.building.wood");
            String slab = I18n.func_135052_a("container.sim.building.slab");
            String door = I18n.func_135052_a("container.sim.building.door");
            String stairs = I18n.func_135052_a("container.sim.building.stairs");
            String grass = I18n.func_135052_a("container.sim.building.grass");
            String bed = I18n.func_135052_a("container.sim.building.bed");
            //如果游戏模式为普通
            if (GameMode.gameMode == GameMode.GAMEMODES.NORMAL) {
                try {
                    //获取方块名称
                    name = theBlock.func_82833_r().toLowerCase();
                } catch (Exception e) {
                    name = "????";
                }
                //System.out.println(name);
                //如果 名字包含 木板，圆石，玻璃，羊毛，砖块，泥土，石砖，栅栏，石头，木头，石板，并且不包含 门，楼梯，草方块
                if (name.contains(planks) || name.contentEquals(cobblestone)
                        || name.contentEquals(glass) || name.contains(wool)
                        || name.contentEquals(bricks)
                        || name.contentEquals(dirt)
                        || name.contentEquals(stone_bricks)
                        || name.contentEquals(fence)
                        || name.contentEquals(stone)
                        || (name.contains(wood) && !name.contains(slab) && !name.contains(door)
                        && !name.contains(stairs) && !name.contains(grass))) {
                    boolean got = false;
                    Iterator it = requirements.entrySet().iterator();
                    for (Map.Entry pairs : requirements.entrySet()) {
                        ItemStack is = (ItemStack) pairs.getKey();

                        if (is.func_77973_b() == theBlock.func_77973_b()) {
                            val = (Integer) pairs.getValue();
                            val++;
                            pairs.setValue(val);
                            got = true;
                            break;
                        }
                    }
                    if (!got) {
                        this.requirements.put(theBlock, 1);
                    }
                }
            } else if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                //创造模式
                return;
            } else if (GameMode.gameMode == GameMode.GAMEMODES.HARDCORE) {
                //专家模式
                name = "";

                try {
                    name = theBlock.func_82833_r().toLowerCase();
                } catch (Exception e) {
                    name = "????";
                }

                if (!name.contains(grass) && !name.contains(bed)) {
                    boolean got = false;
                    for (Map.Entry pairs : requirements.entrySet()) {
                        ItemStack is = (ItemStack) pairs.getKey();
                        if (is.func_77973_b() == theBlock.func_77973_b()) {
                            val = (Integer) pairs.getValue();
                            val++;
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑addToRequirements出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 复制数组数列
     *
     * @param from
     * @param to
     */
    private static void copyArrayList(CopyOnWriteArrayList<Building> from, CopyOnWriteArrayList<Building> to) {
        try {
            for (int i = 0; i < from.size(); i++) {
                to.add(from.get(i));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑copyArrayList出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 获取建筑蓝图
     *
     * @param theType
     * @param searchWords
     * @return
     */
    public static CopyOnWriteArrayList<Building> getBuildingBlueprints(String theType, String searchWords) {
        CopyOnWriteArrayList retBuildings = new CopyOnWriteArrayList();

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
                } catch (Exception e) {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getBuildingBlueprints出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
        try {
            if (ModSimReloaded.theBuildings.size() == 0) {
                loadAllBuildings();
            }

            for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
                try {
                    b = (Building) ModSimReloaded.theBuildings.get(x);
                    if (b.primaryXYZ.isSameCoordsAs(primaryXYZ, false, true)) {
                        return b;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getBuilding出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return b;
    }

    public static Building getBuildingBySearch(String searchWord) {
        Building b = null;
        try {
            for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
                b = (Building) ModSimReloaded.theBuildings.get(x);
                if (b.displayName.contains(searchWord)) {
                    return b;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getBuildingBySearch出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return b;
    }

    public static CopyOnWriteArrayList<Building> getBuildingBySearch(String searchWord, boolean findAll) {
        CopyOnWriteArrayList<Building> ret = new CopyOnWriteArrayList();
        try {
            for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
                Building b = (Building) ModSimReloaded.theBuildings.get(x);
                if (b.displayName.toLowerCase().contains(searchWord.toLowerCase())) {
                    ret.add(b);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getBuildingBySearch出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    public static Building getBuildingByConBox(V3 conBoxLoc) {
        Building b = null;
        try {
            for (int x = 0; x < ModSimReloaded.theBuildings.size(); ++x) {
                b = (Building) ModSimReloaded.theBuildings.get(x);

                try {
                    if (b.conBoxLocation.isSameCoordsAs(conBoxLoc, true, true)) {
                        return b;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑getBuildingByConBox出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return b;
    }

    public void saveThisBuilding() {
        try {
            CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList();
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
                for (String tennant : this.tenants) {
                    if (!tennant.trim().contentEquals("")) {
                        temp = temp + tennant.trim() + ",";
                    }
                }

                strings.add(temp);
                temp = "blocklocs|";
                for (V3 block : this.blockLocations) {
                    if (block != null & block.toString().contains(",")) {
                        temp = temp + block.toString() + "B";
                    }
                }

                strings.add(temp);
                if (this.blockSpecial.size() > 0) {
                    temp = "blockspecial|";
                    for (V3 block : this.blockSpecial) {
                        if (block != null & block.toString().contains(",")) {
                            temp = temp + block.toString() + "," + block.meta + "B";
                        }
                    }

                    strings.add(temp);
                }

                ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator + xyz + ".sk2", strings);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑saveThisBuilding出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void saveAllBuildings() {
        try {
            Minecraft mc = Minecraft.func_71410_x();
            CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList();

            for (int b = 0; b < ModSimReloaded.theBuildings.size(); ++b) {
                strings.clear();
                Building building = (Building) ModSimReloaded.theBuildings.get(b);
                if (building != null && building.primaryXYZ != null) {
                    V3 pxyz = building.primaryXYZ;
                    World buildingWorld = MinecraftServer.func_71276_C().func_71218_a(building.primaryXYZ.theDimension);
                    Block id = buildingWorld.func_180495_p(new BlockPos(pxyz.x.intValue(), pxyz.y.intValue(), pxyz.z.intValue())).func_177230_c();
                    String xyz = "b" + building.primaryXYZ.toString().replaceAll(",", "_");
                    if (id != BlockLoader.blockControlBox && id != BlockLoader.blockConstructorBox) {
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
                        for (String tennant : building.tenants) {
                            if (!tennant.trim().contentEquals("")) {
                                temp = temp + tennant.trim() + ",";
                            }
                        }

                        strings.add(temp);

                        try {
                            temp = "blocklocs|";
                            for (V3 block : building.blockLocations) {
                                if (block != null & block.toString().contains(",")) {
                                    temp = temp + block.toString() + "B";
                                }
                            }
                            strings.add(temp);
                        } catch (Exception e) {
                        }

                        try {
                            if (building.blockSpecial.size() > 0) {
                                temp = "blockspecial|";
                                for (V3 block : building.blockSpecial) {
                                    if (block != null & block.toString().contains(",")) {
                                        temp = temp + block.toString() + "," + block.meta + "B";
                                    }
                                }

                                strings.add(temp);
                            }
                        } catch (Exception e) {
                        }

                        ModSimReloaded.saveSK2(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator + xyz + ".sk2", strings);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("saveAllBuildings出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        ModSimReloaded.log.info("建筑物.saveAllBuildings " + ModSimReloaded.theBuildings.size() + " 建筑");
    }

    public static void loadAllBuildings() {
        try {
            File buildingsFolder = new File(ModSimReloaded.getSavesDataFolder() + "Buildings" + File.separator);
            buildingsFolder.mkdirs();
            File[] files = buildingsFolder.listFiles();
            File f;
            Building build;
            ModSimReloaded.theBuildings.clear();
            label166:
            for (int i = 0; i < files.length; i++) {

                f = files[i];
                if (f.getName().endsWith(".sk2")) {

                    CopyOnWriteArrayList<String> strings = ModSimReloaded.loadSK2(f.getAbsoluteFile().toString());
                    build = new Building();
                    for (String line : strings) {
                        int m1 = line.indexOf("|");
                        String name = line.substring(0, m1);
                        String value = line.substring(m1 + 1);
                        if (name.contentEquals("displayname")) {
                            build.displayName = value;
                        } else if (name.contentEquals("type")) {
                            build.type = value;
                        } else if (name.contentEquals("primaryxyz")) {
                            if (!value.contentEquals("null")) {
                                build.primaryXYZ = new V3(value);
                            }
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

                                    for (int i_j = 0; i_j < lengths; i_j++) {
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

                                    for (int i1 = 0; i1 < lengths; i1++) {
                                        block = array[i1];
                                        if (block.contains(",")) {
                                            build.blockLocations.add(new V3(block));
                                        }
                                    }
                                }
                            } else if (name.contentEquals("blockspecial") && value.contains("B") && value.contains(",")) {
                                blocks = value.split("B");
                                array = blocks;

                                for (int i_j = 0; i_j < blocks.length; i_j++) {
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
                    build.loadStructure();
                    ModSimReloaded.theBuildings.add(build);
                    continue label166;
                } else {
                    if (f.getName().endsWith(".suk")) {
                        build = (Building) ModSimReloaded.loadObject(f.getAbsoluteFile().toString());
                        if (build != null) {
                            V3 xyz = build.primaryXYZ;
                            World buildingWorld = MinecraftServer.func_71276_C().func_71218_a(build.primaryXYZ.theDimension);
                            Block id = buildingWorld.func_180495_p(new BlockPos(xyz.x.intValue(), xyz.y.intValue(), xyz.z.intValue())).func_177230_c();
                            Building dupe = null;
                            if (ModSimReloaded.theBuildings.size() > 0) {
                                dupe = getBuilding(xyz);
                            }

                            if (id == BlockLoader.blockControlBox && dupe == null) {
                                build.loadStructure();
                                ModSimReloaded.theBuildings.add(build);
                            } else {
                                f.delete();
                                ModSimReloaded.log.info("Building: 已删除作为id的建筑=" + id + " or dupe");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("loadAllBuildings出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    public static void checkTenants() {
        try {
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
                    } catch (Exception e) {
                        //var7.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("checkTenants出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void initialiseAllBuildings() {
        try {
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initialiseAllBuildings出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static Building getBuildingForFolk(String partialFilename, String type) {
        Building build = null;
        try {
            File f = new File(ModSimReloaded.getSimukraftFolder() + "/buildings/" + type + "/" + partialFilename);
            if (f.exists()) {
                String name = f.getName().substring(0, f.getName().length() - 4);
                build = new Building(name, type);
                build.loadStructure();
                return build;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getBuildingForFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return build;
    }

    private static void initBuildingsOfType(String type) {
        try {
            File f = new File(ModSimReloaded.getSimukraftFolder() + "/buildings/" + type);

            for (int i = 0; i < f.list().length; i++) {
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
                } else if (type.contentEquals("special")) {
                    buildingsSpec.add(build);
                }

                try {
                    Thread.sleep(30L);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initBuildingsOfType出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    public static Building getFromAllBuildings(String fullname, String type) {
        try {
            if (type.contentEquals("residential")) {
                for (Building build:buildingsRes){
                    if (build.displayName.contentEquals(fullname)) {
                        return build.clone();
                    }
                }
            } else if (type.contentEquals("commercial")) {
                for (Building build:buildingsCom){
                    if (build.displayName.contentEquals(fullname)) {
                        return build.clone();
                    }
                }
            } else if (type.contentEquals("industrial")) {
                for (Building build:buildingsInd){
                    if (build.displayName.contentEquals(fullname)) {
                        return build.clone();
                    }
                }
            } else if (type.contentEquals("other")) {
                for (Building build:buildingsOth){
                    if (build.displayName.contentEquals(fullname)) {
                        return build.clone();
                    }
                }
            } else if (type.contentEquals("special")) {
                for (Building build:buildingsSpec){
                    if (build.displayName.contentEquals(fullname)) {
                        return build.clone();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getFromAllBuildings出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return null;
    }
}
