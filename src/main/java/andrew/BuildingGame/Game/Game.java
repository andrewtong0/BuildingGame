package andrew.BuildingGame.Game;

import andrew.BuildingGame.Commands.BGHost;
import andrew.BuildingGame.Commands.BGPrompt;
import andrew.BuildingGame.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
  Boolean devMode;
  Player host;
  World world;
  List<Player> participants;
  GameSettings settings;
  HashMap<Player, Player> playerChain;
  HashMap<Player, String> playerPrompts;

  public Game(Boolean devMode, List<Player> participants, GameSettings settings) {
    this.devMode = devMode;
    this.participants = participants;
    this.settings= settings;
  }

  public void start() {
//    if (!verifyGamePrerequisites()) { return; }
    host = BGHost.getHost();
    world = host.getWorld();
    Bukkit.broadcastMessage(((Player) host).getName() + " is the game host");
    GenerateBuildArea buildAreaGenerator = new GenerateBuildArea(participants.size(), host, settings);
    buildAreaGenerator.generate();
//    sendTitleMessage();
//    sendActionBarMessage();
    createPlayerChain();
    distributePrompts();
    timer();
  }

  public void stop() {}

  public void timer() {
    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    scheduler.scheduleSyncDelayedTask((Plugin) Main.main, new Runnable() {
      public void run() {
        for (Player p : participants) {
          String playerPrompt = playerPrompts.get(p);
          sendActionBarMessage(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "Your prompt is: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + playerPrompt);
        }
        Game.this.timer();
      }
    }, 1L);
  }

  private void sendTitleMessage() {
    for (Player player : participants) {
      IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + "TEST TITLE" + "\"}"));
      PacketPlayOutTitle p = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
    }
  }

  private void sendActionBarMessage(Player player, String message) {
    TextComponent prompt = new TextComponent(message);
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, prompt);
  }

  private boolean verifyGamePrerequisites() {
    if (participants.size() < 4) {
      Bukkit.broadcastMessage(ChatColor.RED + "Requires at least 4 players to play.");
      return false;
    }
    return true;
  }

  private void createPlayerChain() {
    int numInChain = 1;
    Random rand = new Random();
    playerChain = new HashMap<>();

    // Store reference to complete cyclic chain
    List<Player> participantsClone = new ArrayList<>(participants);
    Player firstPlayer = participantsClone.get(0);
    participantsClone.remove(0);

    Player player;
    Player prevPlayer = firstPlayer;

    while (numInChain <= participantsClone.size()) {
      int playerIndex = rand.nextInt(participantsClone.size());
      player = participantsClone.get(playerIndex);
      participantsClone.remove(player);
      playerChain.put(prevPlayer, player);
      prevPlayer = player;
      numInChain++;
    }

    playerChain.put(prevPlayer, firstPlayer);
  }

  private void distributePrompts() {
    Random rand = new Random();
    HashMap<Player, List<String>> prompts = BGPrompt.getPrompts();
    playerPrompts = new HashMap<>();
    for (Player p : participants) {
      List<String> promptsForPlayer = prompts.get(playerChain.get(p));
      String playerPrompt = promptsForPlayer.get(rand.nextInt(promptsForPlayer.size()));
      playerPrompts.put(p, playerPrompt);
    }
  }
}
