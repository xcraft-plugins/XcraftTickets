package de.xcraft.voronwe.XcraftTickets.Commands.User;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends XcraftCommand {
   public SetWarpCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      if (ticket == null) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), true);
         return true;
      } else if (!sender.getName().equals(ticket.getOwner())) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
         return true;
      } else {
         if (!(sender instanceof Player)) {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_NOT_FROM_CONSOLE.toString(), true);
         } else {
            Player player = (Player)sender;
            Location loc = player.getLocation();
            ticket.setLoc(loc);
            String message = manager.getMessage(sender, args);
            ticket.getLog().add(Log.EntryType.SETWARP, sender.getName(), message);
            Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.ID(ticket.getId()), Msg.Replace.NAME(sender.getName()), Msg.Replace.MESSAGE(message)};
            manager.inform(ticket, Msg.TICKET_BROADCAST_SETWARP.toString(replace), true);
            ticket.clearWatched();
         }

         return true;
      }
   }
}
