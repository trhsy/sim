package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketOpenFlowerGui
 * @Description:
 * @date 2023/07/27 下午 2:12
 */
public class PacketOpenFlowerGui implements IMessage {
    public UUID id;
    public V3 v3;
    public PacketOpenFlowerGui() {
    }

    public PacketOpenFlowerGui(NpcData fd) {
        this.id = UUID.fromString(fd.ID);
        this.v3=fd.job.workPlace;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.v3 = V3.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.id.toString());
        ByteBufUtils.writeUTF8String(buf, this.v3.toString());
    }

    public static class Handler implements IMessageHandler<PacketOpenFlowerGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenFlowerGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenFolkGui出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketOpenFlowerGui message, MessageContext ctx) {
            ModSimLoader.openFlowerGui(message);
        }
    }
}