package com.trhsy.sim.api.buildcraft.api.tablet;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;

public interface ITablet {
    Side getSide();

    void refreshScreen(TabletBitmap var1);

    int getScreenWidth();

    int getScreenHeight();

    void launchProgram(String var1);

    void sendMessage(NBTTagCompound var1);
}
