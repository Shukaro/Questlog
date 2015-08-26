package shukaro.questlog.command;

import cofh.core.command.ISubCommand;
import cofh.lib.util.helpers.StringHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSyntax implements ISubCommand
{
    public static CommandSyntax instance = new CommandSyntax();

    @Override
    public String getCommandName()
    {
        return "syntax";
    }

    @Override
    public int getPermissionLevel()
    {
        return -1;
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        switch (arguments.length)
        {
            case 1:
                StringBuilder output = new StringBuilder(StringHelper.localize("command.questlog.available") + " ");
                List<String> commandList = new ArrayList<String>(CommandHandler.getCommandList());
                Collections.sort(commandList, String.CASE_INSENSITIVE_ORDER);

                int commands = 0;
                for (int i = 0; i < commandList.size() - 1; i++)
                {
                    String name = commandList.get(i);
                    if (CommandHandler.canUseCommand(sender, CommandHandler.getCommandPermission(name), name))
                    {
                        String temp = StringHelper.localize("command.questlog." + name + ".syntax");
                        output.append(temp.substring(0, 9) + " " + StringHelper.YELLOW + temp.substring(10) + StringHelper.WHITE + ", ");
                        commands++;
                    }
                }
                if (commands > 0)
                    output.delete(output.length() - 2, output.length());
                String name = commandList.get(commandList.size() - 1);
                if (CommandHandler.canUseCommand(sender, CommandHandler.getCommandPermission(name), name))
                {
                    if (commands > 0)
                        output.append(" " + StringHelper.localize("command.questlog.and") + " ");
                    String temp = StringHelper.localize("command.questlog." + name + ".syntax");
                    output.append(temp.substring(0, 9) + " " + StringHelper.YELLOW + temp.substring(10) + StringHelper.WHITE + ".");
                }
                // FIXME: properly format this such that commands are clickable for auto-fill. paginate?
                sender.addChatMessage(new ChatComponentText(output.toString()));
                break;
            case 2:
                String commandName = arguments[1];
                if (!CommandHandler.getCommandExists(commandName))
                    throw new CommandNotFoundException("command.questlog.notFound");
                sender.addChatMessage(new ChatComponentText(StringHelper.localize("command.questlog." + commandName + ".syntax")));
                break;
            default:
                throw new WrongUsageException("command.questlog." + getCommandName() + ".syntax");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {

        if (args.length == 2)
            return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, CommandHandler.getCommandList());
        return null;

    }

}