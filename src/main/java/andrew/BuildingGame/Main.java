package andrew.BuildingGame;

import andrew.BuildingGame.Commands.*;
import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import andrew.BuildingGame.Game.GameVars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static Main main;
  public static Game game;
  public static GameSettings settings;
  public static GameVars vars;

  @Override
  public void onEnable() {
    main = this;

    this.getCommand("bghost").setExecutor(new BGHost());
    this.getCommand("bgready").setExecutor(new BGReady());
    this.getCommand("bgstart").setExecutor(new BGStart());
    this.getCommand("bgprompt").setExecutor(new BGPrompt());
    this.getCommand("bgnext").setExecutor(new BGNext());
    this.getCommand("bgsettings").setExecutor(new BGSettings(this));
//    this.getCommand("bgtour").setExecutor(new BGTour());

    settings = new GameSettings(10, 20, 20, 5, 5,
            Material.SNOW_BLOCK, Material.LIGHT_GRAY_TERRACOTTA, Material.GRAY_CONCRETE, true);
  }

  public static void createGame() {
    Player host = BGHost.getHost();
    vars = new GameVars(host, host.getWorld(), BGReady.getParticipants());
    game = new Game(settings, vars);
  }
  public static Game getGame() { return game; }
  public static GameSettings getSettings() { return settings;}
  public static GameVars getVars() { return vars; }
}
