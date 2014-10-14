package me.nonit.traveltickets;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;

import java.util.HashSet;

public class TTUtils
{
    public TTUtils()
    {
    }

    public void spawnParticles( Location location )
    {
        Location newLocation = location.add( 0, 1.2, 0 );

        HashSet<ChordObject> chords = new HashSet<ChordObject>();
        chords.add( new ChordObject( 0.6D, 0.4D ) );
        chords.add( new ChordObject( 0.4D, 0.8D ) );
        chords.add( new ChordObject( 0.0D, 0.6D ) );
        chords.add( new ChordObject( 0.0D, 0.4D ) );
        chords.add( new ChordObject( 0.4D, 0.2D ) );

        double y = 0.1;

        for( ChordObject chord : chords )
        {
            newLocation.add( chord.getX(), y, chord.getZ() );
            Bukkit.getWorld( location.getWorld().getUID() ).playEffect( newLocation, Effect.HAPPY_VILLAGER, 20+(((int)y)*10) );
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
