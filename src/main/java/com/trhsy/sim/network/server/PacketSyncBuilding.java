package com.trhsy.sim.network.server;

import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.Building;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.network.server
 * @ClassName: PacketSyncBuilding
 * @Description: 建筑数据同步网络包 用于服务器向客户端同步建筑信息（如住户、租金、状态等）
 * @date 2025/7/15 17:56
 */
public class PacketSyncBuilding implements IMessage {
    // 需要同步的核心字段（根据Building类筛选）
    private UUID buildingId;         // 建筑唯一ID
    private String buildingName;     // 建筑名称
    private String buildingType;     // 建筑类型（如住宅、商店）
    private float rent;              // 租金
    private int dimension;           // 所在维度
    private V3 controlPos;           // 控制箱位置
    private V3 livingPos;            // 生活区位置
    private int occupantCount;       // 居住人数（避免同步完整NPC列表）
    private boolean isMarkedForDeletion; // 是否标记删除

    // 必须存在的空构造函数（用于网络传输时反序列化）
    public PacketSyncBuilding() {}

    // 构建建筑同步包（服务器端使用）
    public PacketSyncBuilding(Building building) {
        this.buildingId = building.ID;
        this.buildingName = building.buildingName;
        this.buildingType = building.buildingType;
        this.rent = building.rent;
        this.dimension = building.dimension;
        this.controlPos = building.controlXYZ;
        this.livingPos = building.livingXYZ;
        this.occupantCount = building.occupants.size(); // 只同步数量，不同步具体NPC
        this.isMarkedForDeletion = building.markedForDeletion;
    }

    // 从字节流读取数据（反序列化）
    @Override
    public void fromBytes(ByteBuf buf) {
        // 1. 读取建筑ID
        String idStr = readString(buf);
        this.buildingId = UUID.fromString(idStr);

        // 2. 读取建筑基本信息
        this.buildingName = readString(buf);
        this.buildingType = readString(buf);
        this.rent = buf.readFloat();
        this.dimension = buf.readInt();

        // 3. 读取控制箱位置（V3对象）
        double cx = buf.readDouble();
        double cy = buf.readDouble();
        double cz = buf.readDouble();
        this.controlPos = new V3(cx, cy, cz);

        // 4. 读取生活区位置（V3对象）
        double lx = buf.readDouble();
        double ly = buf.readDouble();
        double lz = buf.readDouble();
        this.livingPos = new V3(lx, ly, lz);

        // 5. 读取居住人数和删除标记
        this.occupantCount = buf.readInt();
        this.isMarkedForDeletion = buf.readBoolean();
    }

