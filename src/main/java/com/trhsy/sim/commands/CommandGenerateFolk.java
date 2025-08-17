package com.trhsy.sim.commands;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.commands
 * @ClassName: CommandGenerateFolk
 * @Description: 生成NPC
 * @date 2023/11/20 下午 3:45
 */
public class CommandGenerateFolk implements ICommand {
    private final List aliases;
    NpcData theFolk;
    public CommandGenerateFolk() {
        aliases = new CopyOnWriteArrayList();
        aliases.add("generateNPC");
    }
    @Override
    public String getName() {
        return "generateNPC";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "generateNPC <name>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            if (args.length == 0) {
                ModSimLoader.log.info("获得重生NPC命令");
//                Minecraft mc = Minecraft.getMinecraft();
//                World world = mc.world;
                // 获取发送者所在世界
                World world = sender.getEntityWorld();
                if (world == null) {
                    ModSimLoader.log.error("生成NPC失败：世界对象为空");
                    return;
                }
// 获取发送者的位置（如玩家坐标）
                BlockPos senderPos = sender.getPosition();
                if (senderPos == null) {
                    ModSimLoader.log.error("生成NPC失败：发送者位置为空");
                    return;
                }
                this.theFolk = new NpcData(world, true);
                //NetWorkLoader.net.sendToServer(new PacketNewFolk(true));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("processCommand出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}