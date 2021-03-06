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
  private static GameSettings settings;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Main.createGame();
    game = Main.getGame();
    settings = Main.getSettings();
    game.start(BGReady.getParticipants(), BGHost.getHost(), settings);

    return true;
  }
}
