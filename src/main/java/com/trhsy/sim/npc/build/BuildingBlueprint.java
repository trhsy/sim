package com.trhsy.sim.npc.build;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName BuildingBlueprint
 * @Description todo 建筑蓝图
 * @Author TRHSY
 * @Date 2022/10/1917:42
 **/
public class BuildingBlueprint implements Comparable<BuildingBlueprint> {
    /**
     * 蓝图名称
     */
    public String name = I18n.format("container.sim.gui_Folk_Unknown");
    /**
     * 描述
     */
    public String desc = "";
    /**
     * 建筑类型
     */
    public String buildingType = "";
    /**
     * 作者
     */
    public String author = "Trhsy";
    /**
     * 工作
     */
    public String jobType = "null";
    /**
     * 文件内容
     */
    public String fileContents;
    /**
     * 长
     */
    public int length;
    /**
     * 宽
     */
    public int width;
    /**
     * 高
     */
    public int height;
    /**
     * 建筑方向
     */
    public int direction = 0;
    /**
     * 计数块
     */
    public int blockCount = 0;
    public List<BuildingSymbol> blocks = new CopyOnWriteArrayList<>();
    //蓝图建筑结构
    public IBlockState[] structure;
    public List<BuildingBlueprint> styles = new CopyOnWriteArrayList();

    public BuildingBlueprint(File file) {
        this.addBuildingBlueprint(file);
    }

