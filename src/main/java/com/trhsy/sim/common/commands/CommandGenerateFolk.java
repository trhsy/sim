package com.trhsy.sim.common.commands;

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import com.trhsy.sim.packets.NetWorkLoader;
import com.trhsy.sim.packets.server.GenerateFolkPacket;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandGenerateFolk implements ICommand {

    private final List aliases;

    public CommandGenerateFolk() {
        aliases = new CopyOnWriteArrayList();
        aliases.add("generate NPC");
        aliases.add("generate SIM");
    }

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "generate NPC";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "generate NPC <name>";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argString) {
        try {
            if (argString.length == 0) {
                //FolkData.forceGenerateNewFolk(sender.getEntityWorld());
                NetWorkLoader.net.sendToServer(new GenerateFolkPacket(sender.getEntityWorld(), true));
            } else if (argString.length == 1) {
                FolkData.forceGenerateNewFolk(sender.getEntityWorld(), argString[0]);
            } else if (argString.length == 2) {
                FolkData.forceGenerateNewFolk(sender.getEntityWorld(), argString[0] + " " + argString[1]);
            } else {
                ModSimReloaded.sendChat(I18n.format("container.sim.commands2"));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("processCommand出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender p_71516_1_, String[] args, BlockPos blockPos) {
        if (args.length == 1) {
            String[] names = MinecraftServer.getServer().getAllUsernames();
            return CommandBase.getListOfStringsMatchingLastWord(args, names);
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

}