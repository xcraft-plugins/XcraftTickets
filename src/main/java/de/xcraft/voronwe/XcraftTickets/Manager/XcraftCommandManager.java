package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.XcraftPlugin;
import de.xcraft.voronwe.XcraftTickets.XcraftUsage;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class XcraftCommandManager implements TabExecutor {
   protected final XcraftPlugin plugin;
   private final Map<String, XcraftCommand> commands = new TreeMap();
   private final Map<String, XcraftUsage> usages = new HashMap();

   public XcraftCommandManager(XcraftPlugin plugin) {
      this.plugin = plugin;
      this.registerCommands();
   }

   public XcraftPlugin getPlugin() {
      return this.plugin;
   }

   protected abstract void registerCommands();

   protected void registerBukkitCommand(String bcmd) {
      PluginCommand command = this.plugin.getCommand(bcmd);
      if (command == null) {
         this.plugin.getMessenger().severe("The command '" + bcmd + "' was not defined in the plugin.yml!");
      } else {
         command.setExecutor(this);
         command.setTabCompleter(this);
         this.plugin.getMessenger().info("Registered '" + bcmd + "'.");
      }

   }

   public void registerCommand(XcraftCommand command) {
      try {
         this.commands.put(command.getPattern(), command);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void registerUsage(XcraftUsage usage) {
      this.usages.put(usage.getName(), usage);
   }

   public Map<String, XcraftCommand> getCommands() {
      return this.commands;
   }

   public boolean onCommand(CommandSender sender, Command bcmd, String label, String[] args) {
      String cmd = args.length > 0 ? args[0] : "";
      if (!cmd.equals("") && !cmd.equals("?") && !cmd.equals("help")) {
         if (cmd.equals("save")) {
            this.onSave(sender);
            return true;
         } else if (cmd.equals("load")) {
            this.onLoad(sender);
            return true;
         } else if (cmd.equals("reload")) {
            this.onSave(sender);
            this.onLoad(sender);
            return true;
         } else {
            List<XcraftCommand> matches = new ArrayList();
            Iterator var7 = this.commands.entrySet().iterator();

            while(var7.hasNext()) {
               Entry<String, XcraftCommand> commandEntry = (Entry)var7.next();
               if (cmd.matches((String)commandEntry.getKey()) && bcmd.getName().equals(((XcraftCommand)commandEntry.getValue()).getBukkitCommand())) {
                  if (((XcraftCommand)commandEntry.getValue()).getName().equals(cmd)) {
                     matches.clear();
                     matches.add(commandEntry.getValue());
                     break;
                  }

                  matches.add(commandEntry.getValue());
               }
            }

            if (matches.size() <= 1) {
               if (matches.size() == 0) {
                  this.plugin.getMessenger().sendInfo(sender, ChatColor.RED + "Unknown command: '" + cmd + "'!", true);
                  this.showHelp(sender, bcmd.getName());
                  return true;
               } else {
                  XcraftCommand command = (XcraftCommand)matches.get(0);
                  if (!sender.hasPermission(command.getPermission())) {
                     this.plugin.getMessenger().sendInfo(sender, ChatColor.RED + "You dont have permission to this command!", true);
                     return true;
                  } else if (!command.getUsage().equals("") && !this.validateUsage(sender, command.getUsage().replace("...", "").split(" "), 0, args, 1)) {
                     this.showUsage(sender, command);
                     return true;
                  } else {
                     if (!command.execute(this.plugin.getPluginManager(), sender, (String[])Arrays.copyOfRange(args, 1, args.length))) {
                        this.showUsage(sender, command);
                     }

                     return true;
                  }
               }
            } else {
               this.plugin.getMessenger().sendInfo(sender, "Available commands:", true);
               var7 = matches.iterator();

               while(var7.hasNext()) {
                  XcraftCommand c = (XcraftCommand)var7.next();
                  this.showUsage(sender, c);
               }

               return true;
            }
         }
      } else {
         this.showHelp(sender, bcmd.getName());
         return true;
      }
   }

   private boolean validateUsage(CommandSender sender, String[] commandUsage, int c, String[] params, int p) {
      if (commandUsage.length > c) {
         String usageName = commandUsage[c].substring(1, commandUsage[c].length() - 1);
         if (params.length > p) {
            if (this.usages.containsKey(usageName)) {
               if (((XcraftUsage)this.usages.get(usageName)).validate(params[p])) {
                  return this.validateUsage(sender, commandUsage, c + 1, params, p + 1);
               }

               if (commandUsage[c].startsWith("[")) {
                  return this.validateUsage(sender, commandUsage, c + 1, params, p);
               }

               this.plugin.getMessenger().sendInfo(sender, ((XcraftUsage)this.usages.get(usageName)).getFailMessage(), true);
               return false;
            }

            if (params.equals(usageName)) {
               return this.validateUsage(sender, commandUsage, c + 1, params, p + 1);
            }
         } else if (!commandUsage[c].startsWith("[")) {
            if (this.usages.containsKey(usageName)) {
               this.plugin.getMessenger().sendInfo(sender, ((XcraftUsage)this.usages.get(usageName)).getFailMessage(), true);
            } else {
               this.plugin.getMessenger().sendInfo(sender, ChatColor.RED + "Wrong count of arugments", true);
            }

            return false;
         }
      }

      return true;
   }

   private void showHelp(CommandSender sender, String cmd) {
      this.plugin.getMessenger().sendInfo(sender, ChatColor.GOLD + this.plugin.getDescription().getVersion() + " by " + String.join(", ", this.plugin.getDescription().getAuthors()) + ":", true);
      Iterator var3 = this.commands.values().iterator();

      while(var3.hasNext()) {
         XcraftCommand command = (XcraftCommand)var3.next();
         if (cmd.matches(command.getBukkitCommand())) {
            this.showUsage(sender, command);
         }
      }

   }

   private void showUsage(CommandSender sender, XcraftCommand command) {
      if (sender.hasPermission(command.getPermission())) {
         String usage = command.getUsage();
         Iterator var4 = this.usages.keySet().iterator();

         while(var4.hasNext()) {
            String usageName = (String)var4.next();
            if (usage.contains(usageName)) {
               usage = usage.replace(usageName, ((XcraftUsage)this.usages.get(usageName)).getAlias());
            }
         }

         this.plugin.getMessenger().sendInfo(sender, "&8->&a/" + command.getBukkitCommand() + " " + command.getName() + " " + (usage.isEmpty() ? "" : usage + " ") + "&3- " + command.getDesc(), false);
      }
   }

   public void onLoad(CommandSender sender) {
      if (sender.hasPermission(this.plugin.getName() + ".Reload")) {
         this.plugin.reloadConfig();
         this.plugin.getConfigManager().config = this.plugin.getConfig();
         this.plugin.getConfigManager().load();
         this.plugin.getMessenger().sendInfo(sender, "&aLoaded all data from disc!", true);
         this.plugin.getMessenger().info(this.plugin.getDescription().getName() + " manual reload!");
      }

   }

   public void onSave(CommandSender sender) {
      if (sender.hasPermission(this.plugin.getName() + ".Save")) {
         this.plugin.getConfigManager().save();
         this.plugin.saveConfig();
         this.plugin.getMessenger().sendInfo(sender, "&aSaved all data to disc!", true);
         this.plugin.getMessenger().info(this.plugin.getDescription().getName() + " manual save!");
      }

   }

   public List<String> onTabComplete(CommandSender sender, Command bcmd, String alias, String[] args) {
      List<String> list = new ArrayList();
      Iterator var6 = this.plugin.getCommandManager().getCommands().values().iterator();

      while(var6.hasNext()) {
         XcraftCommand command = (XcraftCommand)var6.next();
         if (bcmd.getName().matches(command.getBukkitCommand()) && sender.hasPermission(command.getPermission())) {
            if (args.length > 1 && args[0].matches(command.getPattern())) {
               String[] usages = command.getUsage().split(" ");
               String token = args[args.length - 1].trim().toLowerCase();
               this.getUsageList(sender, list, usages, token, args.length - 2);
               break;
            }

            if (args.length <= 1 && (args[0].equals("") || command.getName().startsWith(args[0]))) {
               list.add(command.getName());
            }
         }
      }

      return list;
   }

   private List<String> getUsageList(CommandSender sender, List<String> list, String[] usages, String token, int a) {
      if (a >= usages.length - 1 && usages[usages.length - 1].equals("...") && !usages[usages.length - 2].startsWith("[")) {
         return this.getUsageList(sender, list, usages, token, usages.length - 2);
      } else if (a >= usages.length) {
         return list;
      } else {
         String usage;
         String[] var7;
         int var8;
         int var9;
         String u;
         if (usages[a].startsWith("<") && usages[a].endsWith(">")) {
            usage = usages[a].substring(1, usages[a].length() - 1);
            if (!usage.contains("/")) {
               this.onTabComplete(list, sender, usage, token);
               return list;
            } else {
               var7 = usage.split("/");
               var8 = var7.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  u = var7[var9];
                  this.onTabComplete(list, sender, u, token);
               }

               return list;
            }
         } else if (usages[a].startsWith("[") && usages[a].endsWith("]")) {
            usage = usages[a].substring(1, usages[a].length() - 1);
            if (!usage.contains("/")) {
               this.onTabComplete(list, sender, usage, token);
               return this.getUsageList(sender, list, usages, token, a + 1);
            } else {
               var7 = usage.split("/");
               var8 = var7.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  u = var7[var9];
                  this.onTabComplete(list, sender, u, token);
               }

               return list;
            }
         } else {
            return list;
         }
      }
   }

   protected List<String> onTabComplete(List<String> list, CommandSender sender, String usage, String token) {
      boolean foundUsage = false;
      token = token.toLowerCase();
      Iterator var6;
      if (usage.equals("Name")) {
         var6 = this.plugin.getServer().getOnlinePlayers().iterator();

         while(var6.hasNext()) {
            Player player = (Player)var6.next();
            if (player.getName().startsWith(token)) {
               list.add(player.getName());
            }
         }

         return list;
      } else {
         var6 = this.usages.keySet().iterator();

         while(true) {
            String usageName;
            do {
               if (!var6.hasNext()) {
                  if (!foundUsage) {
                     list.add(usage);
                  }

                  return list;
               }

               usageName = (String)var6.next();
            } while(!usage.equals(usageName));

            ((XcraftUsage)this.usages.get(usageName)).onTabComplete(list, sender);
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
               if (!((String)iterator.next()).toLowerCase().startsWith(token)) {
                  iterator.remove();
               }
            }

            foundUsage = true;
         }
      }
   }
}
