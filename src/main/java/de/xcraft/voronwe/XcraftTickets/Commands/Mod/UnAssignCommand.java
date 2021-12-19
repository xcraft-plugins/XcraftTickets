package de.xcraft.voronwe.XcraftTickets.Commands.Mod;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.command.CommandSender;

public class UnAssignCommand extends XcraftCommand {
   public UnAssignCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      if (ticket == null) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), true);
         return true;
      } else {
         ticket.setAssignee((String)null);
         ticket.clearWatched();
         ticket.addToWatched(sender.getName());
         manager.inform(ticket, Msg.TICKET_BROADCAST_UNASSIGN.toString(Msg.Replace.ID(id)), true);
         return true;
      }
   }
}
