package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ========================================
 *
 * @ClassName BuildingReader
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:47
 * ========================================
 **/
public class BuildingReader implements Serializable {
    private static final long serialVersionUID = -1132989807904279141L;
    public String displayName;
    public String type;
    public String[] structure;
    public int layerCount = 0;
    public int width = 0;
    public int length = 0;
    public V3 primaryXYZ;
    public V3 livingXYZ;
    public boolean buildingComplete = false;
    public int capacity = -1;
    public String buildDirection = "";
    public V3 lumbermillMarker = null;
    public int blocksInBuilding = 0;
    public String pk = "0";
    public String author = "Satscape";
    public String displayNameWithoutPK = "";
    public Float rent = 0.0F;
    public Float corpTax = 0.0F;
    public ArrayList<String> tennants = new ArrayList();
    public ArrayList<V3> blockLocations = new ArrayList();
    public transient HashMap<ItemStack, Integer> requirements = new HashMap();
    public transient V3 conBoxLocation = null;
    private static transient ArrayList<Building> buildingsRes = new ArrayList();
    private static transient ArrayList<Building> buildingsCom = new ArrayList();
    private static transient ArrayList<Building> buildingsInd = new ArrayList();
    private static transient ArrayList<Building> buildingsOth = new ArrayList();
    String block1 = "";
    int block1Count = 0;
    String block2 = "";
    int block2Count = 0;
    String block3 = "";
    int block3Count = 0;
    String block4 = "";
    int block4Count = 0;
    String block5 = "";
    int block5Count = 0;
    String block6 = "";
    int block6Count = 0;
    String block7 = "";
    int block7Count = 0;
    String block8 = "";
    int block8Count = 0;
    String block9 = "";
    int block9Count = 0;
    String block10 = "";
    int block10Count = 0;
    String block11 = "";
    int block11Count = 0;
    String block12 = "";
    int block12Count = 0;
    String block13 = "";
    int block13Count = 0;
    String block14 = "";
    int block14Count = 0;
    String block15 = "";
    int block15Count = 0;
    String block16 = "";
    int block16Count = 0;
    String block17 = "";
    int block17Count = 0;
    String block18 = "";
    int block18Count = 0;
    String block19 = "";
    int block19Count = 0;
    String block20 = "";
    int block20Count = 0;
    String block21 = "";
    int block21Count = 0;
    String block22 = "";
    int block22Count = 0;
    String block23 = "";
    int block23Count = 0;
    String block24 = "";
    int block24Count = 0;
    String block25 = "";
    int block25Count = 0;
    String block26 = "";
    int block26Count = 0;

    public BuildingReader(String fname, String theType) {
        this.type = theType;
        this.displayName = fname;
        this.buildingComplete = false;
        if (this.requirements == null) {
            this.requirements = new HashMap();
        }

    }

    public BuildingReader(String fname, String theType, V3 pxyz, V3 lxyz, boolean isComplete) {
        this.type = theType;
        this.displayName = fname;
        this.primaryXYZ = pxyz;
        this.livingXYZ = lxyz;
        this.buildingComplete = isComplete;
        if (this.requirements == null) {
            this.requirements = new HashMap();
        }

    }

    public void removeTennant(String tennant) {
        for(int t = 0; t < this.tennants.size(); ++t) {
            String ten = (String)this.tennants.get(t);
            if (ten.contentEquals(tennant)) {
                this.tennants.remove(t);
                break;
            }
        }

    }

