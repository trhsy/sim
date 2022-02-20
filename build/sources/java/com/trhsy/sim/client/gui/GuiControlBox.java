package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.entity.Marker;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.jobs.Vocation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
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
        if (ModSim.isDayTime()) {
            this.theFolk.gotoXYZ(location, (GotoMethod) null);
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, I18n.format("container.sim.sim_gui_BC_Done")));
        if (this.theBuilding == null) {
            this.buttonList.add(new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Fix_House")));
        } else {
            if (this.theBuilding.blockLocations != null && this.theBuilding.blockLocations.size() > 0 && this.theBuilding.buildingComplete) {
                this.buttonList.add(new GuiButton(1000, this.width - 110, 5, 100, 20, I18n.format("container.sim.Demolish")));
                this.buttonList.add(new GuiButton(1001, this.width - 110, 25, 100, 20, I18n.format("container.sim.Rotate_Stairs")));
            }

            this.buttonList.add(new GuiButton(21, this.width - 110, this.height - 30, 100, 20, I18n.format("container.sim.Show_Employees")));
            this.buttonList.add(new GuiButton(30, this.width - 110, this.height - 50, 100, 20, I18n.format("container.sim.Beam_me_to")));
            int down;
            int fc;
            FolkData folk;
            if (this.theBuilding.type.contentEquals("commercial") || this.theBuilding.type.contentEquals("industrial")) {
                down = 70;
                int idx = 2;
                this.employeeCount = 0;
                this.employees.clear();

                for (fc = 0; fc < ModSim.theFolks.size(); ++fc) {
                    folk = (FolkData) ModSim.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        this.buttonList.add(new GuiButton(idx, this.width - 140, down - 6, 130, 20, I18n.format("container.sim.Fire") + folk.name));
                        this.employees.put(idx + 100, folk.name);
                        if (this.theBuilding.displayName.contains("Depot")) {
                            this.buttonList.add(new GuiButton(idx + 100, this.width - 190, down - 6, 50, 20, I18n.format("container.sim.Tasks")));
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
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Baker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Grocery Store")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Grocer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Butchers")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Butcher")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Burgers")) {
                    ArrayList<FolkData> employees = FolkData.getFolksByEmployedAt(this.theBuilding.primaryXYZ);
                    Boolean flag = false;
                    GuiButton b1;
                    this.buttonList.add(b1 = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Manager")));
                    Iterator iterator = employees.iterator();

                    while(iterator.hasNext()) {
                        FolkData folkData = (FolkData)iterator.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSMANAGER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b1.enabled = false;
                    }

                    flag = false;
                    GuiButton b2;
                    this.buttonList.add(b2 = new GuiButton(2, 10, this.height - 50, 100, 20, I18n.format("container.sim.Hire_Fry_Cook")));
                    Iterator iterator1 = employees.iterator();

                    while(iterator1.hasNext()) {
                        FolkData folkData = (FolkData)iterator1.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSFRYCOOK) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b2.enabled = false;
                    }

                    flag = false;
                    GuiButton b3;
                    this.buttonList.add(b3 = new GuiButton(3, 10, this.height - 70, 100, 20, I18n.format("container.sim.Hire_Waiter")));
                    Iterator iterator2 = employees.iterator();

                    while(iterator2.hasNext()) {
                        FolkData folkData = (FolkData)iterator2.next();
                        if (folkData.employedAt != null && folkData.employedAt.isSameCoordsAs(this.theBuilding.primaryXYZ, true, true) && folkData.vocation == Vocation.BURGERSWAITER) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        b3.enabled = false;
                    }
                }
            }

            if (this.theBuilding.type.contentEquals("industrial")) {
                if (this.theBuilding.displayName.contains("Lumbermill")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Lumberjack")));
                    if (this.employeeCount > 4) {
                        b.enabled = false;
                    }

                    if (BlockMarker.markers.size() == 1) {
                        this.buttonList.add(new GuiButton(20, this.width / 2 + 100, this.height - 30, 100, 20, I18n.format("container.sim.Set_Lumber_area")));
                    }
                }

                if (this.theBuilding.displayName.contains("Builders Merchant")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Merchant")));
                    GuiButton b2;
                    this.buttonList.add(b2 = new GuiButton(25, 10, this.height - 50, 100, 20, I18n.format("container.sim.Buy_Sell")));
                    if (!ModSim.isDayTime() || this.employeeCount == 0) {
                        b2.enabled = false;
                    }

                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Barracks")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Train_Soldier")));
                    if (this.employeeCount > 9) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Sheep Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Shepherd")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Egg Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Egg_Farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Cattle Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Cattle_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Pig Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Pig_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Chicken Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Chicken_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Depot")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Courier")));
                    if (this.employeeCount > 3) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Glass Factory")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Glass_maker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Fishing Dock")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Fisherman")));
                    if (this.employeeCount > 1) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Dairy Farm")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Dairy_farmer")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                if (this.theBuilding.displayName.contains("Cheese Factory")) {
                    this.buttonList.add(b = new GuiButton(1, 10, this.height - 30, 100, 20, I18n.format("container.sim.Hire_Cheesemaker")));
                    if (this.employeeCount > 0) {
                        b.enabled = false;
                    }
                }

                down = 70;
                int idx = 2;

                for (fc = 0; fc < ModSim.theFolks.size(); ++fc) {
                    folk = (FolkData) ModSim.theFolks.get(fc);
                    if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                        if (this.theBuilding.displayName.contains("Barracks")) {
                            this.buttonList.add(new GuiButton(idx, this.width - 210, down - 6, 200, 20, I18n.format("container.sim.Dismiss") + folk.name));
                        } else if (this.theBuilding.displayName.contains("Burgers")) {
                            this.buttonList.add(new GuiButton(idx, this.width - 210, down - 6, 200, 20, I18n.format("container.sim.Fire") + folk.vocation.toString()));
                        } else {
                            this.buttonList.add(new GuiButton(idx, this.width - 140, down - 6, 130, 20, I18n.format("container.sim.Fire") + folk.name));
                        }

                        down += 20;
                    }
                }
            }

        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Building_Control_Panel"), this.width / 2, 17, 16777215);
            if (this.theBuilding == null) {
                this.fontRendererObj.drawString(I18n.format("container.sim.on_this_building")+"(" + this.location.toString() + ")", 5, 77, 16711680);
                this.fontRendererObj.drawString(I18n.format("container.sim.this_Building"), 5, 97, 16711680);
            } else {
                String author = "";
                if (this.theBuilding.author != null && !this.theBuilding.author.contentEquals("")) {
                    author = " by " + this.theBuilding.author;
                }

                String isComplete =I18n.format("container.sim.Under_construction");
                if (this.theBuilding.buildingComplete) {
                    isComplete = I18n.format("container.sim.Active_Building");
                }

                this.fontRendererObj.drawString(I18n.format("container.sim.Building")+" : " + this.theBuilding.displayNameWithoutPK + author, 5, 37, 16777088);
                this.fontRendererObj.drawString(I18n.format("container.sim.Type")+" : " + this.theBuilding.type + " (" + isComplete + ")", 5, 47, 16777088);
                int down;
                if (!this.theBuilding.type.contentEquals("residential")) {
                    if (!this.theBuilding.type.contentEquals("industrial") && !this.theBuilding.type.contentEquals("commercial")) {
                        if (this.theBuilding.type.contentEquals("other")) {
                        }
                    } else {
                        this.fontRendererObj.drawString(I18n.format("container.sim.Employees")+" :", 5, 57, 16777088);
                        down = 70;

                        for (down = 0; down < ModSim.theFolks.size(); ++down) {
                            FolkData folk = (FolkData) ModSim.theFolks.get(down);
                            if (this.theBuilding.primaryXYZ.isSameCoordsAs(folk.employedAt, true, true)) {
                                this.fontRendererObj.drawString(folk.name + " (" + folk.age + ") - " + folk.vocation.toString(), 20, down, 16777120);
                                down += 20;
                            }
                        }
                    }
                } else {
                    String s = "";
                    if (this.theBuilding.tennants.size() > 1 || this.theBuilding.tennants.size() == 0) {
                        s = "s";
                    }

                    this.fontRendererObj.drawString(this.theBuilding.tennants.size() + I18n.format("container.sim.Resident") + s + " :", 5, 57, 16777088);
                    down = 70;

                    for(int t = 0; t < this.theBuilding.tennants.size(); ++t) {
                        String folkname = (String)this.theBuilding.tennants.get(t);
                        this.fontRendererObj.drawString(folkname, 20, down, 16777120);
                        down += 20;
                    }
                }
            }

            super.drawScreen(i, j, f);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            } else {
                GuiEmployFolk ui;
                if (guibutton.displayString.contentEquals("Hire Lumberjack")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.LUMBERJACK);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Baker")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BAKER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Train Soldier")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SOLDIER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Shepherd")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.SHEPHERD);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Grocer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GROCER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Courier")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.COURIER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Merchant")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.MERCHANT);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Butcher")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BUTCHER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Egg Farmer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.EGGFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Pig farmer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.PIGFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Cattle farmer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CATTLEFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Chicken farmer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHICKENFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Glass maker")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.GLASSMAKER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Fisherman")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.FISHERMAN);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Dairy farmer")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.DAIRYFARMER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Cheesemaker")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.CHEESEMAKER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Manager")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSMANAGER);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Fry Cook")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSFRYCOOK);
                    this.mc.displayGuiScreen(ui);
                } else if (guibutton.displayString.contentEquals("Hire Waiter")) {
                    this.mc.currentScreen = null;
                    ui = new GuiEmployFolk(this.location, "", Vocation.BURGERSWAITER);
                    this.mc.displayGuiScreen(ui);
                } else {
                    int bindex;
                    String folkname;
                    if (!guibutton.displayString.contains("Fire ") && !guibutton.displayString.contains("Dismiss ")) {
                        if (guibutton.displayString.contentEquals("Fix House")) {
                            Building b;
                            ModSim.theBuildings.add(b = new Building("Repaired House", "residential", this.location, this.location, true));
                            b.buildingComplete = true;
                            b.capacity = -1;
                            b.author = "Satscape";
                            this.theBuilding = b;
                            Building.saveAllBuildings();
                        } else if (guibutton.displayString.contentEquals("Set Lumber area")) {
                            this.theBuilding.lumbermillMarker = ((Marker)BlockMarker.markers.get(0)).toV3();
                            guibutton.enabled = false;
                        } else if (guibutton.id == 21) {
                            GuiScreen gui = new GuiShowEmployees();
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(gui);
                        } else if (guibutton.displayString.contentEquals("Tasks")) {
                            folkname = (String)this.employees.get(guibutton.id);
                            GuiScreen gui = new GuiCourierTasks(this.location, folkname, this.playerWhoClickedIt);
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(gui);
                        } else if (guibutton.displayString.contentEquals("Buy/Sell")) {
                            GuiScreen uiGuiScreen = new GuiMerchant();
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(uiGuiScreen);
                        } else if (guibutton.id == 30) {
                            GuiScreen guiScreen = new GuiBeamPlayerTo(this.playerWhoClickedIt);
                            this.mc.displayGuiScreen((GuiScreen)null);
                            this.mc.displayGuiScreen(guiScreen);
                        } else if (guibutton.displayString.contentEquals("Rotate Stairs")) {
                            this.rotateStairs();
                        } else if (guibutton.displayString.contentEquals("Demolish!")) {
                            World theWorld = this.playerWhoClickedIt.worldObj;
                            bindex = 0;

                            for (int i = 0; i < ModSim.theBuildings.size(); ++i) {
                                Building build = (Building) ModSim.theBuildings.get(i);

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

                            ModSim.demolishWorld = theWorld;
                            Iterator i$ = this.theBuilding.blockLocations.iterator();

                            while(i$.hasNext()) {
                                V3 blockLoc = (V3)i$.next();
                                Block l = theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue());
                                if (l != null && ModSim.demolishBlocks.size() < 500) {
                                    blockLoc.blockID = l;
                                    ModSim.demolishBlocks.add(blockLoc);
                                }

                                theWorld.setBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), Blocks.air, 0, 3);
                                this.mc.theWorld.spawnParticle("explode", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.30000001192092896D, 0.0D);
                                this.mc.theWorld.spawnParticle("flame", (double)blockLoc.x.intValue(), (double)blockLoc.y.intValue(), (double)blockLoc.z.intValue(), 0.0D, 0.4000000059604645D, 0.0D);
                            }

                            theWorld.playSoundAtEntity(this.playerWhoClickedIt, "random.explode", 1.0F, 1.0F);
                            ModSim.theBuildings.remove(bindex);
                            this.mc.displayGuiScreen((GuiScreen) null);
                        }
                    } else {
                        folkname = guibutton.displayString.substring(guibutton.displayString.indexOf(" ")).trim();
                        guibutton.enabled = false;
                        if (this.theBuilding.displayName.contains("Depot")) {
                            for(bindex = 0; bindex < this.buttonList.size(); ++bindex) {
                                GuiButton but = (GuiButton)this.buttonList.get(bindex);
                                if (but.yPosition == guibutton.yPosition) {
                                    but.enabled = false;
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

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.setIngameFocus();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    private void rotateStairs() {
        World theWorld = this.mc.getIntegratedServer().worldServerForDimension(this.theBuilding.primaryXYZ.theDimension);
        theWorld.playSoundEffect(this.theBuilding.primaryXYZ.x, this.theBuilding.primaryXYZ.y, this.theBuilding.primaryXYZ.z, ModSim.MODID + ":computer", 1.0F, 2.0F);
        Iterator i$ = this.theBuilding.blockLocations.iterator();

        while(true) {
            while(true) {
                while(true) {
                    while(i$.hasNext()) {
                        V3 blockLoc = (V3)i$.next();
                        ItemStack is = new ItemStack(theWorld.getBlock(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()), 1, theWorld.getBlockMetadata(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()));
                        int newmeta;
                        if (Block.getBlockFromItem(is.getItem()) != Blocks.acacia_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.birch_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.dark_oak_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.spruce_stairs && Block.getBlockFromItem(is.getItem()) != Blocks.stone_stairs) {
                            if (Block.getBlockFromItem(is.getItem()) != Blocks.torch && Block.getBlockFromItem(is.getItem()) != Blocks.redstone_torch && Block.getBlockFromItem(is.getItem()) != Blocks.redstone_torch) {
                                if (Block.getBlockFromItem(is.getItem()) == Blocks.bed) {
                                    newmeta = is.getMetadata();
                                    ++newmeta;
                                    if (newmeta == 4) {
                                        newmeta = 0;
                                    }

                                    theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                } else if (Block.getBlockFromItem(is.getItem()) != Blocks.piston && Block.getBlockFromItem(is.getItem()) != Blocks.piston_extension && Block.getBlockFromItem(is.getItem()) != Blocks.piston_head) {
                                    if (Block.getBlockFromItem(is.getItem()) == Blocks.wall_sign) {
                                        newmeta = is.getMetadata();
                                        if (newmeta == 0) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 8;
                                        } else if (newmeta == 8) {
                                            newmeta = 12;
                                        } else if (newmeta == 12) {
                                            newmeta = 0;
                                        }

                                        theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 2);
                                    } else if (Block.getBlockFromItem(is.getItem()) != Blocks.wall_sign && Block.getBlockFromItem(is.getItem()) != Blocks.ladder) {
                                        if (Block.getBlockFromItem(is.getItem()) != Blocks.stone_button && Block.getBlockFromItem(is.getItem()) != Blocks.wooden_button) {
                                            if (Block.getBlockFromItem(is.getItem()) == Blocks.fence_gate) {
                                                newmeta = is.getMetadata();
                                                ++newmeta;
                                                if (newmeta > 3) {
                                                    newmeta = 0;
                                                }

                                                theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                            }
                                        } else {
                                            newmeta = is.getMetadata();
                                            if (newmeta == 1) {
                                                newmeta = 3;
                                            } else if (newmeta == 3) {
                                                newmeta = 2;
                                            } else if (newmeta == 2) {
                                                newmeta = 4;
                                            } else if (newmeta == 4) {
                                                newmeta = 1;
                                            }

                                            theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                        }
                                    } else {
                                        newmeta = is.getMetadata();
                                        if (newmeta == 2) {
                                            newmeta = 5;
                                        } else if (newmeta == 5) {
                                            newmeta = 3;
                                        } else if (newmeta == 3) {
                                            newmeta = 4;
                                        } else if (newmeta == 4) {
                                            newmeta = 2;
                                        }

                                        theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                    }
                                } else {
                                    newmeta = is.getMetadata();
                                    if (newmeta == 2) {
                                        newmeta = 5;
                                    } else if (newmeta == 5) {
                                        newmeta = 3;
                                    } else if (newmeta == 3) {
                                        newmeta = 4;
                                    } else if (newmeta == 4) {
                                        newmeta = 2;
                                    }

                                    theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                                }
                            } else {
                                newmeta = is.getMetadata();
                                if (newmeta == 1) {
                                    newmeta = 3;
                                } else if (newmeta == 3) {
                                    newmeta = 2;
                                } else if (newmeta == 2) {
                                    newmeta = 4;
                                } else if (newmeta == 4) {
                                    newmeta = 1;
                                }

                                theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                            }
                        } else {
                            newmeta = is.getMetadata();
                            if (newmeta == 0) {
                                newmeta = 2;
                            } else if (newmeta == 1) {
                                newmeta = 3;
                            } else if (newmeta == 2) {
                                newmeta = 1;
                            } else if (newmeta == 3) {
                                newmeta = 0;
                            }

                            theWorld.setBlockMetadataWithNotify(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue(), newmeta, 3);
                        }
                    }

                    return;
                }
            }
        }
    }
}
