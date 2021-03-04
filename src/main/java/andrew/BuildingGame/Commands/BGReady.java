package andrew.BuildingGame.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BGReady implements CommandExecutor{
  private static List<Player> readyPlayers = new ArrayList<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
  if (!(sender instanceof Player)) { return false; }
    Player player = (Player) sender;
    if (readyPlayers.contains(player)) {
      readyPlayers.remove(player);
      Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " is no longer ready!");
    } else {
      readyPlayers.add(player);
      Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " is ready!");
    }

    return true;
  }

  public static List<Player> getParticipants() { return readyPlayers; }
}
