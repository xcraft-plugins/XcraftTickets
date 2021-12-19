package de.xcraft.voronwe.XcraftTickets.Commands.User;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends XcraftCommand {
   public ListCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int counter = -1;
      List<Ticket> tickets = (List)manager.getTickets().stream().sorted(Comparator.comparingInt(Ticket::getId)).collect(Collectors.toList());
      Iterator var7 = tickets.iterator();

      while(true) {
         Ticket ticket;
         do {
            if (!var7.hasNext()) {
               if (counter == -1) {
                  pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST_EMTPY.toString(), true);
               } else {
                  pManager.plugin.getMessenger().sendInfo(sender, Msg.BREAK.toString(), false);
               }

               return true;
            }

            ticket = (Ticket)var7.next();
         } while(!ticket.getOwner().equals(sender.getName()) && (args.length <= 0 || !args[0].matches("a.*") || !sender.hasPermission("XcraftTickets.List.All")) && !(sender instanceof ConsoleCommandSender) && (ticket.getAssignee() != null || !sender.hasPermission("XcraftTickets.View.All")) && (ticket.getAssignee() == null || !ticket.getAssignee().equals(sender.getName()) && !manager.getPlugin().getPermission().playerInGroup((Player)sender, ticket.getAssignee())));

         if (counter == -1) {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.BREAK.toString(), false);
            pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST_HEAD.toString(), false);
         }

         ++counter;
         int c = ticket.getLog().size() - 1;
         if (c == 0 && !ticket.hasWatched(sender.getName())) {
            ticket.addToWatched(sender.getName());
         }

         String comments;
         if (ticket.hasWatched(sender.getName())) {
            comments = Msg.TICKET_LIST_COMMENT_READ.toString(Msg.Replace.COMMENTS(String.valueOf(c)));
         } else {
            comments = Msg.TICKET_LIST_COMMENT_UNREAD.toString(Msg.Replace.COMMENTS(String.valueOf(c)));
         }

         Log log = ticket.getLog();
         String userStatus = "";
         if (manager.getPlugin().getServer().getPlayerExact(ticket.getOwner()) != null) {
            userStatus = Msg.TICKET_LIST_MISC_ONLINE.toString();
         } else {
            userStatus = Msg.TICKET_LIST_MISC_OFFLINE.toString();
         }

         String assignee = "";
         if (ticket.isAssigned()) {
            assignee = Msg.TICKET_LIST_ASSIGNEE.toString(Msg.Replace.NAME(ticket.getAssignee()));
         }

         Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.ID(ticket.getId()), Msg.Replace.TIME(log.getDate()), Msg.Replace.MISC(userStatus), Msg.Replace.NAME(ticket.getOwner()), Msg.Replace.ASSIGNEE(assignee), Msg.Replace.COMMENTS(comments), Msg.Replace.MESSAGE(log.getEntry(0).message)};
         pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST.toString(replace), false);
      }
   }
}