    public BuildingBlueprint(String bName, String string) {
        try {
            Scanner sc = new Scanner(string);
            Throwable var4 = null;

            try {
                this.name = bName;
                this.fileContents = string;
                String dim = sc.nextLine();
                this.length = Integer.valueOf(dim.split("x")[0]);
                this.width = Integer.valueOf(dim.split("x")[1]);
                this.height = Integer.valueOf(dim.split("x")[2]);
                this.structure = new IBlockState[this.length * this.width * this.height];
                String keyLine = sc.nextLine();
                String[] keys = keyLine.split(";");
                String[] var8 = keys;
                int charNumber = keys.length;

                for (int var10 = 0; var10 < charNumber; ++var10) {
                    String k = var8[var10];
                    if (k.contains("AU")) {
                        this.author = k.split("=")[1];
                    }

                    if (k.contains("DIR")) {
                        this.direction = Integer.valueOf(k.split("=")[1]);
                    } else if (k.contains("DESC")) {
                        this.desc = k.split("=")[1];
                    } else if (k.contains("JOB")) {
                        this.jobType = k.split("=")[1];
                        ModSimLoader.log.info("Found jobType " + this.jobType + " in " + this.name);
                    } else if (Integer.valueOf(k.split("=")[1].split(",").length) > 1) {
                        this.blocks.add(new BuildingSymbol(k.split("=")[0], k.split("=")[1].split(",")[0], Integer.valueOf(k.split("=")[1].split(",")[1])));
                    } else {
                        this.blocks.add(new BuildingSymbol(k.split("=")[0], k.split("=")[1].split(",")[0], 0));
                    }
                }

                charNumber = 0;
                boolean hasControlBox = false;

                String line;
                while ((line = sc.nextLine()) != null) {
                    char[] var33 = line.toCharArray();
                    int var12 = var33.length;

                    for (int var13 = 0; var13 < var12; ++var13) {
                        char c = var33[var13];
                        //生活块
                        if (String.valueOf(c).contentEquals("!")) {
                            Block block = BlockLoader.blockLiving;
                            this.structure[charNumber] = block.getDefaultState();
                            ++charNumber;
                            //控制箱
                        } else if (String.valueOf(c).contentEquals("$")) {
                            hasControlBox = true;
                            Block block = BlockLoader.blockControlBox;
                            if (this.name.contentEquals(I18n.format("container.sim.ATMs"))) {
                                this.structure[charNumber] = block.getStateFromMeta(1);
                            } else if ("other".equals(this.buildingType) || "special".equals(this.buildingType)) {
                                this.structure[charNumber] = block.getStateFromMeta(2);
                            } else {
                                this.structure[charNumber] = block.getStateFromMeta(0);
                            }
                            ++charNumber;
                            //灯箱
                        } else if (String.valueOf(c).contentEquals("*")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(0);
                            ++charNumber;
                        } else if (String.valueOf(c).contentEquals("+")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(3);
                            ++charNumber;
                        } else if (String.valueOf(c).contentEquals("-")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("0".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(0);
                            ++charNumber;
                        } else if ("1".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(1);
                            ++charNumber;
                        } else if ("2".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(2);
                            ++charNumber;
                        } else if ("3".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(3);
                            ++charNumber;
                        } else if ("4".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(4);
                            ++charNumber;
                        } else if ("5".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("6".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(6);
                            ++charNumber;
                        } else if ("7".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(7);
                            ++charNumber;
                        } else if ("8".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(8);
                            ++charNumber;
                        } else if ("9".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(9);
                            ++charNumber;
                        } else if ("Ã€".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(0);
                            ++charNumber;
                        } else if ("Ã†".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(1);
                            ++charNumber;
                        } else if ("Ã‡".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(2);
                            ++charNumber;
                        } else if ("Ãˆ".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(3);
                            ++charNumber;
                        } else if ("ÃŒ".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(4);
                            ++charNumber;
                        } else if ("Ã�".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("Ã‘".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(6);
                            ++charNumber;
                        } else if ("Ã’".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(7);
                            ++charNumber;
                        } else {
                            String symbol = String.valueOf(c);
                            Block block = null;
                            Iterator var17 = this.blocks.iterator();

                            while (var17.hasNext()) {
                                BuildingSymbol bs = (BuildingSymbol) var17.next();
                                if (bs.symbol.contentEquals(symbol)) {
                                    block = (Block) Block.REGISTRY.getObject(new ResourceLocation(bs.blockName));
                                    IBlockState blockstate = block.getStateFromMeta(bs.meta);
                                    this.structure[charNumber] = blockstate;
                                    ++charNumber;
                                    break;
                                }
                            }
                        }
                    }
                }

                sc.close();
                if (!hasControlBox) {
                    this.structure[0] = BlockLoader.blockControlBox.getDefaultState();
                }
            } catch (Throwable var28) {
                var4 = var28;
                throw var28;
            } finally {
                if (sc != null) {
                    if (var4 != null) {
                        try {
                            sc.close();
                        } catch (Throwable var27) {
                            var4.addSuppressed(var27);
                        }
                    } else {
                        sc.close();
                    }
                }

            }
        } catch (Exception var30) {
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 从文件 添加建筑蓝图
     * @Date 15:58 2022/11/1
     * @Param [file]
     **/
    public void addBuildingBlueprint(File file) {
        try {
//            if(!file.getName().contains("new")){


            InputStream inputStream =new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            /*String newFile=file.getAbsolutePath();
            newFile=newFile.substring(0,newFile.length()-4);
            File file1=new File(newFile+"_new.txt");
            if(!file1.exists()){
                file1.createNewFile();
            }
            FileWriter fw = new FileWriter(file1.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);*/


            Throwable var3 = null;
            try {
                this.fileContents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
                this.name = file.getName().replace(".txt", "");
                System.out.println("当前建筑："+this.name);
                if (file.getAbsolutePath().contains("residential")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_Residential");
                } else if (file.getAbsolutePath().contains("commercial")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_Commercial");
                } else if (file.getAbsolutePath().contains("industrial")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_Industrial");
                } else if (file.getAbsolutePath().contains("other")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_Other");
                } else if (file.getAbsolutePath().contains("special")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_special");
                }/* else if (file.getAbsolutePath().contains("administrative")) {
                    this.buildingType = "administrative";
                } */ else if (file.getAbsolutePath().contains("decorative")) {
                    this.buildingType = I18n.format("container.sim.sim_gui_BC_Decorative");
                }

                String dim = br.readLine();
//                bw.write(dim);
//                bw.write("\r\n");
                this.length = Integer.valueOf(dim.split("x")[0]);
                this.width = Integer.valueOf(dim.split("x")[1]);
                this.height = Integer.valueOf(dim.split("x")[2]);
                this.structure = new IBlockState[this.length * this.width * this.height];
                String keyLine = br.readLine();
                String[] keys = keyLine.split(";");
                String[] var7 = keys;
                int charNumber = keys.length;

                for (int var9 = 0; var9 < charNumber; ++var9) {
                    String k = var7[var9];
                    if (k.contains("AU")) {
                        this.author = k.split("=")[1];
//                        bw.write(k+";");
                        //描述
                    } else if (k.contains("DIR")) {
                        this.direction = Integer.valueOf(k.split("=")[1]);
//                        bw.write(k+";");
                        //工作
                    } else if (k.contains("DESC")) {
                        this.desc = k.split("=")[1];
                    } else if (k.contains("JOB")) {
                        this.jobType = k.split("=")[1];
//                        bw.write(k+";");
//                        ModSimLoader.log.info("找到工作类型： " + this.jobType + " ，在： " + this.name);
                    } else if (Integer.valueOf(k.split("=")[1].split(":").length) > 1) {
                        String symbol = k.split("=")[0];
                        String blockNames = k.split("=")[1];
                        String blockName = blockNames.split(",")[0];
                        int meta = Integer.valueOf(blockNames.split(",")[1]);
                        Block block = Block.getBlockFromName(blockName);
                        this.blocks.add(new BuildingSymbol(symbol, block.getRegistryName().toString(), meta));
                    } else {
                        String symbol = k.split("=")[0];
                        String blockNames = k.split("=")[1];
                        String blockName = blockNames.split(",")[0];
                        int meta = Integer.valueOf(blockNames.split(",")[1]);
                        Block block = Block.getBlockFromName(blockName);
                        //System.out.println(block.getUnlocalizedName());
//                        bw.write(symbol+"="+block.getRegistryName()+","+meta+";");
                        this.blocks.add(new BuildingSymbol(symbol, block.getRegistryName().toString(), 0));
                    }

                }
//                bw.write("\r\n");

                charNumber = 0;

                String line;
                while ((line = br.readLine()) != null) {
//                    bw.write(line);
//                    bw.write("\r\n");
                    char[] var30 = line.toCharArray();

                    for (int var11 = 0; var11 < var30.length; ++var11) {
                        char c = var30[var11];
                        //生活块
                        if (String.valueOf(c).contentEquals("!")) {
                            Block block = BlockLoader.blockLiving;
                            this.structure[charNumber] = block.getDefaultState();
                            ++charNumber;
                            //控制箱
                        } else if (String.valueOf(c).contentEquals("$")) {
                            Block block = BlockLoader.blockControlBox;
                            if (this.name.contentEquals(I18n.format("container.sim.ATMs"))) {
                                this.structure[charNumber] = block.getStateFromMeta(1);
                            } else if ("other".equals(this.buildingType) || "special".equals(this.buildingType)) {
                                this.structure[charNumber] = block.getStateFromMeta(2);
                            } else {
                                this.structure[charNumber] = block.getStateFromMeta(0);
                            }
//                            this.structure[charNumber] = block.getDefaultState();
                            ++charNumber;
                        } else if (String.valueOf(c).contentEquals("*")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(0);
                            ++charNumber;
                        } else if (String.valueOf(c).contentEquals("+")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(3);
                            ++charNumber;
                        } else if (String.valueOf(c).contentEquals("-")) {
                            this.structure[charNumber] = BlockLoader.blockLightBox.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("0".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(0);
                            ++charNumber;
                        } else if ("1".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(1);
                            ++charNumber;
                        } else if ("2".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(2);
                            ++charNumber;
                        } else if ("3".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(3);
                            ++charNumber;
                        } else if ("4".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(4);
                            ++charNumber;
                        } else if ("5".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("6".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(6);
                            ++charNumber;
                        } else if ("7".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(7);
                            ++charNumber;
                        } else if ("8".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(8);
                            ++charNumber;
                        } else if ("9".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockSpecial.getStateFromMeta(9);
                            ++charNumber;
                        } else if ("Ã€".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(0);
                            ++charNumber;
                        } else if ("Ã†".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(1);
                            ++charNumber;
                        } else if ("Ã‡".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(2);
                            ++charNumber;
                        } else if ("Ãˆ".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(3);
                            ++charNumber;
                        } else if ("ÃŒ".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(4);
                            ++charNumber;
                        } else if ("Ã�".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(5);
                            ++charNumber;
                        } else if ("Ã‘".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(6);
                            ++charNumber;
                        } else if ("Ã’".equals(c)) {
                            this.structure[charNumber] = BlockLoader.blockLiving.getStateFromMeta(7);
                            ++charNumber;
                        } else {
                            String symbol = String.valueOf(c);
                            Block block = null;
                            for (BuildingSymbol bs : this.blocks) {
                                if (bs.symbol.contentEquals(symbol)) {
                                    block = (Block) Block.REGISTRY.getObject(new ResourceLocation(bs.blockName));
                                    IBlockState blockstate = block.getStateFromMeta(bs.meta);
                                    if (blockstate != null) {
                                        this.structure[charNumber] = blockstate;
                                        ++charNumber;
                                        break;
                                    }
                                }
                            }
                        }
                        ++this.blockCount;
                    }
                }
                System.out.println("当前建筑所需方块："+this.blockCount);
                //ModSimLoader.log.info("读取建筑物 " + file.getName());
                br.close();
//                bw.close();
            } catch (Throwable var26) {
                throw var26;
            } finally {
                if (br != null) {
                    if (var3 != null) {
                        try {
                            br.close();
//                            bw.close();
                        } catch (Throwable var25) {
                            StackTraceElement element = var25.getStackTrace()[0];
                            ModSimLoader.log.error("addBuildingBlueprint-br出错了：" + var25.getMessage() + "行数：" + element.getLineNumber());
                        }
                    } else {
                        br.close();
//                        bw.close();
                    }
                }

            }
        } catch (Exception var28) {
            StackTraceElement element = var28.getStackTrace()[0];
            ModSimLoader.log.error("addBuildingBlueprint出错了：" + var28.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * @return java.util.Hashtable<net.minecraft.item.Item, java.lang.Integer>
     * @Author fan
     * @Description //TODO 获取建筑需求
     * @Date 15:57 2022/11/1
     * @Param []
     **/
    public Hashtable<Item, Integer> getBuildingRequirements() {
        Hashtable<Item, Integer> ret = new Hashtable();
        IBlockState[] var2 = this.structure;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            IBlockState state = var2[var4];
            if (state != null) {
                Block blockId = state.getBlock();
                //检查是否是必须的块
                if (this.isRequiredBlock(blockId)) {
                    Item item = Item.getItemFromBlock(state.getBlock());
                    if (ret.containsKey(item)) {
                        ret.put(item, (Integer) ret.get(item) + 1);
                    } else {
                        ret.put(item, 1);
                    }
                }
            }
        }

        return ret;
    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 获取建筑要求
     * @Date 12:57 2022/11/1
     * @Param []
     **/
    public String getBuildingRequirementsString() {
        //实际计数
        int actualCount = -1;
        StringBuilder b = new StringBuilder();
        //建筑蓝图不为空
        if (this.getBuildingRequirements() != null && this.getBuildingRequirements().size() > 0) {
            if (actualCount == -1) {
                Iterator it = this.getBuildingRequirements().entrySet().iterator();
                actualCount = 0;
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    if (pairs.getKey() != null) {
                        ++actualCount;
                    }
                }
            }

            float offset = (float) actualCount * 0.2F + 2.5F;
            //所需的块
            b.append(I18n.format("container.sim.render_1") + this.name + ";");

            try {
                Iterator it = this.getBuildingRequirements().entrySet().iterator();

                while (it.hasNext()) {
                    try {
                        Map.Entry pairs = (Map.Entry) it.next();
                        if (pairs.getValue() != null) {
                            String st = pairs.getValue().toString();
                            double stacks = Math.ceil(Double.parseDouble(st) / 64.0D);
                            String ss = "";
                            if ((int) stacks == 0) {
                                //小于1堆
                                ss = I18n.format("container.sim.render_2");
                            } else if ((int) stacks == 1) {
                                //1个堆
                                ss = I18n.format("container.sim.render_3");
                            } else {
                                //堆
                                ss = (int) stacks + I18n.format("container.sim.render_4");
                            }
                            Item is = (Item) pairs.getKey();
                            int quantity = (Integer) pairs.getValue();
                            if (quantity > 0) {
                                String itemName = is.getItemStackDisplayName(new ItemStack(is));
                                //橡木
                                if (itemName.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC9"))) {
                                    //原木
                                    itemName = I18n.format("container.sim.sim_gui_BC10");
                                }
                                //橡木木板
                                if (itemName.toLowerCase().contains(I18n.format("container.sim.sim_gui_BC11"))) {
                                    //木板
                                    itemName = I18n.format("container.sim.sim_gui_BC12");
                                }

                                String line = pairs.getValue() + " x " + itemName + " (" + ss + ")";
                                b.append(line + ";");
                                offset -= 0.2F;
                            }
                        }
                    } catch (Exception var14) {
                        StackTraceElement element = var14.getStackTrace()[0];
                        ModSimLoader.log.error("getBuildingRequirementsString出错了：" + var14.getMessage() + "行数：" + element.getLineNumber());
                    }
                }
            } catch (Exception var15) {
            }
        } else {
            //暂时没有其他需求
            b.append(I18n.format("container.sim.render_No_further_requirements"));
        }
        return b.toString();
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 是否是必须的块
     * @Date 15:54 2022/11/1
     * @Param [blockId]
     **/
    public boolean isRequiredBlock(Block blockId) {
        //木板 圆石 玻璃 羊毛 砖块 泥土 石砖 栅栏 墙 石头 原木
        return blockId == Blocks.PLANKS || blockId == Blocks.COBBLESTONE || blockId == Blocks.GLASS || blockId == Blocks.WOOL || blockId == Blocks.BRICK_BLOCK || blockId == Blocks.DIRT || blockId == Blocks.STONEBRICK || blockId instanceof BlockFence || blockId instanceof BlockWall || blockId == Blocks.STONE || blockId == Blocks.LOG || blockId == Blocks.LOG2;
    }

    /**
     * @return java.lang.String
     * @Author fan
     * @Description //TODO 获取维度
     * @Date 15:57 2022/11/1
     * @Param []
     **/
    public String getDimensions() {
        return this.length + " x " + this.height + " x " + this.width;
    }

    /**
     * @return net.minecraft.util.math.Vec3d
     * @Author fan
     * @Description //TODO 获取第一个预览点
     * @Date 16:07 2022/10/31
     * @Param [pos, buildDirection]
     **/
    public Vec3d getFirstPoint(BlockPos pos, int buildDirection) {
        //北方
        EnumFacing facing = EnumFacing.NORTH;
        switch (buildDirection) {
            case 0:
                facing = EnumFacing.NORTH;
                break;
            case 1:
                //东方
                facing = EnumFacing.EAST;
                break;
            case 2:
                //南方
                facing = EnumFacing.SOUTH;
                break;
            case 3:
                //西方
                facing = EnumFacing.WEST;
        }

        return new Vec3d(pos.offset(facing));
    }

    /**
     * @return net.minecraft.util.math.Vec3d
     * @Author fan
     * @Description //TODO 获取第二个预览点
     * @Date 16:12 2022/11/1
     * @Param [pos, buildDirection]
     **/
    public Vec3d getSecondPoint(BlockPos pos, int buildDirection) {
        EnumFacing facing = EnumFacing.NORTH;
        switch (buildDirection) {
            case 0:
                facing = EnumFacing.NORTH;
                break;
            case 1:
                facing = EnumFacing.EAST;
                break;
            case 2:
                facing = EnumFacing.SOUTH;
                break;
            case 3:
                facing = EnumFacing.WEST;
        }

        return new Vec3d(pos.offset(facing, this.width + 1).up(this.height).offset(facing.rotateY(), this.length));
    }

    @Override
    public int compareTo(BuildingBlueprint o) {
        return this.structure.length - o.structure.length;
    }
}
