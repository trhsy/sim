package com.trhsy.sim.network.client;

import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.npcCode.NpcIdentity;
import com.trhsy.sim.npcCode.V3;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketOpenMineGui
 * @Description:
 * @date 2023/5/16 18:01
 */
public class PacketOpenMineGui implements IMessage {
    private UUID id;
    private V3 loc;
    private EnumFacing facing;
    private int x;
    private int z;
    private NpcIdentity folk;

    public PacketOpenMineGui() {
    }

    public PacketOpenMineGui(UUID id, V3 loc, EnumFacing facing, int x, int z) {
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.x = x;
        this.z = z;
    }

    public PacketOpenMineGui(UUID id, V3 loc, EnumFacing facing, int x, int z, NpcIdentity folk) {
        this.id = id;
        this.loc = loc;
        this.facing = facing;
        this.x = x;
        this.z = z;
        this.folk = folk;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.loc = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.facing = EnumFacing.byName(ByteBufUtils.readUTF8String(buf));
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
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        if (this.folk != null) {
            ByteBufUtils.writeUTF8String(buf, this.folk.toString());
        }

    }

    public static class Handler implements IMessageHandler<PacketOpenMineGui, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketOpenMineGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketOpenMineGui message, MessageContext ctx) {
            if (message.folk == null) {
                ModSimClientLoader.openMineGui(message.id, message.loc, message.facing, message.x, message.z);
            } else {
                ModSimClientLoader.openMineGui(message.id, message.loc, message.facing, message.x, message.z, message.folk);
            }

        }
    }
}
