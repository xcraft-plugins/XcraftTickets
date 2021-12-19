package de.xcraft.voronwe.XcraftTickets.Commands.User;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.command.CommandSender;

public class CloseCommand extends XcraftCommand {
   public CloseCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      if (ticket == null) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), true);
         return true;
      } else if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.Close.All")) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
         return true;
      } else {
         String message = manager.getMessage(sender, args);
         ticket.getLog().add(Log.EntryType.CLOSE, sender.getName(), message);
         if (ticket.getId() == manager.getLastTicket(sender)) {
            manager.setLastTicket(sender, -1);
         }

         manager.setTicketArchived(ticket);
         Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.NAME(sender.getName()), Msg.Replace.ID(id), Msg.Replace.MESSAGE(message)};
         manager.inform(ticket, Msg.TICKET_BROADCAST_CLOSE.toString(replace), true);
         if (!sender.getName().equals(ticket.getOwner())) {
            manager.getPlugin().getConfigManager().addReminder(ticket.getOwner(), id);
         }

         return true;
      }
   }
}
