package me.nonit.traveltickets.commands;

import me.nonit.traveltickets.TTUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TTGiveCommand extends TTSubCommand
{
    public TTGiveCommand()
    {
        super( "give" );
    }

    public boolean onCommand( CommandSender sender, Command cmd, String commandLabel, String[] args )
    {
        if( ! sender.hasPermission( "traveltickets.give" ) )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Sorry, you don't have permission to give travel tickets :(" );
            return true;
        }

        if( args.length < 7 )
        {
            sender.sendMessage( PREFIX + "Give a travel ticket with " + ChatColor.YELLOW + "/" + commandLabel + " give <player> <name> <world> <x> <y> <z> (amount) (cost)" + ChatColor.GREEN + "!" );
            return true;
        }

        Player p = Bukkit.getPlayer( args[1] );

        if( p == null )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Sorry but " +  ChatColor.WHITE + args[1] + ChatColor.RED + " is not online :(" );
            return true;
        }

        PlayerInventory i = p.getInventory();

        if( i.firstEmpty() == -1 )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Sorry but " + ChatColor.WHITE + p.getName() + ChatColor.RED + " has a full inventory!" );
            p.sendMessage( PREFIX + ChatColor.RED + sender.getName() + " tried to give you a ticket but your inventory is full :(" );
            return true;
        }

        String name;
        World world;
        double x;
        double y;
        double z;
        int quantity = 1;
        double cost = 0;

        try
        {
            name = args[2];
            world = Bukkit.getWorld( args[3] );
            x = Double.parseDouble( args[4] );
            y = Double.parseDouble( args[5] );
            z = Double.parseDouble( args[6] );
            if( args.length > 7 )
            {
                quantity = Integer.parseInt( args[7] );
            }
            if( args.length > 8 )
            {
                String costArg = args[8];
                if( costArg.equals( "default" ) )
                {
                    cost = COST * quantity;
                }
                else
                {
                    cost = Double.parseDouble( costArg ) * quantity;
                }
            }
        }
        catch( Exception e )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Sorry but there was an error in your arguments! Sure its correct?" );
            return true;
        }

        if( world == null )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Erm.. there is no world called " + ChatColor.WHITE + args[3] + ChatColor.RED +" on this server!" );
            return true;
        }

        if( cost != 0 && ECONOMY.getBalance( p ) < cost )
        {
            sender.sendMessage( PREFIX + ChatColor.RED + "Sadly " + p.getDisplayName() + " cant afford to pay " + ChatColor.WHITE + ECONOMY.format( cost ) + ChatColor.RED + "!" );

            p.sendMessage( PREFIX + ChatColor.RED + sender.getName() + " tried to charge you " + ChatColor.WHITE + ECONOMY.format( cost ) + ChatColor.RED + " for a ticket, but you only have " + ChatColor.WHITE + ECONOMY.getBalance( p ) + ChatColor.RED + " :(" );
            return true;
        }

        Location location = new Location( world, x, y, z );

        TTUtils utils = new TTUtils();
        ItemStack ticket = utils.makeTicket( name, location, quantity );

        i.addItem( ticket );

        sender.sendMessage( PREFIX + "Success! You gave a ticket called " + ChatColor.YELLOW + name + ChatColor.GREEN + " to " + ChatColor.YELLOW + p.getDisplayName() + ChatColor.GREEN + "!" );
        p.sendMessage( PREFIX + "You received a travel ticket to " + ChatColor.YELLOW + name + ChatColor.GREEN + " from " + ChatColor.YELLOW + sender.getName() + ChatColor.GREEN + "!" );

        if( cost != 0 && ! p.hasPermission( "traveltickets.free" ) )
        {
            ECONOMY.withdrawPlayer( p, cost );

            if( ! TICKET_ACCOUNT.equals( "" ) )
            {
                ECONOMY.depositPlayer( TICKET_ACCOUNT, cost );
            }

            p.sendMessage( PREFIX + "You were charged " + ChatColor.YELLOW + ECONOMY.format( cost ) + ChatColor.GREEN +
                    " for ticket(s) to " + ChatColor.YELLOW + name + ChatColor.GREEN + "!" );
        }

        return true;
    }
}
