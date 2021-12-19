package de.xcraft.voronwe.XcraftTickets.Commands.User;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends XcraftCommand {
   public OpenCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      if (args.length == 1 && args[0].matches("\\d.*")) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.USAGE_NO_MESSAGE.toString(), true);
         return false;
      } else {
         String message = manager.getMessage(sender, args);
         Location loc = null;
         if (sender instanceof Player) {
            loc = ((Player)sender).getLocation();
         } else {
            loc = ((World)manager.plugin.getServer().getWorlds().get(0)).getSpawnLocation();
         }

         Ticket ticket = manager.addTicket(sender.getName(), loc, message);
         ticket.addToWatched(ticket.getOwner());
         Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.ID(ticket.getId()), Msg.Replace.NAME(sender.getName()), Msg.Replace.MESSAGE(message)};
         pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_OPEN_SUCCESSFUL.toString(replace), true);
         manager.inform(ticket, Msg.TICKET_BROADCAST_OPEN.toString(replace), true);
         return true;
      }
   }
}