    private void loadStructure() {
        this.block1 = "";
        this.block1Count = 0;
        this.block2 = "";
        this.block2Count = 0;
        this.block3 = "";
        this.block3Count = 0;
        this.block4 = "";
        this.block4Count = 0;
        this.block5 = "";
        this.block5Count = 0;
        this.block6 = "";
        this.block6Count = 0;
        this.block7 = "";
        this.block7Count = 0;
        this.block8 = "";
        this.block8Count = 0;
        this.block9 = "";
        this.block9Count = 0;
        this.block10 = "";
        this.block10Count = 0;
        this.block11 = "";
        this.block11Count = 0;
        this.block12 = "";
        this.block12Count = 0;
        this.block13 = "";
        this.block13Count = 0;
        this.block14 = "";
        this.block14Count = 0;
        this.block15 = "";
        this.block15Count = 0;
        this.block16 = "";
        this.block16Count = 0;
        this.block17 = "";
        this.block17Count = 0;
        this.block18 = "";
        this.block18Count = 0;
        this.block19 = "";
        this.block19Count = 0;
        this.block20 = "";
        this.block20Count = 0;
        this.block21 = "";
        this.block21Count = 0;
        this.block22 = "";
        this.block22Count = 0;
        this.block23 = "";
        this.block23Count = 0;
        this.block24 = "";
        this.block24Count = 0;
        this.block25 = "";
        this.block25Count = 0;
        this.block26 = "";
        this.block26Count = 0;

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
            File f = new File(ModSim.getSimukraftFolder() + "/buildings/" + this.type + "/" + this.displayName + ".txt");
            if (!f.exists()) {
                ModSim.log.warn("找不到文件");
                return;
            }

            FileInputStream fstream = new FileInputStream(ModSim.getSimukraftFolder() + "/buildings/" + this.type + "/" + this.displayName + ".txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = br.readLine().toString().toLowerCase().trim();
            String[] lineSplit = strLine.split("x");
            int[] dimensions = new int[]{Integer.parseInt(lineSplit[0]), Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2])};
            this.structure = new String[dimensions[0] * dimensions[1] * dimensions[2]];
            this.length = dimensions[0];
            this.width = dimensions[1];
            this.layerCount = dimensions[2];
            strLine = br.readLine().toString().trim();
            HashMap blockKey = new HashMap();
            lineSplit = strLine.split(";");

            int akeyNumber;
            for(akeyNumber = 0; akeyNumber < lineSplit.length; ++akeyNumber) {
                String[] k = lineSplit[akeyNumber].split(",");
                blockKey.put(k[0], k[1]);
                if (k[0].toUpperCase().contentEquals("AU")) {
                    this.author = k[1].trim();
                }
            }

            akeyNumber = 0;
            //int bblockNumber = false;

