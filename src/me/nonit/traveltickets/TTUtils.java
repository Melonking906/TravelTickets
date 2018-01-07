package me.nonit.traveltickets;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TTUtils
{
    public TTUtils()
    {
    }

    public ItemStack makeTicket( String name, Location location, int quantity )
    {
        name = ChatColor.translateAlternateColorCodes( '&', name );
        if( name.length() > 8 )
        {
            name = name.substring( 0, 7 );
        }

        String displayName = ChatColor.GOLD + "Travel Ticket:" + ChatColor.WHITE + " " + name;
        List<String> lore = new ArrayList<String>();
        lore.add( ChatColor.YELLOW + "Warp to " + name );
        lore.add( ChatColor.GREEN + "Right click to warp!" );
        lore.add( ChatColor.GRAY + "" + location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() );
        lore.add( ChatColor.GRAY + location.getWorld().getName() );

        ItemStack ticket = new ItemStack( Material.PAPER, quantity );
        ItemMeta im = ticket.getItemMeta();
        im.setDisplayName( displayName );
        im.setLore( lore );
        ticket.setItemMeta( im );

        return ticket;
    }

    public void spawnParticles( Location location )
    {
        Location newLocation = location.add( 0, 1.2, 0 );

        HashSet<ChordObject> chords = new HashSet<>();
        chords.add( new ChordObject( 0.6D, 0.4D ) );
        chords.add( new ChordObject( 0.4D, 0.8D ) );
        chords.add( new ChordObject( 0.0D, 0.6D ) );
        chords.add( new ChordObject( 0.0D, 0.4D ) );
        chords.add( new ChordObject( 0.4D, 0.2D ) );

        double y = 0.1;

        for( ChordObject chord : chords )
        {
            newLocation.add( chord.getX(), y, chord.getZ() );
            Bukkit.getWorld( location.getWorld().getUID() ).spawnParticle( Particle.VILLAGER_HAPPY, newLocation, 20+(((int)y)*10) );
            newLocation.subtract( chord.getX(), 0, chord.getZ() );
            y += 0.02;
        }
    }

    private class ChordObject
    {
        private double x;
        private double z;

        public ChordObject( double x, double z )
        {
            this.x = x;
            this.z = z;
        }

        public double getX()
        {
            return x;
        }
        public double getZ()
        {
            return z;
        }
    }
}
