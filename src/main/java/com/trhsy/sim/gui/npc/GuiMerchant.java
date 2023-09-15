package com.trhsy.sim.gui.npc;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenFolkGui;
import com.trhsy.sim.network.client.PacketOpenMerchantGui;
import com.trhsy.sim.network.server.PacketBuyStuff;
import com.trhsy.sim.network.server.PacketSellStuff;
import com.trhsy.sim.network.server.PacketrotateStairs;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.util.PricesForBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
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
    private static List<Integer> quantities = new CopyOnWriteArrayList<>();
    //基于玩家库存的销售限制
    private static List<Integer> sellLimits = new CopyOnWriteArrayList<Integer>();
    private Float totalCost = 0.0F;
    private int mouseCount = 0;
    private UUID id;
    public GuiMerchant(PacketOpenMerchantGui message) {
        this.id=message.id;
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
//                        this.sellStuff();
                        NetWorkLoader.net.sendToServer(new PacketSellStuff(this.id));
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
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
//                                this.buyStuff();
                                NetWorkLoader.net.sendToServer(new PacketBuyStuff(this.id,quantities));
                                this.mc.currentScreen = null;
                                this.mc.setIngameFocus();
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
//                        this.sellStuff();
                        NetWorkLoader.net.sendToServer(new PacketSellStuff(this.id));
                        this.mc.currentScreen = null;
                        this.mc.setIngameFocus();
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GUIMERCHANT-actionPerformed出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                    } else if (is.getItem() == stack.getItem() && is.getMetadata() == idmeta && is.getCount() < 64) {
                        is.grow(1);;
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

    public List<IInventory> inventoriesFindClosests(V3 startXYZ, int searchDistance){
        List<IInventory> ret = new CopyOnWriteArrayList();

        try {
            World world = this.mc.world;
//            IBlockState blocks =world.getBlockState(startXYZ.toBlockPos());

            TileEntity te = world.getTileEntity(startXYZ.toBlockPos());
            if (te != null && te instanceof IInventory && !(te instanceof TileEntityFurnace)) {
                ret.add((IInventory)te);
            }

            for(int d = 1; d < searchDistance; ++d) {
                for(int yo = -d; yo <= d; ++yo) {
                    for(int xo = -d; xo <= d; ++xo) {
                        for(int zo = -d; zo <= d; ++zo) {
                            int sx = (int)(Math.round(startXYZ.x) + (long)xo);
                            int sy = (int)(Math.round(startXYZ.y) + (long)yo);
                            int sz = (int)(Math.round(startXYZ.z) + (long)zo);
                            te = world.getTileEntity(new BlockPos(sx, sy, sz));
                            if (te != null && te instanceof IInventory && !this.alreadyGotChest(ret, (IInventory)te)) {
                                ret.add((IInventory)te);
                            }
                        }
                    }
                }
            }

            return ret;
        } catch (Exception var13) {
            return ret;
        }
    }
    /**
     * 已经获得箱子
     * @param chests
     * @param chest
     * @return
     */
    private boolean alreadyGotChest(List<IInventory> chests, IInventory chest) {
        boolean ret = false;
        Iterator var4 = chests.iterator();

        while(var4.hasNext()) {
            IInventory ch = (IInventory)var4.next();
            if (ch.toString().contentEquals(chest.toString())) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
