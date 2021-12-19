package de.xcraft.voronwe.XcraftTickets.Commands.Mod;

import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.command.CommandSender;

public class StatsCommand extends XcraftCommand {
   public StatsCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      pManager.plugin.getMessenger().sendInfo(sender, "&6Stats:", true);
      Map<String, Integer> list = manager.cManager.getStats();
      Map<String, Integer> map = new HashMap();
      int rest = 0;
      Iterator var8 = list.keySet().iterator();

      while(var8.hasNext()) {
         String entry = (String)var8.next();
         if (!manager.getAssignees().contains(entry)) {
            rest += (Integer)list.get(entry);
         } else {
            map.put(entry.trim(), list.get(entry));
         }
      }

      list = null;
      StatsCommand.ValueComparator bvc = new StatsCommand.ValueComparator(map);
      Map<String, Integer> sorted_map = new TreeMap(bvc);
      sorted_map.putAll(map);
      Iterator var10 = sorted_map.keySet().iterator();

      while(var10.hasNext()) {
         String key = (String)var10.next();
         pManager.plugin.getMessenger().sendInfo(sender, "    &3" + key + ": &7" + map.get(key), true);
      }

      pManager.plugin.getMessenger().sendInfo(sender, "    &8Rest: &7" + rest, true);
      return true;
   }

   private class ValueComparator implements Comparator<String> {
      Map<String, Integer> base;

      public ValueComparator(Map<String, Integer> base) {
         this.base = base;
      }

      public int compare(String a, String b) {
         return (Integer)this.base.get(a) >= (Integer)this.base.get(b) ? -1 : 1;
      }
   }
}
