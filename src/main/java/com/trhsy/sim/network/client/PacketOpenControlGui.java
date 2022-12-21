package com.trhsy.sim.network.client;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketOpenControlGui
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/711:44
 **/
public class PacketOpenControlGui implements IMessage {
    public NBTTagCompound nbt;
    private V3 v3;
    private String buildingId;
    private String jobName;
    private String buildingName;
    private String buildingType;
    private String author;
    private NpcIdentity folk;
    private boolean isResidential;

    public PacketOpenControlGui() {
    }
    /**初始化**/
    public PacketOpenControlGui(V3 v3,String buildingId, String buildingName,String buildingType,String jobName,String author, NpcIdentity cfi, boolean isResidential) {
        this.folk = cfi;
        this.v3 = v3;
        this.buildingId = buildingId;
        this.jobName = jobName;
        this.buildingName = buildingName;
        this.author=author;
        this.buildingType=buildingType;
        this.isResidential = isResidential;
    }
    public PacketOpenControlGui(V3 v3,String buildingId,String buildingName,String buildingType,String jobName,String author, boolean isResidential) {
        this.v3 = v3;
        this.buildingId = buildingId;
        this.jobName = jobName;
        this.buildingName = buildingName;
        this.author=author;
        this.buildingType=buildingType;
        this.isResidential = isResidential;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.v3 = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.buildingId = ByteBufUtils.readUTF8String(buf);
        this.jobName = ByteBufUtils.readUTF8String(buf);
        this.buildingName = ByteBufUtils.readUTF8String(buf);
        this.author=ByteBufUtils.readUTF8String(buf);
        this.buildingType=ByteBufUtils.readUTF8String(buf);
        this.isResidential = buf.readBoolean();

        try {
            this.folk = new NpcIdentity(ByteBufUtils.readUTF8String(buf));
        } catch (Exception var3) {
        }

    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.v3.toString());
        ByteBufUtils.writeUTF8String(buf, this.buildingId);
        ByteBufUtils.writeUTF8String(buf, this.jobName);
        ByteBufUtils.writeUTF8String(buf, this.buildingName);
        ByteBufUtils.writeUTF8String(buf, this.author);
        ByteBufUtils.writeUTF8String(buf, this.buildingType);
        buf.writeBoolean(this.isResidential);
        if (this.folk != null) {
            ByteBufUtils.writeUTF8String(buf, this.folk.toString());
        }

    }

    public static class Handler implements IMessageHandler<PacketOpenControlGui, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketOpenControlGui message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketOpenControlGui message, MessageContext ctx) {
            if (message.folk == null) {
                ModSimLoader.openControlGui(message.v3, message.buildingId,message.buildingName,message.jobName,message.buildingType,message.author);
            } else if (message.isResidential) {
                ModSimLoader.openControlGui(message.v3, message.buildingId, message.folk,true,message.buildingName,message.jobName,message.buildingType,message.author);
            } else {
                ModSimLoader.openControlGui(message.v3, message.buildingId,message.folk,message.buildingName,message.jobName,message.buildingType,message.author);
            }

        }
    }
}