package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcIdentity;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.enums.FarmType;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @ClassName PacketOpenFarmGui
 * @Description todo 打开农田箱gui
 * @Author TRHSY
 * @Date 2022/12/1012:38
 **/
public class PacketOpenFarmGui implements IMessage {
    private UUID id;
    private V3 loc;
    private EnumFacing facing;
    /**农场类型**/
    private FarmType farmType;
    private int x;
    private int z;
    private NpcIdentity folk;

    public PacketOpenFarmGui() {
    }

    public PacketOpenFarmGui(UUID id, V3 loc, EnumFacing facing,FarmType farmType, int x, int z) {
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.farmType=farmType;
        this.x = x;
        this.z = z;
    }

    public PacketOpenFarmGui(UUID id, V3 loc, EnumFacing facing,FarmType farmType, int x, int z, NpcIdentity folk) {
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.farmType=farmType;
        this.x = x;
        this.z = z;
        this.folk = folk;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.loc = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.facing = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
        this.farmType = FarmType.byName(ByteBufUtils.readUTF8String(buf));
        this.x = buf.readInt();
        this.z = buf.readInt();

        try {
            this.folk = new NpcIdentity(ByteBufUtils.readUTF8String(buf));
        } catch (Exception var3) {
        }

    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.id.toString());
        ByteBufUtils.writeUTF8String(buf, this.loc.toString());
        ByteBufUtils.writeUTF8String(buf, this.facing.toString());
        ByteBufUtils.writeUTF8String(buf, this.farmType.toString());
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        if (this.folk != null) {
            ByteBufUtils.writeUTF8String(buf, this.folk.toString());
        }

    }

    public static class Handler implements IMessageHandler<PacketOpenFarmGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenFarmGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenFarmGui出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketOpenFarmGui message, MessageContext ctx) {
            if (message.folk == null) {
                ModSimClientLoader.openFarmGui(message.id, message.loc, message.facing,message.farmType, message.x, message.z);
            } else {
                ModSimClientLoader.openFarmGui(message.id, message.loc, message.facing,message.farmType, message.x, message.z, message.folk);
            }

        }
    }
}