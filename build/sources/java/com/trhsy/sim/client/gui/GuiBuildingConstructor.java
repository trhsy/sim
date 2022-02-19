package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
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
import net.minecraft.client.resources.I18n;
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
 * @Description todo 建筑箱
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

            for (int f = 0; f < ModSim.theFolks.size(); ++f) {
                FolkData folk = (FolkData) ModSim.theFolks.get(f);
                if (folk.employedAt != null && folk.employedAt.isSameCoordsAs(this.constructorLoc, true, true)) {
                    if (folk.vocation == Vocation.BUILDER) {
                        JobBuilder theirJob = (JobBuilder) folk.theirJob;
                        if (theirJob.theStage == Stage.IDLE) {
                            theirJob.theStage = Stage.WORKERASSIGNED;
                        }
                    }

                    this.theWorkers.add(folk);
                }
            }
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        this.mc.setIngameNotInFocus();
        if (this.tfSearch != null) {
            this.tfSearch.updateCursorCounter();
        }

        super.updateScreen();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.showPage();
        super.initGui();
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            String sim_gui_BC_Constructor = I18n.format("container.sim.sim_gui_BC_Constructor");
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Constructor, this.width / 2, 17, 16777215);
            String sim_gui_BC_Idle = I18n.format("container.sim.sim_gui_BC_Idle");
            String sim_gui_BC_chosen = I18n.format("container.sim.sim_gui_BC_chosen");
            String s = sim_gui_BC_Idle;
            String t = sim_gui_BC_chosen;

            try {
                if (this.theWorkers.size() > 0) {
                    JobBuilder theirJob = (JobBuilder) ((FolkData) this.theWorkers.get(0)).theirJob;
                    s = theirJob.theStage.toString();
                    if (((FolkData) this.theWorkers.get(0)).theBuilding != null) {
                        t = ((FolkData) this.theWorkers.get(0)).theBuilding.displayName;
                    }
                }
            } catch (Exception var11) {
                String sim_gui_BC_their = I18n.format("container.sim.sim_gui_BC_their");
                s = sim_gui_BC_their;
                t = "";
            }
            String sim_gui_BC_Current = I18n.format("container.sim.sim_gui_BC_Current");
            String sim_gui_BC_Building = I18n.format("container.sim.sim_gui_BC_Building");
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Current + s, this.width / 2, 30, 11206655);
            this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Building + t, this.width / 2, 40, 11206655);
            switch (this.currentPage) {
                case 0:
                    String sim_gui_BC_building_constructor = I18n.format("container.sim.sim_gui_BC_building_constructor");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_building_constructor, this.width / 2, 100, 16777130);
                    break;
                case 1:
                    String sim_gui_BC_building = I18n.format("container.sim.sim_gui_BC_building");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_building, this.width / 2, 100, 16777130);
                    break;
                case 2:
                    String sim_gui_BC_residential = I18n.format("container.sim.sim_gui_BC_residential");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_residential, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 3:
                    String sim_gui_BC_unemployed = I18n.format("container.sim.sim_gui_BC_unemployed");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_unemployed, this.width / 2, 50, 16777130);
                    break;
                case 4:
                    String sim_gui_BC_employees = I18n.format("container.sim.sim_gui_BC_employees");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_employees, this.width / 2, 50, 16777130);
                    break;
                case 5:
                    String sim_gui_BC_commercial = I18n.format("container.sim.sim_gui_BC_commercial");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_commercial, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 6:
                    String sim_gui_BC_industrial = I18n.format("container.sim.sim_gui_BC_industrial");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_industrial, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 7:
                    String sim_gui_BC_Now_choose = I18n.format("container.sim.sim_gui_BC_Now_choose");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_Now_choose, this.width / 2, 50, 16777130);
                    this.tfSearch.drawTextBox();
                    break;
                case 8:
                    String sim_gui_BC_requirements_for = I18n.format("container.sim.sim_gui_BC_requirements_for");
                    this.drawCenteredString(this.fontRendererObj, sim_gui_BC_requirements_for + this.selectedBuilding.displayNameWithoutPK, this.width / 2, 50, 16777130);
                    int y = 70;
                    Iterator it = this.selectedBuilding.requirements.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry) it.next();
                        ItemStack is = (ItemStack) pairs.getKey();
                        if (is != null) {
                            if (y + 20 > this.height - 20) {
                                String sim_gui_BC_block_types = I18n.format("container.sim.sim_gui_BC_block_types");
                                this.drawString(this.fontRendererObj, sim_gui_BC_block_types, 90, y, 16777215);
                            } else {
                                String itemName = is.getDisplayName();
                                if (itemName.toLowerCase().contentEquals("oak wood")) {
                                    itemName = "Logs";
                                }

                                if (itemName.toLowerCase().contains("oak wood planks")) {
                                    itemName = "Planks";
                                }

                                this.displayReq(itemName, (Integer) pairs.getValue(), y);
                                y += 15;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception var12) {
            //var12.printStackTrace();
            ModSim.log.error(var12.getMessage());
        }
        super.drawScreen(i, j, f);
    }

    private void displayReq(String block, int qty, int y) {
        double stacks = Math.floor((double) (qty / 64));
        this.drawString(this.fontRendererObj, qty + "", 90, y, 16777215);
        this.drawString(this.fontRendererObj, "x", 125, y, 16777215);
        this.drawString(this.fontRendererObj, block, 150, y, 16777215);
        String sim_gui_BC_less = I18n.format("container.sim.sim_gui_BC_less");
        String sim_gui_BC_exactly = I18n.format("container.sim.sim_gui_BC_exactly");
        String sim_gui_BC_about_2 = I18n.format("container.sim.sim_gui_BC_about_2");
        String sim_gui_BC_about = I18n.format("container.sim.sim_gui_BC_about");
        String sim_gui_BC_stacks = I18n.format("container.sim.sim_gui_BC_stacks");
        String st = "";
        if (qty < 64) {
            st = sim_gui_BC_less;
        } else if (qty == 64) {
            st = sim_gui_BC_exactly;
        } else if (qty >= 64 && qty < 128) {
            st = sim_gui_BC_about_2;
        } else {
            st = sim_gui_BC_about + (int) (stacks + 1.0D) + sim_gui_BC_stacks;
        }

        this.drawString(this.fontRendererObj, st, 250, y, 16777215);
    }

    private void showPage() {
        this.mc.setIngameNotInFocus();
        this.buttonList.clear();
        String sim_gui_BC_Done = I18n.format("container.sim.sim_gui_BC_Done");
        this.buttonList.add(new GuiButton(0, 2, 12, 50, 20, sim_gui_BC_Done));
        if (this.selectedBuilding == null) {
            this.selectedBuilding = Building.getBuildingByConBox(this.constructorLoc);
        }

        if (this.currentPage == 0) {
            String sim_gui_BC_Choose_building = I18n.format("container.sim.sim_gui_BC_Choose_building");
            this.buttonList.add(new GuiButton(1, this.width / 2 - 60, 150, 120, 20, sim_gui_BC_Choose_building));
            String sim_gui_BC_Hire_builder = I18n.format("container.sim.sim_gui_BC_Hire_builder");
            this.buttonList.add(new GuiButton(2, this.width / 2 - 180, 150, 120, 20, sim_gui_BC_Hire_builder));
            String sim_gui_BC_worker = I18n.format("container.sim.sim_gui_BC_worker");
            String w = sim_gui_BC_worker;
            if (this.theWorkers.size() == 1) {
                w = ((FolkData) this.theWorkers.get(0)).name;
            } else if (this.theWorkers.size() > 1) {
                String sim_gui_BC_Staff = I18n.format("container.sim.sim_gui_BC_Staff");
                w = sim_gui_BC_Staff + "(" + this.theWorkers.size() + ")";
            }
            String sim_gui_BC_Fire = I18n.format("container.sim.sim_gui_BC_Fire");
            this.buttonList.add(new GuiButton(3, this.width / 2 + 60, 150, 120, 20, sim_gui_BC_Fire + w));
            String sim_gui_BC_Show_Employees = I18n.format("container.sim.sim_gui_BC_Show_Employees");
            this.buttonList.add(new GuiButton(4, this.width / 2 + 60, 170, 120, 20, sim_gui_BC_Show_Employees));
            String sim_gui_BC_Terraform_area = I18n.format("container.sim.sim_gui_BC_Terraform_area");
            this.buttonList.add(new GuiButton(5, -600, 170, 120, 20, "-"));
            this.buttonList.add(new GuiButton(6, this.width / 2 - 60, 170, 120, 20, sim_gui_BC_Terraform_area));
            String sim_gui_BC_Hire_terraformer = I18n.format("container.sim.sim_gui_BC_Hire_terraformer");
            this.buttonList.add(new GuiButton(7, this.width / 2 - 180, 170, 120, 20, sim_gui_BC_Hire_terraformer));
            if (this.theWorkers.size() == 0) {
                ((GuiButton) this.buttonList.get(1)).enabled = false;
                ((GuiButton) this.buttonList.get(2)).enabled = true;
                ((GuiButton) this.buttonList.get(3)).enabled = false;
                ((GuiButton) this.buttonList.get(6)).enabled = false;
                ((GuiButton) this.buttonList.get(7)).enabled = true;
            } else {
                ((GuiButton) this.buttonList.get(1)).enabled = true;
                ((GuiButton) this.buttonList.get(2)).enabled = false;
                ((GuiButton) this.buttonList.get(3)).enabled = true;
                ((GuiButton) this.buttonList.get(6)).enabled = true;
                ((GuiButton) this.buttonList.get(7)).enabled = false;
            }
        } else if (this.currentPage == 1) {
            String sim_gui_BC_Residential = I18n.format("container.sim.sim_gui_BC_Residential");
            String sim_gui_BC_Commercial = I18n.format("container.sim.sim_gui_BC_Commercial");
            String sim_gui_BC_Industrial = I18n.format("container.sim.sim_gui_BC_Industrial");
            String sim_gui_BC_Other = I18n.format("container.sim.sim_gui_BC_Other");
            this.buttonList.add(new GuiButton(5, this.width / 2 - 200, 150, 100, 20, sim_gui_BC_Residential));
            this.buttonList.add(new GuiButton(6, this.width / 2 - 100, 150, 100, 20, sim_gui_BC_Commercial));
            this.buttonList.add(new GuiButton(7, this.width / 2, 150, 100, 20, sim_gui_BC_Industrial));
            this.buttonList.add(new GuiButton(8, this.width / 2 + 100, 150, 100, 20, sim_gui_BC_Other));
        } else if (this.currentPage != 3) {
            int x;
            int y;
            int idx;
            if (this.currentPage == 4) {
                try {
                    x = 10;
                    y = 65;
                    idx = 1;

                    for (y = 0; y < ModSim.theFolks.size(); ++y) {
                        FolkData folk = (FolkData) ModSim.theFolks.get(y);
                        String sim_gui_BC_Fire = I18n.format("container.sim.sim_gui_BC_Fire");
                        this.buttonList.add(new GuiButton(idx, x, y, 100, 20, sim_gui_BC_Fire + folk.name));
                        ++x;
                        x += 100;
                        if (x + 100 > this.width) {
                            x = 10;
                            y += 20;
                        }

                        if (y + 20 > this.height - 50) {
                            break;
                        }
                    }
                } catch (Exception var18) {
                    var18.printStackTrace();
                }
            } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                if (this.currentPage == 8) {
                    String sim_gui_BC_Go_Back = I18n.format("container.sim.sim_gui_BC_Go_Back");
                    this.buttonList.add(new GuiButton(1001, this.width / 2 - 100, this.height - 25, 100, 20, sim_gui_BC_Go_Back));
                    String sim_gui_BC_Build_it = I18n.format("container.sim.sim_gui_BC_Build_it");
                    this.buttonList.add(new GuiButton(1000, this.width / 2, this.height - 25, 100, 20, sim_gui_BC_Build_it));
                }
            } else {
                ArrayList<Building> houses = new ArrayList();
                String theType = "";
                this.buildingsOnPage = 0;
                this.tfSearch = new GuiTextField(this.fontRendererObj, this.width / 2 - 50, this.height - 30, 100, 20);
                this.tfSearch.setText(this.search);
                this.tfSearch.setFocused(true);
                this.tfSearch.setMaxStringLength(10);
                if (this.currentPage == 2) {
                    houses = Building.getBuildingBlueprints("residential", this.tfSearch.getText().trim());
                    theType = "residential";
                } else if (this.currentPage == 5) {
                    houses = Building.getBuildingBlueprints("commercial", this.tfSearch.getText().trim());
                    theType = "commercial";
                } else if (this.currentPage == 6) {
                    houses = Building.getBuildingBlueprints("industrial", this.tfSearch.getText().trim());
                    theType = "industrial";
                } else if (this.currentPage == 7) {
                    houses = Building.getBuildingBlueprints("other", this.tfSearch.getText().trim());
                    theType = "other";
                }

                x = 10;
                y = 60;
                idx = 1;
                if (houses != null) {
                    for (int b = 0; b <= houses.size(); ++b) {
                        int boff = b + this.buildingOffset;
                        if (boff < 0) {
                            boff = 0;
                            this.buildingOffset = 0;
                        }
                        String sim_gui_BC_Page = I18n.format("container.sim.sim_gui_BC_Page");
                        if (boff < houses.size()) {
                            if (houses.get(boff) != null) {
                                String line3 = "";
                                Building building = (Building) houses.get(boff);
                                String realCost = "";
                                if (this.theWorkers.size() > 1) {
                                    realCost = " (" + ModSim.displayMoney((float) building.blocksInBuilding * 0.02F * (float) this.theWorkers.size()) + ")";
                                }

                                String line2 = building.ltrCount + " x " + building.ftbCount + " x " + building.layerCount;
                                line3 = ModSim.displayMoney((float) building.blocksInBuilding * 0.02F) + realCost;
                                String line4 = building.author;
                                GuiButton b3;
                                this.buttonList.add(b3 = new GuiButton(idx + 300, x, y + 48, 120, 20, line4));
                                GuiButton b2;
                                this.buttonList.add(b2 = new GuiButton(idx + 200, x, y + 32, 120, 20, line3));
                                GuiButton b1;
                                this.buttonList.add(b1 = new GuiButton(idx + 100, x, y + 16, 120, 20, line2));
                                b1.enabled = false;
                                b2.enabled = false;
                                b3.enabled = false;
                                String pk = "";
                                if (building.displayName.startsWith("PKID")) {
                                    int hyphen = building.displayName.indexOf("-");
                                    pk = building.displayName.substring(0, hyphen + 1);
                                }

                                this.pkIndex.put(idx, pk);
                                this.buttonList.add(new GuiButton(idx, x, y, 120, 20, building.displayNameWithoutPK));
                                x += 120;
                                if (x + 120 > this.width) {
                                    x = 10;
                                    y += 71;
                                }

                                ++idx;
                                ++this.buildingsOnPage;
                                if (this.buildingOffset > 0) {
                                    this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "<" + sim_gui_BC_Page));
                                }

                                if (y + 20 + 20 + 20 + 20 > this.height) {
                                    this.buttonList.add(new GuiButton(500, this.width - 80, this.height - 20, 75, 20, sim_gui_BC_Page + ">"));
                                    break;
                                }
                            }
                        } else {
                            this.buttonList.add(new GuiButton(501, 5, this.height - 20, 75, 20, "<" + sim_gui_BC_Page));
                        }
                    }

                    if (this.fixedBuildingCount == -1) {
                        this.fixedBuildingCount = this.buildingsOnPage;
                    }
                } else {
                    String sim_gui_BC_Nothing_found = I18n.format("container.sim.sim_gui_BC_Nothing_found");
                    this.buttonList.add(new GuiButton(1, 10, 60, 300, 20, sim_gui_BC_Nothing_found));
                }
            }
        }

    }

    @Override
    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public void actionPerformed(GuiButton guibutton) {
        if (System.currentTimeMillis() - this.fuckingBodge >= 100L) {
            this.fuckingBodge = System.currentTimeMillis();
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    if (this.currentPage == 0) {
                        String sim_gui_BC_Choose_building = I18n.format("container.sim.sim_gui_BC_Choose_building");
                        if (guibutton.displayString.contentEquals(sim_gui_BC_Choose_building)) {
                            this.currentPage = 1;
                            this.showPage();
                        } else {
                            GuiEmployFolk gui;
                            if (guibutton.id == 2) {
                                gui = new GuiEmployFolk(this.constructorLoc, this.buildDirection, Vocation.BUILDER);
                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.displayGuiScreen(gui);
                            } else if (guibutton.id == 3) {
                                this.fireAllFolksForThisBuilding();
                                this.currentPage = 0;
                                this.showPage();
                            } else if (guibutton.id == 4) {
                                GuiScreen guiScreen = new GuiShowEmployees();
                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.displayGuiScreen(guiScreen);
                            } else if (guibutton.id != 5) {
                                if (guibutton.id == 6) {
                                    GuiScreen guiScreen = new GuiTerraform((FolkData) this.theWorkers.get(0));
                                    this.mc.displayGuiScreen((GuiScreen) null);
                                    this.mc.displayGuiScreen(guiScreen);
                                } else if (guibutton.id == 7) {
                                    gui = new GuiEmployFolk(this.constructorLoc, "N/A", Vocation.TERRAFORMER);
                                    this.mc.displayGuiScreen((GuiScreen) null);
                                    this.mc.displayGuiScreen(gui);
                                }
                            }
                        }
                    } else if (this.currentPage == 1) {
                        if (guibutton.id == 5) {
                            this.currentPage = 2;
                            this.showPage();
                        } else if (guibutton.id == 6) {
                            this.currentPage = 5;
                            this.showPage();
                        } else if (guibutton.id == 7) {
                            this.currentPage = 6;
                            this.showPage();
                        } else if (guibutton.id == 8) {
                            this.currentPage = 7;
                            this.showPage();
                        }
                    } else if (this.currentPage != 2 && this.currentPage != 5 && this.currentPage != 6 && this.currentPage != 7) {
                        if (this.currentPage == 8) {
                            String sim_gui_BC_Build_it = I18n.format("container.sim.sim_gui_BC_Build_it");
                            if (guibutton.displayString.contentEquals(sim_gui_BC_Build_it)) {
                                if (Building.getBuilding(this.selectedBuilding.primaryXYZ) != null) {
                                    ModSim.theBuildings.remove(this.selectedBuilding);
                                }

                                this.selectedBuilding.conBoxLocation = this.constructorLoc.clone();
                                ModSim.theBuildings.add(this.selectedBuilding);
                                this.selectedBuilding.saveThisBuilding();
                                ModSim.network.sendToAll(new LoadBuildingMessage("GuiBuildingCon"));

                                for (int i = 0; i < this.theWorkers.size(); ++i) {
                                    FolkData theWorker = (FolkData) this.theWorkers.get(i);
                                    theWorker.theBuilding = this.selectedBuilding;
                                    theWorker.saveThisFolk();
                                }

                                this.mc.displayGuiScreen((GuiScreen) null);
                                this.mc.setIngameFocus();
                                return;
                            }

                            if (guibutton.id == 1001) {
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
                        if (guibutton.id == 500) {
                            this.buildingOffset += this.fixedBuildingCount;
                            this.showPage();
                            return;
                        }

                        if (guibutton.id == 501) {
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
                        pkPrefix = (String) this.pkIndex.get(guibutton.id);
                        this.selectedBuilding = Building.getFromAllBuildings(pkPrefix + guibutton.displayString, type);

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
        for (int i = 0; i < this.theWorkers.size(); ++i) {
            FolkData worker = (FolkData) this.theWorkers.get(i);
            if (worker.vocation == Vocation.BUILDER) {
                JobBuilder theirJob = (JobBuilder) worker.theirJob;
                theirJob.theStage = Stage.IDLE;
                worker.theBuilding = null;
            }

            worker.selfFire();
        }

        this.theWorkers.clear();
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        } else {
            if (this.tfSearch != null) {
                this.tfSearch.textboxKeyTyped(c, i);
                this.search = this.tfSearch.getText();
                if (!this.search.endsWith(":")) {
                    this.buildingOffset = 0;
                    this.showPage();
                }
            }

        }
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        if (this.tfSearch != null) {
            this.tfSearch.mouseClicked(i, j, k);
        }

        super.mouseClicked(i, j, k);
    }
}

