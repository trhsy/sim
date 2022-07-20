package com.trhsy.sim.common.gui.folk;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.PricesForBlocks;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiMerchant
 * @Description todo 商人
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:31
 * ========================================
 **/
public class GuiMerchant extends GuiScreen {
    private int currentPage = 0;
    private static ArrayList<Integer> quantities = new ArrayList();
    private static ArrayList<Integer> sellLimits = new ArrayList();
    private Float totalCost = 0.0F;
    private int mouseCount = 0;

    public GuiMerchant() {
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public void func_73876_c() {
    }

    @Override
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        quantities.clear();
        sellLimits.clear();

        for (int i = 0; i < 9; ++i) {
            quantities.add(0);
            sellLimits.add(0);
        }

        this.showPage();
    }

    @Override
    public void func_73863_a(int i, int j, float f) {
        if (this.mouseCount < 10) {
            ++this.mouseCount;
            Mouse.setGrabbed(false);
        }

        this.func_146276_q_();
        if (this.currentPage == 0) {
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant0"), this.field_146294_l / 2, 5, 16777215);
        } else if (this.currentPage == 1) {
            this.func_73732_a(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant1"), this.field_146294_l / 2, 5, 16777215);
            this.func_73731_b(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant2"), 2, 25, 16777120);
            this.func_73731_b(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant3"), 100, 25, 16777120);
            this.func_73731_b(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant4"), 200, 25, 16777120);
            this.func_73731_b(this.field_146289_q, I18n.func_135052_a("container.sim.Merchant5"), 350, 25, 16777120);
            String blockName = "";
            String price = "";
            Float fprice = 0.0F;
            String subtotal = "";
            float grandTotal = 0.0F;

            for (int b = 0; b < 9; ++b) {
                if (b == 0) {
                    //木板
                    blockName = I18n.func_135052_a("container.sim.Merchant16");
                    fprice = PricesForBlocks.getPrice(Blocks.field_150344_f, true);
                } else if (b == 1) {
                    //木材
                    blockName = I18n.func_135052_a("container.sim.Merchant17");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150364_r, true);
                } else if (b == 2) {
                    //圆石
                    blockName = I18n.func_135052_a("container.sim.Merchant18");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150347_e, true);
                } else if (b == 3) {
                    //石头
                    blockName = I18n.func_135052_a("container.sim.Merchant19");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150348_b, true);
                } else if (b == 4) {
                    //玻璃
                    blockName = I18n.func_135052_a("container.sim.Merchant20");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150359_w, true);
                } else if (b == 5) {
                    //羊毛
                    blockName = I18n.func_135052_a("container.sim.Merchant21");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150325_L, true);
                } else if (b == 6) {
                    //板砖
                    blockName = I18n.func_135052_a("container.sim.Merchant22");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150336_V, true);
                } else if (b == 7) {
                    //石砖
                    blockName = I18n.func_135052_a("container.sim.Merchant23");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_150417_aV, true);
                } else if (b == 8) {
                    //栏栅
                    blockName = I18n.func_135052_a("container.sim.Merchant24");;
                    fprice = PricesForBlocks.getPrice(Blocks.field_180407_aO, true);
                }

