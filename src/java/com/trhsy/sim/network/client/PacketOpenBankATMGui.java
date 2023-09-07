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
 * @ClassName PacketOpenBankATMGui
 * @Description todo 打开银行的GUI
 * @Author TRHSY
 * @Date 2023/7/1223:01
 **/
public class PacketOpenBankATMGui implements IMessage {
    public UUID id;
    public V3 v3;
    public PacketOpenBankATMGui() {
    }

    public PacketOpenBankATMGui(NpcData fd) {
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

    public static class Handler implements IMessageHandler<PacketOpenBankATMGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenBankATMGui message, MessageContext ctx) {
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

        private void handle(PacketOpenBankATMGui message, MessageContext ctx) {
            ModSimLoader.openBankATMGui(message);
        }
    }
}
