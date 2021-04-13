package andrew.BuildingGame.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BGHost implements CommandExecutor {
  private static Player host;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (!sender.hasPermission("bg.host")) {
      sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
    } else {
      host = (Player) sender;
      sender.sendMessage(sender.getName() + " has been set to the game host");
    }
    return true;
  }

  public static Player getHost() { return host; }
}