                price = PricesForBlocks.formatPrice(fprice);
                subtotal = PricesForBlocks.formatPrice((float) (Integer) quantities.get(b) * fprice);
                grandTotal += (float) (Integer) quantities.get(b) * fprice;
                this.func_73731_b(this.field_146289_q, blockName, 2, 40 + b * 20, 16777215);
                this.func_73731_b(this.field_146289_q, price, 100, 40 + b * 20, 16777215);
                this.func_73731_b(this.field_146289_q, quantities.get(b) + "", 200, 40 + b * 20, 16777215);
                this.func_73731_b(this.field_146289_q, subtotal, 350, 40 + b * 20, 16777215);
            }

            this.func_73731_b(this.field_146289_q, "Total: " + PricesForBlocks.formatPrice(grandTotal), 2, this.field_146295_m - 15, 15794175);
            this.totalCost = grandTotal;
        } else if (this.currentPage == 2) {
        }

        super.func_73863_a(i, j, f);
    }

    private void showPage() {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, 2, 2, 50, 20, I18n.func_135052_a("container.sim.Merchant6")));
        if (this.currentPage == 0) {
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 70, I18n.func_135052_a("container.sim.Merchant7")));
            this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 90, I18n.func_135052_a("container.sim.Merchant8")));
        } else {
            int b;
            if (this.currentPage == 1) {
                for (b = 0; b < 9; ++b) {
                    this.field_146292_n.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                    this.field_146292_n.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                }

                this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 100, this.field_146295_m - 20, 100, 20, I18n.func_135052_a("container.sim.Merchant9")));
            } else if (this.currentPage == 2) {
                for (b = 0; b < 9; ++b) {
                    this.field_146292_n.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                    this.field_146292_n.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                }

                this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 100, this.field_146295_m - 20, 100, 20, I18n.func_135052_a("container.sim.Merchant10")));
            }
        }

    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {
        if (guibutton.field_146124_l) {
            if (guibutton.field_146127_k == 0) {
                this.field_146297_k.field_71462_r = null;
                this.field_146297_k.func_71381_h();
            }

            if (this.currentPage == 0) {
                if (guibutton.field_146127_k == 1) {
                    this.currentPage = 1;
                } else if (guibutton.field_146127_k == 2) {
                    this.sellStuff();
                }

                this.showPage();
            } else {
                int q;
                if (this.currentPage == 1) {
                    if (guibutton.field_146127_k != 1) {
                        if (guibutton.field_146127_k >= 100 && guibutton.field_146127_k < 200) {
                            q = (Integer) quantities.get(guibutton.field_146127_k - 100);
                            if (q > 0) {
                                --q;
                                quantities.set(guibutton.field_146127_k - 100, q);
                            }
                        } else if (guibutton.field_146127_k >= 200) {
                            q = (Integer) quantities.get(guibutton.field_146127_k - 200);
                            ++q;
                            quantities.set(guibutton.field_146127_k - 200, q);
                        } else if (guibutton.field_146127_k == 2) {
                            if (ModSimReloaded.states.credits < this.totalCost) {
                                ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Merchant11"));
                                this.field_146297_k.field_71462_r = null;
                                this.field_146297_k.func_71381_h();
                            } else {
                                this.buyStuff();
                            }
                        }
                    }
                } else if (this.currentPage == 2) {
                    if (guibutton.field_146127_k >= 100 && guibutton.field_146127_k < 200) {
                        q = (Integer) quantities.get(guibutton.field_146127_k - 100);
                        if (q > 0) {
                            --q;
                            quantities.set(guibutton.field_146127_k - 100, q);
                        }
                    } else if (guibutton.field_146127_k >= 200) {
                        q = (Integer) quantities.get(guibutton.field_146127_k - 200);
                        if (q < (Integer) sellLimits.get(guibutton.field_146127_k - 200)) {
                            ++q;
                            quantities.set(guibutton.field_146127_k - 200, q);
                        }
                    } else if (guibutton.field_146127_k == 2) {
                        this.sellStuff();
                    }
                }
            }

        }
    }

    /**
     * 买东西
     */
    private void buyStuff() {
        ModSimReloaded.log.info("准备买东西");
        ItemStack stack = null;
        //int quant = false;
        Block block = null;
        boolean ok = false;
        Float stackPrice = 0.0F;
        ArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.field_146297_k.field_71439_g.field_70165_t, this.field_146297_k.field_71439_g.field_70163_u, this.field_146297_k.field_71439_g.field_70161_v, this.field_146297_k.field_71439_g.field_71093_bK), 5);
        if (chests != null && chests.size() != 0) {
            for (int i = 0; i < 9; ++i) {
                int quant = (Integer) quantities.get(i);
                ModSimReloaded.log.info(String.valueOf(quant));
                if (quant > 0) {
                    if (i == 0) {
                        block = Blocks.field_150344_f;
                    } else if (i == 1) {
                        block = Blocks.field_150364_r;
                    } else if (i == 2) {
                        block = Blocks.field_150347_e;
                    } else if (i == 3) {
                        block = Blocks.field_150348_b;
                    } else if (i == 4) {
                        block = Blocks.field_150359_w;
                    } else if (i == 5) {
                        block = Blocks.field_150325_L;
                    } else if (i == 6) {
                        block = Blocks.field_150336_V;
                    } else if (i == 7) {
                        block = Blocks.field_150417_aV;
                    } else if (i == 8) {
                        block = Blocks.field_180407_aO;
                    }

                    for (int c = 1; c <= quant; ++c) {
                        stack = new ItemStack(block, 64);
                        this.placeIntoChest((IInventory) chests.get(0), stack, stack.func_77960_j(), 64);
                        stackPrice = PricesForBlocks.getPrice(block, true);
                        GameStates var10000 = ModSimReloaded.states;
                        var10000.credits -= stackPrice;
                    }

                    PricesForBlocks.adjustPrice(block, true);
                }
            }

            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
            this.field_146297_k.field_71441_e.func_72980_b(this.field_146297_k.field_71439_g.field_70165_t, this.field_146297_k.field_71439_g.field_70163_u, this.field_146297_k.field_71439_g.field_70161_v, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000L);
                    } catch (Exception var2) {
                    }

                    GuiMerchant.this.field_146297_k.field_71441_e.func_72980_b(GuiMerchant.this.field_146297_k.field_71439_g.field_70165_t, GuiMerchant.this.field_146297_k.field_71439_g.field_70163_u, GuiMerchant.this.field_146297_k.field_71439_g.field_70161_v, ModSim.MODID + ":merchm", 1.0F, 1.0F, false);
                }
            });
            t.start();
        } else {
            ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Merchant12"));
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    private void sellStuff() {
        ItemStack stack = null;
        //int quant = false;
        Block block = null;
        boolean ok = false;
        Float stackPrice = 0.0F;
        //int stackCount = false;
        ArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.field_146297_k.field_71439_g.field_70165_t, this.field_146297_k.field_71439_g.field_70163_u, this.field_146297_k.field_71439_g.field_70161_v, this.field_146297_k.field_71439_g.field_71093_bK), 5);
        if (chests == null | chests.size() == 0) {
            ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Merchant13"));
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        } else {
            float total = 0.0F;

            for (int g = 0; g < ((IInventory) chests.get(0)).func_70302_i_(); ++g) {
                ItemStack is = ((IInventory) chests.get(0)).func_70301_a(g);
                if (is != null && is.field_77994_a >= 1) {
                    stackPrice = PricesForBlocks.getPrice(Block.func_149634_a(is.func_77973_b()), false);
                    if (stackPrice > 0.0F) {
                        GameStates var10000 = ModSimReloaded.states;
                        var10000.credits += stackPrice;
                        PricesForBlocks.adjustPrice((Block) block, false);
                        total += stackPrice;
                        ((IInventory) chests.get(0)).func_70299_a(g, (ItemStack) null);
                    }
                }
            }

            if (total == 0.0F) {
                ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Merchant14"));
            } else {
                this.field_146297_k.field_71441_e.func_72980_b(this.field_146297_k.field_71439_g.field_70165_t, this.field_146297_k.field_71439_g.field_70163_u, this.field_146297_k.field_71439_g.field_70161_v, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.Merchant15") + ModSimReloaded.displayMoney(total));
            }

            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    @Override
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void func_73869_a(char c, int i) {
        if (i == 1) {
            this.field_146297_k.field_71462_r = null;
            this.field_146297_k.func_71381_h();
        }
    }

    @Override
    protected void func_73864_a(int i, int j, int k) {
        try {
            super.func_73864_a(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 放到箱子里
     * @param chest
     * @param stack
     * @param idmeta
     * @param quantity
     * @return
     */
    public boolean placeIntoChest(IInventory chest, ItemStack stack, int idmeta, int quantity) {
        Minecraft mc = Minecraft.func_71410_x();
        Boolean placedOK = false;
        if (stack == null) {
            placedOK=true;
        } else {
            //for (int q = 1; q <= quantity; ++q) {
                for (int i = 0; i < chest.func_70302_i_(); ++i) {
                    ItemStack is = chest.func_70301_a(i);

                    if (is == null) {
                        is = new ItemStack(stack.func_77973_b(), 64, idmeta);
                        chest.func_70299_a(i, is);
                        placedOK = true;
                        break;
                    }

                    if (is == stack && is.func_77960_j() == idmeta && is.field_77994_a < 64) {
                        ++is.field_77994_a;
                        chest.func_70299_a(1, is);
                        placedOK = true;
                        break;
                    }
                }
            //}


        }
        return placedOK;
    }
}

