package de.xcraft.voronwe.XcraftTickets.Commands;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.XcraftUsage;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import java.util.List;
import org.bukkit.command.CommandSender;

public class MessageUsage extends XcraftUsage {
   TicketManager tManager;

   public MessageUsage(TicketManager tManager) {
      super("MESSAGE", Msg.USAGE_MESSAGE.toString());
      this.tManager = tManager;
   }

   public boolean validate(String arg) {
      return true;
   }

   public String getFailMessage() {
      return Msg.USAGE_NO_MESSAGE.toString();
   }

   public List<String> onTabComplete(List<String> list, CommandSender sender) {
      if (sender.hasPermission("XcraftTickets.Phrases")) {
         list.addAll(this.tManager.getPhrases().values());
      }

      return list;
   }
}
