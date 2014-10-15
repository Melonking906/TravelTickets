package me.nonit.traveltickets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TTCommand implements CommandExecutor
{
    private final List<TTSubCommand> commands;

    public TTCommand()
    {
        commands = new ArrayList<TTSubCommand>();

        commands.add( new TTMainCommand() );
        commands.add( new TTGiveCommand() );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String commandLabel, String[] args )
    {
        String subCommand = "";

        if( args.length > 0 )
        {
            subCommand = args[0];
        }

        for( TTSubCommand command : commands )
        {
            if( command.getName().equalsIgnoreCase( subCommand ) )
            {
                command.onCommand( sender, cmd, commandLabel, args );
                return true;
            }
        }

        commands.get( 0 ).onCommand( sender, cmd, commandLabel, args );
        return true;
    }
}