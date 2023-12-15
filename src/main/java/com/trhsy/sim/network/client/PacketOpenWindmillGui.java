package com.trhsy.sim.network.client;

import com.trhsy.sim.npcCode.V3;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @ClassName PacketOpenWindmillGui
 * @Description todo
 * @Author TRHSY
 * @Date 2023/8/2011:17
 **/
public class PacketOpenWindmillGui implements IMessage {

    public V3 v3;
    public UUID uuid;
    public PacketOpenWindmillGui() {
    }

    public PacketOpenWindmillGui(EntityPlayer player, V3 v3) {
        this.v3 = v3;
        this.uuid = player.getUniqueID();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.v3 = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.v3.toString());
        ByteBufUtils.writeUTF8String(buf,this.uuid.toString());
    }

    public static class Handler implements IMessageHandler<PacketOpenWindmillGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenWindmillGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketOpenWindmillGui message, MessageContext ctx) {
        }
    }
}