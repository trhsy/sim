package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.block.Marker;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketAddNewMarker
 * @Description:
 * @date 2023/5/4 15:58
 */
public class PacketAddNewMarker implements IMessage {

    private BlockPos pos;

    public PacketAddNewMarker() {
    }

    public PacketAddNewMarker(BlockPos pos) {
        this.pos = pos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = V3.fromString(ByteBufUtils.readUTF8String(buf)).toBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, V3.fromBlockPos(this.pos).toString());
    }
    public static class Handler implements IMessageHandler<PacketAddNewMarker, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketAddNewMarker message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketAddNewMarker message, MessageContext ctx) {
            ModSimClientLoader.markers.add(new Marker(message.pos, Minecraft.getMinecraft().player.dimension, Minecraft.getMinecraft().player));
        }
    }
}
