package andrew.BuildingGame;

import andrew.BuildingGame.Commands.BGHost;
import andrew.BuildingGame.Commands.BGPrompt;
import andrew.BuildingGame.Commands.BGReady;
import andrew.BuildingGame.Commands.BGStart;
import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static Main main;

  @Override
  public void onEnable() {
    main = this;
    // TODO: Create defaults that automatically populate this if not specified
    GameSettings settings = new GameSettings(5, 15, 15, 30,
            5, Material.SNOW, Material.DIAMOND_BLOCK, Material.NETHER_BRICK);
    Game game = new Game(true, BGReady.getParticipants(), settings);
    getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Test");

    this.getCommand("bghost").setExecutor(new BGHost());
    this.getCommand("bgready").setExecutor(new BGReady());
    this.getCommand("bgstart").setExecutor(new BGStart());
    this.getCommand("bgprompt").setExecutor(new BGPrompt());
  }
}
