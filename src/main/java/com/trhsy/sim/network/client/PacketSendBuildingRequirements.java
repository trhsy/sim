package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.build.BlueprintRequirements;
import com.trhsy.sim.npcCode.build.BuildingBlueprint;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @ClassName PacketSendBuildingRequirements
 * @Description todo 获取蓝图
 * @Author TRHSY
 * @Date 2022/10/2117:33
 **/
public class PacketSendBuildingRequirements  implements IMessage {
    BuildingBlueprint bp;
    UUID conBoxId;
    BlueprintRequirements cbr = null;

    public PacketSendBuildingRequirements() {
    }

    public PacketSendBuildingRequirements(BuildingBlueprint bp, UUID conBoxId) {
        this.bp = bp;
        this.conBoxId = conBoxId;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        UUID entityId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        String requirements = ByteBufUtils.readUTF8String(buf);
        this.cbr = new BlueprintRequirements(entityId, requirements);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.conBoxId.toString());
        ByteBufUtils.writeUTF8String(buf, this.bp.getBuildingRequirementsString());
    }

    public static class Handler implements IMessageHandler<PacketSendBuildingRequirements, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketSendBuildingRequirements message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketSendBuildingRequirements出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketSendBuildingRequirements message, MessageContext ctx) {
            BlueprintRequirements existingBP = null;
            for (BlueprintRequirements b: ModSimClientLoader.blueprintReqs){
                if (b.entityId == message.cbr.entityId) {
                    existingBP = b;
                    break;
                }
            }
            if (existingBP != null) {
                existingBP.requirements = message.cbr.requirements;
            } else {
                ModSimClientLoader.blueprintReqs.add(message.cbr);
            }

        }
    }
}