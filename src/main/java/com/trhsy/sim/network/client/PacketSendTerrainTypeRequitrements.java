package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.build.BlueprintRequirements;
import com.trhsy.sim.npc.build.TerrainType;
import com.trhsy.sim.npc.build.TerrainTypeRequitrements;
import com.trhsy.sim.npc.job.JobTerrainFormer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;
import java.util.UUID;

/**
 * @ClassName PacketSendTerrainTypeRequitrements
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/269:56
 **/
public class PacketSendTerrainTypeRequitrements implements IMessage {
    public TerrainType terrainType;
    public JobTerrainFormer jobTerrainFormer;
    public TerrainTypeRequitrements terrainTypeRequitrements=null;
    public PacketSendTerrainTypeRequitrements(){

    }
    public  PacketSendTerrainTypeRequitrements(TerrainType terrainType,JobTerrainFormer jobTerrainFormer){
        this.terrainType=terrainType;
        this.jobTerrainFormer=jobTerrainFormer;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        UUID entityId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        String requirements = ByteBufUtils.readUTF8String(buf);
        this.terrainTypeRequitrements = new TerrainTypeRequitrements(entityId, requirements);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.jobTerrainFormer.conBox.getUniqueID().toString());
        ByteBufUtils.writeUTF8String(buf, "");
    }
    public static class Handler implements IMessageHandler<PacketSendTerrainTypeRequitrements, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketSendTerrainTypeRequitrements message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketSendTerrainTypeRequitrements出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketSendTerrainTypeRequitrements message, MessageContext ctx) {
            TerrainTypeRequitrements existingBP = null;
            for (TerrainTypeRequitrements t:ModSimLoader.terrainTypeReqs){

                if (t.entityId == message.terrainTypeRequitrements.entityId) {
                    existingBP=t;
                    break;
                }
            }

            if (existingBP != null) {
                existingBP.requirements = message.terrainTypeRequitrements.requirements;
            } else {
                ModSimLoader.terrainTypeReqs.add(message.terrainTypeRequitrements);
            }
        }
    }
}
