package de.xcraft.voronwe.XcraftTickets.Commands.Admin;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import java.util.List;
import org.bukkit.command.CommandSender;

public class ModCommand extends XcraftCommand {
   public ModCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      List<String> assignees = manager.getAssignees();
      if (args.length < 1) {
         return false;
      } else {
         if (args[0].equals("list")) {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_LIST.toString(Msg.Replace.MISC(String.join(", ", assignees))), true);
         } else if (args[0].equals("add")) {
            if (assignees.add(args[1])) {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_ADD.toString(Msg.Replace.NAME(args[1])), true);
            }
         } else {
            if (!args[0].equals("remove")) {
               return false;
            }

            if (assignees.remove(args[1])) {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_REMOVE.toString(Msg.Replace.NAME(args[1])), true);
            } else {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_MOD_NOT_FOUND.toString(Msg.Replace.NAME(args[1])), true);
            }
         }

         return true;
      }
   }
}
