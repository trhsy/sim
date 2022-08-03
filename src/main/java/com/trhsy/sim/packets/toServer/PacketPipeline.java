package com.trhsy.sim.packets.toServer;

import io.netty.channel.ChannelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

@ChannelHandler.Sharable
public class PacketPipeline {
    public PacketPipeline() {
    }

    public void sendToServer(OpenFolkInventoryPacket openFolkInventoryPacket) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        openFolkInventoryPacket.handleServerSide(player);
    }
}
