package com.trhsy.sim.common.util;

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.SimukraftPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler implements IMessageHandler<SimukraftPacket, IMessage> {
    public PacketHandler() {
    }

    @Override
    public IMessage onMessage(SimukraftPacket message, MessageContext ctx) {
        try {
            String par1 = "";
            String cmd = SimukraftPacket.cmd;
            String val = "";
            World world = null;
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            String sside = "none";
            if (side == Side.SERVER) {
                sside = "Server";
                if (cmd.contentEquals("loadbuilding")) {
                    Building.loadAllBuildings();
                }
            } else if (side == Side.CLIENT) {
                sside = "Client";
                world = Minecraft.func_71410_x().field_71441_e;
            } else if (cmd.contentEquals("updateFolkPosition")) {
                FolkData folk = FolkData.getFolkByName(par1);
                V3 newpos = new V3(val);
                if (folk != null && newpos != null) {
                    folk.serverToClientLocationUpdate(newpos);
                }
            } else if (cmd.contentEquals("gamereset")) {
                ModSimReloaded.resetAndLoadNewWorld();
            }

            ModSimReloaded.log.info("PacketHandler: " + sside + "-收到了侧包: " + cmd);
            return null;
        } catch (Exception var11) {
            var11.printStackTrace();
            return message;
        }
    }
}