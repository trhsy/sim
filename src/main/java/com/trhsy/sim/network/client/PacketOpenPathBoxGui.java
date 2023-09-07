package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.Marker;
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
 * @ClassName: PacketOpenPathBoxGui
 * @Description:
 * @date 2023/08/10 上午 9:38
 */
public class PacketOpenPathBoxGui implements IMessage {

    public V3 v3;

    public PacketOpenPathBoxGui() {
    }

    public PacketOpenPathBoxGui(V3 v3) {
        this.v3 = v3;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.v3 = V3.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.v3.toString());
    }
    public static class Handler implements IMessageHandler<PacketOpenPathBoxGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenPathBoxGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketOpenPathBoxGui message, MessageContext ctx) {
            ModSimLoader.OpenPathBox(message);
        }
    }
}
