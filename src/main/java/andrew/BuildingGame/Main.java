package andrew.BuildingGame;

import andrew.BuildingGame.Commands.*;
import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static Main main;
  private static Game game;

  @Override
  public void onEnable() {
    main = this;

    this.getCommand("bghost").setExecutor(new BGHost());
    this.getCommand("bgready").setExecutor(new BGReady());
    this.getCommand("bgstart").setExecutor(new BGStart());
    this.getCommand("bgprompt").setExecutor(new BGPrompt());
    this.getCommand("bgnext").setExecutor(new BGNext());
  }

  public static void createGame() { game = new Game(true); }
  public static Game getGame() { return game; }
}
