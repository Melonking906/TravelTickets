package me.nonit.traveltickets.commands;

import me.nonit.traveltickets.TravelTickets;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class TTSubCommand
{
    private final String name;

    protected static final String PREFIX = TravelTickets.getPrefix();
    protected static final Double COST = TravelTickets.getCost();
    protected static final String TICKET_ACCOUNT = TravelTickets.getTicketAccount();
    protected static final  Economy ECONOMY = TravelTickets.getEconomy();;

    public TTSubCommand( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
}