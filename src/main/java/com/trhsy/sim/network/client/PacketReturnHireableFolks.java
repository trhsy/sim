package com.trhsy.sim.network.client;

import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Iterator;
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
    public List<NpcIdentity> folkNames = new CopyOnWriteArrayList<>();
    public PacketReturnHireableFolks() {
        this.folkNames= new CopyOnWriteArrayList<>();
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
                if (cFolk.length() > 0 && cFolk.contains(",")) {
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
            s+=fName.id + "," + fName.name + "," + fName.age + "," + fName.status + "," + fName.job + "," + fName.house + "," + fName.relationship + "," + fName.hunger + "," + fName.maturityAge + "," + fName.skinPath + ";";
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
                var4.printStackTrace();
            }

            return null;
        }

        private void handle(PacketReturnHireableFolks message, MessageContext ctx) {
            ModSimLoader.tempHireableNpcNames.clear();
            ModSimLoader.tempHireableNpcNames = message.folkNames;
        }
    }
}
