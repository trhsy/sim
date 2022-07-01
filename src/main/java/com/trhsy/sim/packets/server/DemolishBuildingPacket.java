package com.trhsy.sim.packets.server;

import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class DemolishBuildingPacket implements IMessage {
    Block theBlock;
    static String[] v3;
    static V3 buildingV3;
    static ArrayList<V3> v3s;
    static Building theBuilding = null;
    static World theWorld = null;

    public DemolishBuildingPacket() {
    }

    public DemolishBuildingPacket(int x, int y, int z, int dim) {
        buildingV3 = new V3(x, y, z, dim);
        theBuilding = Building.getBuilding(buildingV3);
        theWorld = MinecraftServer.getServer().worldServers[0];
    }

    public DemolishBuildingPacket(V3 theV3) {
        buildingV3 = theV3;
        theBuilding = Building.getBuilding(buildingV3);
        theWorld = MinecraftServer.getServer().worldServers[0];
    }

    public DemolishBuildingPacket(Building building, ArrayList<V3> v3array) {
        buildingV3 = building.primaryXYZ;
        theBuilding = building;
        v3s = v3array;
        theWorld = MinecraftServer.getServer().worldServers[0];
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        v3 = ByteBufUtils.readUTF8String(buf).split(",");
        buildingV3 = new V3(Integer.parseInt(v3[0]), Integer.parseInt(v3[1]), Integer.parseInt(v3[2]), Integer.parseInt(v3[3]));
        theBuilding = Building.getBuilding(buildingV3);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, theBuilding.primaryXYZ.toString());
    }
    public static class Handler implements IMessageHandler<DemolishBuildingPacket, IMessage> {
        @Override
        public IMessage onMessage(DemolishBuildingPacket message, MessageContext ctx) {

            for (V3 blockLoc : v3s)
            {
                Block l = theWorld.getBlockState(new BlockPos(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue())).getBlock();

                if (l != null && ModSimReloaded.demolishBlocks.size() < 500)
                {
                    blockLoc.blockID = l;
                    ModSimReloaded.demolishBlocks.add(blockLoc);
                }

                theWorld.setBlockToAir(new BlockPos(blockLoc.x.intValue(), blockLoc.y.intValue(), blockLoc.z.intValue()));

            }

            //player.openGui(ModSimukraft.instance, message.id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            return null;
        }
    }
}
