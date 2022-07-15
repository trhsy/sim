package com.trhsy.sim.common.commands;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令更改金钱
 */
public class CommandChangeCredits implements ICommand {

    private final List aliases;

    /**
     * 命令更改金币
     */
    public CommandChangeCredits() {
        aliases = new ArrayList();
        aliases.add("credits");
    }

    @Override
    public String getCommandName() {
        return "credits";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "change credits <amount>";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argString) {
        try {
        if (argString.length == 0) {
            //无效的参数，应该是：/credits <amount>
            ModSimReloaded.sendChat(I18n.format("container.sim.commands1"));
            return;
        }
            if (argString.length < 2) {
                ModSimReloaded.states.credits = Float.parseFloat(argString[0]);
                ModSimReloaded.states.saveStates();
            } else {
                //无效的参数，应该是：/credits <amount>
                ModSimReloaded.sendChat(I18n.format("container.sim.commands1"));
                return;
            }
        } catch (Exception e) {
            //金额必须是数字！
            ModSimReloaded.sendChat(I18n.format("container.sim.commands2"));
            //ModSimReloaded.log.error("初始化对齐梁出差了：" + e.getMessage());
            return;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
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

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
