package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.entity.util.NpcSkin;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketSendFolkSkin
 * @Description: 皮肤
 * @date 2022/10/13 9:38
 */
public class PacketSendFolkSkin implements IMessage {
    private String uuid = "";
    private String path = "";

    public PacketSendFolkSkin() {
    }

    public PacketSendFolkSkin(String uuid, String path) {
        this.uuid = uuid;
        this.path = path;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String read = ByteBufUtils.readUTF8String(buf);
        String name = read.substring(0, read.indexOf("|"));
        String value = read.substring(read.indexOf("|") + 1);
        this.uuid = name;
        this.path = value;
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid + "|" + this.path);
    }

    public static class Handler implements IMessageHandler<PacketSendFolkSkin, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketSendFolkSkin message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketSendFolkSkin message, MessageContext ctx) {
            ModSimLoader.folkSkins.add(new NpcSkin(message.uuid, message.path));
        }
    }
}
