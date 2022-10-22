package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.build.BlueprintRequirements;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import com.trhsy.sim.npc.job.JobBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;
import java.util.UUID;

/**
 * @ClassName PacketSendBuildingRequirements
 * @Description todo
 * @Author TRHSY
 * @Date 2022/10/2117:33
 **/
public class PacketSendBuildingRequirements  implements IMessage {
    BuildingBlueprint bp;
    JobBuilder job;
    BlueprintRequirements cbr = null;

    public PacketSendBuildingRequirements() {
    }

    public PacketSendBuildingRequirements(BuildingBlueprint bp, JobBuilder job) {
        this.bp = bp;
        this.job = job;
    }

    public void fromBytes(ByteBuf buf) {
        UUID entityId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        String requirements = ByteBufUtils.readUTF8String(buf);
        this.cbr = new BlueprintRequirements(entityId, requirements);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.job.conBox.getUniqueID().toString());
        ByteBufUtils.writeUTF8String(buf, this.bp.getBuildingRequirementsString());
    }

    public static class Handler implements IMessageHandler<PacketSendBuildingRequirements, IMessage> {
        public Handler() {
        }

        public IMessage onMessage(PacketSendBuildingRequirements message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketSendBuildingRequirements message, MessageContext ctx) {
            BlueprintRequirements existingBP = null;
            Iterator var4 = ModSimLoader.blueprintReqs.iterator();

            while(var4.hasNext()) {
                BlueprintRequirements req = (BlueprintRequirements)var4.next();
                if (req.entityId == message.cbr.entityId) {
                    existingBP = req;
                    break;
                }
            }

            if (existingBP != null) {
                existingBP.requirements = message.cbr.requirements;
            } else {
                ModSimLoader.blueprintReqs.add(message.cbr);
            }

        }
    }
}