    // 将数据写入字节流（序列化）
    @Override
    public void toBytes(ByteBuf buf) {
        // 1. 写入建筑ID（UUID转字符串，避免直接写UUID的兼容性问题）
        writeString(buf, buildingId.toString());

        // 2. 写入建筑基本信息
        writeString(buf, buildingName);
        writeString(buf, buildingType);
        buf.writeFloat(rent);
        buf.writeInt(dimension);

        // 3. 写入控制箱位置（V3对象）
        if (controlPos != null) {
            buf.writeDouble(controlPos.x);
            buf.writeDouble(controlPos.y);
            buf.writeDouble(controlPos.z);
        } else {
            buf.writeDouble(0);
            buf.writeDouble(0);
            buf.writeDouble(0);
        }

        // 4. 写入生活区位置（V3对象）
        if (livingPos != null) {
            buf.writeDouble(livingPos.x);
            buf.writeDouble(livingPos.y);
            buf.writeDouble(livingPos.z);
        } else {
            buf.writeDouble(0);
            buf.writeDouble(0);
            buf.writeDouble(0);
        }

        // 5. 写入居住人数和删除标记
        buf.writeInt(occupantCount);
        buf.writeBoolean(isMarkedForDeletion);
    }
    // 字符串写入工具（处理长度前缀）
    private void writeString(ByteBuf buf, String str) {
        if (str == null) {
            buf.writeInt(0); // 空字符串长度为0
            return;
        }

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length); // 写入长度（使用int确保兼容性）
        buf.writeBytes(bytes);

    }

    // 字符串读取工具（处理长度前缀）
    private String readString(ByteBuf buf) {
        int length = buf.readInt();
        if (length <= 0) {
            return ""; // 空字符串
        }
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }
    // 网络包处理逻辑（客户端）
    public static class Handler implements IMessageHandler<PacketSyncBuilding, IMessage> {
        @Override
        @SideOnly(Side.CLIENT) // 仅在客户端执行
        public IMessage onMessage(PacketSyncBuilding message, MessageContext ctx) {
            // 使用客户端主线程调度器处理（避免线程安全问题）
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                handleSync(message);
            });
            return null; // 无需返回响应包
        }

        // 处理建筑同步数据
        @SideOnly(Side.CLIENT)
        private void handleSync(PacketSyncBuilding message) {
            // 1. 从客户端缓存获取建筑（若不存在则创建临时建筑）
            Building clientBuilding = ClientBuildingCache.getBuilding(message.buildingId);
            if (clientBuilding == null) {
                // 客户端没有该建筑，初始化一个新实例（只包含核心字段）
                clientBuilding = new Building(
                        message.buildingName,
                        message.rent,
                        message.controlPos,
                        message.livingPos
                );
                clientBuilding.ID = message.buildingId; // 强制设置ID
            }

            // 2. 更新建筑核心数据（只同步必要字段）
            clientBuilding.buildingType = message.buildingType;
            clientBuilding.dimension = message.dimension;
            clientBuilding.rent = message.rent;
            clientBuilding.markedForDeletion = message.isMarkedForDeletion;

            // 3. 特殊处理：若建筑标记为删除，则从缓存移除
            if (message.isMarkedForDeletion) {
                ClientBuildingCache.removeBuilding(message.buildingId);
                // 触发UI刷新（如移除建筑显示）
                BuildingUIManager.removeBuildingUI(message.buildingId);
                return;
            }

            // 4. 更新居住人数（客户端不存储完整NPC列表，只显示数量）
            clientBuilding.occupants.clear(); // 清空本地列表（避免冗余）
            // （可选）添加占位符，仅用于UI显示人数
            for (int i = 0; i < message.occupantCount; i++) {
                clientBuilding.occupants.add(null); // 用null占位，仅标记数量
            }

            // 5. 保存到客户端缓存
            ClientBuildingCache.updateBuilding(clientBuilding);

            // 6. 触发UI刷新（如建筑信息面板、租金显示）
            BuildingUIManager.refreshBuildingUI(clientBuilding);
        }
    }

    // 客户端建筑缓存（用于存储同步的建筑数据）
    public static class ClientBuildingCache {
        private static final Map<UUID, Building> cachedBuildings = new HashMap<>();

        // 获取缓存的建筑
        public static Building getBuilding(UUID id) {
            return cachedBuildings.get(id);
        }

        // 更新建筑缓存
        public static void updateBuilding(Building building) {
            cachedBuildings.put(building.ID, building);
        }

        // 移除建筑缓存
        public static void removeBuilding(UUID id) {
            cachedBuildings.remove(id);
        }

        // 清空缓存（如切换世界时）
        public static void clear() {
            cachedBuildings.clear();
        }
    }

    // 建筑UI管理器（示例：处理UI刷新）
    public static class BuildingUIManager {
        // 刷新建筑UI显示（如租金面板、居住人数）
        public static void refreshBuildingUI(Building building) {
            // 实际项目中，这里会更新游戏内的UI组件
            // 例如：更新建筑信息面板的租金和居住人数
        }

        // 移除建筑UI显示
        public static void removeBuildingUI(UUID buildingId) {
            // 实际项目中，这里会从屏幕上移除对应的建筑UI
        }
    }
}
