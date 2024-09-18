package com.trhsy.sim.network.server;

import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.block.FarmBox;
import com.trhsy.sim.npcCode.block.MineBox;
import com.trhsy.sim.npcCode.job.JobBuilder;
import com.trhsy.sim.npcCode.job.JobFarmer;
import com.trhsy.sim.npcCode.job.JobMiner;
import com.trhsy.sim.npcCode.job.JobTerrainFormer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
            V3 v3=new V3(message.pos.x+0.5,message.pos.y-1,message.pos.z+0.5);
            if (fd.job != null) {
                //被其他地方雇佣，尝试雇佣其他人
                ctx.getServerHandler().player.sendMessage(new TextComponentString(fd.getName() + new TextComponentTranslation("container.sim.hire_elsewhere",new Object[0]).getUnformattedText()));
            } else {
                //建筑工
                if (message.job.contentEquals(new TextComponentTranslation("container.sim.Vocation1",new Object[0]).getUnformattedText())) {
                    fd.job = new JobBuilder(fd, message.pos, message.buildDirection, ctx.getServerHandler().player.world);
                    BlockConstructorBox cons = (BlockConstructorBox) fd.job.jobWorld.getBlockState(v3.toBlockPos()).getBlock();
                    cons.employee = fd;
                    //规划师
                }else if(message.job.contentEquals(new TextComponentTranslation("container.sim.Vocation16",new Object[0]).getUnformattedText())){
                    fd.job = new JobTerrainFormer(fd,message.pos,ctx.getServerHandler().player.world);
                    BlockConstructorBox cons = (BlockConstructorBox) fd.job.jobWorld.getBlockState(v3.toBlockPos()).getBlock();
                    cons.employee = fd;
                    //农民
                } else if (message.job.contentEquals(new TextComponentTranslation("container.sim.Hire_farmer",new Object[0]).getUnformattedText())) {
                    //农田箱
                    FarmBox farmBox = ModSimLoader.getFarm(v3);
                    if (farmBox != null) {
                        fd.job = new JobFarmer(fd, v3.toBlockPos(), ctx.getServerHandler().player.world, farmBox);
                        farmBox.employee = fd;
                        farmBox.saveFarm();
                    }
                    //矿工
                } else if (message.job.contentEquals(new TextComponentTranslation("container.sim.Vocation4",new Object[0]).getUnformattedText())) {
                    MineBox mineBox = ModSimLoader.getMine(v3);
                    if (mineBox != null) {
                        fd.job = new JobMiner(fd, v3.toBlockPos(), ctx.getServerHandler().player.world, mineBox);
                        mineBox.employee = fd;
                        mineBox.saveMine();
                    }
                } else {
                    //去雇佣地点
                    fd.hireAt(message.pos, message.job, ctx.getServerHandler().player.world);
                }

            }
        }
    }
}