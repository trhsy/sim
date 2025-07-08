package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.block.Marker;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntityAnimation implements IMessage{
    private String entityId;
    private EnumHand hand;
    public PacketEntityAnimation(EntityLivingBase entity, EnumHand hand) {
        this.entityId = entity.getUniqueID().toString();
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId=ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.entityId);
    }

    public static class Handler implements IMessageHandler<PacketEntityAnimation, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketEntityAnimation message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketEntityAnimation message, MessageContext ctx) {
//            EntityLivingBase entity = (EntityLivingBase) Minecraft.getMinecraft().world.getUniqueDataId(message.entityId);
//            EnumHand.MAIN_HAND
        }
    }
}
