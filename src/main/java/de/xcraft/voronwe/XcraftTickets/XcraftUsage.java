package de.xcraft.voronwe.XcraftTickets;

import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class XcraftUsage {
   private final String name;
   private String alias;

   public XcraftUsage(String name, String alias) {
      this.name = name;
      this.alias = alias;
   }

   public abstract boolean validate(String var1);

   public abstract String getFailMessage();

   public abstract List<String> onTabComplete(List<String> var1, CommandSender var2);

   public String getName() {
      return this.name;
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }
}