            for(int i = 0; i < this.layerCount; ++i) {
                strLine = br.readLine().trim();
                int bblockNumber = 0;

                for(int wid = 0; wid < this.width; ++wid) {
                    for(int len = 0; len < this.length; ++len) {
                        try {
                            String letter = strLine.substring(bblockNumber, bblockNumber + 1);
                            char cha = letter.charAt(0);
                            if (letter.contentEquals("!")) {
                                this.structure[akeyNumber] = "air,999";
                            } else if (letter.contentEquals("$")) {
                                this.structure[akeyNumber] = "" + BlockLoader.blockControlBox;
                            } else if (letter.contentEquals("*")) {
                                this.structure[akeyNumber] = "" + BlockLoader.blockLightBox;
                            } else if (cha >= '0' && cha <= '9') {
                                this.structure[akeyNumber] = "air," + cha;
                            } else {
                                this.structure[akeyNumber] = (String)blockKey.get(letter);
                                String[] sbid = this.structure[akeyNumber].split(",");
                                Block bid = Block.getBlockFromName(sbid[0]);
                                if (this.block1 == sbid[0]) {
                                    ++this.block1Count;
                                } else if (this.block2 == sbid[0]) {
                                    ++this.block2Count;
                                } else if (this.block3 == sbid[0]) {
                                    ++this.block3Count;
                                } else if (this.block4 == sbid[0]) {
                                    ++this.block4Count;
                                } else if (this.block5 == sbid[0]) {
                                    ++this.block5Count;
                                } else if (this.block6 == sbid[0]) {
                                    ++this.block6Count;
                                } else if (this.block7 == sbid[0]) {
                                    ++this.block7Count;
                                } else if (this.block8 == sbid[0]) {
                                    ++this.block8Count;
                                } else if (this.block9 == sbid[0]) {
                                    ++this.block9Count;
                                } else if (this.block10 == sbid[0]) {
                                    ++this.block10Count;
                                } else if (this.block11 == sbid[0]) {
                                    ++this.block11Count;
                                } else if (this.block12 == sbid[0]) {
                                    ++this.block12Count;
                                } else if (this.block13 == sbid[0]) {
                                    ++this.block13Count;
                                } else if (this.block14 == sbid[0]) {
                                    ++this.block14Count;
                                } else if (this.block15 == sbid[0]) {
                                    ++this.block15Count;
                                } else if (this.block16 == sbid[0]) {
                                    ++this.block16Count;
                                } else if (this.block17 == sbid[0]) {
                                    ++this.block17Count;
                                } else if (this.block18 == sbid[0]) {
                                    ++this.block18Count;
                                } else if (this.block19 == sbid[0]) {
                                    ++this.block19Count;
                                } else if (this.block20 == sbid[0]) {
                                    ++this.block20Count;
                                } else if (this.block21 == sbid[0]) {
                                    ++this.block21Count;
                                } else if (this.block22 == sbid[0]) {
                                    ++this.block22Count;
                                } else if (this.block23 == sbid[0]) {
                                    ++this.block23Count;
                                } else if (this.block24 == sbid[0]) {
                                    ++this.block24Count;
                                } else if (this.block25 == sbid[0]) {
                                    ++this.block25Count;
                                } else if (this.block26 == sbid[0]) {
                                    ++this.block26Count;
                                } else if (this.block1 == "") {
                                    this.block1 = sbid[0];
                                    ++this.block1Count;
                                } else if (this.block1 != "" && this.block2 == "") {
                                    this.block2 = sbid[0];
                                    ++this.block2Count;
                                } else if (this.block2 != "" && this.block3 == "") {
                                    this.block3 = sbid[0];
                                    ++this.block3Count;
                                } else if (this.block3 != "" && this.block4 == "") {
                                    this.block4 = sbid[0];
                                    ++this.block4Count;
                                } else if (this.block4 != "" && this.block5 == "") {
                                    this.block5 = sbid[0];
                                    ++this.block5Count;
                                } else if (this.block5 != "" && this.block6 == "") {
                                    this.block6 = sbid[0];
                                    ++this.block6Count;
                                } else if (this.block6 != "" && this.block7 == "") {
                                    this.block7 = sbid[0];
                                    ++this.block7Count;
                                } else if (this.block7 != "" && this.block8 == "") {
                                    this.block8 = sbid[0];
                                    ++this.block8Count;
                                } else if (this.block8 != "" && this.block9 == "") {
                                    this.block9 = sbid[0];
                                    ++this.block9Count;
                                } else if (this.block9 != "" && this.block10 == "") {
                                    this.block10 = sbid[0];
                                    ++this.block10Count;
                                } else if (this.block10 != "" && this.block11 == "") {
                                    this.block11 = sbid[0];
                                    ++this.block11Count;
                                } else if (this.block11 != "" && this.block12 == "") {
                                    this.block12 = sbid[0];
                                    ++this.block12Count;
                                } else if (this.block12 != "" && this.block13 == "") {
                                    this.block12 = sbid[0];
                                    ++this.block12Count;
                                } else if (this.block13 != "" && this.block14 == "") {
                                    this.block14 = sbid[0];
                                    ++this.block14Count;
                                } else if (this.block14 != "" && this.block15 == "") {
                                    this.block15 = sbid[0];
                                    ++this.block15Count;
                                } else if (this.block15 != "" && this.block16 == "") {
                                    this.block16 = sbid[0];
                                    ++this.block16Count;
                                } else if (this.block16 != "" && this.block17 == "") {
                                    this.block17 = sbid[0];
                                    ++this.block17Count;
                                } else if (this.block17 != "" && this.block18 == "") {
                                    this.block18 = sbid[0];
                                    ++this.block18Count;
                                } else if (this.block18 != "" && this.block19 == "") {
                                    this.block19 = sbid[0];
                                    ++this.block19Count;
                                } else if (this.block19 != "" && this.block20 == "") {
                                    this.block20 = sbid[0];
                                    ++this.block20Count;
                                } else if (this.block20 != "" && this.block21 == "") {
                                    this.block21 = sbid[0];
                                    ++this.block21Count;
                                } else if (this.block21 != "" && this.block22 == "") {
                                    this.block22 = sbid[0];
                                    ++this.block22Count;
                                } else if (this.block22 != "" && this.block23 == "") {
                                    this.block23 = sbid[0];
                                    ++this.block23Count;
                                } else if (this.block23 != "" && this.block24 == "") {
                                    this.block24 = sbid[0];
                                    ++this.block24Count;
                                } else if (this.block24 != "" && this.block25 == "") {
                                    this.block25 = sbid[0];
                                    ++this.block25Count;
                                } else if (this.block25 != "" && this.block26 == "") {
                                    this.block26 = sbid[0];
                                    ++this.block26Count;
                                }
                            }

                            ++akeyNumber;
                            ++bblockNumber;
                            if (!letter.contentEquals("A")) {
                                ++this.blocksInBuilding;
                            }

                            this.rent = (float)this.blocksInBuilding * 0.01F;
                            this.corpTax = 3.0F;
                        } catch (Exception var18) {
                            ModSim.log.error("Caught exception: " + var18.getMessage());
                        }
                    }
                }

                if (this.block1 != "") {
                    this.addToRequirements(this.block1, this.block1Count, 0);
                }

                if (this.block2 != "") {
                    this.addToRequirements(this.block2, this.block2Count, 0);
                }

                if (this.block3 != "") {
                    this.addToRequirements(this.block3, this.block3Count, 0);
                }

                if (this.block4 != "") {
                    this.addToRequirements(this.block4, this.block4Count, 0);
                }

                if (this.block5 != "") {
                    this.addToRequirements(this.block5, this.block5Count, 0);
                }

                if (this.block6 != "") {
                    this.addToRequirements(this.block6, this.block6Count, 0);
                }

                if (this.block7 != "") {
                    this.addToRequirements(this.block7, this.block7Count, 0);
                }

                if (this.block8 != "") {
                    this.addToRequirements(this.block8, this.block8Count, 0);
                }

                if (this.block9 != "") {
                    this.addToRequirements(this.block9, this.block9Count, 0);
                }

                if (this.block10 != "") {
                    this.addToRequirements(this.block10, this.block10Count, 0);
                }

                if (this.block11 != "") {
                    this.addToRequirements(this.block11, this.block11Count, 0);
                }

                if (this.block12 != "") {
                    this.addToRequirements(this.block12, this.block12Count, 0);
                }

                if (this.block13 != "") {
                    this.addToRequirements(this.block13, this.block13Count, 0);
                }

                if (this.block14 != "") {
                    this.addToRequirements(this.block14, this.block14Count, 0);
                }

                if (this.block15 != "") {
                    this.addToRequirements(this.block15, this.block15Count, 0);
                }

                if (this.block16 != "") {
                    this.addToRequirements(this.block16, this.block16Count, 0);
                }

                if (this.block17 != "") {
                    this.addToRequirements(this.block17, this.block17Count, 0);
                }

                if (this.block18 != "") {
                    this.addToRequirements(this.block18, this.block18Count, 0);
                }

                if (this.block19 != "") {
                    this.addToRequirements(this.block19, this.block19Count, 0);
                }

                if (this.block20 != "") {
                    this.addToRequirements(this.block20, this.block20Count, 0);
                }

                if (this.block21 != "") {
                    this.addToRequirements(this.block21, this.block21Count, 0);
                }

                if (this.block22 != "") {
                    this.addToRequirements(this.block22, this.block22Count, 0);
                }

                if (this.block23 != "") {
                    this.addToRequirements(this.block23, this.block23Count, 0);
                }

                if (this.block24 != "") {
                    this.addToRequirements(this.block24, this.block24Count, 0);
                }

                if (this.block25 != "") {
                    this.addToRequirements(this.block25, this.block25Count, 0);
                }

                if (this.block26 != "") {
                    this.addToRequirements(this.block26, this.block26Count, 0);
                }

                in.close();
                br.close();
            }
        } catch (Exception var19) {
            ModSim.log.error("被抓住的例外: " + var19.getMessage());
        }

    }

    private void addToRequirements(String block, int amount, int meta) {
        //int val = false;
        ItemStack theBlock = new ItemStack(Block.getBlockFromName(block), amount, meta);
        String name;
        Iterator it;
        boolean got;
        Map.Entry pairs;
        ItemStack is;
        int val;
        if (ModSim.gameMode == GameMode.NORMAL) {
            name = "";

            try {
                name = theBlock.getDisplayName().toLowerCase();
            } catch (Exception var12) {
                name = "????";
            }

            if (name.contains("planks") || name.contentEquals("cobblestone") || name.contentEquals("glass") || name.contains("wool") || name.contentEquals("bricks") || name.contentEquals("dirt") || name.contentEquals("stone bricks") || name.contentEquals("fence") || name.contentEquals("stone") || name.contains("wood") && !name.contains("slab") && !name.contains("door") && !name.contains("stairs") && !name.contains("grass")) {
                it = this.requirements.entrySet().iterator();
                got = false;

                while(it.hasNext()) {
                    pairs = (Map.Entry)it.next();
                    is = (ItemStack)pairs.getKey();
                    if (is == theBlock) {
                        val = (Integer)pairs.getValue();
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
            if (ModSim.gameMode == GameMode.CREATIVE) {
                return;
            }

            if (ModSim.gameMode == GameMode.HARDCORE) {
                name = "";

                try {
                    name = theBlock.getDisplayName().toLowerCase();
                } catch (Exception var11) {
                    name = "????";
                }

                if (!name.contains("grass") && !name.contains("bed")) {
                    it = this.requirements.entrySet().iterator();
                    got = false;

                    while(it.hasNext()) {
                        pairs = (Map.Entry)it.next();
                        is = (ItemStack)pairs.getKey();
                        if (is == theBlock) {
                            val = (Integer)pairs.getValue();
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
}