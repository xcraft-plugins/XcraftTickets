package de.xcraft.voronwe.XcraftTickets.Commands.Mod;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.command.CommandSender;

public class PhrasesCommand extends XcraftCommand {
   public PhrasesCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      Map<String, String> map = manager.getPhrases();
      String msg;
      if (args[0].equals("list")) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_PHRASES_LIST.toString(), true);
         Iterator var6 = map.keySet().iterator();

         while(var6.hasNext()) {
            msg = (String)var6.next();
            pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_PHRASES_ENTRY.toString(Msg.Replace.NAME(msg), Msg.Replace.MESSAGE((String)map.get(msg))), false);
         }
      } else {
         int i;
         String key;
         if (args[0].equals("add")) {
            key = args[1];
            msg = "";

            for(i = 2; i < args.length; ++i) {
               msg = msg + args[i] + " ";
            }

            msg.trim();
            manager.getPhrases().put(key, msg);
            pManager.plugin.getMessenger().sendInfo(sender, "Successuflly added the phrase (" + key + " = " + msg + ")", true);
            pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_PHRASES_ADD.toString(Msg.Replace.NAME(key), Msg.Replace.MESSAGE(msg)), true);
         } else if (args[0].equals("remove")) {
            key = args[1];
            if (manager.getPhrases().remove(key) != null) {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_PHRASES_REMOVE.toString(Msg.Replace.NAME(key)), true);
            } else {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_PHRASE_NOT_FOUND.toString(Msg.Replace.NAME(key)), true);
            }
         } else {
            if (!args[0].equals("append")) {
               return false;
            }

            key = args[1];
            msg = "";

            for(i = 2; i < args.length; ++i) {
               msg = msg + args[i] + " ";
            }

            msg.trim();
            if (manager.getPhrases().containsKey(key)) {
               manager.getPhrases().put(key, (String)manager.getPhrases().get(key) + msg);
               pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_PHRASES_APPEND.toString(Msg.Replace.NAME(key), Msg.Replace.MESSAGE(msg)), true);
            } else {
               pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_PHRASE_NOT_FOUND.toString(Msg.Replace.NAME(key)), true);
            }
         }
      }

      return true;
   }
}
