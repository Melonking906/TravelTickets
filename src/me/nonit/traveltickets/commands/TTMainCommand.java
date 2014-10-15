package me.nonit.traveltickets.commands;

import me.nonit.traveltickets.TTUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TTMainCommand extends TTSubCommand
{
    public TTMainCommand()
    {
        super( "" );
    }

    public boolean onCommand( CommandSender sender, Command cmd, String commandLabel, String[] args )
    {
        if( ! (sender instanceof Player ) )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "You must be a player to make tickets!" );
            return true;
        }

        Player p = (Player) sender;

        if( ! p.hasPermission( "traveltickets.make" ) )
        {
            p.sendMessage( PREFIX + ChatColor.RED + "Sorry, you don't have permission to make travel tickets :(" );
            return true;
        }

        if( args.length < 1 )
        {
            p.sendMessage( PREFIX + "Make a travel ticket to this spot with " + ChatColor.YELLOW + "/" + commandLabel + " <name> (amount)" + ChatColor.GREEN + " :D" );
            return true;
        }

        int quantity = 1;

        if( args.length > 1 )
        {
            try
            {
                quantity = Integer.parseInt( args[1] );
            }
            catch( Exception e )
            {
                quantity = 1;
            }
        }

        if( COST != 0 && ECONOMY.getBalance( p ) < (COST * quantity) )
        {
            p.sendMessage( PREFIX + ChatColor.RED + "It costs " + ChatColor.WHITE + ECONOMY.format( COST * quantity ) + ChatColor.RED +
                    " to make a ticket, you have " +
                    ChatColor.WHITE + ECONOMY.format( ECONOMY.getBalance( p ) ) + ChatColor.RED + " :(" );
            return true;
        }

        PlayerInventory i = p.getInventory();

        if( i.firstEmpty() == -1 )
        {
            p.sendMessage( PREFIX + ChatColor.RED + "Please make sure you have inventory space for your new ticket!" );
            return true;
        }

        String name = args[0];
        Location location = p.getLocation();

        TTUtils utils = new TTUtils();
        ItemStack ticket = utils.makeTicket( name, location, quantity );

        i.addItem( ticket );
        p.sendMessage( PREFIX + "Success you made a travel ticket to this spot :D" );

        if( COST != 0 && ! p.hasPermission( "traveltickets.free" ) )
        {
            ECONOMY.withdrawPlayer( p, COST * quantity );

            if( ! TICKET_ACCOUNT.equals( "" ) )
            {
                ECONOMY.depositPlayer( TICKET_ACCOUNT, COST * quantity );
            }

            p.sendMessage( PREFIX + "You payed " + ChatColor.YELLOW + ECONOMY.format( COST ) + ChatColor.GREEN +
                    " for the ticket to " + ChatColor.YELLOW + name + ChatColor.GREEN + "!" );
        }

        return true;
    }
}
