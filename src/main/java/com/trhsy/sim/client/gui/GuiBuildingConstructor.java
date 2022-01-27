package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.jobs.JobBuilder;
import com.trhsy.sim.common.jobs.Stage;
import com.trhsy.sim.common.jobs.Vocation;
import com.trhsy.sim.packets.server.LoadBuildingMessage;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ========================================
 *
 * @ClassName GuiBuildingConstructor
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:27
 * ========================================
 **/
public class GuiBuildingConstructor extends GuiScreen {
    private int mouseCount = 0;
    private int currentPage = 0;
    private ArrayList<FolkData> theWorkers = new ArrayList();
    V3 constructorLoc;
    String buildDirection = "";
    private int buildingOffset = 0;
    private int buildingsOnPage = 0;
    private int fixedBuildingCount = -1;
    private HashMap pkIndex = new HashMap();
    private GuiTextField tfSearch;
    private String search = "";
    private Building selectedBuilding = null;
    private int previousPage = 1;
    long fuckingBodge = 0L;

    public GuiBuildingConstructor(V3 location, String buildDirection, ArrayList<FolkData> theFolks) {
        this.constructorLoc = location;
        this.buildDirection = buildDirection;
        if (theFolks != null) {
            this.theWorkers = theFolks;
        } else {
            this.theWorkers.clear();

            for(int f = 0; f < ModSimukraft.theFolks.size(); ++f) {
                FolkData folk = (FolkData)ModSimukraft.theFolks.get(f);
                if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.constructorLoc, true, true)) {
                    if (folk.vocation == Vocation.BUILDER) {
                        JobBuilder theirJob = (JobBuilder)folk.theirJob;
                        if (theirJob.theStage == Stage.IDLE) {
                            theirJob.theStage = Stage.WORKERASSIGNED;
                        }
                    }

                    this.theWorkers.add(folk);
                }
            }
        }

    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_73876_c() {
        this.field_146297_k.func_71364_i();
        if (this.tfSearch != null) {
            this.tfSearch.func_146178_a();
        }

        super.func_73876_c();
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.showPage();
        super.func_73866_w_();
    }

    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, "Building Constructor", this.field_146294_l / 2, 17, 16777215);
            String s = "Idle";
            String t = "Not chosen yet";

            try {
                if (this.theWorkers.size() > 0) {
                    JobBuilder theirJob = (JobBuilder)((FolkData)this.theWorkers.get(0)).theirJob;
                    s = theirJob.theStage.toString();
                    if (((FolkData)this.theWorkers.get(0)).theBuilding != null) {
                        t = ((FolkData)this.theWorkers.get(0)).theBuilding.displayName;
                    }
                }
            } catch (Exception var11) {
                s = "on their way";
                t = "";
            }

            this.func_73732_a(this.field_146289_q, "Current status: " + s, this.field_146294_l / 2, 30, 11206655);
            this.func_73732_a(this.field_146289_q, "Building type: " + t, this.field_146294_l / 2, 40, 11206655);
            if (this.currentPage == 0) {
                this.func_73732_a(this.field_146289_q, "Please choose a task for this building constructor", this.field_146294_l / 2, 100, 16777130);
            } else if (this.currentPage == 1) {
                this.func_73732_a(this.field_146289_q, "Please choose a type of building", this.field_146294_l / 2, 100, 16777130);
            } else if (this.currentPage == 2) {
                this.func_73732_a(this.field_146289_q, "Now choose the residential building to build", this.field_146294_l / 2, 50, 16777130);
                this.tfSearch.func_146194_f();
            } else if (this.currentPage == 3) {
                this.func_73732_a(this.field_146289_q, "Choose an unemployed Sim-U-Folk you want to hire", this.field_146294_l / 2, 50, 16777130);
            } else if (this.currentPage == 4) {
                this.func_73732_a(this.field_146289_q, "Here are all your employees", this.field_146294_l / 2, 50, 16777130);
            } else if (this.currentPage == 5) {
                this.func_73732_a(this.field_146289_q, "Now choose the commercial building to build", this.field_146294_l / 2, 50, 16777130);
                this.tfSearch.func_146194_f();
            } else if (this.currentPage == 6) {
                this.func_73732_a(this.field_146289_q, "Now choose the industrial building to build", this.field_146294_l / 2, 50, 16777130);
                this.tfSearch.func_146194_f();
            } else if (this.currentPage == 7) {
                this.func_73732_a(this.field_146289_q, "Now choose the other type of building to build", this.field_146294_l / 2, 50, 16777130);
                this.tfSearch.func_146194_f();
            } else if (this.currentPage == 8) {
                this.func_73732_a(this.field_146289_q, "Building requirements for " + this.selectedBuilding.displayNameWithoutPK, this.field_146294_l / 2, 50, 16777130);
                int y = 70;
                Iterator it = this.selectedBuilding.requirements.entrySet().iterator();

                while(it.hasNext()) {
                    Map.Entry pairs = (Map.Entry)it.next();
                    ItemStack is = (ItemStack)pairs.getKey();
                    if (is != null) {
                        if (y + 20 > this.field_146295_m - 20) {
                            this.func_73731_b(this.field_146289_q, "...and several more block types", 90, y, 16777215);
                        } else {
                            String itemName = is.func_82833_r();
                            if (itemName.toLowerCase().contentEquals("oak wood")) {
                                itemName = "Logs";
                            }

                            if (itemName.toLowerCase().contains("oak wood planks")) {
                                itemName = "Planks";
                            }

                            this.displayReq(itemName, (Integer)pairs.getValue(), y);
                            y += 15;
                        }
                    }
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        super.func_73863_a(i, j, f);
    }

    private void displayReq(String block, int qty, int y) {
        double stacks = Math.floor((double)(qty / 64));
        this.func_73731_b(this.field_146289_q, qty + "", 90, y, 16777215);
        this.func_73731_b(this.field_146289_q, "x", 125, y, 16777215);
        this.func_73731_b(this.field_146289_q, block, 150, y, 16777215);
        String st = "";
        if (qty < 64) {
            st = "(less than one stack)";
        } else if (qty == 64) {
            st = "(exactly 1 stack)";
        } else if (qty >= 64 && qty < 128) {
            st = "(about 2 stacks)";
        } else {
            st = "(about " + (int)(stacks + 1.0D) + " stacks)";
        }

        this.func_73731_b(this.field_146289_q, st, 250, y, 16777215);
    }

    private void showPage() {
        this.field_146297_k.func_71364_i();
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 2, 12, 50, 20, "Done"));
        if (this.selectedBuilding == null) {
            this.selectedBuilding = Building.getBuildingByConBox(this.constructorLoc);
        }

        if (this.currentPage == 0) {
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 60, 150, 120, 20, "Choose building"));
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 180, 150, 120, 20, "Hire builder"));
            String w = "worker";
            if (this.theWorkers.size() == 1) {
                w = ((FolkData)this.theWorkers.get(0)).name;
            } else if (this.theWorkers.size() > 1) {
                w = "Staff (" + this.theWorkers.size() + ")";
            }

            this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 60, 150, 120, 20, "Fire " + w));
            this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 60, 170, 120, 20, "Show Employees"));
            this.field_146292_n.add(new GuiButton(5, -600, 170, 120, 20, "-"));
            this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 - 60, 170, 120, 20, "Terraform area"));
            this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2 - 180, 170, 120, 20, "Hire terraformer"));
            if (this.theWorkers.size() == 0) {
                ((GuiButton)this.field_146292_n.get(1)).field_146124_l = false;
                ((GuiButton)this.field_146292_n.get(2)).field_146124_l = true;
                ((GuiButton)this.field_146292_n.get(3)).field_146124_l = false;
                ((GuiButton)this.field_146292_n.get(6)).field_146124_l = false;
                ((GuiButton)this.field_146292_n.get(7)).field_146124_l = true;
            } else {
                ((GuiButton)this.field_146292_n.get(1)).field_146124_l = true;
                ((GuiButton)this.field_146292_n.get(2)).field_146124_l = false;
                ((GuiButton)this.field_146292_n.get(3)).field_146124_l = true;
                ((GuiButton)this.field_146292_n.get(6)).field_146124_l = true;
                ((GuiButton)this.field_146292_n.get(7)).field_146124_l = false;
            }
        } else if (this.currentPage == 1) {
            this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 200, 150, 100, 20, "Residential"));
            this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 - 100, 150, 100, 20, "Commercial"));
            this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2, 150, 100, 20, "Industrial"));
            this.field_146292_n.add(new GuiButton(8, this.field_146294_l / 2 + 100, 150, 100, 20, "Other"));
        } else if (this.currentPage != 3) {
            int x;
            int y;
            if (this.currentPage == 4) {
                try {
                    int x = 10;
                    int y = 65;
                    x = 1;

                    for(y = 0; y < ModSimukraft.theFolks.size(); ++y) {
                        FolkData folk = (FolkData)ModSimukraft.theFolks.get(y);
                        this.field_146292_n.add(new GuiButton(x, x, y, 100, 20, "Fire " + folk.name));
                        ++x;
                        x += 100;
                        if (x + 100 > this.field_146294_l) {
                            x = 10;
                            y += 20;
                        }

                        if (y + 20 > this.field_146295_m - 50) {
                            break;
                        }
                    }
                } catch (Exception var18) {
                    var18.printStackTrace();
                }
            } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                if (this.currentPage == 8) {
                    this.field_146292_n.add(new GuiButton(1001, this.field_146294_l / 2 - 100, this.field_146295_m - 25, 100, 20, "Go Back"));
                    this.field_146292_n.add(new GuiButton(1000, this.field_146294_l / 2, this.field_146295_m - 25, 100, 20, "Build it!"));
                }
            } else {
                ArrayList<Building> houses = new ArrayList();
                String theType = "";
                this.buildingsOnPage = 0;
                this.tfSearch = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - 50, this.field_146295_m - 30, 100, 20);
                this.tfSearch.func_146180_a(this.search);
                this.tfSearch.func_146195_b(true);
                this.tfSearch.func_146203_f(10);
                if (this.currentPage == 2) {
                    houses = Building.getBuildingBlueprints("residential", this.tfSearch.func_146179_b().trim());
                    theType = "residential";
                } else if (this.currentPage == 5) {
                    houses = Building.getBuildingBlueprints("commercial", this.tfSearch.func_146179_b().trim());
                    theType = "commercial";
                } else if (this.currentPage == 6) {
                    houses = Building.getBuildingBlueprints("industrial", this.tfSearch.func_146179_b().trim());
                    theType = "industrial";
                } else if (this.currentPage == 7) {
                    houses = Building.getBuildingBlueprints("other", this.tfSearch.func_146179_b().trim());
                    theType = "other";
                }

                x = 10;
                y = 60;
                int idx = 1;
                if (houses != null) {
                    for(int b = 0; b <= houses.size(); ++b) {
                        int boff = b + this.buildingOffset;
                        if (boff < 0) {
                            boff = 0;
                            this.buildingOffset = 0;
                        }

                        if (boff < houses.size()) {
                            if (houses.get(boff) != null) {
                                String line3 = "";
                                Building building = (Building)houses.get(boff);
                                String realCost = "";
                                if (this.theWorkers.size() > 1) {
                                    realCost = " (" + ModSimukraft.displayMoney((float)building.blocksInBuilding * 0.02F * (float)this.theWorkers.size()) + ")";
                                }

                                String line2 = building.ltrCount + " x " + building.ftbCount + " x " + building.layerCount;
                                line3 = ModSimukraft.displayMoney((float)building.blocksInBuilding * 0.02F) + realCost;
                                String line4 = building.author;
                                GuiButton b3;
                                this.field_146292_n.add(b3 = new GuiButton(idx + 300, x, y + 48, 120, 20, line4));
                                GuiButton b2;
                                this.field_146292_n.add(b2 = new GuiButton(idx + 200, x, y + 32, 120, 20, line3));
                                GuiButton b1;
                                this.field_146292_n.add(b1 = new GuiButton(idx + 100, x, y + 16, 120, 20, line2));
                                b1.field_146124_l = false;
                                b2.field_146124_l = false;
                                b3.field_146124_l = false;
                                String pk = "";
                                if (building.displayName.startsWith("PKID")) {
                                    int hyphen = building.displayName.indexOf("-");
                                    pk = building.displayName.substring(0, hyphen + 1);
                                }

                                this.pkIndex.put(idx, pk);
                                this.field_146292_n.add(new GuiButton(idx, x, y, 120, 20, building.displayNameWithoutPK));
                                x += 120;
                                if (x + 120 > this.field_146294_l) {
                                    x = 10;
                                    y += 71;
                                }

                                ++idx;
                                ++this.buildingsOnPage;
                                if (this.buildingOffset > 0) {
                                    this.field_146292_n.add(new GuiButton(501, 5, this.field_146295_m - 20, 75, 20, "< Page"));
                                }

                                if (y + 20 + 20 + 20 + 20 > this.field_146295_m) {
                                    this.field_146292_n.add(new GuiButton(500, this.field_146294_l - 80, this.field_146295_m - 20, 75, 20, "Page >"));
                                    break;
                                }
                            }
                        } else {
                            this.field_146292_n.add(new GuiButton(501, 5, this.field_146295_m - 20, 75, 20, "< Page!"));
                        }
                    }

                    if (this.fixedBuildingCount == -1) {
                        this.fixedBuildingCount = this.buildingsOnPage;
                    }
                } else {
                    this.field_146292_n.add(new GuiButton(1, 10, 60, 300, 20, "Nothing found, go back and choose another"));
                }
            }
        }

    }

    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void func_146284_a(GuiButton guibutton) {
        if (System.currentTimeMillis() - this.fuckingBodge >= 100L) {
            this.fuckingBodge = System.currentTimeMillis();
            if (guibutton.field_146124_l) {
                if (guibutton.field_146127_k == 0) {
                    this.field_146297_k.field_71462_r = null;
                    this.field_146297_k.func_71381_h();
                } else {
                    if (this.currentPage == 0) {
                        if (guibutton.field_146126_j.contentEquals("Choose building")) {
                            this.currentPage = 1;
                            this.showPage();
                        } else {
                            GuiEmployFolk gui;
                            if (guibutton.field_146127_k == 2) {
                                gui = new GuiEmployFolk(this.constructorLoc, this.buildDirection, Vocation.BUILDER);
                                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                                this.field_146297_k.displayGuiScreen(gui);
                            } else if (guibutton.field_146127_k == 3) {
                                this.fireAllFolksForThisBuilding();
                                this.currentPage = 0;
                                this.showPage();
                            } else if (guibutton.field_146127_k == 4) {
                                GuiScreen gui = new GuiShowEmployees();
                                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                                this.field_146297_k.displayGuiScreen(gui);
                            } else if (guibutton.field_146127_k != 5) {
                                if (guibutton.field_146127_k == 6) {
                                    GuiScreen gui = new GuiTerraform((FolkData)this.theWorkers.get(0));
                                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                                    this.field_146297_k.displayGuiScreen(gui);
                                } else if (guibutton.field_146127_k == 7) {
                                    gui = new GuiEmployFolk(this.constructorLoc, "N/A", Vocation.TERRAFORMER);
                                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                                    this.field_146297_k.displayGuiScreen(gui);
                                }
                            }
                        }
                    } else if (this.currentPage == 1) {
                        if (guibutton.field_146127_k == 5) {
                            this.currentPage = 2;
                            this.showPage();
                        } else if (guibutton.field_146127_k == 6) {
                            this.currentPage = 5;
                            this.showPage();
                        } else if (guibutton.field_146127_k == 7) {
                            this.currentPage = 6;
                            this.showPage();
                        } else if (guibutton.field_146127_k == 8) {
                            this.currentPage = 7;
                            this.showPage();
                        }
                    } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                        if (this.currentPage == 8) {
                            if (guibutton.field_146126_j.contentEquals("Build it!")) {
                                if (Building.getBuilding(this.selectedBuilding.primaryXYZ) != null) {
                                    ModSimukraft.theBuildings.remove(this.selectedBuilding);
                                }

                                this.selectedBuilding.conBoxLocation = this.constructorLoc.clone();
                                ModSimukraft.theBuildings.add(this.selectedBuilding);
                                this.selectedBuilding.saveThisBuilding();
                                ModSimukraft.network.sendToAll(new LoadBuildingMessage("GuiBuildingCon"));

                                for(int i = 0; i < this.theWorkers.size(); ++i) {
                                    FolkData theWorker = (FolkData)this.theWorkers.get(i);
                                    theWorker.theBuilding = this.selectedBuilding;
                                    theWorker.saveThisFolk();
                                }

                                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                                this.field_146297_k.func_71381_h();
                                return;
                            }

                            if (guibutton.field_146127_k == 1001) {
                                this.currentPage = this.previousPage;
                                this.showPage();
                            }
                        } else if (this.currentPage != 3 && this.currentPage == 4) {
                            this.fireAllFolksForThisBuilding();
                            this.currentPage = 0;
                            this.showPage();
                        }
                    } else {
                        this.previousPage = this.currentPage;
                        if (guibutton.field_146127_k == 500) {
                            this.buildingOffset += this.fixedBuildingCount;
                            this.showPage();
                            return;
                        }

                        if (guibutton.field_146127_k == 501) {
                            this.buildingOffset -= this.fixedBuildingCount;
                            this.showPage();
                            return;
                        }

                        String type = "";
                        if (this.currentPage == 2) {
                            type = "residential";
                        }

                        if (this.currentPage == 5) {
                            type = "commercial";
                        }

                        if (this.currentPage == 6) {
                            type = "industrial";
                        }

                        if (this.currentPage == 7) {
                            type = "other";
                        }

                        String pkPrefix = "";
                        pkPrefix = (String)this.pkIndex.get(guibutton.field_146127_k);
                        this.selectedBuilding = Building.getFromAllBuildings(pkPrefix + guibutton.field_146126_j, type);

                        try {
                            this.selectedBuilding.buildDirection = this.buildDirection;
                        } catch (Exception var5) {
                        }

                        this.currentPage = 8;
                        this.showPage();
                    }

                }
            }
        }
    }

    public void fireAllFolksForThisBuilding() {
        for(int i = 0; i < this.theWorkers.size(); ++i) {
            FolkData worker = (FolkData)this.theWorkers.get(i);
            if (worker.vocation == Vocation.BUILDER) {
                JobBuilder theirJob = (JobBuilder)worker.theirJob;
                theirJob.theStage = Stage.IDLE;
                worker.theBuilding = null;
            }

            worker.selfFire();
        }

        this.theWorkers.clear();
    }

    public void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        } else {
            if (this.tfSearch != null) {
                this.tfSearch.func_146201_a(c, i);
                this.search = this.tfSearch.func_146179_b();
                if (!this.search.endsWith(":")) {
                    this.buildingOffset = 0;
                    this.showPage();
                }
            }

        }
    }

    public void func_73864_a(int i, int j, int k) {
        if (this.tfSearch != null) {
            this.tfSearch.func_146192_a(i, j, k);
        }

        super.func_73864_a(i, j, k);
    }
}

