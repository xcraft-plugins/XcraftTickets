package de.xcraft.voronwe.XcraftTickets.Commands.Mod;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.command.CommandSender;

public class UndoCommand extends XcraftCommand {
   public UndoCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      if (ticket == null) {
         ticket = manager.getArchivedTicket(id);
      }

      if (ticket == null) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), true);
         return true;
      } else {
         Log.LogEntry entry = ticket.getLog().getEntry(ticket.getLog().size() - 1);
         if (!entry.player.equals(sender.getName())) {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_UNDO_IMPOSSIBLE.toString(), true);
            return true;
         } else {
            boolean done = false;
            switch(entry.type) {
            case OPEN:
               manager.deleteTicket(ticket);
               done = true;
               break;
            case COMMENT:
               ticket.getLog().remove(entry);
               done = true;
               break;
            case REOPEN:
               ticket.getLog().remove(entry);
               manager.setTicketArchived(ticket);
               done = true;
               break;
            case CLOSE:
               ticket.getLog().remove(entry);
               manager.addTicket(ticket);
               done = true;
               break;
            case ASSIGN:
               ticket.getLog().remove(entry);
               ticket.setAssignee((String)null);
               done = true;
               break;
            case SETWARP:
               pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_UNDO_IMPOSSIBLE.toString(), true);
               return true;
            }

            if (done) {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_UNDO_SUCCESSFUL.toString(), true);
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
