package com.trhsy.sim.network.server;

import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.block.BlockFarmingBox;
import com.trhsy.sim.block.BlockMiningBox;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.block.MineBox;
import com.trhsy.sim.npc.job.JobBuilder;
import com.trhsy.sim.npc.job.JobFarmer;
import com.trhsy.sim.npc.job.JobTerrainFormer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketHireFolk
 * @Description todo 雇佣npc
 * @Author TRHSY
 * @Date 2022/10/2013:53
 **/
public class PacketHireFolk implements IMessage {
    private String uuid;
    private String job;
    private V3 pos;
    private int buildDirection = -1;

    public PacketHireFolk() {
    }

    public PacketHireFolk(String uuid, String job, V3 pos) {
        this.uuid = uuid;
        this.job = job;
        this.pos = pos;
    }

    public PacketHireFolk(String uuid, String job, V3 pos, int bd) {
        this.uuid = uuid;
        this.job = job;
        this.pos = pos;
        this.buildDirection = bd;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = ByteBufUtils.readUTF8String(buf);
        this.job = ByteBufUtils.readUTF8String(buf);
        this.pos = V3.fromString(ByteBufUtils.readUTF8String(buf));
        this.buildDirection = buf.readInt();
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid);
        ByteBufUtils.writeUTF8String(buf, this.job);
        ByteBufUtils.writeUTF8String(buf, this.pos.toString());
        buf.writeInt(this.buildDirection);
    }

    public static class Handler implements IMessageHandler<PacketHireFolk, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketHireFolk message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketHireFolk出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketHireFolk message, MessageContext ctx) {
            NpcData fd = ModSimLoader.getFolkDataByUID(message.uuid);
            if (fd.job != null) {
                ctx.getServerHandler().playerEntity.addChatMessage(new TextComponentString(fd.getName() + I18n.format("container.sim.hire_elsewhere")));
            } else {
                //建筑工
                if (message.job.contentEquals(I18n.format("container.sim.Vocation1"))) {
                    fd.job = new JobBuilder(fd, message.pos, message.buildDirection, ctx.getServerHandler().playerEntity.worldObj);
                    BlockConstructorBox cons = (BlockConstructorBox) fd.job.jobWorld.getBlockState(message.pos.toBlockPos()).getBlock();
                    cons.employee = fd;
                }else if(message.job.contentEquals(I18n.format("container.sim.Vocation16"))){
                    fd.job = new JobTerrainFormer(fd,message.pos,ctx.getServerHandler().playerEntity.worldObj);
                    BlockConstructorBox cons = (BlockConstructorBox) fd.job.jobWorld.getBlockState(message.pos.toBlockPos()).getBlock();
                    cons.employee = fd;
                } else if (message.job.contentEquals(I18n.format("container.sim.Hire_farmer"))) {
                    //农田箱
                    FarmBox farmBox = ModSimLoader.getFarm(message.pos);
                    if (farmBox != null) {
                        fd.job = new JobFarmer(fd, message.pos.toBlockPos(), ctx.getServerHandler().playerEntity.worldObj, farmBox);
                        farmBox.employee = fd;
                    }
                } else if (message.job.contentEquals(I18n.format("container.sim.Vocation4"))) {
                    MineBox mineBox = ModSimLoader.getMine(message.pos);
                    if (mineBox != null) {
                        //fd.job = new JobMiner(fd, message.pos.toBlockPos(), ctx.getServerHandler().playerEntity.worldObj, mineBox);
                        mineBox.employee = fd;
                    }
                } else {
                    fd.hireAt(message.pos, message.job, ctx.getServerHandler().playerEntity.worldObj);
                }

            }
        }
    }
}