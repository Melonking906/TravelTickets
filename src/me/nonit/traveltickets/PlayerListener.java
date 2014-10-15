package me.nonit.traveltickets;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerListener implements Listener
{
    private static final String PREFIX = TravelTickets.getPrefix();
    private static final boolean EFFECT = TravelTickets.isEffect();
    private static final boolean SINGLE_USE = TravelTickets.isSingleUse();

    public PlayerListener()
    {
    }

    @EventHandler
    public void onTicketClick( PlayerInteractEvent e )
    {
        if( e.getAction().equals( Action.RIGHT_CLICK_AIR ) || e.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
        {
            Player p = e.getPlayer();
            ItemStack i = p.getItemInHand();

            if( !i.getType().equals( Material.PAPER ) )
            {
                return;
            }

            ItemMeta im = i.getItemMeta();

            if( !im.getDisplayName().contains( ChatColor.GOLD + "Travel Ticket" ) )
            {
                return;
            }

            if( !p.hasPermission( "traveltickets.use" ) )
            {
                p.sendMessage( PREFIX + ChatColor.RED + "Sorry, but you don't have permission to use travel tickets." );
                return;
            }

            List<String> lore = im.getLore();

            String[] nameStrings;
            String[] blockStrings;
            String worldString;

            // Attempt to get location info from ticket.
            try
            {
                nameStrings = ChatColor.stripColor( lore.get( 0 ) ).split( " " );
                blockStrings = ChatColor.stripColor( lore.get( 2 ) ).split( "/" );
                worldString = ChatColor.stripColor( lore.get( 3 ) );
            }
            catch( Exception ex )
            {
                return;
            }

            String name = nameStrings[2];
            double x = Double.parseDouble( blockStrings[0] );
            double y = Double.parseDouble( blockStrings[1] );
            double z = Double.parseDouble( blockStrings[2] );
            World world = Bukkit.getServer().getWorld( worldString );

            if( world == null )
            {
                p.sendMessage( PREFIX + ChatColor.RED + "Looks like the world " + ChatColor.WHITE + worldString + ChatColor.RED + " is unavailable right now :(" );
                return;
            }

            Location from = p.getLocation();
            Location to = new Location( world, x, y, z );

            // Destroy item
            if( SINGLE_USE )
            {
                useItem( p );
            }

            p.teleport( to );
            p.sendMessage( PREFIX + "Wooosh, you traveled to " + ChatColor.YELLOW + name + ChatColor.GREEN + "!" );

            // Spawn particles
            if( EFFECT )
            {
                TTUtils utils = new TTUtils();

                utils.spawnParticles( from );
                utils.spawnParticles( to );
            }
        }
    }

    private void useItem( Player player )
    {
        ItemStack handItems = player.getItemInHand();

        if( handItems.getAmount() > 1 )
        {
            handItems.setAmount( handItems.getAmount() - 1 );
            player.setItemInHand( handItems );
            return;
        }

        player.setItemInHand( new ItemStack( Material.AIR ) );
    }
}
