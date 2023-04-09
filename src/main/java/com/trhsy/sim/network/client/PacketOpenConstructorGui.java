package com.trhsy.sim.network.client;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketOpenConstructorGui
 * @Description todo 打开建筑箱gui
 * @Author TRHSY
 * @Date 2022/10/1916:19
 **/
public class PacketOpenConstructorGui implements IMessage {
    private int bDir = 0;
    private BlockPos pos;
    private NpcIdentity folk;
    /**
     * @Author fan
     * @Description //TODO 所在维度
     * @Date 10:13 2023/4/8
     * @Param
     * @return
     **/
    private int dimension;

    public PacketOpenConstructorGui() {
    }

    public PacketOpenConstructorGui(BlockPos p, int buildDirection,int dimension) {
        this.pos = p;
        this.bDir = buildDirection;
        this.dimension=dimension;
    }

    public PacketOpenConstructorGui(BlockPos p, int buildDirection, NpcIdentity cfi,int dimension) {
        this.pos = p;
        this.bDir = buildDirection;
        this.folk = cfi;
        this.dimension=dimension;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.dimension=buf.readInt();
        this.bDir = buf.readInt();
        this.pos = V3.fromString(ByteBufUtils.readUTF8String(buf)).toBlockPos();
        try {
            this.folk = new NpcIdentity(ByteBufUtils.readUTF8String(buf));
        } catch (Exception var3) {
        }
        
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dimension);
        buf.writeInt(this.bDir);
        ByteBufUtils.writeUTF8String(buf, this.pos.getX()+","+this.pos.getY()+","+this.pos.getZ()+","+this.dimension);
        if (this.folk != null) {
            ByteBufUtils.writeUTF8String(buf, this.folk.toString());
        }
        
    }

    public static class Handler implements IMessageHandler<PacketOpenConstructorGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenConstructorGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketOpenConstructorGui出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketOpenConstructorGui message, MessageContext ctx) {
            if (message.folk == null) {
                ModSimLoader.openConstructorGui(message.pos, message.bDir,message.dimension);
            } else {
                ModSimLoader.openConstructorGui(message.pos, message.bDir, message.folk,message.dimension);
            }

        }
    }
}