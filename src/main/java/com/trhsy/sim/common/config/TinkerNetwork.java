package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Iterator;

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
            //this.registerPacket(StencilTableSelectionPacket.class);
            //this.registerPacket(PartCrafterSelectionPacket.class);
            //this.registerPacket(ToolStationSelectionPacket.class);
            //this.registerPacket(ToolStationTextPacket.class);
            //this.registerPacketServer(TinkerStationTabPacket.class);
            //this.registerPacketServer(InventoryCraftingSyncPacket.class);
            //this.registerPacketClient(InventorySlotSyncPacket.class);
            //this.registerPacketClient(EntityMovementChangePacket.class);
            //this.registerPacketClient(ToolBreakAnimationPacket.class);
            //this.registerPacketClient(SmelteryFluidUpdatePacket.class);
            //this.registerPacketClient(SmelteryFuelUpdatePacket.class);
            //this.registerPacketClient(SmelteryInventoryUpdatePacket.class);
            //this.registerPacketServer(SmelteryFluidClicked.class);
            //this.registerPacketClient(FluidUpdatePacket.class);
            //this.registerPacketClient(FaucetActivationPacket.class);
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
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
            Iterator var4 = world.playerEntities.iterator();

            while(var4.hasNext()) {
                EntityPlayer player = (EntityPlayer)var4.next();
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP)player;
                    if (world.getPlayerManager().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition)) {
                        sendTo(packet, playerMP);
                    }
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }


    }
}
