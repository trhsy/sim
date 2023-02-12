package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.util.FarmType;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;
import java.util.UUID;

/**
 * @ClassName PacketUpdateFarmBox
 * @Description todo 更新服务器端的养殖箱
 * @Author TRHSY
 * @Date 2022/12/1014:55
 **/
public class PacketUpdateFarmBox implements IMessage {
    public UUID id;
    public int x;
    public int z;
    public EnumFacing facing;
    public FarmType farmType;


    public PacketUpdateFarmBox() {
    }

    public PacketUpdateFarmBox(UUID id, int x, int z, EnumFacing facing,FarmType farmType) {
        this.id = id;
        this.x = x;
        this.z = z;
        this.facing = facing;
        this.farmType = farmType;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.facing = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
        this.farmType = FarmType.byName(ByteBufUtils.readUTF8String(buf));
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.id.toString());
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, this.facing.toString());
        ByteBufUtils.writeUTF8String(buf, this.farmType.toString());
    }

    public static class Handler implements IMessageHandler<PacketUpdateFarmBox, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketUpdateFarmBox message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketUpdateFarmBox message, MessageContext ctx) {
            for (FarmBox farm:ModSimLoader.farms){
                if (farm.ID.equals(message.id)) {
                    farm.x = message.x;
                    farm.z = message.z;
                    farm.facing = message.facing;
                    farm.farmType = message.farmType;
                    break;
                }
            }

        }
    }
}