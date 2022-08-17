package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;


/**
 * @ClassName TinkerNetwork
 * @Description todo
 * @Author Tian
 * @Date 2022/5/120:35
 **/
public class TinkerNetwork extends NetworkWrapper{
    public static TinkerNetwork instance = new TinkerNetwork();

    public TinkerNetwork() {
        super("sim");
    }
    public void setup() {
        try {
            this.registerPacketClient(ConfigSyncPacket.class);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("TinkerNetwork-setup出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static void sendToAll(AbstractPacket packet) {
        instance.network.sendToAll(packet);
    }

    public static void sendTo(AbstractPacket packet, EntityPlayerMP player) {
        instance.network.sendTo(packet, player);
    }

    public static void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point) {
        instance.network.sendToAllAround(packet, point);
    }

    public static void sendToDimension(AbstractPacket packet, int dimensionId) {
        instance.network.sendToDimension(packet, dimensionId);
    }

    public static void sendToServer(AbstractPacket packet) {
        instance.network.sendToServer(packet);
    }

    public static void sendToClients(WorldServer world, BlockPos pos, AbstractPacket packet) {
        try {
            Chunk chunk = world.getChunkFromBlockCoords(pos);
            for(EntityPlayer player:world.playerEntities){
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP)player;
                    if (world.getPlayerManager().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition)) {
                        sendTo(packet, playerMP);
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("sendToClients出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }
}
