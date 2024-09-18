package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketUpdateNPC
 * @Description:
 * @date 2024/4/10 18:02
 */
public class PacketUpdateNPC implements IMessage {

    public List<NpcData> folks = new CopyOnWriteArrayList();

    @Override
    public void fromBytes(ByteBuf buf) {
        String rawFolkData = ByteBufUtils.readUTF8String(buf);
        String[] rawFolks = rawFolkData.split(";");
        String[] var4 = rawFolks;
        int var5 = rawFolks.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            String cFolk = var4[var6];
            NpcData npcData=ModSimLoader.getFolkDataByUID(cFolk);
            this.folks.add(npcData);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String s="";
        for (NpcData npcData:ModSimLoader.folks){
            s+=npcData.ID+";";
        }
        ByteBufUtils.writeUTF8String(buf, s);
    }
    public static class Handler implements IMessageHandler<PacketUpdateNPC, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketUpdateNPC message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketUpdateMoney出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }
        private void handle(PacketUpdateNPC message, MessageContext ctx) {
            ModSimClientLoader.folks = message.folks;
        }
    }

}
