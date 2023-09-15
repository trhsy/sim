package com.trhsy.sim.gui.block;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.network.client.PacketOpenPathBoxGui;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.util.Courier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui.block
 * @ClassName: GuiPathBox
 * @Description:
 * @date 2023/08/10 上午 9:40
 */
public class GuiPathBox extends GuiScreen {
    public V3 location;
    GuiTextField theGuiTextField1;
    public String errorText = "";
    private int mouseCount = 0;
    public GuiPathBox(PacketOpenPathBoxGui message) {
        this.location = message.v3;
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        try {
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.updateCursorCounter();
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("updateScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public void initGui() {
        try {
            this.buttonList.clear();
            //完成
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, I18n.format("container.sim.sim_gui_BC_Done")));
            GuiButton b=new GuiButton(2, this.width / 2 - 100, 160, I18n.format("container.sim.sim_gui_Set_new"));
            //置新的快递点/传送点
            this.buttonList.add(b);
            this.theGuiTextField1 = new GuiTextField(0,this.fontRendererObj, this.width / 2 - this.width / 3 / 2, 138, this.width / 3, 20);
            this.theGuiTextField1.setMaxStringLength(23);


        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GuiPathBox-initGui出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public void drawScreen(int i, int j, float f) {
        try {
            if (this.mouseCount < 10) {
                ++this.mouseCount;
                Mouse.setGrabbed(false);
            }

            this.drawDefaultBackground();
            //路径箱
            this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox4"), this.width / 2, 17, 16777215);

            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.drawTextBox();
            }
//            try {
//                if (this.thePathBox.marker1XYZ == null) {
//                    this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox5"), this.width / 2, 27, 16711680);
//                }
//            } catch (Exception e) {
//                this.drawCenteredString(this.fontRendererObj, I18n.format("container.sim.PathBox6"), this.width / 2, 27, 16711680);
//            }
            this.drawCenteredString(this.fontRendererObj, this.errorText, this.width / 2, this.height - 50, 16711680);
            super.drawScreen(i, j, f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GuiPathBox-drawScreen出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //var6.printStackTrace();
        }

    }
    @Override
    public void actionPerformed(GuiButton guibutton) {
        try {
            if (guibutton.enabled) {
                if (guibutton.id == 0) {
                    this.mc.currentScreen = null;
                    this.mc.setIngameFocus();
                } else {
                    //设置新的快递点/传送点
                    if (guibutton.displayString.contentEquals(I18n.format("container.sim.sim_gui_Set_new"))) {
                        String name = this.theGuiTextField1.getText().trim();
                        if (name.length() == 0) {
                            //请输入此快递点/发送点的名称
                            this.errorText =I18n.format("container.sim.Markers5");
                            this.theGuiTextField1.isFocused();
                            return;
                        }
                        String id = "cp" + this.location.x + "_" + this.location.y + "_" + this.location.z + "_D" + this.location.dimension;
                        Courier point =new  Courier(id,this.location,name);
                        List<IInventory> chestInvs = inventoriesFindClosest(this.location, 5);
                        if (chestInvs.size() == 0) {
                            //错误：将至少一个箱子靠近标记。
                            this.errorText = I18n.format("container.sim.Markers6");
                            return;
                        }

                        for (int p = 0; p < ModSimLoader.theCourierPoints.size(); ++p) {
                            Courier epoint = ModSimLoader.theCourierPoints.get(p);
                            if (epoint.equals(point)) {
                                //错误：名称必须是唯一的,' ' 已被使用。
                                this.errorText = I18n.format("container.sim.Markers7") + point.toString() + I18n.format("container.sim.Markers8");
                                return;
                            }
                        }
                        ModSimLoader.theCourierPoints.add(point);
                        saveCourierTasksAndPoints(id,name,this.location);
                        //添加了快递点/传送点'
                        this.errorText = I18n.format("container.sim.Markers9") + name + I18n.format("container.sim.Markers10");
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GUIPATHBOX-actionPerformed出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void saveCourierTasksAndPoints(String id,String name, V3 point) {
        try {
            CopyOnWriteArrayList strings= new CopyOnWriteArrayList();;

            strings.add("id|"+id);
            strings.add("location|" + point.toString());
            strings.add("name|" + name);
            File f = new File(ModSimLoader.getSavesDataFolder()+File.separator + "CourierPoints"+ File.separator);
            if (!f.exists()) {
                f.mkdirs();
            }
            ModSimLoader.saveSK2(ModSimLoader.getSavesDataFolder()+File.separator + "CourierPoints" + File.separator + id + ".sk2", strings);
        }catch (Exception e){
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("GUIPATHBOX-saveCourierTasksAndPoints出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void keyTyped(char c, int i) {
        try {if (i == 1) {
            this.mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.textboxKeyTyped(c, i);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("keyTyped出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        try {
            if (this.theGuiTextField1 != null) {
                this.theGuiTextField1.mouseClicked(i, j, k);
            }
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("mouseClicked出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            //e.printStackTrace();
        }
    }

    public List<IInventory> inventoriesFindClosest(V3 startXYZ, int searchDistance) {
        List ret = new CopyOnWriteArrayList();

        try {
            World world = this.mc.world;
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
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiPathBox-inventoriesFindClosest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return ret;
        }
    }
    private boolean alreadyGotChest(List<IInventory> chests, IInventory chest) {
        boolean ret = false;
        Iterator var4 = chests.iterator();
        try {
            while(var4.hasNext()) {
                IInventory ch = (IInventory)var4.next();
                if (ch.toString().contentEquals(chest.toString())) {
                    ret = true;
                    break;
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("GuiPathBox-alreadyGotChest出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
        return ret;
    }
}
