package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import andrew.BuildingGame.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BGStart implements CommandExecutor {
  private static Game game;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // TODO: Dynamically set settings
    GameSettings settings = new GameSettings(10, 20, 20, 5,
            5, Material.SNOW_BLOCK, Material.LIGHT_GRAY_TERRACOTTA, Material.GRAY_CONCRETE);
    Main.createGame();
    game = Main.getGame();
    game.start(BGReady.getParticipants(), BGHost.getHost(), settings);

    return true;
  }
}
