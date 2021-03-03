package andrew.BuildingGame;

import andrew.BuildingGame.Commands.BGReady;
import andrew.BuildingGame.Game.Game;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
//  Game game = new Game();

  @Override
  public void onEnable() {
    this.getCommand("bgready").setExecutor(new BGReady());
  }
}
