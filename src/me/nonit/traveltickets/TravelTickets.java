package me.nonit.traveltickets;

import me.nonit.traveltickets.commands.TTCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TravelTickets extends JavaPlugin
{
    private static String prefix;
    private static Double cost;
    private static String ticketAccount;
    private static boolean effect;
    private static boolean singleUse;
    private static Economy economy;

    public TravelTickets()
    {
        economy = null;
    }

    @Override
    public void onEnable()
    {
        Logger l = getLogger();
        Configuration c = getConfig();

        saveDefaultConfig();
        try
        {
            prefix = ChatColor.YELLOW + c.getString( "prefix" ) + ChatColor.GREEN + " ";
            cost = c.getDouble( "cost" );
            ticketAccount = c.getString( "ticket_account" );
            effect = c.getBoolean( "effect" );
            singleUse = c.getBoolean( "single_use" );
        }
        catch( Exception e )
        {
            l.info( "There was an error with your config..." );
            setEnabled( false );
        }

        if( cost != 0 && ! setupEconomy() )
        {
            l.info( "There is no econ plugin installed, set cost to 0 or try installing Fe!" );
            setEnabled( false );
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents( new PlayerListener(), this );

        getCommand("travelticket").setExecutor( new TTCommand() );
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public static Economy getEconomy()
    {
        return economy;
    }

    public static String getPrefix()
    {
        return prefix;
    }

    public static Double getCost()
    {
        return cost;
    }

    public static String getTicketAccount()
    {
        return ticketAccount;
    }

    public static boolean isEffect()
    {
        return effect;
    }

    public static boolean isSingleUse()
    {
        return singleUse;
    }
}
