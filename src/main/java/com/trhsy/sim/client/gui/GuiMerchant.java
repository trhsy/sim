package com.trhsy.sim.client.gui;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.PricesForBlocks;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName GuiMerchant
 * @Description todo
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

        public boolean func_73868_f() {
                return false;
        }

        public void func_73876_c() {
        }

        public void func_73866_w_() {
                Keyboard.enableRepeatEvents(true);
                quantities.clear();
                sellLimits.clear();

                for(int i = 0; i < 9; ++i) {
                        quantities.add(0);
                        sellLimits.add(0);
                }

                this.showPage();
        }

        public void func_73863_a(int i, int j, float f) {
                if (this.mouseCount < 10) {
                        ++this.mouseCount;
                        Mouse.setGrabbed(false);
                }

                this.func_146276_q_();
                if (this.currentPage == 0) {
                        this.func_73732_a(this.field_146289_q, "Hello, how can I help you today?", this.field_146294_l / 2, 5, 16777215);
                } else if (this.currentPage == 1) {
                        this.func_73732_a(this.field_146289_q, "I've got some bargains for you...", this.field_146294_l / 2, 5, 16777215);
                        this.func_73731_b(this.field_146289_q, "Block pack", 2, 25, 16777120);
                        this.func_73731_b(this.field_146289_q, "Price per pack", 100, 25, 16777120);
                        this.func_73731_b(this.field_146289_q, "Quantity (packs of 64)", 200, 25, 16777120);
                        this.func_73731_b(this.field_146289_q, "Sub-total", 350, 25, 16777120);
                        String blockName = "";
                        String price = "";
                        Float fprice = 0.0F;
                        String subtotal = "";
                        float grandTotal = 0.0F;

                        for(int b = 0; b < 9; ++b) {
                                if (b == 0) {
                                        blockName = "Planks";
                                        fprice = PricesForBlocks.getPrice(Blocks.planks, true);
                                } else if (b == 1) {
                                        blockName = "Logs";
                                        fprice = PricesForBlocks.getPrice(Blocks.field_150364_r, true);
                                } else if (b == 2) {
                                        blockName = "Cobblestone";
                                        fprice = PricesForBlocks.getPrice(Blocks.cobblestone, true);
                                } else if (b == 3) {
                                        blockName = "Stone";
                                        fprice = PricesForBlocks.getPrice(Blocks.stone, true);
                                } else if (b == 4) {
                                        blockName = "Glass";
                                        fprice = PricesForBlocks.getPrice(Blocks.glass, true);
                                } else if (b == 5) {
                                        blockName = "Wool";
                                        fprice = PricesForBlocks.getPrice(Blocks.wool, true);
                                } else if (b == 6) {
                                        blockName = "Bricks";
                                        fprice = PricesForBlocks.getPrice(Blocks.brick_block, true);
                                } else if (b == 7) {
                                        blockName = "Stone Bricks";
                                        fprice = PricesForBlocks.getPrice(Blocks.stonebrick, true);
                                } else if (b == 8) {
                                        blockName = "Fence";
                                        fprice = PricesForBlocks.getPrice(Blocks.fence, true);
                                }

                                price = PricesForBlocks.formatPrice(fprice);
                                subtotal = PricesForBlocks.formatPrice((float)(Integer)quantities.get(b) * fprice);
                                grandTotal += (float)(Integer)quantities.get(b) * fprice;
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
                this.field_146292_n.add(new GuiButton(0, 2, 2, 50, 20, "Goodbye!"));
                if (this.currentPage == 0) {
                        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, 70, "I want to buy building materials"));
                        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, 90, "I want to sell building materials"));
                } else {
                        int b;
                        if (this.currentPage == 1) {
                                for(b = 0; b < 9; ++b) {
                                        this.field_146292_n.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                                        this.field_146292_n.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                                }

                                this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 100, this.field_146295_m - 20, 100, 20, "* Buy *"));
                        } else if (this.currentPage == 2) {
                                for(b = 0; b < 9; ++b) {
                                        this.field_146292_n.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                                        this.field_146292_n.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                                }

                                this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 100, this.field_146295_m - 20, 100, 20, "* Sell *"));
                        }
                }

        }

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
                                                        q = (Integer)quantities.get(guibutton.field_146127_k - 100);
                                                        if (q > 0) {
                                                                --q;
                                                                quantities.set(guibutton.field_146127_k - 100, q);
                                                        }
                                                } else if (guibutton.field_146127_k >= 200) {
                                                        q = (Integer)quantities.get(guibutton.field_146127_k - 200);
                                                        ++q;
                                                        quantities.set(guibutton.field_146127_k - 200, q);
                                                } else if (guibutton.field_146127_k == 2) {
                                                        if (ModSimukraft.states.credits < this.totalCost) {
                                                                ModSimukraft.sendChat("Merchant: 'Sorry, your card has been declined, you could try buying less.'");
                                                                this.field_146297_k.field_71462_r = null;
                                                                this.field_146297_k.func_71381_h();
                                                        } else {
                                                                this.buyStuff();
                                                        }
                                                }
                                        }
                                } else if (this.currentPage == 2) {
                                        if (guibutton.field_146127_k >= 100 && guibutton.field_146127_k < 200) {
                                                q = (Integer)quantities.get(guibutton.field_146127_k - 100);
                                                if (q > 0) {
                                                        --q;
                                                        quantities.set(guibutton.field_146127_k - 100, q);
                                                }
                                        } else if (guibutton.field_146127_k >= 200) {
                                                q = (Integer)quantities.get(guibutton.field_146127_k - 200);
                                                if (q < (Integer)sellLimits.get(guibutton.field_146127_k - 200)) {
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

        private void buyStuff() {
                ItemStack stack = null;
                int quant = false;
                Block block = null;
                boolean ok = false;
                Float stackPrice = 0.0F;
                ArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.field_146297_k.thePlayer.posX, this.field_146297_k.thePlayer.posY, this.field_146297_k.thePlayer.posZ, this.field_146297_k.thePlayer.field_71093_bK), 5);
                if (chests != null && chests.size() != 0) {
                        for(int i = 0; i < 9; ++i) {
                                int quant = (Integer)quantities.get(i);
                                if (quant > 0) {
                                        if (i == 0) {
                                                block = Blocks.planks;
                                        } else if (i == 1) {
                                                block = Blocks.field_150364_r;
                                        } else if (i == 2) {
                                                block = Blocks.cobblestone;
                                        } else if (i == 3) {
                                                block = Blocks.stone;
                                        } else if (i == 4) {
                                                block = Blocks.glass;
                                        } else if (i == 5) {
                                                block = Blocks.wool;
                                        } else if (i == 6) {
                                                block = Blocks.brick_block;
                                        } else if (i == 7) {
                                                block = Blocks.stonebrick;
                                        } else if (i == 8) {
                                                block = Blocks.fence;
                                        }

                                        for(int c = 1; c <= quant; ++c) {
                                                stack = new ItemStack(block, 64);
                                                this.placeIntoChest((IInventory)chests.get(0), stack, stack.func_77960_j(), 64);
                                                stackPrice = PricesForBlocks.getPrice(block, true);
                                                GameStates var10000 = ModSimukraft.states;
                                                var10000.credits -= stackPrice;
                                        }

                                        PricesForBlocks.adjustPrice(block, true);
                                }
                        }

                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                        this.field_146297_k.field_71441_e.playSound(this.field_146297_k.thePlayer.posX, this.field_146297_k.thePlayer.posY, this.field_146297_k.thePlayer.posZ, "satscapesimukraft:cash", 1.0F, 1.0F, false);
                        Thread t = new Thread(new Runnable() {
                                public void run() {
                                        try {
                                                Thread.sleep(3000L);
                                        } catch (Exception var2) {
                                        }

                                        GuiMerchant.this.field_146297_k.field_71441_e.playSound(GuiMerchant.this.field_146297_k.thePlayer.posX, GuiMerchant.this.field_146297_k.thePlayer.posY, GuiMerchant.this.field_146297_k.thePlayer.posZ, "satscapesimukraft:merchm", 1.0F, 1.0F, false);
                                }
                        });
                        t.start();
                } else {
                        ModSimukraft.sendChat("Merchant: Please place a chest down here, and I will place your items in there.");
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                }
        }

        private void sellStuff() {
                ItemStack stack = null;
                int quant = false;
                Block block = null;
                boolean ok = false;
                Float stackPrice = 0.0F;
                int stackCount = false;
                ArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.field_146297_k.thePlayer.posX, this.field_146297_k.thePlayer.posY, this.field_146297_k.thePlayer.posZ, this.field_146297_k.thePlayer.field_71093_bK), 5);
                if (chests == null | chests.size() == 0) {
                        ModSimukraft.sendChat("Merchant: Please place a chest down here, and place stacks of 64 blocks in there.");
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                } else {
                        float total = 0.0F;

                        for(int g = 0; g < ((IInventory)chests.get(0)).func_70302_i_(); ++g) {
                                ItemStack is = ((IInventory)chests.get(0)).func_70301_a(g);
                                if (is != null && is.field_77994_a == 64) {
                                        stackPrice = PricesForBlocks.getPrice(Block.func_149634_a(is.func_77973_b()), false);
                                        if (stackPrice > 0.0F) {
                                                GameStates var10000 = ModSimukraft.states;
                                                var10000.credits += stackPrice;
                                                PricesForBlocks.adjustPrice((Block)block, false);
                                                total += stackPrice;
                                                ((IInventory)chests.get(0)).func_70299_a(g, (ItemStack)null);
                                        }
                                }
                        }

                        if (total == 0.0F) {
                                ModSimukraft.sendChat("Merchant: There were no valid stacks I want to buy from you in the chest?!");
                        } else {
                                this.field_146297_k.field_71441_e.playSound(this.field_146297_k.thePlayer.posX, this.field_146297_k.thePlayer.posY, this.field_146297_k.thePlayer.posZ, "satscapesimukraft:cash", 1.0F, 1.0F, false);
                                ModSimukraft.sendChat("Sold all valid stacks for a total of " + ModSimukraft.displayMoney(total));
                        }

                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                }
        }

        public void func_146281_b() {
                Keyboard.enableRepeatEvents(false);
        }

        protected void func_73869_a(char c, int i) {
                if (i == 1) {
                        this.field_146297_k.field_71462_r = null;
                        this.field_146297_k.func_71381_h();
                }
        }

        protected void func_73864_a(int i, int j, int k) {
                super.func_73864_a(i, j, k);
        }

        public boolean placeIntoChest(IInventory chest, ItemStack stack, int idmeta, int quantity) {
                Minecraft mc = Minecraft.getMinecraft();
                Boolean placedOK = false;
                if (stack != null) {
                        return true;
                } else {
                        for(int q = 1; q <= quantity; ++q) {
                                for(int g = 0; g < chest.func_70302_i_(); ++g) {
                                        ItemStack is = chest.func_70301_a(g);
                                        if (is == null) {
                                                is = new ItemStack(stack.func_77973_b(), 1, idmeta);
                                                chest.func_70299_a(g, is);
                                                placedOK = true;
                                                break;
                                        }

                                        if (is == stack && is.func_77960_j() == idmeta && is.field_77994_a < 64) {
                                                ++is.field_77994_a;
                                                chest.func_70299_a(g, is);
                                                placedOK = true;
                                                break;
                                        }
                                }
                        }

                        return placedOK;
                }
        }
}

