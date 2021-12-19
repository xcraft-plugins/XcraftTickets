package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.XcraftTickets;
import de.xcraft.voronwe.XcraftTickets.Commands.AssigneeUsage;
import de.xcraft.voronwe.XcraftTickets.Commands.IDUsage;
import de.xcraft.voronwe.XcraftTickets.Commands.MessageUsage;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Admin.ModCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.AssignCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.PhrasesCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.StatsCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.UnAssignCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.UndoCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.Mod.WarpCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.CloseCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.ListCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.LogCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.OpenCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.ReOpenCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.SetWarpCommand;
import de.xcraft.voronwe.XcraftTickets.Commands.User.ViewCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandManager extends XcraftCommandManager {
   TicketManager tManager;

   public CommandManager(XcraftTickets plugin) {
      super(plugin);
   }

   protected void registerCommands() {
      this.tManager = (TicketManager)this.plugin.getPluginManager();
      this.registerBukkitCommand("ticket");
      this.registerBukkitCommand("tl");
      this.registerCommand(new OpenCommand(this, "ticket", "open", "o.*", "<MESSAGE> ...", Msg.COMMAND_OPEN.toString(), "xcrafttickets.open"));
      this.registerCommand(new LogCommand(this, "ticket", "log", "l|lo.*|co.*", "<ID> <MESSAGE> ...", Msg.COMMAND_LOG.toString(), "xcrafttickets.log"));
      this.registerCommand(new ListCommand(this, "ticket", "list", "li.*", "", Msg.COMMAND_LIST.toString(), "xcrafttickets.list"));
      this.registerCommand(new ViewCommand(this, "ticket", "view", "v.*|i.*", "<ID> [all]", Msg.COMMAND_VIEW.toString(), "xcrafttickets.view"));
      this.registerCommand(new CloseCommand(this, "ticket", "close", "c|cl.*", "<ID> <MESSAGE> ...", Msg.COMMAND_CLOSE.toString(), "xcrafttickets.close"));
      this.registerCommand(new ReOpenCommand(this, "ticket", "reopen", "reo.*|ro", "<ID> <MESSAGE> ...", Msg.COMMAND_REOPEN.toString(), "xcrafttickets.reopen"));
      this.registerCommand(new SetWarpCommand(this, "ticket", "setwarp", "se.*|sw", "<ID> [MESSAGE] ...", Msg.COMMAND_SETWARP.toString(), "xcrafttickets.setwarp"));
      this.registerCommand(new WarpCommand(this, "ticket", "warp", "w.*|tp", "<ID>", Msg.COMMAND_WARP.toString(), "xcrafttickets.warp"));
      this.registerCommand(new AssignCommand(this, "ticket", "assign", "a.*", "<ID> <ASSIGNEE>", Msg.COMMAND_ASSIGN.toString(), "xcrafttickets.assign"));
      this.registerCommand(new UnAssignCommand(this, "ticket", "unassign", "una.*|ua", "<ID>", Msg.COMMAND_UNASSIGN.toString(), "xcrafttickets.unassign"));
      this.registerCommand(new UndoCommand(this, "ticket", "undo", "und.*|ud", "<ID>", Msg.COMMAND_UNDO.toString(), "xcrafttickets.undo"));
      this.registerCommand(new PhrasesCommand(this, "ticket", "phrases", "p.*", "<list/add/remove/append> [MESSAGE]", Msg.COMMAND_PHRASES.toString(), "xcrafttickets.phrases"));
      this.registerCommand(new StatsCommand(this, "ticket", "stats", "st.*", "", Msg.COMMAND_STATS.toString(), "xcrafttickets.stats"));
      this.registerCommand(new ModCommand(this, "ticket", "mod", "m.*", "<add/remove/list> [Assignee]", Msg.COMMAND_MOD.toString(), "xcrafttickets.mod"));
      this.registerUsage(new IDUsage(this.tManager));
      this.registerUsage(new MessageUsage(this.tManager));
      this.registerUsage(new AssigneeUsage(this.tManager));
   }

   public boolean onCommand(CommandSender sender, Command bcmd, String label, String[] args) {
      return bcmd.getName().equals("tl") ? ((XcraftCommand)this.getCommands().get("li.*")).execute(this.tManager, sender, args) : super.onCommand(sender, bcmd, label, args);
   }

   public void onLoad(CommandSender sender) {
      super.onLoad(sender);
      Msg.init(this.plugin);
   }
}
