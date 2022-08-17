package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @ClassName AbstractPacket
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1123:55
 **/
public abstract class AbstractPacket implements IMessage {
    public AbstractPacket() {
    }

    public abstract IMessage handleClient(NetHandlerPlayClient var1);

    public abstract IMessage handleServer(NetHandlerPlayServer var1);

    protected void writePos(BlockPos pos, ByteBuf buf) {
        try {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("writePos出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    protected BlockPos readPos(ByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        return new BlockPos(x, y, z);
    }
}
