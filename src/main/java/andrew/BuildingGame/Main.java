package andrew.BuildingGame;

import andrew.BuildingGame.Commands.*;
import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import org.bukkit.Material;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static Main main;
  private static Game game;
  private static GameSettings settings;

  @Override
  public void onEnable() {
    main = this;

    this.getCommand("bghost").setExecutor(new BGHost());
    this.getCommand("bgready").setExecutor(new BGReady());
    this.getCommand("bgstart").setExecutor(new BGStart());
    this.getCommand("bgprompt").setExecutor(new BGPrompt());
    this.getCommand("bgnext").setExecutor(new BGNext());
    this.getCommand("bgsettings").setExecutor(new BGSettings(this));

    settings = new GameSettings(10, 20, 20, 5, 5,
            Material.SNOW_BLOCK, Material.LIGHT_GRAY_TERRACOTTA, Material.GRAY_CONCRETE, true);
  }

  public static void createGame() { game = new Game(true); }
  public static Game getGame() { return game; }
  public static GameSettings getSettings() { return settings; }
}
