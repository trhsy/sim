package com.trhsy.sim.commands;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenSetupGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.commands
 * @ClassName: CommandStart
 * @Description:
 * @date 2023/5/4 10:14
 */
public class CommandStart implements ICommand {
    private final List aliases = new ArrayList();

    public CommandStart() {
        this.aliases.add("simmode");
    }

    @Override
    public String getCommandName() {
        return "simmode";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "simmode <amount>";
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 && sender instanceof EntityPlayer) {
            try {
                /*if (ModSimLoader.gamemode != 999) {
                    //模拟城市已经启动，您当前无法更改游戏模式
                    ModSimLoader.sendChat(I18n.format("container.sim.Command1"));
                } else {*/
                    NetWorkLoader.net.sendTo(new PacketOpenSetupGui(), (EntityPlayerMP)sender);
                //}
            } catch (Exception var5) {
                ModSimLoader.sendChat(I18n.format("container.sim.Command2"));
            }
        } else {
            ModSimLoader.sendChat(I18n.format("container.sim.Command2"));
        }

    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
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
