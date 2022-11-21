package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.npc.build.TerrainType;
import com.trhsy.sim.npc.job.JobBuilder;
import com.trhsy.sim.npc.job.JobTerrainFormer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketSendTerrainType
 * @Description todo 发送规划土地的类型
 * @Author TRHSY
 * @Date 2022/11/2121:02
 **/
public class PacketSendTerrainType implements IMessage{

    String folkID = "";
    String terrainName = "";
    String terrainType = "";
    String bPos = "";
    public PacketSendTerrainType(){}
    public PacketSendTerrainType(TerrainType terrainType,String folkID,String bPos){
        this.folkID=folkID;
        this.terrainName=terrainType.terrainName;
        this.terrainType=terrainType.terrainType;
        this.bPos=bPos;
    }
    public void fromBytes(ByteBuf buf) {
        this.terrainName = ByteBufUtils.readUTF8String(buf);
        this.terrainType = ByteBufUtils.readUTF8String(buf);
        this.folkID = ByteBufUtils.readUTF8String(buf);
        this.bPos = ByteBufUtils.readUTF8String(buf);
    }
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.terrainName);
        ByteBufUtils.writeUTF8String(buf, this.terrainType);
        ByteBufUtils.writeUTF8String(buf, this.folkID);
        ByteBufUtils.writeUTF8String(buf, this.bPos);
    }
    public static class Handler implements IMessageHandler<PacketSendTerrainType, IMessage> {
        public Handler() {
        }

        public IMessage onMessage(PacketSendTerrainType message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketSendTerrainType message, MessageContext ctx) {
            TerrainType terrainType=new TerrainType(message.terrainName, message.terrainType);
            ModSimLoader.getFolkDataByUID(message.folkID).job = new JobTerrainFormer(ModSimLoader.getFolkDataByUID(message.folkID), terrainType,V3.fromString(message.bPos).toBlockPos(),ctx.getServerHandler().playerEntity.worldObj);
        }
    }
}
