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

    public void func_73866_w_() {
        boolean robbed = false;
        ArrayList<V3> blocks = Job.findClosestBlocks(this.bankLocation, Blocks.field_150484_ah, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        blocks = Job.findClosestBlocks(this.bankLocation, Blocks.field_150475_bE, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        blocks = Job.findClosestBlocks(this.bankLocation, Blocks.field_150340_R, 10);
        if (blocks.size() == 0) {
            robbed = true;
        }

        if (robbed) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
            ModSimukraft.sendChat("Looks like you've robbed the bank! Replace the items and we'll let you off and let you use this ATM. Next time we won't be so nice about it!");
        } else {
            if (ModSimukraft.theCommodities.size() == 0) {
                Commodity.refreshAvailableCommoditities();
            }

            this.field_146292_n.clear();
            if (this.theScreen == GuiBankATM.ATMscreen.START) {
                this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 50, 50, 100, 20, "Deposit items"));
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 70, 100, 20, "Buy Commodities"));
            } else {
                int offset;
                int inv;
                if (this.theScreen == GuiBankATM.ATMscreen.DEPOSIT) {
                    offset = 30;

                    for(inv = 0; inv < this.thePlayer.field_71071_by.func_70302_i_(); ++inv) {
                        ItemStack is = this.thePlayer.field_71071_by.func_70301_a(inv);
                        if (is != null) {
                            if (is.func_77973_b() == Items.field_151045_i) {
                                this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceDiamond)));
                                this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, "Sell " + is.field_77994_a + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceDiamond * (float)is.field_77994_a)));
                                offset += 20;
                            } else if (is.func_77973_b() == Items.field_151166_bC) {
                                this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceEmerald)));
                                this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, "Sell " + is.field_77994_a + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceEmerald * (float)is.field_77994_a)));
                                offset += 20;
                            } else if (is.func_77973_b() == Items.field_151137_ax) {
                                this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceRedstone)));
                                this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, "Sell " + is.field_77994_a + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceRedstone * (float)is.field_77994_a)));
                                offset += 20;
                            } else if (is.func_77973_b() == Items.field_151114_aO) {
                                this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGlowstone)));
                                this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, "Sell " + is.field_77994_a + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGlowstone * (float)is.field_77994_a)));
                                offset += 20;
                            } else if (is.func_77973_b() == Items.gold_ingot) {
                                this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, "Sell 1 for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGold)));
                                this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, "Sell " + is.field_77994_a + " for " + ModSimukraft.displayMoney(PricesForBlocks.bankPriceGold * (float)is.field_77994_a)));
                                offset += 20;
                            }
                        }
                    }
                } else if (this.theScreen == GuiBankATM.ATMscreen.COMMODITIES) {
                    offset = 30;

                    for(inv = 0; inv < ModSimukraft.theCommodities.size(); ++inv) {
                        this.field_146292_n.add(new GuiButton(inv + 200, this.field_146294_l / 2, offset, 20, 20, "-"));
                        this.field_146292_n.add(new GuiButton(inv + 300, this.field_146294_l / 2 + 20, offset, 20, 20, "+"));
                        offset += 20;
                    }

                    this.field_146292_n.add(new GuiButton(400, this.field_146294_l - 60, this.field_146295_m - 30, 50, 20, "Buy"));
                }
            }

        }
    }

    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, "Sim-U-Bank Ltd", this.field_146294_l / 2, 5, 16777215);
        if (this.theScreen == GuiBankATM.ATMscreen.START) {
            this.func_73732_a(this.field_146289_q, "Welcome to Sim-U-Bank, using this ATM you can deposit your gems and stones", this.field_146294_l / 2, 15, 65280);
            this.func_73732_a(this.field_146289_q, "in exchange for Sim-U-Credits, we offer the best prices for your unwanted", this.field_146294_l / 2, 25, 65280);
            this.func_73732_a(this.field_146289_q, "Diamonds, Emeralds, Redstones, Glowstones and Gold.", this.field_146294_l / 2, 35, 65280);
        } else {
            int offset;
            if (this.theScreen == GuiBankATM.ATMscreen.DEPOSIT) {
                offset = 35;
                boolean playerHasItems = false;
                this.func_73732_a(this.field_146289_q, "Items in your inventory that this bank accepts:", this.field_146294_l / 2, 15, 65280);

                for(int inv = 0; inv < this.thePlayer.field_71071_by.func_70302_i_(); ++inv) {
                    ItemStack is = this.thePlayer.field_71071_by.func_70301_a(inv);
                    if (is != null && (is.func_77973_b() == Items.field_151045_i || is.func_77973_b() == Items.field_151166_bC || is.func_77973_b() == Items.field_151137_ax || is.func_77973_b() == Items.field_151114_aO || is.func_77973_b() == Items.gold_ingot)) {
                        this.func_73731_b(this.field_146289_q, is.field_77994_a + " x " + is.func_82833_r(), 40, offset, 65280);
                        playerHasItems = true;
                        offset += 20;
                    }
                }

                if (!playerHasItems) {
                    this.func_73731_b(this.field_146289_q, "You don't have anything we want to buy, sorry.", 40, offset, 65280);
                }
            } else if (this.theScreen == GuiBankATM.ATMscreen.COMMODITIES) {
                this.func_73732_a(this.field_146289_q, "Commodities available to buy today", this.field_146294_l / 2, 20, 65280);
                offset = 35;
                if (ModSimukraft.theCommodities.size() == 0) {
                    this.func_73731_b(this.field_146289_q, "Currently no items, come back later.", 20, offset, 65280);
                }

                for(int it = 0; it < ModSimukraft.theCommodities.size(); ++it) {
                    Commodity item = (Commodity)ModSimukraft.theCommodities.get(it);
                    this.func_73731_b(this.field_146289_q, item.quantity + " x " + item.theItemStack.func_82833_r() + " @ " + ModSimukraft.displayMoney(item.priceEach) + " each", 20, offset, 65280);
                    int qty = 0;

                    for(int ci = 0; ci < this.cart.size(); ++ci) {
                        Commodity cartItem = (Commodity)this.cart.get(ci);
                        if (cartItem.theItemStack.func_82833_r().contentEquals(item.theItemStack.func_82833_r())) {
                            qty = cartItem.quantity;
                        }
                    }

                    this.func_73731_b(this.field_146289_q, qty + "", this.field_146294_l / 2 - 30, offset, 65280);
                    offset += 20;
                }
            }
        }

        this.func_73732_a(this.field_146289_q, this.errorText, this.field_146294_l / 2, this.field_146295_m - 15, 16711680);
        super.func_73863_a(i, j, f);
    }

    protected void func_146284_a(GuiButton guibutton) {
        if (System.currentTimeMillis() - this.fuckingBodge >= 500L) {
            this.fuckingBodge = System.currentTimeMillis();
            if (guibutton.field_146126_j.contentEquals("Deposit items")) {
                this.theScreen = GuiBankATM.ATMscreen.DEPOSIT;
                this.func_73866_w_();
            } else if (guibutton.field_146126_j.contentEquals("Buy Commodities")) {
                this.theScreen = GuiBankATM.ATMscreen.COMMODITIES;
                this.func_73866_w_();
            } else {
                GameStates var10000;
                if (guibutton.field_146127_k >= 100 && guibutton.field_146127_k < 200) {
                    ItemStack is = this.thePlayer.field_71071_by.func_70301_a(guibutton.field_146127_k - 100);
                    ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, "satscapesimukraft:cashshort", 1.0F, 1.0F, false);
                    String money = guibutton.field_146126_j.substring(guibutton.field_146126_j.indexOf("for ") + 4);
                    NumberFormat format = NumberFormat.getInstance();
                    Object number = 0;

                    try {
                        number = format.parse(money);
                    } catch (Exception var9) {
                    }

                    float soldFor = ((Number)number).floatValue();
                    var10000 = ModSimukraft.states;
                    var10000.credits += soldFor;
                    --is.field_77994_a;
                    if (is.field_77994_a == 0) {
                        is = null;
                    }

                    this.thePlayer.field_71071_by.func_70299_a(guibutton.field_146127_k - 100, is);
                    this.func_73866_w_();
                } else if (guibutton.field_146127_k >= 500 && guibutton.field_146127_k < 600) {
                    ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, "satscapesimukraft:cashshort", 1.0F, 1.0F, false);
                    NumberFormat format = NumberFormat.getInstance();
                    Object number = 0;

                    try {
                        number = format.parse(guibutton.field_146126_j.substring(guibutton.field_146126_j.indexOf("for ") + 4));
                    } catch (Exception var8) {
                    }

                    float soldFor = ((Number)number).floatValue();
                    var10000 = ModSimukraft.states;
                    var10000.credits += soldFor;
                    this.thePlayer.field_71071_by.func_70299_a(guibutton.field_146127_k - 500, (ItemStack)null);
                    this.func_73866_w_();
                } else {
                    int ci;
                    Commodity cartItem;
                    Commodity comm;
                    if (guibutton.field_146127_k >= 200 && guibutton.field_146127_k < 300) {
                        comm = (Commodity)ModSimukraft.theCommodities.get(guibutton.field_146127_k - 200);

                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            if (cartItem.theItemStack.func_82833_r().contentEquals(comm.theItemStack.func_82833_r()) && cartItem.quantity > 0) {
                                --cartItem.quantity;
                                break;
                            }

                            if (cartItem.quantity == 0) {
                                this.cart.remove(ci);
                                break;
                            }
                        }
                    } else if (guibutton.field_146127_k >= 300 && guibutton.field_146127_k < 400) {
                        comm = (Commodity)ModSimukraft.theCommodities.get(guibutton.field_146127_k - 300);
                        boolean added = false;

                        for(int ci = 0; ci < this.cart.size(); ++ci) {
                            Commodity cc = (Commodity)this.cart.get(ci);
                            if (cc.theItemStack.func_82833_r().contentEquals(comm.theItemStack.func_82833_r())) {
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
                    } else if (guibutton.field_146127_k == 400) {
                        if (this.cart.size() == 0) {
                            this.errorText = "You've not added any items.";
                            return;
                        }

                        float cost = 0.0F;

                        ItemStack is;
                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            is = cartItem.theItemStack;
                            is.field_77994_a = cartItem.quantity;
                            cost += (float)cartItem.quantity * cartItem.priceEach;
                        }

                        if (cost > ModSimukraft.states.credits) {
                            this.errorText = "The cost is " + ModSimukraft.displayMoney(cost) + ", but you only have " + ModSimukraft.displayMoney(ModSimukraft.states.credits);
                            return;
                        }

                        for(ci = 0; ci < this.cart.size(); ++ci) {
                            cartItem = (Commodity)this.cart.get(ci);
                            is = cartItem.theItemStack;
                            is.field_77994_a = cartItem.quantity;
                            this.thePlayer.field_71071_by.func_70441_a(is);

                            for(int ai = 0; ai < ModSimukraft.theCommodities.size(); ++ai) {
                                Commodity ac = (Commodity)ModSimukraft.theCommodities.get(ai);
                                if (ac.theItemStack.func_82833_r().contentEquals(cartItem.theItemStack.func_82833_r())) {
                                    ModSimukraft.theCommodities.remove(ai);
                                    break;
                                }
                            }
                        }

                        var10000 = ModSimukraft.states;
                        var10000.credits -= cost;
                        ModSimukraft.sendChat("Bought commodities worth " + ModSimukraft.displayMoney(cost));
                        ModSimukraft.proxy.getClientWorld().playSound(this.thePlayer.posX, this.thePlayer.posY, this.thePlayer.posZ, "satscapesimukraft:cash", 1.0F, 1.0F, false);
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                    }
                }
            }

        }
    }

    protected void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }

}
