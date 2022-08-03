package com.trhsy.sim.packets.toServer;

import com.trhsy.sim.ModSim;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class OpenFolkInventoryPacket extends AbstractPacket {
    private byte id;

    public OpenFolkInventoryPacket() {
    }

    public OpenFolkInventoryPacket(byte id) {
        this.id = id;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeByte(this.id);
    }
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.id = buffer.readByte();
    }
    @Override
    public void handleClientSide(EntityPlayer player) {
    }
    @Override
    public void handleServerSide(EntityPlayer player) {
        FMLNetworkHandler.openGui(player, ModSim.instance, this.id, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
    }
}