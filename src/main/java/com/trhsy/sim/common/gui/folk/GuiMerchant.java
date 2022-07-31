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
import java.util.concurrent.CopyOnWriteArrayList;

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
    //持有购买数量
    private static CopyOnWriteArrayList<Integer> quantities = new CopyOnWriteArrayList<Integer>();
    //基于玩家库存的销售限制
    private static CopyOnWriteArrayList<Integer> sellLimits = new CopyOnWriteArrayList<Integer>();
    private Float totalCost = 0.0F;
    private int mouseCount = 0;

    public GuiMerchant() {
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
        try {
            Keyboard.enableRepeatEvents(true);
            quantities.clear();
            sellLimits.clear();
            for (int i = 0; i < 9; i++) {
                quantities.add(0);
                sellLimits.add(0);
            }
            this.showPage();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                this.mouseCount++;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            if (this.currentPage == 0) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Merchant0"), this.width / 2, 5, 16777215);
            } else if (this.currentPage == 1) {
                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.Merchant1"), this.width / 2, 5, 16777215);
                this.drawString(this.fontRendererObj, I18n.format("container.sim.Merchant2"), 2, 25, 16777120);
                this.drawString(this.fontRendererObj, I18n.format("container.sim.Merchant3"), 100, 25, 16777120);
                this.drawString(this.fontRendererObj, I18n.format("container.sim.Merchant4"), 200, 25, 16777120);
                this.drawString(this.fontRendererObj, I18n.format("container.sim.Merchant5"), 350, 25, 16777120);
                String blockName = "";
                String price = "";
                Float fprice = 0.0F;
                String subtotal = "";
                float grandTotal = 0.0F;

                for (int b = 0; b < 9; ++b) {
                    if (b == 0) {
                        //木板
                        blockName = I18n.format("container.sim.Merchant16");
                        fprice = PricesForBlocks.getPrice(Blocks.planks, true);
                    } else if (b == 1) {
                        //木材
                        blockName = I18n.format("container.sim.Merchant17");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.log, true);
                    } else if (b == 2) {
                        //圆石
                        blockName = I18n.format("container.sim.Merchant18");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.cobblestone, true);
                    } else if (b == 3) {
                        //石头
                        blockName = I18n.format("container.sim.Merchant19");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.stone, true);
                    } else if (b == 4) {
                        //玻璃
                        blockName = I18n.format("container.sim.Merchant20");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.glass, true);
                    } else if (b == 5) {
                        //羊毛
                        blockName = I18n.format("container.sim.Merchant21");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.wool, true);
                    } else if (b == 6) {
                        //板砖
                        blockName = I18n.format("container.sim.Merchant22");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.brick_block, true);
                    } else if (b == 7) {
                        //石砖
                        blockName = I18n.format("container.sim.Merchant23");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.stonebrick, true);
                    } else if (b == 8) {
                        //栏栅
                        blockName = I18n.format("container.sim.Merchant24");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.oak_fence, true);
                    }

                    price = PricesForBlocks.formatPrice(fprice);
                    subtotal = PricesForBlocks.formatPrice((float) (Integer) quantities.get(b) * fprice);
                    grandTotal += (float) (Integer) quantities.get(b) * fprice;
                    this.drawString(this.fontRendererObj, blockName, 2, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, price, 100, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, quantities.get(b) + "", 200, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, subtotal, 350, 40 + b * 20, 16777215);
                }

                this.drawString(this.fontRendererObj, "Total: " + PricesForBlocks.formatPrice(grandTotal), 2, this.height - 15, 15794175);
                this.totalCost = grandTotal;
            } else if (this.currentPage == 2) {
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void showPage() {
        try {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, 2, 2, 50, 20, I18n.format("container.sim.sim_gui_BC_Go_Back")));
            if (this.currentPage == 0) {
                //主要的
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 70, I18n.format("container.sim.Merchant7")));
                this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 90, I18n.format("container.sim.Merchant8")));
            } else if (this.currentPage == 1) {
                //买
                for (int b = 0; b < 9; ++b) {
                    this.buttonList.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                    this.buttonList.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                }

                this.buttonList.add(new GuiButton(2, this.width - 100, this.height - 20, 100, 20, I18n.format("container.sim.Merchant9")));
            } else if (this.currentPage == 2) {
                //卖
                for (int b = 0; b < 9; ++b) {
                    this.buttonList.add(new GuiButton(100 + b, 250, 35 + b * 20, 20, 20, "<"));
                    this.buttonList.add(new GuiButton(200 + b, 270, 35 + b * 20, 20, 20, ">"));
                }

                this.buttonList.add(new GuiButton(2, this.width - 100, this.height - 20, 100, 20, I18n.format("container.sim.Merchant10")));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("showPage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                }

                if (this.currentPage == 0) {
                    //买分页
                    if (guibutton.id == 1) {
                        this.currentPage = 1;
                    } else if (guibutton.id == 2) {
                        this.sellStuff();
                    }

                    this.showPage();
                } else if (this.currentPage == 1) {
                    if (guibutton.id != 1) {
                        //数量较少
                        if (guibutton.id >= 100 && guibutton.id < 200) {
                            int q = (Integer) quantities.get(guibutton.id - 100);
                            if (q > 0) {
                                q--;
                                quantities.set(guibutton.id - 100, q);
                            }
                        } else if (guibutton.id >= 200) {
                            int q = (Integer) quantities.get(guibutton.id - 200);
                            q++;
                            quantities.set(guibutton.id - 200, q);
                        } else if (guibutton.id == 2) {
                            if (ModSimReloaded.states.credits < this.totalCost) {
                                //抱歉，您的卡已被拒绝，您可以尝试减少购买。
                                ModSimReloaded.sendChat(I18n.format("container.sim.Merchant11"));
                                this.mc.currentScreen = null;
                                this.mc.setIngameFocus();
                            } else {
                                this.buyStuff();
                            }
                        }
                    }
                } else if (this.currentPage == 2) {
                    if (guibutton.id >= 100 && guibutton.id < 200) {
                        int q = (Integer) quantities.get(guibutton.id - 100);
                        if (q > 0) {
                            q--;
                            quantities.set(guibutton.id - 100, q);
                        }
                    } else if (guibutton.id >= 200) {
                        //阻止玩家卖出比他们拥有的更多的东西
                        int q = (Integer) quantities.get(guibutton.id - 200);
                        if (q < (Integer) sellLimits.get(guibutton.id - 200)) {
                            q++;
                            quantities.set(guibutton.id - 200, q);
                        }
                    } else if (guibutton.id == 2) {
                        this.sellStuff();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("GUIMERCHANT-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }



    }

    /**
     * 买东西
     * 购买当前显示在购买页面上的东西
     */
    private void buyStuff() {
        try {
            ModSimReloaded.log.info("准备买东西");
            ItemStack stack = null;
            int quant = 0;
            Block block = null;
            boolean ok = false;
            Float stackPrice = 0.0F;
            CopyOnWriteArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.dimension), 5);
            if (chests != null && chests.size() != 0) {
                for (int i = 0; i < 9; i++) {
                    quant = (Integer) quantities.get(i);
                    ModSimReloaded.log.info(String.valueOf(quant));
                    if (quant > 0) {
                        if (i == 0) {
                            block = Blocks.planks;
                        } else if (i == 1) {
                            block = Blocks.log;
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
                            block = Blocks.oak_fence;
                        }

                        for (int c = 1; c <= quant; c++) {
                            stack = new ItemStack(block, 64);
                            this.placeIntoChest((IInventory) chests.get(0), stack, stack.getMetadata(), 64);
                            stackPrice = PricesForBlocks.getPrice(block, true);
                            GameStates var10000 = ModSimReloaded.states;
                            //64 * 基本价格 + 25% 加价
                            var10000.credits -= stackPrice;
                        }

                        PricesForBlocks.adjustPrice(block, true);
                    }
                }

                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000L);
                        } catch (Exception e) {
                        }

                        GuiMerchant.this.mc.theWorld.playSound(GuiMerchant.this.mc.thePlayer.posX, GuiMerchant.this.mc.thePlayer.posY, GuiMerchant.this.mc.thePlayer.posZ, ModSim.MODID + ":merchm", 1.0F, 1.0F, false);
                    }
                });
                t.start();
            } else {
                ModSimReloaded.sendChat(I18n.format("container.sim.Merchant12"));
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                return;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("buyStuff出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 将选定的玩家库存出售给商家
     */
    private void sellStuff() {
        try {
            ItemStack stack = null;
            int quant = 0;
            Block block = null;
            boolean ok = false;
            Float stackPrice = 0.0F;
            int stackCount = 0;
            CopyOnWriteArrayList<IInventory> chests = Job.inventoriesFindClosest(new V3(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.dimension), 5);
            if (chests == null | chests.size() == 0) {
                ModSimReloaded.sendChat(I18n.format("container.sim.Merchant13"));
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                return;
            } else {
                float total = 0.0F;

                for (int g = 0; g < ((IInventory) chests.get(0)).getSizeInventory(); g++) {
                    ItemStack is = ((IInventory) chests.get(0)).getStackInSlot(g);
                    if (is != null && is.stackSize >= 1) {
                        stackPrice = PricesForBlocks.getPrice(Block.getBlockFromItem(is.getItem()), false);
                        if (stackPrice > 0.0F) {
                            //64 * 基本价格
                            ModSimReloaded.states.credits += stackPrice;
                            PricesForBlocks.adjustPrice((Block) block, false);
                            total += stackPrice;
                            ((IInventory) chests.get(0)).setInventorySlotContents(g, (ItemStack) null);
                        }
                    }
                }

                if (total == 0.0F) {
                    //箱子里没有我想从你那里买的有效堆栈？
                    ModSimReloaded.sendChat(I18n.format("container.sim.Merchant14"));
                } else {
                    this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                    ModSimReloaded.sendChat(I18n.format("container.sim.Merchant15") + ModSimReloaded.displayMoney(total));
                }

                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("sellStuff出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void onGuiClosed() {
        try {
            Keyboard.enableRepeatEvents(false);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onGuiClosed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    protected void keyTyped(char c, int i) {
        try {
            if (i == 1) {
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("mouseClicked鼠标点击出错：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 放到箱子里
     *
     * @param chest
     * @param stack
     * @param idmeta
     * @param quantity
     * @return
     */
    public boolean placeIntoChest(IInventory chest, ItemStack stack, int idmeta, int quantity) {
        //Minecraft mc = Minecraft.getMinecraft();
        Boolean placedOK = false;
        try {
            if (stack == null) {
                placedOK = true;
            } else {
                for (int q = 1; q <= quantity; q++) {
                    for (int i = 0; i < chest.getSizeInventory(); i++) {
                        ItemStack is = chest.getStackInSlot(i);

                        if (is == null) {
                            is = new ItemStack(stack.getItem(), 64, idmeta);
                            chest.setInventorySlotContents(i, is);
                            placedOK = true;
                            //重新进入数量循环
                            break;
                        }

                        if (is == stack && is.getMetadata() == idmeta && is.stackSize < 64) {
                            ++is.stackSize;
                            chest.setInventorySlotContents(1, is);
                            placedOK = true;
                            break;
                        }
                    }
                }


            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("placeIntoChest出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return placedOK;
    }
}

