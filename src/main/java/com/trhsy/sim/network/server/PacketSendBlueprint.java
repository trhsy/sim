package com.trhsy.sim.network.server;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.BuildingBlueprint;
import com.trhsy.sim.npcCode.job.JobBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @ClassName PacketSendBlueprint
 * @Description todo 发送蓝图
 * @Author TRHSY
 * @Date 2022/10/2118:02
 **/
public class PacketSendBlueprint implements IMessage {
    //蓝图名称
    String bName = "";
    //建筑类型
    String bType = "";
    //建筑文件内容
    String bp = "";
    //npc id
    String folkID = "";
    //起始位置
    String bPos = "";
    int direction = 0;
    private int fs_i=0;
    public PacketSendBlueprint() {
    }

    public PacketSendBlueprint(BuildingBlueprint blueprint, String id, String blockPos, int dir) {
        this.bName = blueprint.name;
        this.bType = blueprint.buildingType;
        this.bp = blueprint.fileContents;
        this.folkID = id;
        this.bPos = blockPos;
        this.direction = dir;
        this.fs_i=0;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.bName = ByteBufUtils.readUTF8String(buf);
        this.bType = ByteBufUtils.readUTF8String(buf);
        this.folkID = ByteBufUtils.readUTF8String(buf);
        this.bPos = ByteBufUtils.readUTF8String(buf);
        this.direction = buf.readInt();
//        for (int i = 0; i < this.fs_i; i++) {
//            this.bp += ByteBufUtils.readUTF8String(buf);
//        }
//        NBTTagCompound tagCompound=ByteBufUtils.readTag(buf);
//        for (int i = 0; i < 999999999; i+=400) {
//            String s=tagCompound.getString("byte"+i);
//            if(StringUtils.isNullOrEmpty(s)){
//                this.bp+=s;
//            }else{
//                break;
//            }
//        }
//        this.bp=tagCompound.getString("byte1");
        //this.bp = ByteBufUtils.readUTF8String(buf);

    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.bName);
        ByteBufUtils.writeUTF8String(buf, this.bType);
        ByteBufUtils.writeUTF8String(buf, this.folkID);
        ByteBufUtils.writeUTF8String(buf, this.bPos);
        buf.writeInt(this.direction);
//        NBTTagCompound tag = new NBTTagCompound();
/*        for (int i = 0; i < this.bp.length(); i+=1000) {
            this.fs_i++;
            if(this.bp.length()-i>1000){
                ByteBufUtils.writeUTF8String(buf, this.bp.substring(i,i+1000));
            }else{
                ByteBufUtils.writeUTF8String(buf, this.bp.substring(i,this.bp.length()));
            }
        }*/
//        tag.setString("byte",this.bp);
//        ByteBufUtils.writeTag(buf, tag);
        //ByteBufUtils.writeUTF8String(buf, this.bp);

    }

    public static class Handler implements IMessageHandler<PacketSendBlueprint, IMessage> {
        public Handler() {
        }
        @Override
        public IMessage onMessage(PacketSendBlueprint message, MessageContext ctx) {
            try {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    this.handle(message, ctx);
                });
            } catch (Exception var4) {
                StackTraceElement element = var4.getStackTrace()[0];
                ModSimLoader.log.error("PacketSendBlueprint出错了：" + var4.getMessage() + "行数：" + element.getLineNumber());
            }

            return null;
        }

        private void handle(PacketSendBlueprint message, MessageContext ctx) {
            BuildingBlueprint newBp = new BuildingBlueprint(message.bName, message.bp,message.bType);

            ModSimLoader.getFolkDataByUID(message.folkID).job = new JobBuilder(ModSimLoader.getFolkDataByUID(message.folkID), newBp, V3.fromString(message.bPos).toBlockPos(), message.direction, ctx.getServerHandler().player.world);
        }
    }
}