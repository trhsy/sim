package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.SimmodeStart;
import com.trhsy.sim.npcCode.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.server
 * @ClassName: NPC数据同步包
 * @Description:PacketSyncNpcData
 * @date 2025/7/11 15:40
 */
public class PacketSyncNpcData implements IMessage {
    private String entityId;
    private NpcData npcData;

    public PacketSyncNpcData() {
    }

    public PacketSyncNpcData(NpcData npc) {
        this.entityId=npc.ID.toString();
        npcData=npc;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
// 从ByteBuf读取数据
        this.entityId = ByteBufUtils.readUTF8String(buf);
        // 读取其他NPC数据（略）
    }

    @Override
    public void toBytes(ByteBuf buf) {
// 写入数据到ByteBuf
        ByteBufUtils.writeUTF8String(buf, this.entityId);
        // 写入其他NPC数据（略）
    }
    // 客户端处理逻辑
    public static class Handler implements IMessageHandler<PacketSyncNpcData, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncNpcData message, MessageContext ctx) {
            NpcData fd = ModSimLoader.getFolkDataByUID(UUID.fromString(message.entityId));
            Minecraft.getMinecraft().addScheduledTask(() -> {
                // 更新客户端NPC数据缓存
                // 更新客户端NPC缓存
                SimmodeStart.updateClientNpcCache(message.entityId, fd);
            });
            return null;
        }
    }
}
