package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BGStart implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    GameSettings settings = new GameSettings(5, 20, 20, 5,
            5, Material.SNOW_BLOCK, Material.LIGHT_GRAY_TERRACOTTA, Material.GRAY_CONCRETE);
    Game game = new Game(true, BGReady.getParticipants(), settings);
    game.start();

    return true;
  }
}
