package de.xcraft.voronwe.XcraftTickets.Commands;

import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import org.bukkit.command.CommandSender;

public abstract class XcraftCommand {
   private final XcraftCommandManager cManager;
   private final String bukkitCommand;
   private final String name;
   private final String pattern;
   private String usage;
   private String desc;
   private final String permission;

   public XcraftCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      this.cManager = cManager;
      this.bukkitCommand = command;
      this.name = name;
      this.pattern = pattern;
      this.usage = usage;
      this.desc = desc;
      this.permission = permission;
   }

   public void addCommandShortcut(XcraftCommandManager manager, String shortcut) {
   }

   public abstract boolean execute(XcraftPluginManager var1, CommandSender var2, String[] var3);

   protected void sendInfo(CommandSender sender, String msg, boolean showPrefix) {
      this.cManager.getPlugin().getMessenger().sendInfo(sender, msg, showPrefix);
   }

   public String getUsage() {
      return this.usage;
   }

   public void setUsage(String usage) {
      this.usage = usage;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getBukkitCommand() {
      return this.bukkitCommand;
   }

   public String getName() {
      return this.name;
   }

   public String getPattern() {
      return this.pattern;
   }

   public String getPermission() {
      return this.permission;
   }
}
