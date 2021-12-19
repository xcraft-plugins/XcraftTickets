package de.xcraft.voronwe.XcraftTickets.Commands;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.XcraftUsage;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import java.util.List;
import org.bukkit.command.CommandSender;

public class AssigneeUsage extends XcraftUsage {
   TicketManager tManager;

   public AssigneeUsage(TicketManager tManager) {
      super("ASSIGNEE", Msg.USAGE_ASSIGNEE.toString());
      this.tManager = tManager;
   }

   public boolean validate(String arg) {
      return this.tManager.getAssignees().contains(arg);
   }

   public String getFailMessage() {
      return Msg.USAGE_NO_ASSIGNEE.toString();
   }

   public List<String> onTabComplete(List<String> list, CommandSender sender) {
      list.addAll(this.tManager.getAssignees());
      return list;
   }
}
