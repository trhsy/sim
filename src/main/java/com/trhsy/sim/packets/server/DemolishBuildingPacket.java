package com.trhsy.sim.packets.server;

import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DemolishBuildingPacket implements IMessage {
    Block theBlock;
    static String[] v3;
    static V3 buildingV3;
    static List<V3> v3s;
    static Building theBuilding = null;
    static World theWorld = null;

    public DemolishBuildingPacket() {
    }

    public DemolishBuildingPacket(int x, int y, int z, int dim) {
        try {
            buildingV3 = new V3(x, y, z, dim);
            theBuilding = Building.getBuilding(buildingV3);
            theWorld = MinecraftServer.getServer().worldServers[0];
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("DemolishBuildingPacket出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public DemolishBuildingPacket(V3 theV3) {
        try {
            buildingV3 = theV3;
            theBuilding = Building.getBuilding(buildingV3);
            theWorld = MinecraftServer.getServer().worldServers[0];
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("DemolishBuildingPacket出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public DemolishBuildingPacket(Building building, List<V3> v3array) {
        try {
            buildingV3 = building.primaryXYZ;
            theBuilding = building;
            v3s = v3array;
            theWorld = MinecraftServer.getServer().worldServers[0];
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("DemolishBuildingPacket出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            v3 = ByteBufUtils.readUTF8String(buf).split(",");
            buildingV3 = new V3(Integer.parseInt(v3[0]), Integer.parseInt(v3[1]), Integer.parseInt(v3[2]), Integer.parseInt(v3[3]));
            theBuilding = Building.getBuilding(buildingV3);
            buf.release();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("fromBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeUTF8String(buf, theBuilding.primaryXYZ.toString());
            //ReferenceCountUtil.release(buf);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toBytes出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static class Handler implements IMessageHandler<DemolishBuildingPacket, IMessage> {
        @Override
        public IMessage onMessage(DemolishBuildingPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (V3 blockLoc : v3s) {
                                Block l = theWorld.getBlockState(new BlockPos(blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord)).getBlock();

                                if (l != null && ModSimReloaded.demolishBlocks.size() < 500) {
                                    blockLoc.blockID = l;
                                    ModSimReloaded.demolishBlocks.add(blockLoc);
                                }

                                theWorld.setBlockToAir(new BlockPos(blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord));

                            }
                        } catch (Exception e) {
                            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onMessage出错了：" + e.getMessage()+"行数："+element.getLineNumber());
                        }


                        //player.openGui(ModSimukraft.instance, message.id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                    }
                });

            }
            return null;
        }
    }
}
