package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ========================================
 *
 * @ClassName GuiControlBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:28
 * ========================================
 **/
public class GuiControlBox extends GuiScreen {
    private int mouseCount = 0;
    public V3 location;
    public Building theBuilding = null;
    public FolkData theFolk = null;
    public int employeeCount = 0;
    private HashMap employees = new HashMap();
    private EntityPlayer playerWhoClickedIt = null;

    public GuiControlBox(V3 location, EntityPlayer thePlayer) {
        this.location = location.clone();
        Building.loadAllBuildings();
        this.theBuilding = Building.getBuilding(location);
        this.playerWhoClickedIt = thePlayer;
    }

    public GuiControlBox(V3 location, FolkData folk) {
        this.location = location;
        Building.loadAllBuildings();
        this.theBuilding = Building.getBuilding(location);
        this.theFolk = folk;
        if (ModSimukraft.isDayTime()) {
            this.theFolk.gotoXYZ(location, (GotoMethod)null);
        }

    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
    }

    public void func_73866_w_() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 5, 5, 50, 20, "Done"));
        if (this.theBuilding == null) {
            this.field_146292_n.add(new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Fix House"));
        } else {
            if (this.theBuilding.blockLocations != null && this.theBuilding.blockLocations.size() > 0 && this.theBuilding.buildingComplete) {
                this.field_146292_n.add(new GuiButton(1000, this.field_146294_l - 110, 5, 100, 20, "Demolish!"));
                this.field_146292_n.add(new GuiButton(1001, this.field_146294_l - 110, 25, 100, 20, "Rotate Stairs"));
            }

            this.field_146292_n.add(new GuiButton(21, this.field_146294_l - 110, this.field_146295_m - 30, 100, 20, "Show Employees"));
            this.field_146292_n.add(new GuiButton(30, this.field_146294_l - 110, this.field_146295_m - 50, 100, 20, "Beam me to.."));
            int down;
            int fc;
            FolkData folk;
            if (this.theBuilding.type.contentEquals("commercial") || this.theBuilding.type.contentEquals("industrial")) {
                down = 70;
                int idx = 2;
                this.employeeCount = 0;
                this.employees.clear();

                for(fc = 0; fc < ModSimukraft.theFolks.size(); ++fc) {
                    folk = (FolkData)ModSimukraft.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 140, down - 6, 130, 20, "Fire " + folk.name));
                        this.employees.put(idx + 100, folk.name);
                        if (this.theBuilding.displayName.contains("Depot")) {
                            this.field_146292_n.add(new GuiButton(idx + 100, this.field_146294_l - 190, down - 6, 50, 20, "Tasks"));
                        }

                        down += 20;
                        ++this.employeeCount;
                        ++idx;
                    }
                }
            }

            GuiButton b;
            if (this.theBuilding.type.contentEquals("commercial")) {
                if (this.theBuilding.displayName.contains("Bakery")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Baker"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Grocery Store")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Grocer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Butchers")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Butcher"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Burgers")) {
                    ArrayList<FolkData> employees = FolkData.getFolksByEmployedAt(this.theBuilding.primaryXYZ);
                    Boolean flag = false;
                    GuiButton b1;
                    this.field_146292_n.add(b1 = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Manager"));
                    Iterator i$ = employees.iterator();

                    while(i$.hasNext()) {
                        FolkData folk = (FolkData)i$.next();
                        if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folk.vocation == Vocation.BURGERSMANAGER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b1.field_146124_l = false;
                    }

                    flag = false;
                    GuiButton b2;
                    this.field_146292_n.add(b2 = new GuiButton(2, 10, this.field_146295_m - 50, 100, 20, "Hire Fry Cook"));
                    Iterator i$ = employees.iterator();

                    while(i$.hasNext()) {
                        FolkData folk = (FolkData)i$.next();
                        if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folk.vocation == Vocation.BURGERSFRYCOOK) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b2.field_146124_l = false;
                    }

                    flag = false;
                    GuiButton b3;
                    this.field_146292_n.add(b3 = new GuiButton(3, 10, this.field_146295_m - 70, 100, 20, "Hire Waiter"));
                    Iterator i$ = employees.iterator();

                    while(i$.hasNext()) {
                        FolkData folk = (FolkData)i$.next();
                        if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folk.vocation == Vocation.BURGERSWAITER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b3.field_146124_l = false;
                    }
                }
            }

            if (this.theBuilding.type.contentEquals("industrial")) {
                if (this.theBuilding.displayName.contains("Lumbermill")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Lumberjack"));
                    if (this.employeeCount > 4) {
                        b.field_146124_l = false;
                    }

                    if (BlockMarker.markers.size() == 1) {
                        this.field_146292_n.add(new GuiButton(20, this.field_146294_l / 2 + 100, this.field_146295_m - 30, 100, 20, "Set Lumber area"));
                    }
                }

                if (this.theBuilding.displayName.contains("Builders Merchant")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Merchant"));
                    GuiButton b2;
                    this.field_146292_n.add(b2 = new GuiButton(25, 10, this.field_146295_m - 50, 100, 20, "Buy/Sell"));
                    if (!ModSimukraft.isDayTime() || this.employeeCount == 0) {
                        b2.field_146124_l = false;
                    }

                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Barracks")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Train Soldier"));
                    if (this.employeeCount > 9) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Sheep Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Shepherd"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Egg Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Egg Farmer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Cattle Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Cattle farmer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Pig Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Pig farmer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Chicken Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Chicken farmer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Depot")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Courier"));
                    if (this.employeeCount > 3) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Glass Factory")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Glass maker"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Fishing Dock")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Fisherman"));
                    if (this.employeeCount > 1) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Dairy Farm")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Dairy farmer"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Cheese Factory")) {
                    this.field_146292_n.add(b = new GuiButton(1, 10, this.field_146295_m - 30, 100, 20, "Hire Cheesemaker"));
                    if (this.employeeCount > 0) {
                        b.field_146124_l = false;
                    }
                }

                down = 70;
                int idx = 2;

                for(fc = 0; fc < ModSimukraft.theFolks.size(); ++fc) {
                    folk = (FolkData)ModSimukraft.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        if (this.theBuilding.displayName.contains("Barracks")) {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 210, down - 6, 200, 20, "Dismiss " + folk.name));
                        } else if (this.theBuilding.displayName.contains("Burgers")) {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 210, down - 6, 200, 20, "Fire " + folk.vocation.toString()));
                        } else {
                            this.field_146292_n.add(new GuiButton(idx, this.field_146294_l - 140, down - 6, 130, 20, "Fire " + folk.name));
                        }

                        down += 20;
                    }
                }
            }

        }
    }

    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "Building Control Panel", this.field_146294_l / 2, 17, 16777215);
            if (this.theBuilding == null) {
                this.field_146289_q.func_78276_b("ERROR: lost info on this building (" + this.location.toString() + ")", 5, 77, 16711680);
                this.field_146289_q.func_78276_b("only click 'fix house' below if this Building IS a house", 5, 97, 16711680);
            } else {
                String author = "";
                if (this.theBuilding.author != null && !this.theBuilding.author.contentEquals("")) {
                    author = " by " + this.theBuilding.author;
                }

                String isComplete = "Under construction";
                if (this.theBuilding.buildingComplete) {
                    isComplete = "Active Building";
                }

                this.field_146289_q.func_78276_b("Building : " + this.theBuilding.displayNameWithoutPK + author, 5, 37, 16777088);
                this.field_146289_q.func_78276_b("Type : " + this.theBuilding.type + " (" + isComplete + ")", 5, 47, 16777088);
                int down;
                if (!this.theBuilding.type.contentEquals("residential")) {
                    if (!this.theBuilding.type.contentEquals("industrial") && !this.theBuilding.type.contentEquals("commercial")) {
                        if (this.theBuilding.type.contentEquals("other")) {
                        }
                    } else {
                        this.field_146289_q.func_78276_b("Employees :", 5, 57, 16777088);
                        int down = 70;

                        for(down = 0; down < ModSimukraft.theFolks.size(); ++down) {
                            FolkData folk = (FolkData)ModSimukraft.theFolks.get(down);
                            if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                                this.field_146289_q.func_78276_b(folk.name + " (" + folk.age + ") - " + folk.vocation.toString(), 20, down, 16777120);
                                down += 20;
                            }
                        }
                    }
                } else {
                    String s = "";
                    if (this.theBuilding.tennants.size() > 1 || this.theBuilding.tennants.size() == 0) {
                        s = "s";
                    }

                    this.field_146289_q.func_78276_b(this.theBuilding.tennants.size() + " Resident" + s + " :", 5, 57, 16777088);
                    down = 70;

                    for(int t = 0; t < this.theBuilding.tennants.size(); ++t) {
                        String folkname = (String)this.theBuilding.tennants.get(t);
                        this.field_146289_q.func_78276_b(folkname, 20, down, 16777120);
                        down += 20;
                    }
                }
            }

            super.func_73863_a(i, j, f);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            } else {
                GuiEmployFolk ui;
                if (guibutton.field_146126_j.contentEquals("Hire Lumberjack")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.LUMBERJACK);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Baker")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BAKER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Train Soldier")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SOLDIER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Shepherd")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SHEPHERD);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Grocer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GROCER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Courier")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.COURIER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Merchant")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.MERCHANT);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Butcher")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BUTCHER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Egg Farmer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.EGGFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Pig farmer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.PIGFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Cattle farmer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CATTLEFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Chicken farmer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHICKENFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Glass maker")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GLASSMAKER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Fisherman")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.FISHERMAN);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Dairy farmer")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.DAIRYFARMER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Cheesemaker")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHEESEMAKER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Manager")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSMANAGER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Fry Cook")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSFRYCOOK);
                    this.field_146297_k.displayGuiScreen(ui);
                } else if (guibutton.field_146126_j.contentEquals("Hire Waiter")) {
                    this.field_146297_k.field_71462_r = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSWAITER);
                    this.field_146297_k.displayGuiScreen(ui);
                } else {
                    int bindex;
                    String folkname;
                    if (!guibutton.field_146126_j.contains("Fire ") && !guibutton.field_146126_j.contains("Dismiss ")) {
                        if (guibutton.field_146126_j.contentEquals("Fix House")) {
                            Building b;
                            ModSimukraft.theBuildings.add(b = new Building("Repaired House", "residential", this.location, this.location, true));
                            b.buildingComplete = true;
                            b.capacity = -1;
                            b.author = "Satscape";
                            this.theBuilding = b;
                            Building.saveAllBuildings();
                        } else if (guibutton.field_146126_j.contentEquals("Set Lumber area")) {
                            this.theBuilding.lumbermillMarker = ((Marker)BlockMarker.markers.get(0)).toV3();
                            guibutton.field_146124_l = false;
                        } else if (guibutton.field_146127_k == 21) {
                            GuiScreen gui = new GuiShowEmployees();
                            this.field_146297_k.displayGuiScreen((GuiScreen)null);
                            this.field_146297_k.displayGuiScreen(gui);
                        } else if (guibutton.field_146126_j.contentEquals("Tasks")) {
                            folkname = (String)this.employees.get(guibutton.field_146127_k);
                            GuiScreen gui = new GuiCourierTasks(this.location, folkname, this.playerWhoClickedIt);
                            this.field_146297_k.displayGuiScreen((GuiScreen)null);
                            this.field_146297_k.displayGuiScreen(gui);
                        } else if (guibutton.field_146126_j.contentEquals("Buy/Sell")) {
                            GuiScreen ui = new GuiMerchant();
                            this.field_146297_k.displayGuiScreen((GuiScreen)null);
                            this.field_146297_k.displayGuiScreen(ui);
                        } else if (guibutton.field_146127_k == 30) {
                            GuiScreen ui = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                            this.field_146297_k.displayGuiScreen((GuiScreen)null);
                            this.field_146297_k.displayGuiScreen(ui);
                        } else if (guibutton.field_146126_j.contentEquals("Rotate Stairs")) {
                            this.rotateStairs();
                        } else if (guibutton.field_146126_j.contentEquals("Demolish!")) {
                            World theWorld = this.playerWhoClickedIt.field_70170_p;
                            bindex = 0;

                            for(int i = 0; i < ModSimukraft.theBuildings.size(); ++i) {
                                Building build = (Building)ModSimukraft.theBuildings.get(i);

                                try {
                                    if (build.primaryXYZ.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true)) {
                                        bindex = i;
                                        FolkData theFolk = FolkData.getFolkByEmployedAt(this.theBuilding.primaryXYZ);
                                        if (theFolk != null) {
                                            theFolk.selfFire();
                                        }
                                    }
                                } catch (Exception var7) {
                                }
                            }

                            ModSimukraft.demolishWorld = theWorld;
                            Iterator i$ = this.theBuilding.blockLocations.iterator();

                            while(i$.hasNext()) {
                                V3 blockLoc = (V3)i$.next();
                                Block l = theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue());
                                if (l != null && ModSimukraft.demolishBlocks.size() < 500) {
                                    blockLoc.blockID = l;
                                    ModSimukraft.demolishBlocks.add(blockLoc);
                                }

                                theWorld.setBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), Blocks.field_150350_a, 0, 3);
                                this.field_146297_k.field_71441_e.func_72869_a("explode", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.30000001192092896D, 0.0D);
                                this.field_146297_k.field_71441_e.func_72869_a("flame", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.4000000059604645D, 0.0D);
                            }

                            theWorld.func_72956_a(this.playerWhoClickedIt, "random.explode", 1.0F, 1.0F);
                            ModSimukraft.theBuildings.remove(bindex);
                            this.field_146297_k.displayGuiScreen((GuiScreen)null);
                        }
                    } else {
                        folkname = guibutton.field_146126_j.substring(guibutton.field_146126_j.indexOf(" ")).trim();
                        guibutton.field_146124_l = false;
                        if (this.theBuilding.displayName.contains("Depot")) {
                            for(bindex = 0; bindex < this.field_146292_n.size(); ++bindex) {
                                GuiButton but = (GuiButton)this.field_146292_n.get(bindex);
                                if (but.field_146129_i == guibutton.field_146129_i) {
                                    but.field_146124_l = false;
                                }
                            }
                        }

                        FolkData folk = FolkData.getFolkByName(folkname);
                        if (folk != null) {
                            folk.selfFire();
                        }
                    }
                }

            }
        }
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.func_71381_h();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
    }

    private void rotateStairs() {
        World theWorld = this.field_146297_k.func_71401_C().worldServerForDimension(this.theBuilding.primaryXYZ.theDimension);
        theWorld.func_72908_a(this.theBuilding.primaryXYZ.x, this.theBuilding.primaryXYZ.y, this.theBuilding.primaryXYZ.z, "satscapesimukraft:computer", 1.0F, 2.0F);
        Iterator i$ = this.theBuilding.blockLocations.iterator();

        while(true) {
            while(true) {
                while(true) {
                    while(i$.hasNext()) {
                        V3 blockLoc = (V3)i$.next();
                        ItemStack is = new ItemStack(theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()), 1, theWorld.func_72805_g(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()));
                        int newmeta;
                        if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150400_ck && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150487_bG && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150401_cl && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150485_bF && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150446_ar) {
                            if (Block.func_149634_a(is.func_77973_b()) != Blocks.torch && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150429_aA && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150429_aA) {
                                if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_150324_C) {
                                    newmeta = is.func_77960_j();
                                    ++newmeta;
                                    if (newmeta == 4) {
                                        newmeta = 0;
                                    }

                                    theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                } else if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150331_J && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150326_M && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150332_K) {
                                    if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_150444_as) {
                                        newmeta = is.func_77960_j();
                                        if (newmeta == 0) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 8;
                                        } else if (newmeta == 8) {
                                            newmeta = 12;
                                        } else if (newmeta == 12) {
                                            newmeta = 0;
                                        }

                                        theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                    } else if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150444_as && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150468_ap) {
                                        if (Block.func_149634_a(is.func_77973_b()) != Blocks.field_150430_aB && Block.func_149634_a(is.func_77973_b()) != Blocks.field_150471_bO) {
                                            if (Block.func_149634_a(is.func_77973_b()) == Blocks.field_150396_be) {
                                                newmeta = is.func_77960_j();
                                                ++newmeta;
                                                if (newmeta > 3) {
                                                    newmeta = 0;
                                                }

                                                theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                            }
                                        } else {
                                            newmeta = is.func_77960_j();
                                            if (newmeta == 1) {
                                                newmeta = 3;
                                            } else if (newmeta == 3) {
                                                newmeta = 2;
                                            } else if (newmeta == 2) {
                                                newmeta = 4;
                                            } else if (newmeta == 4) {
                                                newmeta = 1;
                                            }

                                            theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                        }
                                    } else {
                                        newmeta = is.func_77960_j();
                                        if (newmeta == 2) {
                                            newmeta = 5;
                                        } else if (newmeta == 5) {
                                            newmeta = 3;
                                        } else if (newmeta == 3) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 2;
                                        }

                                        theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                    }
                                } else {
                                    newmeta = is.func_77960_j();
                                    if (newmeta == 2) {
                                        newmeta = 5;
                                    } else if (newmeta == 5) {
                                        newmeta = 3;
                                    } else if (newmeta == 3) {
                                        newmeta = 4;
                                    } else if (newmeta == 4) {
                                        newmeta = 2;
                                    }

                                    theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                }
                            } else {
                                newmeta = is.func_77960_j();
                                if (newmeta == 1) {
                                    newmeta = 3;
                                } else if (newmeta == 3) {
                                    newmeta = 2;
                                } else if (newmeta == 2) {
                                    newmeta = 4;
                                } else if (newmeta == 4) {
                                    newmeta = 1;
                                }

                                theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                            }
                        } else {
                            newmeta = is.func_77960_j();
                            if (newmeta == 0) {
                                newmeta = 2;
                            } else if (newmeta == 1) {
                                newmeta = 3;
                            } else if (newmeta == 2) {
                                newmeta = 1;
                            } else if (newmeta == 3) {
                                newmeta = 0;
                            }

                            theWorld.func_72921_c(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                        }
                    }

                    return;
                }
            }
        }
    }
}
