package com.trhsy.sim.network.client;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.client
 * @ClassName: PacketReturnHireableFolks
 * @Description: 可雇佣的人
 * @date 2022/10/13 15:04
 */
public class PacketReturnHireableFolks implements IMessage {
    public List<NpcIdentity> folkNames = new CopyOnWriteArrayList<NpcIdentity>();
    public PacketReturnHireableFolks() {
        this.folkNames= new CopyOnWriteArrayList<NpcIdentity>();
        //ModSimLoader.log.info("开始读取可雇佣的人");
        for (NpcData folk:ModSimLoader.folks){
            NpcIdentity identity = folk.getClientIdentity();
            if(identity!=null &&!folk.isDead){
                this.folkNames.add(identity);
            }
        }
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.folkNames.clear();
        try {
            String rawFolkData = ByteBufUtils.readUTF8String(buf);
            String[] rawFolks = rawFolkData.split(";");
            String[] var4 = rawFolks;
            int var5 = rawFolks.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String cFolk = var4[var6];
                if (cFolk.length() > 0 && cFolk.contains(",_,")) {
                }

                this.folkNames.add(new NpcIdentity(cFolk));
            }
        } catch (Exception var8) {

        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        String s="";
        for (NpcIdentity fName :this.folkNames){
            s+=fName.id + ",_," + fName.name + ",_," + fName.age + ",_," + fName.status + ",_," + fName.job + ",_," + fName.house + ",_," + fName.relationship + ",_," + fName.hunger + ",_," + fName.maturityAge + ",_," + fName.skinPath + ";";
        }
            ByteBufUtils.writeUTF8String(buf, s);

    }

    public static class Handler implements IMessageHandler<PacketReturnHireableFolks, IMessage> {
        public Handler() {
        }

        @Override
        public IMessage onMessage(PacketReturnHireableFolks message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketReturnHireableFolks出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketReturnHireableFolks message, MessageContext ctx) {
            ModSimClientLoader.tempHireableNpcNames.clear();
            ModSimClientLoader.tempHireableNpcNames = message.folkNames;
            //ModSimLoader.log.info("获取可以受雇佣的人，客户端收到数据包，并处理");
        }
    }
}
