package me.nonit.traveltickets.commands;

import me.nonit.traveltickets.TravelTickets;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TravelTicketCommand implements CommandExecutor
{
    private static final String PREFIX = TravelTickets.getPrefix();
    private static final Double COST = TravelTickets.getCost();
    private static final String TICKET_ACCOUNT = TravelTickets.getTicketAccount();

    private static Economy e;

    public TravelTicketCommand()
    {
        e = TravelTickets.getEconomy();
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args )
    {
        if( ! (sender instanceof Player) )
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
            p.sendMessage( PREFIX + "Make a travel ticket to this spot with " + ChatColor.YELLOW + "/" + s + " <name> (amount)" + ChatColor.GREEN + " :D" );
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

        if( COST != 0 && e.getBalance( p ) < (COST * quantity) )
        {
            p.sendMessage( PREFIX + ChatColor.RED + "It costs " + ChatColor.WHITE + e.format( COST * quantity ) + ChatColor.RED +
                                                    " to make a ticket, you have " +
                                                    ChatColor.WHITE + e.format( e.getBalance( p ) ) + ChatColor.RED + " :(" );
            return true;
        }

        PlayerInventory i = p.getInventory();

        if( i.firstEmpty() == -1 )
        {
            p.sendMessage( PREFIX + ChatColor.RED + "Please make sure you have inventory space for your new ticket!" );
            return true;
        }

        String name = args[0];
        name = ChatColor.translateAlternateColorCodes( '&', name );

        if( name.length() > 8 )
        {
            name = name.substring( 0, 7 );
        }

        Location l = p.getLocation();

        // Make the ticket.
        String displayName = ChatColor.GOLD + "Travel Ticket:" + ChatColor.WHITE + " " + name;
        List<String> lore = new ArrayList<String>();
        lore.add( ChatColor.YELLOW + "Warp to " + name );
        lore.add( ChatColor.GREEN + "Right click to warp!" );
        lore.add( ChatColor.GRAY + "" + l.getBlockX() + "/" + l.getBlockY() + "/" + l.getBlockZ() );
        lore.add( ChatColor.GRAY + l.getWorld().getName() );

        ItemStack ticket = new ItemStack( Material.PAPER, quantity );
        ItemMeta im = ticket.getItemMeta();
        im.setDisplayName( displayName );
        im.setLore( lore );
        ticket.setItemMeta( im );

        // Done
        i.addItem( ticket );
        p.sendMessage( PREFIX + "Success you made a travel ticket to this spot :D" );

        if( COST != 0 && ! p.hasPermission( "traveltickets.free" ) )
        {
            e.withdrawPlayer( p, COST * quantity );

            if( ! TICKET_ACCOUNT.equals( "" ) )
            {
                e.depositPlayer( TICKET_ACCOUNT, COST * quantity );
            }

            p.sendMessage( PREFIX + "You payed " + ChatColor.YELLOW + e.format( COST ) + ChatColor.GREEN +
                    " for the ticket to " + ChatColor.YELLOW + name + ChatColor.GREEN + "!" );
        }

        return true;
    }
}
