package com.trhsy.sim.common.gui.blocks;
/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.Commodity;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.PricesForBlocks;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.enums.ATMscreen;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
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
 * @Description todo 银行ATM
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:24
 * ========================================
 **/
public class GuiBankATM extends GuiScreen {
    //银行地址
    private V3 bankLocation = null;
    //玩家
    private EntityPlayer thePlayer = null;
    //鼠标计数
    private int mouseCount = 0;
    //银屏
    private ATMscreen theScreen;
    private ArrayList<Commodity> cart;
    //错误文本
    private String errorText;

    long fuckingBodge;

    public GuiBankATM(V3 location, EntityPlayer player) {
        try {
            this.theScreen = ATMscreen.START;
            this.cart = new ArrayList();
            this.errorText = "";
            this.fuckingBodge = 0L;
            this.bankLocation = location;
            this.thePlayer = player;
        } catch (Exception e) {
            ModSimReloaded.log.error("GuiBankATM出错了：" + e.getMessage());
        }
    }

    @Override
    public void func_73866_w_() {
        try {
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
                //看起来你已经抢了银行！
                String sim_gui_ATMs = I18n.func_135052_a("container.sim.sim_gui_ATMs");
                ModSimReloaded.sendChat(sim_gui_ATMs);
            } else {
                if (ModSimReloaded.theCommodities.size() == 0) {
                    Commodity.refreshAvailableCommoditities();
                }

                this.field_146292_n.clear();
                if (this.theScreen == ATMscreen.START) {
                    //寄存物品
                    String sim_gui_ATMs_Deposit = I18n.func_135052_a("container.sim.sim_gui_ATMs_Deposit");
                    this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 50, 50, 100, 20, sim_gui_ATMs_Deposit));
                    //购买商品
                    String sim_gui_ATMs_Commodities = I18n.func_135052_a("container.sim.sim_gui_ATMs_Commodities");
                    this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, 70, 100, 20, sim_gui_ATMs_Commodities));
                } else {
                    int offset;
                    int inv;
                    if (this.theScreen == ATMscreen.DEPOSIT) {
                        offset = 30;

                        for(inv = 0; inv < this.thePlayer.field_71071_by.func_70302_i_(); inv++) {
                            ItemStack is = this.thePlayer.field_71071_by.func_70301_a(inv);
                            if (is != null) {
                                String sim_gui_ATMs_Sell_1 = I18n.func_135052_a("container.sim.sim_gui_ATMs_Sell_1");
                                String sim_gui_ATMs_Sell = I18n.func_135052_a("container.sim.sim_gui_ATMs_Sell");
                                String sim_gui_ATMs_for = I18n.func_135052_a("container.sim.sim_gui_ATMs_for");
                                if (is.func_77973_b() == Items.field_151045_i) {

                                    this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceDiamond)));

                                    this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.field_77994_a + sim_gui_ATMs_for + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceDiamond * (float) is.field_77994_a)));
                                    offset += 20;
                                } else if (is.func_77973_b() == Items.field_151166_bC) {
                                    this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceEmerald)));
                                    this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.field_77994_a + sim_gui_ATMs_for + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceEmerald * (float) is.field_77994_a)));
                                    offset += 20;
                                } else if (is.func_77973_b() == Items.field_151137_ax) {
                                    this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceRedstone)));
                                    this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.field_77994_a + sim_gui_ATMs_for + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceRedstone * (float) is.field_77994_a)));
                                    offset += 20;
                                } else if (is.func_77973_b() == Items.field_151114_aO) {
                                    this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceGlowstone)));
                                    this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.field_77994_a + sim_gui_ATMs_for + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceGlowstone * (float) is.field_77994_a)));
                                    offset += 20;
                                } else if (is.func_77973_b() == Items.field_151043_k) {
                                    this.field_146292_n.add(new GuiButton(inv + 100, this.field_146294_l / 2, offset, 100, 20, sim_gui_ATMs_Sell_1 + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceGold)));
                                    this.field_146292_n.add(new GuiButton(inv + 500, this.field_146294_l / 2 + 100, offset, 100, 20, sim_gui_ATMs_Sell + is.field_77994_a + sim_gui_ATMs_for + ModSimReloaded.displayMoney(PricesForBlocks.bankPriceGold * (float) is.field_77994_a)));
                                    offset += 20;
                                }
                            }
                        }
                    } else if (this.theScreen == ATMscreen.COMMODITIES) {
                        offset = 30;

                        for (inv = 0; inv < ModSimReloaded.theCommodities.size(); inv++) {
                            this.field_146292_n.add(new GuiButton(inv + 200, this.field_146294_l / 2, offset, 20, 20, "-"));
                            this.field_146292_n.add(new GuiButton(inv + 300, this.field_146294_l / 2 + 20, offset, 20, 20, "+"));
                            offset += 20;
                        }
                        String sim_gui_ATMs_Buy = I18n.func_135052_a("container.sim.sim_gui_ATMs_Buy");
                        this.field_146292_n.add(new GuiButton(400, this.field_146294_l - 60, this.field_146295_m - 30, 50, 20, sim_gui_ATMs_Buy));
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("initGui出错了：" + e.getMessage());
        }

    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.func_146276_q_();
            String sim_gui_ATMs_Ltd = I18n.func_135052_a("container.sim.sim_gui_ATMs_Ltd");
            this.func_73732_a(this.field_146289_q, sim_gui_ATMs_Ltd, this.field_146294_l / 2, 5, 16777215);
            if (this.theScreen == ATMscreen.START) {
                String sim_gui_ATMs_Welcome = I18n.func_135052_a("container.sim.sim_gui_ATMs_Welcome");
                this.func_73732_a(this.field_146289_q, sim_gui_ATMs_Welcome, this.field_146294_l / 2, 15, 65280);
                String sim_gui_ATMs_exchange = I18n.func_135052_a("container.sim.sim_gui_ATMs_exchange");
                this.func_73732_a(this.field_146289_q, sim_gui_ATMs_exchange, this.field_146294_l / 2, 25, 65280);
                String sim_gui_ATMs_Diamonds = I18n.func_135052_a("container.sim.sim_gui_ATMs_Diamonds");
                this.func_73732_a(this.field_146289_q, sim_gui_ATMs_Diamonds, this.field_146294_l / 2, 35, 65280);
            } else {
                int offset;
                if (this.theScreen == ATMscreen.DEPOSIT) {
                    offset = 35;
                    boolean playerHasItems = false;
                    String sim_gui_ATMs_Items = I18n.func_135052_a("container.sim.sim_gui_ATMs_Items");
                    this.func_73732_a(this.field_146289_q, sim_gui_ATMs_Items, this.field_146294_l / 2, 15, 65280);

                    for (int inv = 0; inv < this.thePlayer.field_71071_by.func_70302_i_(); inv++) {
                        ItemStack is = this.thePlayer.field_71071_by.func_70301_a(inv);
                        if (is != null && (is.func_77973_b() == Items.field_151045_i || is.func_77973_b() == Items.field_151166_bC || is.func_77973_b() == Items.field_151137_ax || is.func_77973_b() == Items.field_151114_aO || is.func_77973_b() == Items.field_151043_k)) {
                            this.func_73731_b(this.field_146289_q, is.field_77994_a + " x " + is.func_82833_r(), 40, offset, 65280);
                            playerHasItems = true;
                            offset += 20;
                        }
                    }

                    if (!playerHasItems) {
                        String sim_gui_ATMs_sorry = I18n.func_135052_a("container.sim.sim_gui_ATMs_sorry");
                        this.func_73731_b(this.field_146289_q, sim_gui_ATMs_sorry, 40, offset, 65280);
                    }
                } else if (this.theScreen == ATMscreen.COMMODITIES) {
                    String sim_gui_ATMs_today = I18n.func_135052_a("container.sim.sim_gui_ATMs_today");
                    this.func_73732_a(this.field_146289_q, sim_gui_ATMs_today, this.field_146294_l / 2, 20, 65280);
                    offset = 35;
                    if (ModSimReloaded.theCommodities.size() == 0) {
                        String sim_gui_ATMs_later = I18n.func_135052_a("container.sim.sim_gui_ATMs_later");
                        this.func_73731_b(this.field_146289_q, sim_gui_ATMs_later, 20, offset, 65280);
                    }

                    for (int it = 0; it < ModSimReloaded.theCommodities.size(); it++) {
                        Commodity item = (Commodity) ModSimReloaded.theCommodities.get(it);
                        this.func_73731_b(this.field_146289_q, item.quantity + " x " + item.theItemStack.func_82833_r() + " @ " + ModSimReloaded.displayMoney(item.priceEach) + " each", 20, offset, 65280);
                        int qty = 0;

                        for (int ci = 0; ci < this.cart.size(); ++ci) {
                            Commodity cartItem = (Commodity) this.cart.get(ci);
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
        } catch (Exception e) {
            ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage());
        }


    }

    /**
     * 执行动作
     * @param guibutton
     */
    @Override
    protected void func_146284_a(GuiButton guibutton) {
        try {
            if (System.currentTimeMillis() - this.fuckingBodge >= 500L) {
                this.fuckingBodge = System.currentTimeMillis();
                String sim_gui_ATMs_Deposit = I18n.func_135052_a("container.sim.sim_gui_ATMs_Deposit");
                String sim_gui_ATMs_Commodities = I18n.func_135052_a("container.sim.sim_gui_ATMs_Commodities");
                if (guibutton.field_146126_j.contentEquals(sim_gui_ATMs_Deposit)) {
                    this.theScreen = ATMscreen.DEPOSIT;
                    this.func_73866_w_();
                } else if (guibutton.field_146126_j.contentEquals(sim_gui_ATMs_Commodities)) {
                    this.theScreen = ATMscreen.COMMODITIES;
                    this.func_73866_w_();
                } else {
                    GameStates var10000;
                    if (guibutton.field_146127_k >= 100 && guibutton.field_146127_k < 200) {
                        ItemStack is = this.thePlayer.field_71071_by.func_70301_a(guibutton.field_146127_k - 100);
                        ModSim.proxy.getClientWorld().func_72980_b(this.thePlayer.field_70165_t, this.thePlayer.field_70163_u, this.thePlayer.field_70161_v, ModSim.MODID + ":cashshort", 1.0F, 1.0F, false);
                        String money = guibutton.field_146126_j.substring(guibutton.field_146126_j.indexOf(I18n.func_135052_a("container.sim.trhsy1")) + 4);
                        NumberFormat format = NumberFormat.getInstance();
                        Object number = 0;

                        try {
                            number = format.parse(money);
                        } catch (Exception var9) {
                        }

                        float soldFor = ((Number)number).floatValue();
                        var10000 = ModSimReloaded.states;
                        var10000.credits += soldFor;
                        --is.field_77994_a;
                        if (is.field_77994_a == 0) {
                            is = null;
                        }

                        this.thePlayer.field_71071_by.func_70299_a(guibutton.field_146127_k - 100, is);
                        this.func_73866_w_();
                    } else if (guibutton.field_146127_k >= 500 && guibutton.field_146127_k < 600) {
                        ModSim.proxy.getClientWorld().func_72980_b(this.thePlayer.field_70165_t, this.thePlayer.field_70163_u, this.thePlayer.field_70161_v, ModSim.MODID + ":cashshort", 1.0F, 1.0F, false);
                        NumberFormat format = NumberFormat.getInstance();
                        Object number = 0;

                        try {
                            number = format.parse(guibutton.field_146126_j.substring(guibutton.field_146126_j.indexOf(I18n.func_135052_a("container.sim.trhsy1")) + 4));
                        } catch (Exception var8) {
                        }

                        float soldFor = ((Number)number).floatValue();
                        var10000 = ModSimReloaded.states;
                        var10000.credits += soldFor;
                        this.thePlayer.field_71071_by.func_70299_a(guibutton.field_146127_k - 500, (ItemStack)null);
                        this.func_73866_w_();
                    } else {
                        int ci;
                        Commodity cartItem;
                        Commodity comm;
                        if (guibutton.field_146127_k >= 200 && guibutton.field_146127_k < 300) {
                            comm = (Commodity) ModSimReloaded.theCommodities.get(guibutton.field_146127_k - 200);

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
                            comm = (Commodity) ModSimReloaded.theCommodities.get(guibutton.field_146127_k - 300);
                            boolean added = false;

                            for(int cj = 0; cj < this.cart.size(); ++cj) {
                                Commodity cc = (Commodity)this.cart.get(cj);
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
                                String sim_gui_ATMs_added = I18n.func_135052_a("container.sim.sim_gui_ATMs_added");
                                this.errorText = sim_gui_ATMs_added;
                                return;
                            }

                            float cost = 0.0F;

                            ItemStack is;
                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = (Commodity) this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.field_77994_a = cartItem.quantity;
                                cost += (float) cartItem.quantity * cartItem.priceEach;
                            }

                            if (cost > ModSimReloaded.states.credits) {
                                String sim_gui_ATMs_cost = I18n.func_135052_a("container.sim.sim_gui_ATMs_cost");
                                String sim_gui_ATMs_only = I18n.func_135052_a("container.sim.sim_gui_ATMs_only");
                                this.errorText = sim_gui_ATMs_cost + ModSimReloaded.displayMoney(cost) + sim_gui_ATMs_only + ModSimReloaded.displayMoney(ModSimReloaded.states.credits);
                                return;
                            }

                            for (ci = 0; ci < this.cart.size(); ++ci) {
                                cartItem = (Commodity) this.cart.get(ci);
                                is = cartItem.theItemStack;
                                is.field_77994_a = cartItem.quantity;
                                this.thePlayer.field_71071_by.func_70441_a(is);

                                for (int ai = 0; ai < ModSimReloaded.theCommodities.size(); ++ai) {
                                    Commodity ac = (Commodity) ModSimReloaded.theCommodities.get(ai);
                                    if (ac.theItemStack.func_82833_r().contentEquals(cartItem.theItemStack.func_82833_r())) {
                                        ModSimReloaded.theCommodities.remove(ai);
                                        break;
                                    }
                                }
                            }

                            var10000 = ModSimReloaded.states;
                            var10000.credits -= cost;
                            String sim_gui_ATMs_worth = I18n.func_135052_a("container.sim.sim_gui_ATMs_worth");
                            ModSimReloaded.sendChat(sim_gui_ATMs_worth + ModSimReloaded.displayMoney(cost));
                            ModSim.proxy.getClientWorld().func_72980_b(this.thePlayer.field_70165_t, this.thePlayer.field_70163_u, this.thePlayer.field_70161_v, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                            this.field_146297_k.field_71462_r = null;
                            this.field_146297_k.func_71381_h();
                        }
                    }
                }

            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }

    @Override
    protected void func_73869_a(char c, int i) {
        try {
            if (i == 1) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage());
        }

    }
    @Override
    public boolean func_73868_f() {
        return false;
    }
    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }

}
