package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.npc.job.JobBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketSendBlueprint
 * @Description todo 发送蓝图
 * @Author TRHSY
 * @Date 2022/10/2118:02
 **/
public class PacketSendBlueprint implements IMessage {
    String bName = "";
    String bType = "";
    String bp = "";
    String folkID = "";
    String bPos = "";
    int direction = 0;

    public PacketSendBlueprint() {
    }

    public PacketSendBlueprint(BuildingBlueprint blueprint, String id, String blockPos, int dir) {
        this.bName = blueprint.name;
        this.bType = blueprint.buildingType;
        this.bp = blueprint.fileContents;
        this.folkID = id;
        this.bPos = blockPos;
        this.direction = dir;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bName = ByteBufUtils.readUTF8String(buf);
        this.bType = ByteBufUtils.readUTF8String(buf);
        this.bp = ByteBufUtils.readUTF8String(buf);
        this.folkID = ByteBufUtils.readUTF8String(buf);
        this.bPos = ByteBufUtils.readUTF8String(buf);
        this.direction = buf.readInt();
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bName);
        ByteBufUtils.writeUTF8String(buf, this.bType);
        ByteBufUtils.writeUTF8String(buf, this.bp);
        ByteBufUtils.writeUTF8String(buf, this.folkID);
        ByteBufUtils.writeUTF8String(buf, this.bPos);
        buf.writeInt(this.direction);
    }

    public static class Handler implements IMessageHandler<PacketSendBlueprint, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketSendBlueprint message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketSendBlueprint message, MessageContext ctx) {
            BuildingBlueprint newBp = new BuildingBlueprint(message.bName, message.bp);
            newBp.buildingType = message.bType;
            ModSimLoader.getFolkDataByUID(message.folkID).job = new JobBuilder(ModSimLoader.getFolkDataByUID(message.folkID), newBp, V3.fromString(message.bPos).toBlockPos(), message.direction, ctx.getServerHandler().playerEntity.worldObj);
        }
    }
}