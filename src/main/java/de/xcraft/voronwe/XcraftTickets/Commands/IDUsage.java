package de.xcraft.voronwe.XcraftTickets.Commands;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.XcraftUsage;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;

public class IDUsage extends XcraftUsage {
   TicketManager tManager;

   public IDUsage(TicketManager tManager) {
      super("ID", Msg.USAGE_ID.toString());
      this.tManager = tManager;
   }

   public boolean validate(String arg) {
      return arg.matches("\\d.*");
   }

   public String getFailMessage() {
      return Msg.USAGE_NO_TICKET_ID.toString();
   }

   public List<String> onTabComplete(List<String> list, CommandSender sender) {
      int lastTicket = this.tManager.getLastTicket(sender);
      List<Ticket> ticketsUnassigned = new ArrayList();
      List<Ticket> ticketsAssigned = new ArrayList();
      List<Ticket> ticketsOwned = new ArrayList();
      Iterator var7 = this.tManager.getTickets().iterator();

      while(true) {
         while(true) {
            Ticket ticket;
            do {
               if (!var7.hasNext()) {
                  this.sort(ticketsAssigned);
                  this.sort(ticketsUnassigned);
                  this.sort(ticketsOwned);
                  if (lastTicket != -1) {
                     list.add(String.valueOf(lastTicket));
                  }

                  var7 = ticketsAssigned.iterator();

                  while(var7.hasNext()) {
                     ticket = (Ticket)var7.next();
                     list.add(String.valueOf(ticket.getId()));
                  }

                  var7 = ticketsUnassigned.iterator();

                  while(var7.hasNext()) {
                     ticket = (Ticket)var7.next();
                     list.add(String.valueOf(ticket.getId()));
                  }

                  var7 = ticketsOwned.iterator();

                  while(var7.hasNext()) {
                     ticket = (Ticket)var7.next();
                     list.add(String.valueOf(ticket.getId()));
                  }

                  return list;
               }

               ticket = (Ticket)var7.next();
            } while(lastTicket != -1 && lastTicket == ticket.getId());

            if (ticket.isAssigned() && ticket.isAssignee(sender, this.tManager)) {
               ticketsAssigned.add(ticket);
            } else if (!ticket.isAssigned()) {
               ticketsUnassigned.add(ticket);
            } else if (ticket.getOwner().equals(sender.getName())) {
               ticketsOwned.add(ticket);
            }
         }
      }
   }

   private void sort(List<Ticket> ticketsAssigned) {
      for(int n = ticketsAssigned.size(); n > 1; --n) {
         for(int i = 0; i < n - 1; ++i) {
            if (((Ticket)ticketsAssigned.get(i)).getProcessed() > ((Ticket)ticketsAssigned.get(i + 1)).getProcessed()) {
               Ticket temp = (Ticket)ticketsAssigned.get(i);
               ticketsAssigned.set(i, ticketsAssigned.get(i + 1));
               ticketsAssigned.set(i + 1, temp);
            }
         }
      }

   }
}
