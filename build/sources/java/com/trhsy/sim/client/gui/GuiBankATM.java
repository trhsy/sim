package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.gui.enums.ATMscreen;
import com.trhsy.sim.common.Commodity;
import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.PricesForBlocks;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.jobs.Job;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiBankATM
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:24
 * ========================================
 **/
public class GuiBankATM extends GuiScreen {
    private V3 bankLocation = null;
    private EntityPlayer thePlayer = null;
    private int mouseCount = 0;
    private ATMscreen theScreen;
    private ArrayList<Commodity> cart;
    private String errorText;
    long fuckingBodge;

    public GuiBankATM(V3 location, EntityPlayer player) {
        this.theScreen = ATMscreen.START;
        this.cart = new ArrayList();
        this.errorText = "";
        this.fuckingBodge = 0L;
        this.bankLocation = location;
        this.thePlayer = player;
    }

    @Override
    public void initGui() {
        boolean robbed = false;
        ArrayList<V3> blocks = Job.findClosestBlocks(this.bankLocation, Blocks.diamond_block, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        blocks = Job.findClosestBlocks(this.bankLocation, Blocks.emerald_block, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        blocks = Job.findClosestBlocks(this.bankLocation, Blocks.gold_block, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        if (robbed) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
            ModSimukraft.sendChat("Looks like you've robbed the bank! Replace the items and we'll let you off and let you use this ATM. Next time we won't be so nice about it!");
        } else {
            if (ModSimukraft.theCommodities.size() == 0) {
                Commodity.refreshAvailableCommoditities();
            }

            this.buttonList.clear();
            if (this.theScreen == ATMscreen.START) {
                this.buttonList.add(new GuiButton(0, this.width / 2 - 50, 50, 100, 20, "Deposit items"));
                this.buttonList.add(new GuiButton(1, this.width / 2 - 50, 70, 100, 20, "Buy Commodities"));
            } else {
                int offset;
                int inv;
                if (this.theScreen == ATMscreen.DEPOSIT) {
                    offset = 30;

                    for(inv = 0; inv < this.thePlayer.inventory.getSizeInventory(); ++inv) {
                        ItemStack is = this.thePlayer.inventory.getStackInSlot(inv);
                        if (is != null) {
                            if (is.getItem() == Items.diamond) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceDiamond)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, "Sell " + is.stackSize + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceDiamond * (float)is.stackSize)));
                                offset += 20;
                            } else if (is.getItem() == Items.emerald) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceEmerald)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, "Sell " + is.stackSize + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceEmerald * (float)is.stackSize)));
                                offset += 20;
                            } else if (is.getItem() == Items.redstone) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceRedstone)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, "Sell " + is.stackSize + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceRedstone * (float)is.stackSize)));
                                offset += 20;
                            } else if (is.getItem() == Items.glowstone_dust) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGlowstone)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, "Sell " + is.stackSize + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGlowstone * (float)is.stackSize)));
                                offset += 20;
                            } else if (is.getItem() == Items.gold_ingot) {
                                this.buttonList.add(new GuiButton(inv + 100, this.width / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGold)));
                                this.buttonList.add(new GuiButton(inv + 500, this.width / 2 + 100, offset, 100, 20, "Sell " + is.stackSize + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGold * (float)is.stackSize)));
                                offset += 20;
                            }
                        }
                    }
                } else if (this.theScreen == ATMscreen.COMMODITIES) {
                    offset = 30;

                    for(inv = 0; inv < ModSimukraft.theCommodities.size(); ++inv) {
                        this.buttonList.add(new GuiButton(inv + 200, this.width / 2, offset, 20, 20, "-"));
                        this.buttonList.add(new GuiButton(inv + 300, this.width / 2 + 20, offset, 20, 20, "+"));
                        offset += 20;
                    }

                    this.buttonList.add(new GuiButton(400, this.width - 60, this.height - 30, 50, 20, "Buy"));
                }
            }

        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Sim-U-Bank Ltd", this.width / 2, 5, 16777215);
        if (this.theScreen == ATMscreen.START) {
            this.drawCenteredString(this.fontRendererObj, "Welcome to Sim-U-Bank, using this ATM you can deposit your gems and stones", this.width / 2, 15, 65280);
            this.drawCenteredString(this.fontRendererObj, "in exchange for Sim-U-Credits, we offer the best prices for your unwanted", this.width / 2, 25, 65280);
            this.drawCenteredString(this.fontRendererObj, "Diamonds, Emeralds, Redstones, Glowstones and Gold.", this.width / 2, 35, 65280);
        } else {
            int offset;
            if (this.theScreen == ATMscreen.DEPOSIT) {
                offset = 35;
                boolean playerHasItems = false;
                this.drawCenteredString(this.fontRendererObj, "Items in your inventory that this bank accepts:", this.width / 2, 15, 65280);

                for(int inv = 0; inv < this.thePlayer.inventory.getSizeInventory(); ++inv) {
                    ItemStack is = this.thePlayer.inventory.getStackInSlot(inv);
                    if (is != null && (is.getItem() == Items.diamond || is.getItem() == Items.emerald || is.getItem() == Items.redstone || is.getItem() == Items.glowstone_dust || is.getItem() == Items.gold_ingot)) {
                        this.drawString(this.fontRendererObj, is.stackSize + " x " + is.getDisplayName(), 40, offset, 65280);
                        playerHasItems = true;
                        offset += 20;
                    }
                }

                if (!playerHasItems) {
                    this.drawString(this.fontRendererObj, "You don't have anything we want to buy, sorry.", 40, offset, 65280);
                }
            } else if (this.theScreen == ATMscreen.COMMODITIES) {
                this.drawCenteredString(this.fontRendererObj, "Commodities available to buy today", this.width / 2, 20, 65280);
                offset = 35;
                if (ModSimukraft.theCommodities.size() == 0) {
                    this.drawString(this.fontRendererObj, "Currently no items, come back later.", 20, offset, 65280);
                }

                for(int it = 0; it < ModSimukraft.theCommodities.size(); ++it) {
                    Commodity item = (Commodity)ModSimukraft.theCommodities.get(it);
                    this.drawString(this.fontRendererObj, item.quantity + " x " + item.theItemStack.getDisplayName() + " @ " + ModSimukraft.displayMoney(item.priceEach) + " each", 20, offset, 65280);
                    int qty = 0;

                    for(int ci = 0; ci < this.cart.size(); ++ci) {
                        Commodity cartItem = (Commodity)this.cart.get(ci);
                        if (cartItem.theItemStack.getDisplayName().contentEquals(item.theItemStack.getDisplayName())) {
                            qty = cartItem.quantity;
                        }
                    }

                    this.drawString(this.fontRendererObj, qty + "", this.width / 2 - 30, offset, 65280);
                    offset += 20;
                }
            }
        }

        this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 15, 16711680);
        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (System.currentTimeMillis() - this.fuckingBodge >= 500L) {
            this.fuckingBodge = System.currentTimeMillis();
            if (guibutton.displayString.contentEquals("Deposit items")) {
                this.theScreen = ATMscreen.DEPOSIT;
                this.initGui();
            } else if (guibutton.displayString.contentEquals("Buy Commodities")) {
                this.theScreen = ATMscreen.COMMODITIES;
                this.initGui();
            } else {
                GameStates var10000;
                if (guibutton.id >= 100 && guibutton.id < 200) {
                    ItemStack is = this.thePlayer.inventory.getStackInSlot(guibutton.id - 100);
                    ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSimukraft.MODID + ":cashshort", 1.0F, 1.0F, false);
                    String money = guibutton.displayString.substring(guibutton.displayString.indexOf("for ") + 4);
                    NumberFormat format = NumberFormat.getInstance();
                    Object number = 0;

                    try {
                        number = format.parse(money);
                    } catch (Exception var9) {
                    }

                    float soldFor = ((Number)number).floatValue();
                    var10000 = ModSimukraft.states;
                    var10000.credits += soldFor;
                    --is.stackSize;
                    if (is.stackSize == 0) {
                        is = null;
                    }

                    this.thePlayer.inventory.setInventorySlotContents(guibutton.id - 100, is);
                    this.initGui();
                } else if (guibutton.id >= 500 && guibutton.id < 600) {
                    ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSimukraft.MODID + ":cashshort", 1.0F, 1.0F, false);
                    NumberFormat format = NumberFormat.getInstance();
                    Object number = 0;

                    try {
                        number = format.parse(guibutton.displayString.substring(guibutton.displayString.indexOf("for ") + 4));
                    } catch (Exception var8) {
                    }

                    float soldFor = ((Number)number).floatValue();
                    var10000 = ModSimukraft.states;
                    var10000.credits += soldFor;
                    this.thePlayer.inventory.setInventorySlotContents(guibutton.id - 500, (ItemStack)null);
                    this.initGui();
                } else {
                    int ci;
                    Commodity cartItem;
                    Commodity comm;
                    if (guibutton.id >= 200 && guibutton.id < 300) {
                        comm = (Commodity)ModSimukraft.theCommodities.get(guibutton.id - 200);

                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            if (cartItem.theItemStack.getDisplayName().contentEquals(comm.theItemStack.getDisplayName()) && cartItem.quantity > 0) {
                                --cartItem.quantity;
                                break;
                            }

                            if (cartItem.quantity == 0) {
                                this.cart.remove(ci);
                                break;
                            }
                        }
                    } else if (guibutton.id >= 300 && guibutton.id < 400) {
                        comm = (Commodity)ModSimukraft.theCommodities.get(guibutton.id - 300);
                        boolean added = false;

                        for(int cj = 0; cj < this.cart.size(); ++cj) {
                            Commodity cc = (Commodity)this.cart.get(cj);
                            if (cc.theItemStack.getDisplayName().contentEquals(comm.theItemStack.getDisplayName())) {
                                if (cc.quantity >= comm.quantity) {
                                    return;
                                }

                                ++cc.quantity;
                                added = true;
                                break;
                            }
                        }

                        if (!added) {
                            this.cart.add(new Commodity(comm.theItemStack, 1, comm.priceEach));
                        }
                    } else if (guibutton.id == 400) {
                        if (this.cart.size() == 0) {
                            this.errorText = "You've not added any items.";
                            return;
                        }

                        float cost = 0.0F;

                        ItemStack is;
                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            is = cartItem.theItemStack;
                            is.stackSize = cartItem.quantity;
                            cost += (float)cartItem.quantity * cartItem.priceEach;
                        }

                        if (cost > ModSimukraft.states.credits) {
                            this.errorText = "The cost is " + ModSimukraft.displayMoney(cost) + ", but you only have " + ModSimukraft.displayMoney(ModSimukraft.states.credits);
                            return;
                        }

                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            is = cartItem.theItemStack;
                            is.stackSize = cartItem.quantity;
                            this.thePlayer.inventory.addItemStackToInventory(is);

                            for(int ai = 0; ai < ModSimukraft.theCommodities.size(); ++ai) {
                                Commodity ac = (Commodity)ModSimukraft.theCommodities.get(ai);
                                if (ac.theItemStack.getDisplayName().contentEquals(cartItem.theItemStack.getDisplayName())) {
                                    ModSimukraft.theCommodities.remove(ai);
                                    break;
                                }
                            }
                        }

                        var10000 = ModSimukraft.states;
                        var10000.credits -= cost;
                        ModSimukraft.sendChat("Bought commodities worth " + ModSimukraft.displayMoney(cost));
                        ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, ModSimukraft.MODID + ":cash", 1.0F, 1.0F, false);
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    }
                }
            }

        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        if (i == 1) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

}
