package com.trhsy.sim.commands;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketUpdateMoney;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.commands
 * @ClassName: CommandChangeCredits
 * @Description: 命令更改金钱
 * @date 2022/10/9 13:21
 */
public class CommandChangeCredits implements ICommand {

    private final List aliases;
    /**
     * 命令更改金币
     */
    public CommandChangeCredits() {
        aliases = new CopyOnWriteArrayList();
        aliases.add("credits");
    }
    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        //String s= I18n.format("container.sim.change_credits");
        return "credits <amount>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] argString) throws CommandException {
        try {
            if(argString.length>0){
                ModSimLoader.money = Float.parseFloat(argString[0]);
                ModSimLoader.saveStates();
                NetWorkLoader.net.sendToAll(new PacketUpdateMoney());
            }else{
                //无效的参数，应该是：/credits <amount>
                ModSimLoader.sendChat(I18n.format("container.sim.commands1"));
                return;
            }
        } catch (Exception e) {
            //金额必须是数字！
            ModSimLoader.sendChat(I18n.format("container.sim.commands2"));
            //StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return;
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
