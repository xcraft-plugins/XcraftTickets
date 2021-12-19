package de.xcraft.voronwe.XcraftTickets.Commands.User;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.ConfigManager;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.command.CommandSender;

public class ViewCommand extends XcraftCommand {
   public ViewCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      ConfigManager cManager = manager.getPlugin().getConfigManager();
      String assignee;
      if (!sender.getName().toUpperCase().equals("CONSOLE")) {
         assignee = sender.getName();

         try {
            if (cManager.getReminder(assignee).contains(String.valueOf(id))) {
               cManager.removeReminder(assignee, id);
            }
         } catch (NullPointerException var13) {
         }
      }

      if (ticket == null) {
         ticket = manager.getArchivedTicket(id);
         if (ticket == null) {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), false);
            return true;
         }
      }

      if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.View.All")) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(Msg.Replace.ID(id)), false);
         return true;
      } else {
         assignee = "";
         if (ticket.isAssigned()) {
            assignee = Msg.TICKET_VIEW_ASSIGNEE.toString(Msg.Replace.NAME(ticket.getAssignee()));
         }

         Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.ID(id), Msg.Replace.NAME(ticket.getOwner()), Msg.Replace.ASSIGNEE(assignee)};
         pManager.plugin.getMessenger().sendInfo(sender, Msg.BREAK.toString(), false);
         pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_VIEW_INFO.toString(replace), false);
         String[] entries = ticket.getLog().getEntries(ticket.getId());
         int start = 0;
         if (args.length < 2 && entries.length > 5) {
            start = entries.length - 4;
            pManager.plugin.getMessenger().sendInfo(sender, entries[0], false);
            pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_VIEW_BREAK.toString(Msg.Replace.ID(id), Msg.Replace.MISC(String.valueOf(entries.length - 4))), false);
         }

         for(int i = start; i < entries.length; ++i) {
            pManager.getPlugin().getMessenger().sendInfo(sender, entries[i], false);
         }

         ticket.addToWatched(sender.getName());
         pManager.plugin.getMessenger().sendInfo(sender, Msg.BREAK.toString(), false);
         return true;
      }
   }
}
