package com.trhsy.sim.gui.npc;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.util.PricesForBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName GuiMerchant
 * @Description todo 建筑商
 * @Author TRHSY
 * @Date 2023/7/323:02
 **/
public class GuiMerchant extends GuiScreen {
    private int currentPage = 0;
    //持有购买数量
    private static List<Integer> quantities = new CopyOnWriteArrayList<Integer>();
    //基于玩家库存的销售限制
    private static List<Integer> sellLimits = new CopyOnWriteArrayList<Integer>();
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
            this.quantities.clear();
            sellLimits.clear();
            for (int i = 0; i < 9; i++) {
                this.quantities.add(0);
                sellLimits.add(0);
            }
            this.showPage();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiMerchant-initGui出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                        fprice = PricesForBlocks.getPrice(Blocks.PLANKS, true);
                    } else if (b == 1) {
                        //木材
                        blockName = I18n.format("container.sim.Merchant17");
                        fprice = PricesForBlocks.getPrice(Blocks.LOG, true);
                    } else if (b == 2) {
                        //圆石
                        blockName = I18n.format("container.sim.Merchant18");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.COBBLESTONE, true);
                    } else if (b == 3) {
                        //石头
                        blockName = I18n.format("container.sim.Merchant19");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.STONE, true);
                    } else if (b == 4) {
                        //玻璃
                        blockName = I18n.format("container.sim.Merchant20");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.GLASS, true);
                    } else if (b == 5) {
                        //羊毛
                        blockName = I18n.format("container.sim.Merchant21");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.WOOL, true);
                    } else if (b == 6) {
                        //板砖
                        blockName = I18n.format("container.sim.Merchant22");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.BRICK_BLOCK, true);
                    } else if (b == 7) {
                        //石砖
                        blockName = I18n.format("container.sim.Merchant23");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.STONEBRICK, true);
                    } else if (b == 8) {
                        //栏栅
                        blockName = I18n.format("container.sim.Merchant24");
                        ;
                        fprice = PricesForBlocks.getPrice(Blocks.OAK_FENCE, true);
                    }

                    price = PricesForBlocks.formatPrice(fprice);
                    subtotal = PricesForBlocks.formatPrice((float) this.quantities.get(b) * fprice);
                    grandTotal += (float) this.quantities.get(b) * fprice;
                    this.drawString(this.fontRendererObj, blockName, 2, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, price, 100, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, this.quantities.get(b) + "", 200, 40 + b * 20, 16777215);
                    this.drawString(this.fontRendererObj, subtotal, 350, 40 + b * 20, 16777215);
                }

                this.drawString(this.fontRendererObj, "Total: " + PricesForBlocks.formatPrice(grandTotal), 2, this.height - 15, 15794175);
                this.totalCost = grandTotal;
            } else if (this.currentPage == 2) {
            }

            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiMerchant-drawScreen出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("showPage出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                            int q = this.quantities.get(guibutton.id - 100);
                            if (q > 0) {
                                q--;
                                this.quantities.set(guibutton.id - 100, q);
                            }
                        } else if (guibutton.id >= 200) {
                            int q = this.quantities.get(guibutton.id - 200);
                            q++;
                            this.quantities.set(guibutton.id - 200, q);
                        } else if (guibutton.id == 2) {
                            if (ModSimLoader.money < this.totalCost) {
                                //抱歉，您的卡已被拒绝，您可以尝试减少购买。
                                ModSimLoader.sendChat(I18n.format("container.sim.Merchant11"));
                                this.mc.currentScreen = null;
                                this.mc.setIngameFocus();
                            } else {
                                this.buyStuff();
                            }
                        }
                    }
                } else if (this.currentPage == 2) {
                    if (guibutton.id >= 100 && guibutton.id < 200) {
                        int q = this.quantities.get(guibutton.id - 100);
                        if (q > 0) {
                            q--;
                            this.quantities.set(guibutton.id - 100, q);
                        }
                    } else if (guibutton.id >= 200) {
                        //阻止玩家卖出比他们拥有的更多的东西
                        int q = this.quantities.get(guibutton.id - 200);
                        if (q < sellLimits.get(guibutton.id - 200)) {
                            q++;
                            this.quantities.set(guibutton.id - 200, q);
                        }
                    } else if (guibutton.id == 2) {
                        this.sellStuff();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GUIMERCHANT-actionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    /**
     * 买东西
     * 购买当前显示在购买页面上的东西
     */
    private void buyStuff() {
        try {
            ModSimLoader.log.info("准备买东西");
            ItemStack stack = null;
            int quant = 0;
            Block block = null;
            boolean ok = false;
            Float stackPrice = 0.0F;
            //找到箱子
            List<IInventory> chests = Job.inventoriesFindClosest(new V3(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.dimension), 5);
            if (chests != null && chests.size() != 0) {
                for (int i = 0; i < 9; i++) {
                    quant = this.quantities.get(i);
                    //ModSimLoader.log.info(String.valueOf(quant));
                    if (quant > 0) {
                        if (i == 0) {
                            block = Blocks.PLANKS;
                        } else if (i == 1) {
                            block = Blocks.LOG;
                        } else if (i == 2) {
                            block = Blocks.COBBLESTONE;
                        } else if (i == 3) {
                            block = Blocks.STONE;
                        } else if (i == 4) {
                            block = Blocks.GLASS;
                        } else if (i == 5) {
                            block = Blocks.WOOL;
                        } else if (i == 6) {
                            block = Blocks.BRICK_BLOCK;
                        } else if (i == 7) {
                            block = Blocks.STONEBRICK;
                        } else if (i == 8) {
                            block = Blocks.OAK_FENCE;
                        }

                        for (int c = 1; c <= quant; c++) {
                            stack = new ItemStack(block, 64);
                            this.placeIntoChest(chests.get(0), stack, stack.getMetadata(), 64);
                            stackPrice = PricesForBlocks.getPrice(block, true);
                            //64 * 基本价格 + 25% 加价
                            ModSimLoader.money -= stackPrice;
                        }

                        PricesForBlocks.adjustPrice(block, true);
                    }
                }

                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, soundEvent,SoundCategory.BLOCKS, 1, 1, false);
//                this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1, 1, false);
                ThreadPoolExecutor threadPoolExecutor = ModSimLoader.threadPoolExecutor;
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000L);
                        } catch (Exception e) {
                        }
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":merchm"));
                        GuiMerchant.this.mc.theWorld.playSound(GuiMerchant.this.mc.thePlayer.posX, GuiMerchant.this.mc.thePlayer.posY, GuiMerchant.this.mc.thePlayer.posZ, soundEvent,SoundCategory.BLOCKS, 1, 1, false);
                    }
                });
                //threadPoolExecutor.shutdown();
            } else {
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant12"));
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                return;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("buyStuff出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
            List<IInventory> chests = Job.inventoriesFindClosest(new V3(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.dimension), 5);
            if (chests == null | chests.size() == 0) {
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant13"));
                this.mc.currentScreen = null;
                this.mc.setIngameFocus();
                return;
            }
            float total = 0.0F;

            for (int g = 0; g < chests.get(0).getSizeInventory(); g++) {
                ItemStack is = chests.get(0).getStackInSlot(g);
                if (is != null ) {
                    if(is.stackSize == 64){
                        stackPrice = PricesForBlocks.getPrice(Block.getBlockFromItem(is.getItem()), false);
                        if (stackPrice > 0.0F) {
                            //64 * 基本价格
                            ModSimLoader.money += stackPrice;
                            PricesForBlocks.adjustPrice((Block) block, false);
                            total += stackPrice;
                            (chests.get(0)).setInventorySlotContents(g, (ItemStack) null);
                        }
                    }
                }
            }

            if (total == 0.0F) {
                //箱子里没有我想从你那里买的有效堆栈？
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant14"));
            } else {
                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cash"));
                this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, soundEvent,SoundCategory.BLOCKS, 1, 1, false);
                ModSimLoader.sendChat(I18n.format("container.sim.Merchant15") + ModSimLoader.displayMoney(total));
            }

            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("sellStuff出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    @Override
    public void onGuiClosed() {
        try {
            Keyboard.enableRepeatEvents(false);
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiMerchant-onGuiClosed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("keyTyped出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("mouseClicked鼠标点击出错：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                return placedOK;
            }
            for (int q = 1; q <= quantity; q++) {
                for (int g = 0; g < chest.getSizeInventory(); g++) {
                    ItemStack is = chest.getStackInSlot(g);

                    if (is == null) {
                        is = new ItemStack(stack.getItem(), 1, idmeta);
                        chest.setInventorySlotContents(g, is);
                        placedOK = true;
                        //重新进入数量循环
                        break;
                    } else if (is.getItem() == stack.getItem() && is.getMetadata() == idmeta && is.stackSize < 64) {
                        is.stackSize++;
                        chest.setInventorySlotContents(g, is);
                        placedOK = true;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("placeIntoChest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return placedOK;
    }
}
